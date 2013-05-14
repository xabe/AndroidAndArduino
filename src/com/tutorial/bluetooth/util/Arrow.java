package com.tutorial.bluetooth.util;

import java.util.ArrayList;
import java.util.List;

import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

/**
 * Clase que dibuja la fecha para indicar la dirección del coche
 * @author Chabir Atrahouch
 *
 */
public class Arrow {
	private Paint circleSimple;
	private Paint circleBlur;
	
	private Paint arrowSimple;
	private Paint arrowBlur;
	
	private float centerX;
	private float centerY;
	private float finalX;
	private float finalY;
	private float radioSpeedIni;
	private float radioSpeedFin;
	private float radioArrow;
	private Path path;
	
	
	public Arrow(float x, float y, float radioSpeed) {
		this.centerX = x;
		this.centerY = y;
		//Restamos el ancho del circulo
		this.radioSpeedIni = radioSpeed - 20;
		this.radioArrow = radioSpeed / 10;
		this.radioSpeedFin = this.radioArrow + 20;
		
		circleSimple = new Paint();
		circleSimple.setAntiAlias(true);
		circleSimple.setDither(true);
		circleSimple.setColor(Color.argb(248, 255, 255, 255));
	    circleSimple.setStrokeWidth(20f);
	    circleSimple.setStyle(Paint.Style.FILL_AND_STROKE);
	    circleSimple.setStrokeJoin(Paint.Join.ROUND);
	    circleSimple.setStrokeCap(Paint.Cap.ROUND);
	    
	    circleBlur = new Paint();
	    circleBlur.set(circleSimple);
	    circleBlur.setColor(Color.parseColor("#FFA9A9A9"));
	    circleBlur.setStrokeWidth(30f);
	    circleBlur.setMaskFilter(new BlurMaskFilter(15, BlurMaskFilter.Blur.NORMAL));
	    
	    arrowSimple = new Paint();
	    arrowSimple.setAntiAlias(true);
		arrowSimple.setDither(true);
		arrowSimple.setColor(Color.argb(248, 255, 255, 255));
		arrowSimple.setStrokeWidth(4f);
	    arrowSimple.setStyle(Paint.Style.FILL_AND_STROKE);
	    arrowSimple.setStrokeJoin(Paint.Join.MITER);
	    arrowSimple.setStrokeCap(Paint.Cap.BUTT);
	    
	    arrowBlur = new Paint();
	    arrowBlur.set(arrowSimple);
	    arrowBlur.setColor(Color.parseColor("#FFFF4500"));
	    arrowBlur.setStrokeWidth(3f);
	    arrowBlur.setMaskFilter(new BlurMaskFilter(15, BlurMaskFilter.Blur.SOLID));
	    
	    update(Constants.INIT_VELOCITY_DEGREES);
	}
	
	
	public void update(int actualdegrees){
		List<Point> points = new ArrayList<Point>();
		
		finalX = centerX + radioSpeedIni * (float)Math.cos(Math.toRadians(actualdegrees)); 
		finalY = centerY + radioSpeedIni * (float)Math.sin(Math.toRadians(actualdegrees));
		points.add(new Point((int)(finalX), (int)(finalY)));
		
		finalX = centerX + radioSpeedFin * (float)Math.cos(Math.toRadians(actualdegrees + 180 - 10)); 
		finalY = centerY + radioSpeedFin * (float)Math.sin(Math.toRadians(actualdegrees + 180 - 10));
		points.add(new Point((int)(finalX), (int)(finalY)));
		
		finalX = centerX + radioSpeedFin * (float)Math.cos(Math.toRadians(actualdegrees - 180 + 10)); 
		finalY = centerY + radioSpeedFin * (float)Math.sin(Math.toRadians(actualdegrees - 180 + 10));
		points.add(new Point((int)(finalX), (int)(finalY)));		
		
		createPath(points);
	}
	
	private void createPath(List<Point> points){
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
	}
	
	public void draw(Canvas canvas) {
		canvas.drawCircle(centerX, centerY, radioArrow, circleSimple);
		canvas.drawCircle(centerX, centerY, radioArrow, circleBlur);
		
		canvas.drawPath(path, arrowSimple);
		canvas.drawPath(path, arrowBlur);
	}

}
