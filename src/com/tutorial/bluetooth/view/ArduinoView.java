package com.tutorial.bluetooth.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.view.MotionEvent;

import com.tutorial.bluetooth.util.Circle;
import com.tutorial.bluetooth.util.Constants;

/**
 * Clase que pinta dos circulos  rojo y amarilo que cuando se pulsa sobre ellos se enciende y se apaga envia dicha información por 
 * bluetooh al arduino.
 * @author Chabir Atrahouch
 *
 */
public class ArduinoView extends BaseView {

	private Circle yellow;
	private Circle red;
	private Paint paintOnYellow;
	private Paint paintOnRed;
	private Paint paintOffYellow;
	private Paint paintOffRed;
	private Handler mhandler;

	public ArduinoView(Context context) {
		super(context);	
		
		paintOnYellow = new Paint();
		paintOnYellow.setAntiAlias(true);
		paintOnYellow.setDither(true);
		paintOnYellow.setColor(Color.argb(235,255,255,0));
		paintOnYellow.setStyle(Paint.Style.FILL);
		
		paintOnRed = new Paint();
		paintOnRed.setAntiAlias(true);
		paintOnRed.setDither(true);
		paintOnRed.setColor(Color.argb(235,205,0,0));
		paintOnRed.setStyle(Paint.Style.FILL);
		
		paintOffYellow = new Paint();
		paintOffYellow.setAntiAlias(true);
		paintOffYellow.setDither(true);
		paintOffYellow.setColor(Color.argb(235,102,102,0));
		paintOffYellow.setStyle(Paint.Style.FILL);
		
		paintOffRed = new Paint();
		paintOffRed.setAntiAlias(true);
		paintOffRed.setDither(true);
		paintOffRed.setColor(Color.argb(235,102,0,0));
		paintOffRed.setStyle(Paint.Style.FILL);
	}
	
	public void setMhandler(Handler mhandler) {
		this.mhandler = mhandler;
	}

	protected void init() {
		int positionYellonX = (getWidth() / 4);
		int positionRedX = 3 * positionYellonX;
		
		int cy = getHeight() / 2;
		
		yellow = new Circle(positionYellonX, cy, 100f, paintOnYellow, paintOffYellow, false);
		
		red = new Circle(positionRedX, cy, 100f, paintOnRed, paintOffRed, false);
	}	

	public void update() {
		
	}

	public void draw(Canvas canvas) {
		if(canvas != null)
		{
			//Background color
			canvas.drawColor(Color.WHITE);			
			
			yellow.canvas(canvas);
			red.canvas(canvas);			
		}
	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		byte out[];
		if (event.getAction() == MotionEvent.ACTION_DOWN) 
		{
			try
			{
				if(yellow.touch(event.getX(), event.getY()))
				{
					if(yellow.isShowOn())
					{
						out = Constants.YELLOW_ON.getBytes("UTF-8");
					}
					else
					{
						out = Constants.YELLOW_OFF.getBytes("UTF-8");
					}
					mhandler.obtainMessage(Constants.MESSAGE_WRITE, out.length, -1, out).sendToTarget();
				}
				if(red.touch(event.getX(), event.getY()))
				{
					if(red.isShowOn())
					{
						out = Constants.RED_ON.getBytes("UTF-8");
					}
					else
					{
						out = Constants.RED_OFF.getBytes("UTF-8");
					}
					mhandler.obtainMessage(Constants.MESSAGE_WRITE, out.length, -1, out).sendToTarget();
				}
			}catch (Exception e) {
			}
		}
		return true;
	}

}
