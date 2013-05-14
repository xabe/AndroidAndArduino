package com.tutorial.bluetooth.sensor;

/**
 * Interfaz que recibe la informacion del sensor de orientacion
 * @author Chabir Atrahouch
 *
 */
public interface OrientationListener {

	public void onOrientationChanged(float azimuth,float pitch, float roll);
	
}
