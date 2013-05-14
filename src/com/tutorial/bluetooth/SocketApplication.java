package com.tutorial.bluetooth;

import android.app.Application;
import android.bluetooth.BluetoothDevice;

/**
 * Clase para poder pasar entre Activitys el BluetoothDevice
 * @author Biomed
 *
 */
public class SocketApplication extends Application {

	private BluetoothDevice device = null;

	public BluetoothDevice getDevice() {
		return device;
	}

	public void setDevice(BluetoothDevice device) {
		this.device = device;
	}	
}

