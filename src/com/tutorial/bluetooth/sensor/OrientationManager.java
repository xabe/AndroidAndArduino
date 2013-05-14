package com.tutorial.bluetooth.sensor;

import java.util.List;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

/**
 * Clase que getiona la sensor de orientacion  creación, parada, etc...
 * @author Chabir Atrahouch
 *
 */
public class OrientationManager {
	private Sensor sensor;
    private SensorManager sensorManager;
    private SensorEventListener sensorEventListener;
 
    private Boolean supported;
    private boolean running = false;
    private Context context;
    
    public OrientationManager(Context context) {
		this.context = context;
	}
 
    /**
     * Devuelve si esta en ejecucion el sensorListener
     */
    public boolean isListening() {
        return running;
    }
 
    /**
     * Des registra el sensorEventListener
     */
    public void stopListening() {
        running = false;
        try {
            if (sensorManager != null && sensorEventListener != null) {
                sensorManager.unregisterListener(sensorEventListener);
            }
        } catch (Exception e) {}
    }
 
    /**
     * Returns true if at least one Orientation sensor is available
     */
    public boolean isSupported() {
        if (supported == null) {
            if (context != null) {
                sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
                List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ORIENTATION);
                supported = Boolean.valueOf(sensors.size() > 0);
            } else {
                supported = Boolean.FALSE;
            }
        }
        return supported;
    }
 
    /**
     * Registers a listener and start listening
     */
    public void startListening(OrientationListener orientationListener) {
    	if (context != null) 
    	{
    		sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    		List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ORIENTATION);
    		if (sensors.size() > 0) {
    			sensor = sensors.get(0);
    			sensorEventListener = new SensorEventListener(orientationListener);
    			running = sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_GAME);
    		}
    	}
    }
}
