package com.tutorial.bluetooth.connect;

import java.io.IOException;

import com.tutorial.bluetooth.util.Constants;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.ParcelUuid;
import android.util.Log;

/**
 * Este hilo se ejecuta al intentar realizar una conexión de salida
 * con un dispositivo. Se ejecuta directamente a través de la conexión ya sea éxitosa o fracasada.
 */
public class ConnectThread extends Thread {
	private static final String TAG = "ConnectThread";
    
    // Member fields
    private final BluetoothAdapter mAdapter;
    private final BluetoothService bluetoothService;
    
    private BluetoothSocket mmSocket;
    private final BluetoothDevice bluetoothDevice;
    
    private final ParcelUuid[] uuids;

    public ConnectThread(BluetoothService service, BluetoothDevice device) {
    	bluetoothService = service;
    	bluetoothDevice = device;
    	mAdapter = BluetoothAdapter.getDefaultAdapter();
        setName(TAG);
        uuids = Constants.getUUIDs(device);
//        BluetoothSocket tmp = null;
//        try 
//        {
//        	//Metodo 1
//        	tmp = device.createRfcommSocketToServiceRecord(Constants.MY_UUID);
//        	//Metodo 2
//        	Method m = device.getClass().getMethod("createInsecureRfcommSocket", new Class[] { int.class });
//        	tmp = (BluetoothSocket) m.invoke(device, 1);    
//        	//Metodo 3
//        	tmp = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
//        } catch (Exception e) {
//            Log.e(TAG, "Socket Type: " + "create() failed", e);
//        }
    }
    
    private boolean connect(ParcelUuid uuid){
    	try 
    	{
    		Log.d(TAG, "trying to connect " + uuid.getUuid());
    		mmSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid.getUuid());
            mmSocket.connect();
        } catch (IOException e) {
        	Log.e(TAG, "unable to connect " + " connection failure", e);
            try {
                mmSocket.close();
            } catch (IOException e2) {
                Log.e(TAG, "unable to close() " +
                        " socket during connection failure", e2);
            }
            return false;
        }
    	return true;
    }

    public void run() {
        Log.i(TAG, "BEGIN mConnectThread");

        // Always cancel discovery because it will slow down a connection
        mAdapter.cancelDiscovery();

        // Make a connection to the BluetoothSocket
        boolean find = false;
        //Si no queremos recorer el for de uuids y saber el uuid del dispositivo 
        //tenemos que hacer lo siguiente
        //Process process = Runtime.getRuntime().exec("su -c 'sdptool records " + device.getAddress() + "'");
        //process.waitFor();
        for(int i = uuids.length - 1 ; i >= 0; i--){
        	if(connect(uuids[i]))
        	{
        		find = true;
        		break;
        	}
        }      
        
        if(!find)
        {
        	bluetoothService.connectionFailed();
        	return;
        }
        
        // Reset the ConnectThread because we're done
        synchronized (bluetoothService) {
        	bluetoothService.setmConnectThread(null);
        }

        // Start the connected thread
        bluetoothService.connected(mmSocket, bluetoothDevice);
    }

    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "close() of connect " + " socket failed", e);
        }
    }
}
