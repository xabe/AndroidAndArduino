package com.tutorial.bluetooth.broadReceiver;

import com.tutorial.bluetooth.MainActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Clase para saber cuando termina la busquedad de dispositivos de la red de bluetooth y quitar los registros de los de la actividad
 * @author Chabir Atrahouch
 *
 */
public class BroadcastReceiverDiscovery extends BroadcastReceiver {

	private MainActivity activity;

	public BroadcastReceiverDiscovery(MainActivity activity) {
		this.activity = activity;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		activity.unregisterReceiver(activity.getBroadcastReceiverFound());
		activity.unregisterReceiver(this);
		activity.getClicked().setDiscoveryFinished(true);
	}

}
