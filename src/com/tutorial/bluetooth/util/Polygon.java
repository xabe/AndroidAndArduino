package com.tutorial.bluetooth.util;

import java.util.List;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

/**
 * Clase generica que dibuja un poligono
 * @author Chabir Atrahouch
 *
 */
public class Polygon {
	private Path path;
	private Paint paint;
	private boolean shown;
	private List<Point> points;
	
	public Polygon(List<Point> points) {
		this.points = points;
		this.path = new Path();
		this. path.setFillType(Path.FillType.EVEN_ODD);
		for(int i = 0; i < points.size(); i++){
			if( i == 0)
			{
				this.path.moveTo(points.get(i).x,points.get(i).y);
			}
			this.path.lineTo(points.get(i).x,points.get(i).y);
		}
		this.path.lineTo(points.get(0).x,points.get(0).y);
		this.path.close();
	    this.shown = false;
	}
	
	public List<Point> getPoints() {
		return points;
	}
	
	public void setPaint(Paint paint) {
		this.paint = paint;
	}
	
	public Paint getPaint() {
		return paint;
	}
	
	public boolean isShown() {
		return shown;
	}
	
	public void setShown(boolean shown) {
		this.shown = shown;
	}
	
	public void draw(Canvas canvas){
		if(isShown())
		{
			canvas.drawPath(path, getPaint());
		}
	}

}
