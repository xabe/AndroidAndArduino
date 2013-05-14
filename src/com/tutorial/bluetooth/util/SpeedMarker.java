package com.tutorial.bluetooth.util;

import java.util.ArrayList;
import java.util.List;

import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Shader;

/**
 * Clase que dibuja la velocidad del coche
 * @author Chabir Atrahouch
 *
 */
public class SpeedMarker {	
	private Paint simple;
	private Paint blur;
	
	private int initX;
	private int initY;
	private Path path;
	
	
	public SpeedMarker(int x, int y, int maxVelocity, int minVelocity, int with) {	  
		initX = x;
		initY = y;
	    simple = new Paint();
	    simple.setAntiAlias(true);
		simple.setDither(true);
		simple.setColor(Color.argb(248, 255, 255, 255));
		simple.setStrokeWidth(4f);
	    simple.setStyle(Paint.Style.FILL_AND_STROKE);
	    simple.setStrokeJoin(Paint.Join.MITER);
	    simple.setStrokeCap(Paint.Cap.BUTT);
	    
	    blur = new Paint();
	    blur.set(simple);
	    blur.setStrokeWidth(3f);
	    blur.setMaskFilter(new BlurMaskFilter(15, BlurMaskFilter.Blur.SOLID));
	    blur.setShader(new LinearGradient(with - Constants.MARGIN_VELOCITY, minVelocity, 
				with - Constants.MARGIN_VELOCITY, maxVelocity, 														
				Color.WHITE, 
				Color.parseColor("#FFFF0000"), 
				Shader.TileMode.CLAMP));
	    update(initY);
	}
	
	
	public void update(int actualY){
		List<Point> points = new ArrayList<Point>();
		
		//Punto de abajo 
		points.add(new Point(initX, initY));
		
		//Punto de que esta la velocidad
		points.add(new Point(initX, actualY));
		
		float finalX = initX + ((initY - actualY) * (float)Math.sin(Math.toRadians(10.8))); 
		points.add(new Point((int)(finalX), actualY));		
		
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
		canvas.drawPath(path, simple);
		canvas.drawPath(path, blur);
	}

}
