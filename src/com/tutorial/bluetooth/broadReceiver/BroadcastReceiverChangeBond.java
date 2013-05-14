package com.tutorial.bluetooth.broadReceiver;

import com.tutorial.bluetooth.MainActivity;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Clase para detecta cuando se produce un cambio en esta de emparejamiento
 * @author Chabir Atrahouch
 *
 */
public class BroadcastReceiverChangeBond extends BroadcastReceiver{
	
	private MainActivity activity;
	
	public BroadcastReceiverChangeBond(MainActivity activity) {
		this.activity = activity;
	}
	
	
	@Override
    public void onReceive(Context context, Intent intent) {
		if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(intent.getAction()))
		{
			int prevBondState = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, -1);
		    int bondState = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1);

		    if(activity.isWaitingForBonding())
		    {
		    	if (prevBondState == BluetoothDevice.BOND_BONDING)
		        {
		    		// check for both BONDED and NONE here because in some error cases the bonding fails and we need to fail gracefully.
		            if (bondState == BluetoothDevice.BOND_BONDED || bondState == BluetoothDevice.BOND_NONE)
		            {
		            	this.activity.getPairedDevices();
						this.activity.getAdapter().notifyDataSetChanged();
						this.activity.setWaitingForBonding(false);
		            }
		        }
		    }
		}    
     }

}
