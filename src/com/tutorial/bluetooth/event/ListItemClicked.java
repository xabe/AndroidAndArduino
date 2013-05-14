package com.tutorial.bluetooth.event;

import android.bluetooth.BluetoothDevice;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.tutorial.bluetooth.MainActivity;
import com.tutorial.bluetooth.util.Constants;

/**
 * Clase que gestionas los eventos producidos en la lista de dispositivos descubiertos
 * @author Chabir Atrahouch
 *
 */
public class ListItemClicked implements OnItemClickListener {
	
	private MainActivity activity;
	
	public ListItemClicked(MainActivity activity) {
		this.activity = activity;
	}	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		this.activity.setBdDevice(this.activity.getArrayListBluetoothDevices().get(position));
		boolean connect = false;
		try 
		{	        
			connect = Constants.createBond(this.activity.getBdDevice());
			int bondState = this.activity.getBdDevice().getBondState();
			if (bondState == BluetoothDevice.BOND_NONE || bondState == BluetoothDevice.BOND_BONDING)
			{
				this.activity.setWaitingForBonding(true);
			}
			else
			{
				if(connect)
				{
					this.activity.getPairedDevices();
					this.activity.getAdapter().notifyDataSetChanged();
				}
				else
				{
					this.activity.setWaitingForBonding(true);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
