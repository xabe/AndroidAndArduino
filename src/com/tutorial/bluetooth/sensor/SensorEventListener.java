package com.tutorial.bluetooth.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

/**
 * Clase que getiona el evento producido por el sensor de orientacion y se lo envia a OrientationListener
 * @author Chabir Atrahouch
 *
 */
public class SensorEventListener implements android.hardware.SensorEventListener {
	private float azimuth;
	private float pitch;
	private float roll;
	private OrientationListener listener;

	public SensorEventListener(OrientationListener listener) {
		this.listener = listener;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		azimuth = event.values[0]; // azimuth
		pitch = event.values[1]; // pitch
		roll = event.values[2]; // roll
		listener.onOrientationChanged(azimuth, pitch, roll);
	}

}
