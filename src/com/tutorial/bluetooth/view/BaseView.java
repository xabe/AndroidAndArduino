package com.tutorial.bluetooth.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.tutorial.bluetooth.util.HandleData;
import com.tutorial.bluetooth.util.ViewThread;

/**
 * Clase abstracta que tiene los metodos comunes y de las dos aplicaciones que tenesmo contruidas
 * @author Chabir Atrahouch
 *
 */
public abstract class BaseView extends SurfaceView implements SurfaceHolder.Callback, HandleData{
	protected static final String TAG = BaseView.class.getSimpleName();
	protected ViewThread thread;
	private StringBuilder sb;
	
	public BaseView(Context context) {
		super(context);	
		getHolder().addCallback(this);
		thread = new ViewThread(getHolder(), this);
		setFocusable(true);
		setFocusableInTouchMode(true); 
		sb = new StringBuilder();
	}
	
	protected abstract void init();	
	
	/**
	 * Metodo del surfaceView
	 */
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		init();
		if (!thread.isAlive()) {
			thread.start();
        }
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		thread.setRunning(true);
		if (!thread.isAlive()) {
			thread.doStart();
			thread.start();
        }
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "Surface is being destroyed");
		thread.setRunning(false);
		boolean retry = true;
		while (retry) {
			try {
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
			}
		}
		Log.d(TAG, "Thread was shut down cleanly");
	}
	
	
	/**
	 * Metodos de actulizar la image y pintar
	 */
	
	public abstract void update();

	public abstract void draw(Canvas canvas);
	
	
	/**
	 * Metodos de recivir del blueetooth
	 */
	
	public void reciveData(byte[] buffer, int bytes) {	
		String readMessage = new String(buffer, 0, bytes);
		sb.append(readMessage);                                             
        int endOfLineIndex = sb.indexOf("\r\n");                           
        if (endOfLineIndex > 0) {
        	String sbprint = sb.substring(0, endOfLineIndex);               // extract string
            sb.delete(0, sb.length()); 
        	Toast.makeText(getContext(), "Hemos recibido del bluetooth lo siguiente : "+sbprint, Toast.LENGTH_SHORT).show();
        }
	}
	
	protected String byteToString(byte[] buffer, int bytes)
	{
	    StringBuilder s = new StringBuilder();

	    for(int i = 0; i < bytes; i++)
	    {
	        s.append((char)buffer[i]);
	    }

	    return s.toString();    
	}
}
