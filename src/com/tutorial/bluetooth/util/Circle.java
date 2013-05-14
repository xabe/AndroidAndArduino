package com.tutorial.bluetooth.util;

import android.graphics.Canvas;
import android.graphics.Paint;


/**
 * Clase que dibuja una flecha
 * @author Chabir Atrahouch
 *
 */
public class Circle {
	private float cx;
	private float cy;
	private float radius;
	private Paint on;
	private Paint off;
	private boolean showOn;

	public Circle(float cx, float cy, float radius,Paint on, Paint off,boolean showOn) {
		this.cx = cx;
		this.cy = cy;
		this.radius = radius;
		this.on = on;
		this.off = off;
		this.showOn = showOn;
	}
	
	public boolean isShowOn() {
		return showOn;
	}
	
	public void setShowOn(boolean showOn) {
		this.showOn = showOn;
	}

	public float getCx() {
		return cx;
	}

	public void setCx(float cx) {
		this.cx = cx;
	}

	public float getCy() {
		return cy;
	}

	public void setCy(float cy) {
		this.cy = cy;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}
	
	public void canvas(Canvas canvas){
		if(isShowOn())
		{
			canvas.drawCircle(cx, cy, radius, on);
		}
		else
		{
			canvas.drawCircle(cx, cy, radius, off);
		}
	}
	
	private float distanciaPuntos(float x, float y)
	{
	    float distancia_x = x - cx;
	    float distancia_y = y - cy;
	    return (float) Math.sqrt((distancia_x*distancia_x)+(distancia_y*distancia_y));
	}
	
	public boolean touch(float x, float y){
		if ( distanciaPuntos(x,y) <= radius)
	    {
			setShowOn(!isShowOn());
			return true;
	    }
		return false;
	}
}
