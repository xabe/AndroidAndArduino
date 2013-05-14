package com.tutorial.bluetooth.util;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Clase que dibuja una Marca para el velocimetro
 * @author Chabir Atrahouch
 *
 */
public class Dial {
	
	private float xini;
	private float yini;
	private float xfin;
	private float yfin;	
	private Paint backgroundSimple;
	private Paint backgroundBlur;
	
	public Dial() {		
	}

	public float getXini() {
		return xini;
	}

	public void setXini(float xini) {
		this.xini = xini;
	}

	public float getYini() {
		return yini;
	}

	public void setYini(float yini) {
		this.yini = yini;
	}

	public float getXfin() {
		return xfin;
	}

	public void setXfin(float xfin) {
		this.xfin = xfin;
	}

	public float getYfin() {
		return yfin;
	}

	public void setYfin(float yfin) {
		this.yfin = yfin;
	}
	
	public void setBackgroundBlur(Paint backgroundBlur) {
		this.backgroundBlur = backgroundBlur;
	}
	
	public void setBackgroundSimple(Paint backgroundSimple) {
		this.backgroundSimple = backgroundSimple;
	}
	
	public Paint getBackgroundBlur() {
		return backgroundBlur;
	}
	
	public Paint getBackgroundSimple() {
		return backgroundSimple;
	}
	
	public void draw(Canvas canvas){
		canvas.drawLine(getXini(), getYini(), getXfin(), getYfin(), backgroundSimple);
		canvas.drawLine(getXini(), getYini(), getXfin(), getYfin(), backgroundBlur);
	}

}
