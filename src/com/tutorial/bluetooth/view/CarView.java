package com.tutorial.bluetooth.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.MotionEvent;

import com.tutorial.bluetooth.sensor.OrientationListener;
import com.tutorial.bluetooth.util.Constants;
import com.tutorial.bluetooth.util.Gearbox;
import com.tutorial.bluetooth.util.RotateWheel;
import com.tutorial.bluetooth.util.SpeedMarker;
import com.tutorial.bluetooth.util.SpeedoMeter;

/**
 * Clase que dibujar y getiona el velocimetro para controlar el coche por bluetooth con arduino
 * @author Chabir Atrahouch
 *
 */
public class CarView extends BaseView implements OrientationListener{
	
	private float radioSpeed;
	private float centerX = 0;
	private float centerY = 0;
	
	private SpeedoMeter speedometer;
	private SpeedMarker speedMarker;
	private RotateWheel rotateWheel;
	private Gearbox gearbox;
	
	private Handler mhandler;
	
	private boolean loadData;
	private Typeface myTypeface;
	private int minPositionVelocity;
	private int maxPositionVelocity;

	public CarView(Context context) {
		super(context);
		loadData = false;
	}
	
	public void setMhandler(Handler mhandler) {
		this.mhandler = mhandler;
	}
	
	@Override
	protected void init() {
		
		int w = getWidth();
		int h = getHeight();
		
		maxPositionVelocity = h - Constants.MARGIN_VELOCITY;
		minPositionVelocity = Constants.MARGIN_VELOCITY;
		
 		radioSpeed = (h >> 1) - 60;		
 		 
		centerX = w >> 1;
		centerY = h >> 1;
		
		speedometer = new SpeedoMeter(centerX, centerY, minPositionVelocity, maxPositionVelocity, w, radioSpeed,myTypeface);
		speedMarker = new SpeedMarker(w - Constants.MARGIN_VELOCITY, maxPositionVelocity, minPositionVelocity, maxPositionVelocity, w);
		gearbox = new Gearbox(50, minPositionVelocity, maxPositionVelocity, myTypeface);
		rotateWheel = new RotateWheel(centerX, centerY, radioSpeed);
		
		updateSpeed(maxPositionVelocity);
		
		loadData = true;
	}	
	
	public void setMyTypeface(Typeface myTypeface) {
		this.myTypeface = myTypeface;
	}	
	
	@Override
	public void onOrientationChanged(float azimuth, float pitch, float roll) {		
		try
		{
			rotateWheel.setDraw(false);
			byte out[];
			if (15.0 < pitch && pitch < 90)//Izquierda
			{
				rotateWheel.getLeft().setShown(true);
				out =  Constants.LEFT.getBytes("UTF-8");
			}
			else if (-15.0 > pitch && pitch > -90)//Derecha
			{
				rotateWheel.getRight().setShown(true);
				out =  Constants.RIGHT.getBytes("UTF-8");
			}
			else //center
			{
				out =  Constants.CENTER.getBytes("UTF-8");
			}
			mhandler.obtainMessage(Constants.MESSAGE_WRITE, out.length, -1, out).sendToTarget();
		}catch (Exception e) {
			
		}
	}
	

	public void update() {
		
	}
	
	private void updateSpeed(float y){
		try
		{
			speedMarker.update((int)y);
			speedometer.update();
			int velocidad = speedometer.getActualdegrees() - Constants.INIT_VELOCITY_DEGREES;
			if(velocidad > 255)
			{
				velocidad = 255;
			}
			
			//Trasnformamos la velocidad de 0-9 lo que lee arduino
			velocidad = (velocidad * 9)/255;			
			byte out[] = String.valueOf(velocidad).getBytes("UTF-8");
			//Enviamos la velocidad que queremos ir
			mhandler.obtainMessage(Constants.MESSAGE_WRITE, out.length, -1, out).sendToTarget();
		}catch (Exception e) {
		}
	}
	
	private void updateDirecction(float x, float y){
		try
		{
			gearbox.update((int)x, (int)y);
			byte out[] = String.valueOf(gearbox.getSelectPosition()).getBytes("UTF-8");
			//Enviamos la direccion que queremos ir
			mhandler.obtainMessage(Constants.MESSAGE_WRITE, out.length, -1, out).sendToTarget();
		}catch (Exception e) {
		}
	}

	public void draw(Canvas canvas) {
		if(canvas != null && loadData)
		{
			canvas.drawColor(Color.BLACK);	
			speedometer.draw(canvas);
			speedMarker.draw(canvas);
			rotateWheel.draw(canvas);
			gearbox.draw(canvas);
		}
	}
	
	private int getPorcentaje(float y){
		return (int) ((y - minPositionVelocity) * 100) / (maxPositionVelocity - minPositionVelocity);
	}

	private float calculateVelocity(float y){
		return  ((100 - getPorcentaje(y)) * (speedometer.getMaxdegress() - Constants.INIT_VELOCITY_DEGREES) / 100) + Constants.INIT_VELOCITY_DEGREES;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {		
		if(event.getAction() == MotionEvent.ACTION_DOWN)
		{
			if(event.getX() <=  (getWidth() >> 1))
			{
				updateDirecction(event.getX() , event.getY());
			}
		}
		
		if (event.getAction() == MotionEvent.ACTION_MOVE) 
		{
			if(event.getX() >=  (getWidth() >> 1))
			{
				float y;
				if(event.getY() >= maxPositionVelocity) //Minima valocidad
				{
					y = maxPositionVelocity;
					speedometer.setActualdegrees(Constants.INIT_VELOCITY_DEGREES);
				}
				else if(event.getY() <= minPositionVelocity) // Maxima velocidad
				{
					y = minPositionVelocity;
					speedometer.setActualdegrees(speedometer.getMaxdegress());
				}
				else
				{
					y = event.getY();
					speedometer.setActualdegrees((int)calculateVelocity(event.getY()));
				}
				updateSpeed(y);
			}
		}			
		return true;
	}

}
