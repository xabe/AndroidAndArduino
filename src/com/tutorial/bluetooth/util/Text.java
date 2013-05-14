package com.tutorial.bluetooth.util;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Clase que pinta los texto del velocimetro
 * @author Chabir Atrahouch
 *
 */
public class Text {
	private String text;
	private Paint paint;
	private float x;
	private float y;
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}
	
	public void setPaint(Paint paint) {
		this.paint = paint;
	}
	
	public Paint getPaint() {
		return paint;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void draw(Canvas canvas){
		canvas.drawText(text, getX(), getY(), paint);
	}

}
