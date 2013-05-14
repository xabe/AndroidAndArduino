package com.tutorial.bluetooth.util;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

/**
 * Clase que dibuja las fechas de girar a las izquierda o la derecha
 * @author Chabir Atrahouch
 *
 */
public class RotateWheel {
	private Paint backgroundSimple;
	private Polygon left;
	private Polygon right;

	
	
	public RotateWheel(float centerX, float centerY, float radioSpeed) {
		backgroundSimple = new Paint();
	    backgroundSimple.setAntiAlias(true);
	    backgroundSimple.setDither(true);
	    backgroundSimple.setColor(Color.parseColor("#FF7fff00"));
	    backgroundSimple.setStrokeWidth(1f);
	    backgroundSimple.setStyle(Paint.Style.FILL_AND_STROKE);
	    backgroundSimple.setStrokeJoin(Paint.Join.MITER);
	    backgroundSimple.setStrokeCap(Paint.Cap.BUTT);
		
		
		List<Point> pointsLeft = new ArrayList<Point>();
		List<Point> pointsRight = new ArrayList<Point>();
		
		//Calculado la flecha de la derecha		
		//
		//		    F
		//		    | \
		//      A---G  \
		//		|       \E
		//		B---C   /
		//	        |  /
		//          D /
		//
		pointsRight.add(new Point((int)(centerX + (radioSpeed + 60)), (int)(centerY + 20)));
		pointsRight.add(new Point((int)(centerX + (radioSpeed + 60)), (int)(centerY - 20)));
		
		pointsRight.add(new Point((int)(centerX + (radioSpeed + 60 + 40)), (int)(centerY - 20)));
		pointsRight.add(new Point((int)(centerX + (radioSpeed + 60 + 40)), (int)(centerY - 40)));
		
		pointsRight.add(new Point((int)(centerX + (radioSpeed + 60 + 40 + 40)), (int)(centerY)));
		
		pointsRight.add(new Point((int)(centerX + (radioSpeed + 60 + 40)), (int)(centerY + 40)));
		pointsRight.add(new Point((int)(centerX + (radioSpeed + 60 + 40)), (int)(centerY + 20)));
		right = new Polygon(pointsRight);
		right.setPaint(backgroundSimple);
		
		//Calculando la flecha de la izquierda
		//
		//		F
		//	  /	|
		//   /  G---A
		//E	/		|
		//	\	C---B
		//	 \  |
		//    \ D
		//
		pointsLeft.add(new Point((int)(centerX - (radioSpeed + 60)), (int)(centerY + 20)));
		pointsLeft.add(new Point((int)(centerX - (radioSpeed + 60)), (int)(centerY - 20)));
		
		pointsLeft.add(new Point((int)(centerX - (radioSpeed + 60 + 40)), (int)(centerY - 20)));
		pointsLeft.add(new Point((int)(centerX - (radioSpeed + 60 + 40)), (int)(centerY - 40)));
		
		pointsLeft.add(new Point((int)(centerX - (radioSpeed + 60 + 40 + 40)), (int)(centerY)));
		
		pointsLeft.add(new Point((int)(centerX - (radioSpeed + 60 + 40)), (int)(centerY + 40)));
		pointsLeft.add(new Point((int)(centerX - (radioSpeed + 60 + 40)), (int)(centerY + 20)));
		left = new Polygon(pointsLeft);
		left.setPaint(backgroundSimple);
		
		
	}
	
	public Polygon getLeft() {
		return left;
	}
	
	public Polygon getRight() {
		return right;
	}	
	
	public void setDraw(boolean draw){
		left.setShown(draw);
		right.setShown(draw);
	}
	
	public void draw(Canvas canvas) {
		left.draw(canvas);
		right.draw(canvas);
	}

}
