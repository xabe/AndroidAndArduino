package com.tutorial.bluetooth.connect;

import java.io.IOException;
import java.io.InputStream;
import android.os.Handler;
import java.io.OutputStream;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.tutorial.bluetooth.util.Constants;

/**
 * Este hilo se ejecuta durante una conexión con un dispositivo remoto.
 * Maneja todas las transmisiones entrantes y salientes.
 */
public class ConnectedThread extends Thread {
	private static final String TAG = "ConnectedThread";
	
	private final BluetoothService service;
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private final Handler mhandler;
    private volatile boolean stopWorker;

    public ConnectedThread(BluetoothService bluetoothService, Handler handler, BluetoothSocket socket) {
        Log.d(TAG, "create ConnectedThread");
        setName(TAG);
        service = bluetoothService;
        mhandler = handler;
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        stopWorker = false;

        // Get the BluetoothSocket input and output streams
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
            Log.e(TAG, "temp sockets not created", e);
        }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    public void run() {
        Log.i(TAG, "BEGIN mConnectedThread");
        byte[] packetBytes;
        int bytes;
        int bytesAvailable;

        // Keep listening to the InputStream while connected
        while(!Thread.currentThread().isInterrupted() && !stopWorker){
            try 
            {
            	 bytesAvailable = mmInStream.available();                        
                 if(bytesAvailable > 0)
                 {
                     packetBytes = new byte[bytesAvailable];
                     // Read from the InputStream
                     bytes = mmInStream.read(packetBytes);
                     	
                     // Send the obtained bytes to the UI Activity
                     mhandler.obtainMessage(Constants.MESSAGE_READ, bytes, -1, packetBytes).sendToTarget();
                    
                 }
            } catch (IOException e) {
            	stopWorker = true;
                Log.e(TAG, "disconnected", e);
                service.connectionLost();
                break;
            }
        }
    }

    /**
     * Write to the connected OutStream.
     * @param buffer  The bytes to write
     */
    public void write(byte[] buffer) {
        try {
            mmOutStream.write(buffer);
            mmOutStream.flush();
        } catch (IOException e) {
            Log.e(TAG, "Exception during write", e);
        }
    }

    public void cancel() {
    	stopWorker = true;
        try 
        {
            mmSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "close() of connect socket failed", e);
        }
    }
}