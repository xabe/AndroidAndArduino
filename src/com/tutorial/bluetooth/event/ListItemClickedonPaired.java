package com.tutorial.bluetooth.event;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.tutorial.bluetooth.MainActivity;

/**
 * Clase que gestionas los eventos producidos en la lista de eventos emparejados
 * @author Chabir Atrahouch
 *
 */
public class ListItemClickedonPaired implements OnItemClickListener
{
	private MainActivity activity;
	
	public ListItemClickedonPaired(MainActivity activity) {
		this.activity = activity;
	}
	
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
    	this.activity.setBdDevice(this.activity.getArrayListPairedBluetoothDevices().get(position));
        try {
//            Boolean removeBonding = Constants.removeBond(this.activity.getBdDevice());
//            if(removeBonding)
//            {
//            	this.activity.getArrayListpaired().remove(position);
//            	this.activity.getAdapter().notifyDataSetChanged();
//            }
//        	  if(Constants.connect(this.activity.getBdDevice()))
//        	  {
        		  this.activity.intentCar();
//        	  }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
