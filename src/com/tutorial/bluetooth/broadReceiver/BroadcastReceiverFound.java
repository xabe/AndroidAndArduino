package com.tutorial.bluetooth.broadReceiver;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.tutorial.bluetooth.R;
import com.tutorial.bluetooth.MainActivity;

/**
 * Clase para descubrir dispositivos en la red de bluetooth
 * @author Chabir Atrahouch
 *
 */
public class BroadcastReceiverFound extends BroadcastReceiver{
	
	private MainActivity activity;
	
	public BroadcastReceiverFound(MainActivity activity) {
		this.activity = activity;
	}
	
	
	 @Override
     public void onReceive(Context context, Intent intent) {
         String action = intent.getAction();
         if(BluetoothDevice.ACTION_FOUND.equals(action)){
             Toast.makeText(context, R.string.deviceFound, Toast.LENGTH_SHORT).show();

             BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//           Parcelable[] uuidExtra = intent.getParcelableArrayExtra(BluetoothDevice.EXTRA_UUID);
//           for (int i=0; i<uuidExtra.length; i++) {
//              out.append("\n  Device: " + device.getName() + ", " + device + ", Service: " + uuidExtra[i].toString());
//           }

             if(this.activity.getArrayListBluetoothDevices().size()<1) 
             {
            	 this.activity.getDetectedAdapter().add(device.getName()+"\n"+device.getAddress());
            	 this.activity.getArrayListBluetoothDevices().add(device);
            	 this.activity.getDetectedAdapter().notifyDataSetChanged();
             }
             else
             {
                 boolean flag = true;
                 for(int i = 0; i<this.activity.getArrayListBluetoothDevices().size();i++)
                 {
                     if(device.getAddress().equals(this.activity.getArrayListBluetoothDevices().get(i).getAddress()))
                     {
                         flag = false;
                     }
                 }
                 if(flag == true)
                 {
                	 this.activity.getDetectedAdapter().add(device.getName()+"\n"+device.getAddress());
                	 this.activity.getArrayListBluetoothDevices().add(device);
                	 this.activity.getDetectedAdapter().notifyDataSetChanged();
                 }
             }
         }           
     }

}
