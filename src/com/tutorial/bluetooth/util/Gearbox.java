package com.tutorial.bluetooth.util;

import java.util.ArrayList;
import java.util.List;

import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;

/**
 * Clase que dibuja la caja de cambios 
 * @author Chabir Atrahouch
 *
 */
public class Gearbox {
	private Paint simple;
	private Paint blur;
	private Paint selectBackground;
	private Paint textPaintSelect;
	private Paint textPaint;
	
	private Polygon positionD;	
	private Polygon positionDSelect;
	private Text textD;	
	
	private Polygon positionN;
	private Polygon positionNSelect;
	private Text textN;	
	
	private Polygon positionR;
	private Polygon positionRSelect;
	private Text textR;	
	
	private int initX;
	private int initY;
	
	private int finX;
	private int finY;
	
	private int divisiones;
	
	private int selectPosition = Constants.POSITION_N;
	
	public Gearbox(int initX, int initY, int finY, Typeface myTypeface) {	  
		this.initX = initX;
		this.initY = initY;
		
		this.finX = initX + Constants.MARGIN_GEARBOX;
		this.finY = finY;
		
		simple = new Paint();
	    simple.setAntiAlias(true);
		simple.setDither(true);
		simple.setColor(Color.argb(248, 255, 255, 255));
		simple.setStrokeWidth(4f);
	    simple.setStyle(Paint.Style.STROKE);
	    simple.setStrokeJoin(Paint.Join.MITER);
	    simple.setStrokeCap(Paint.Cap.BUTT);
	    
	    blur = new Paint();
	    blur.set(simple);
	    blur.setColor(Color.parseColor("#FFA9A9A9"));
	    blur.setStrokeWidth(15f);
	    blur.setMaskFilter(new BlurMaskFilter(15, BlurMaskFilter.Blur.SOLID));	
	    
	    selectBackground = new Paint();
	    selectBackground.setAntiAlias(true);
	    selectBackground.setDither(true);
	    selectBackground.setColor(Color.parseColor("#FF7fff00"));
		selectBackground.setStrokeWidth(1f);
		selectBackground.setStyle(Paint.Style.FILL_AND_STROKE);
		selectBackground.setStrokeJoin(Paint.Join.MITER);
		selectBackground.setStrokeCap(Paint.Cap.BUTT);
	    
		
		textPaintSelect = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
		textPaintSelect.setAntiAlias(true);
		textPaintSelect.setStyle(Paint.Style.FILL);
		textPaintSelect.setTextAlign(Paint.Align.CENTER);
		textPaintSelect.setColor(Color.BLACK);
		textPaintSelect.setTypeface(myTypeface);
		textPaintSelect.setTextSize(Constants.SIZE_TEXT_3);
		
		textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
		textPaint.setAntiAlias(true);
		textPaint.setStyle(Paint.Style.FILL);
		textPaint.setTextAlign(Paint.Align.CENTER);
		textPaint.setColor(Color.WHITE);
		textPaint.setTypeface(myTypeface);
		textPaint.setTextSize(Constants.SIZE_TEXT_3);
		
	    List<Point> points = new ArrayList<Point>();
	    
	    divisiones = (finY - initY) / 3;
	    int positionXtext = initX  + (Constants.MARGIN_GEARBOX  / 2);
	   
	    //
	    //
	    //  1----2
	    //  |    |
	    //  3----4
	    //  |    |
	    //  5----6
	    //  |    |
	    //  7----8
	    //
	    //
	    points.add(new Point(initX, initY));  //1
	    points.add(new Point(finX, initY)); // 2	
	    
	    points.add(new Point(finX, initY + divisiones)); //4
	    points.add(new Point(initX, initY + divisiones)); //3
	    
	    positionD = new Polygon(points);	
	    positionD.setPaint(blur);
	    positionD.setShown(true);
	    
	    positionDSelect = new Polygon(points);	
	    positionDSelect.setPaint(selectBackground);
	    positionDSelect.setShown(false);
	    
	    textD = new Text();
	    textD.setPaint(textPaint);
	    textD.setText("D");
	    textD.setX(positionXtext);
	    textD.setY((initY + divisiones) - Constants.MARGIN_TEXT_GEARBOX);
	    
	    points.clear();
	    
	    points.add(new Point(initX, initY + divisiones)); //3
	    points.add(new Point(finX, initY + divisiones)); //4
	    
	    points.add(new Point(finX, initY + (divisiones * 2)));  //6
	    points.add(new Point(initX, initY + (divisiones * 2))); //5
	    
	    positionN = new Polygon(points);	
	    positionN.setPaint(blur);
	    positionN.setShown(true);
	    
	    positionNSelect = new Polygon(points);	
	    positionNSelect.setPaint(selectBackground);
	    positionNSelect.setShown(false);
	    
	    textN = new Text();
	    textN.setPaint(textPaint);
	    textN.setText("N");
	    textN.setX(positionXtext);
	    textN.setY((initY + (divisiones * 2)) - Constants.MARGIN_TEXT_GEARBOX);
	    
	    points.clear();
	    
	    points.add(new Point(initX, initY + (divisiones * 2))); //5
	    points.add(new Point(finX, initY + (divisiones * 2)));  //6
	    
	    points.add(new Point(finX, finY)); //8
	    points.add(new Point(initX, finY)); //7
	    
	    positionR = new Polygon(points);	
	    positionR.setPaint(blur);
	    positionR.setShown(true);
	    
	    positionRSelect = new Polygon(points);	
	    positionRSelect.setPaint(selectBackground);
	    positionRSelect.setShown(false);
	    
	    textR = new Text();
	    textR.setPaint(textPaint);
	    textR.setText("R");
	    textR.setX(positionXtext);
	    textR.setY(finY - Constants.MARGIN_TEXT_GEARBOX);

	}
	
	public void update(int x, int y){
		if((x <= finX && x >= initX)  &&
			(y <= finY && y >= initY))
		{
			if(y <= (initY + divisiones) && y >= initY)
			{
				selectPosition = Constants.POSITION_D;
			}
			else if(y <= (initY + (divisiones * 2)) && y >= initY)
			{
				selectPosition = Constants.POSITION_N;
			}
			else
			{
				selectPosition = Constants.POSITION_R;
			}			
		}
	}
	
	public String getSelectPosition() {
		String result = "";
		switch (selectPosition) {
		case Constants.POSITION_D:	result = Constants.FORWARD;
			break;
		case Constants.POSITION_N:	result = Constants.STOP;	
			break;
		case Constants.POSITION_R:	result = Constants.BACKkWARD;
			break;		
		}
		return result;
	}
	
	public void draw(Canvas canvas) {	
		positionDSelect.setShown(false);
		positionNSelect.setShown(false);
		positionRSelect.setShown(false);
		switch (selectPosition) {
		case Constants.POSITION_D:	positionDSelect.setShown(true);
									break;
		case Constants.POSITION_N:	positionNSelect.setShown(true);			
									break;
		case Constants.POSITION_R:	positionRSelect.setShown(true);	
									break;		
		}
		positionDSelect.draw(canvas);
		positionNSelect.draw(canvas);
		positionRSelect.draw(canvas);
		positionD.draw(canvas);
		positionN.draw(canvas);
		positionR.draw(canvas);
		
		textD.setPaint(textPaint);
		textN.setPaint(textPaint);
		textR.setPaint(textPaint);
		switch (selectPosition) {
		case Constants.POSITION_D:	textD.setPaint(textPaintSelect);
									break;
		case Constants.POSITION_N:	textN.setPaint(textPaintSelect);
									break;
		case Constants.POSITION_R:	textR.setPaint(textPaintSelect);
									break;		
		}
		textD.draw(canvas);
		textN.draw(canvas);
		textR.draw(canvas);
	}

}
