package com.tutorial.bluetooth.util;

/**
 * Interfaz para manejar los datos recibidos por el  blueetooth
 * @author Chabir Atrahouch
 *
 */
public interface HandleData {
	
	void reciveData(byte[] buffer,int bytes);

}
