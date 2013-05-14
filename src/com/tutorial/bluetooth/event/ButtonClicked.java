package com.tutorial.bluetooth.event;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.tutorial.bluetooth.R;
import com.tutorial.bluetooth.MainActivity;
import com.tutorial.bluetooth.util.Constants;

/**
 * Clase que getiona los eventos producidos en los botones de las apliaciones 
 * @author Chabir Atrahouch
 *
 */
public class ButtonClicked implements OnClickListener{
	
	private MainActivity activity;
	private volatile boolean discoveryFinished;
	
	public void setDiscoveryFinished(boolean discoveryFinished) {
		this.discoveryFinished = discoveryFinished;
	}
	
	private Runnable discoveryWorkder = new Runnable() {
		public void run() 
		{
			/* Start search device */
			if (activity.getBluetoothAdapter().isDiscovering()) {
				activity.getBluetoothAdapter().cancelDiscovery();
	        }
			activity.getBluetoothAdapter().startDiscovery();
			for (;;) 
			{
				if (discoveryFinished) 
				{
					break;
				}
				try 
				{
					Thread.sleep(100);
				} 
				catch (InterruptedException e){}
			}
		}
	};
	
	public ButtonClicked(MainActivity activity) {
		this.activity = activity;
	}
	
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.buttonOn:
        	onBluetooth();
            break;
        case R.id.buttonSearch:
        	this.activity.getArrayListBluetoothDevices().clear();
            startSearching();
            break;
        case R.id.buttonDesc:
        	makeDiscoverable();
            break;
        case R.id.buttonOff:
        	offBluetooth();
            break;
        default:
            break;
        }
    }
    
    /**
     * Accion para descubir otros dispositivos
     */
    private void startSearching() {
    	if (!this.activity.getBluetoothAdapter().isEnabled())
		{
    		Toast.makeText(this.activity, "El bluetooth desactivado active el bluetooth", Toast.LENGTH_LONG).show();
			return;
		}
    	
    	discoveryFinished = false;
    	
    	//Accion para descubrir dispositivos
    	IntentFilter discoveryFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
    	this.activity.registerReceiver(this.activity.getBroadcastReceiverDiscovery(), discoveryFilter);
    	
    	//Accion para buscar dispositivos
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.activity.registerReceiver(this.activity.getBroadcastReceiverFound(), intentFilter);
        
        //Mostramos la Modal para poder para la busquedad 
        Constants.indeterminateInternal(this.activity, new Handler(), "Escaneando...", discoveryWorkder, new OnDismissListener() {
			public void onDismiss(DialogInterface dialog)
			{

				for (; activity.getBluetoothAdapter().isDiscovering();)
				{

					activity.getBluetoothAdapter().cancelDiscovery();
				}

				discoveryFinished = true;
			}
		}, true);
    }
    
    /**
     * Encendemos el bluetooth
     */
    private void onBluetooth() {
        if(!this.activity.getBluetoothAdapter().isEnabled())
        {
        	this.activity.getBluetoothAdapter().enable();
        }
    }
    
    /**
     * Apagamos el bluetooth
     */
    private void offBluetooth() {
        if(this.activity.getBluetoothAdapter().isEnabled())
        {
        	this.activity.getBluetoothAdapter().disable();
        }
    }
    
    /**
     * Permitimos que otros dipositivos no descubra
     */
    private void makeDiscoverable() {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        this.activity.startActivity(discoverableIntent);
    }
}