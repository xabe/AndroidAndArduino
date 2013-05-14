package com.tutorial.bluetooth.util;

import java.util.ArrayList;
import java.util.List;

import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Shader;
import android.graphics.Typeface;

/**
 * Clase que dibuja el velocimetro del coche 
 * @author Chabir Atrahouch
 *
 */
public class SpeedoMeter {
	private Paint backgroundSimple;
	private Paint backgroundBlur;
	
	private Paint backgroundSimpleSingle;
	private Paint backgroundBlurSingle;
	
	private Paint backgroundSimpleSamll;
	private Paint backgroundBlurSamll;
	
	private Paint infoBackground;
	
	private Paint velocityBackground;
	
	private Paint textVelocityPaint;
	private Paint textInfoPaint;
	
	private List<Dial> pointsVelocity;
	private List<Text> textsVelocity;
	private Arrow arrow;
	private Polygon info;
	private Polygon velocity;
	private Text textInfoAcutalKm;
	private Text textInfoTotalKm;
	
	private int actualdegrees = Constants.INIT_VELOCITY_DEGREES;
	private int maxdegress = 0;
	private float centerX;
	private float centerY;
	private float radioSpeed;
	
	public SpeedoMeter(float centerX, float centerY, int maxVelocity, int minVelocity, int with, float radioSpeed,Typeface myTypeface) {
		this.centerX = centerX;
		this.centerY = centerY;
		this.radioSpeed = radioSpeed;
		
		//-----Estilos para la lineas gruesa-------//
		
		backgroundSimple = new Paint();
	    backgroundSimple.setAntiAlias(true);
	    backgroundSimple.setDither(true);
	    backgroundSimple.setColor(Color.argb(248, 255, 255, 255));
	    backgroundSimple.setStrokeWidth(7f);
	    backgroundSimple.setStyle(Paint.Style.STROKE);
	    backgroundSimple.setStrokeJoin(Paint.Join.MITER);
	    backgroundSimple.setStrokeCap(Paint.Cap.BUTT);
	    
	    backgroundBlur = new Paint();
	    backgroundBlur.set(backgroundSimple);
	    backgroundBlur.setColor(Color.parseColor("#FFA9A9A9"));
	    backgroundBlur.setStrokeWidth(15f);
	    backgroundBlur.setMaskFilter(new BlurMaskFilter(15, BlurMaskFilter.Blur.SOLID));	    
	    
	  //-----Estilos para la lineas medianas-------//
	    
	    backgroundSimpleSingle = new Paint();
	    backgroundSimpleSingle.setAntiAlias(true);
	    backgroundSimpleSingle.setDither(true);
	    backgroundSimpleSingle.setColor(Color.argb(248, 255, 255, 255));
	    backgroundSimpleSingle.setStrokeWidth(5f);
	    backgroundSimpleSingle.setStyle(Paint.Style.STROKE);
	    backgroundSimpleSingle.setStrokeJoin(Paint.Join.MITER);
	    backgroundSimpleSingle.setStrokeCap(Paint.Cap.BUTT);
	    
	    backgroundBlurSingle = new Paint();
	    backgroundBlurSingle.set(backgroundSimpleSingle);
	    backgroundBlurSingle.setColor(Color.parseColor("#FFA9A9A9"));
	    backgroundBlurSingle.setStrokeWidth(10f);
	    backgroundBlurSingle.setMaskFilter(new BlurMaskFilter(15, BlurMaskFilter.Blur.SOLID));
	    
	    //-----Estilos para la lineas pequeñas-------//
	    
	    backgroundSimpleSamll = new Paint();
	    backgroundSimpleSamll.setAntiAlias(true);
	    backgroundSimpleSamll.setDither(true);
	    backgroundSimpleSamll.setColor(Color.argb(248, 255, 255, 255));
	    backgroundSimpleSamll.setStrokeWidth(2f);
	    backgroundSimpleSamll.setStyle(Paint.Style.STROKE);
	    backgroundSimpleSamll.setStrokeJoin(Paint.Join.MITER);
	    backgroundSimpleSamll.setStrokeCap(Paint.Cap.BUTT);
	    
	    backgroundBlurSamll = new Paint();
	    backgroundBlurSamll.set(backgroundSimpleSamll);
	    backgroundBlurSamll.setColor(Color.parseColor("#FFA9A9A9"));
	    backgroundBlurSamll.setStrokeWidth(5f);
	    backgroundBlurSamll.setMaskFilter(new BlurMaskFilter(15, BlurMaskFilter.Blur.SOLID));
	    
	    //-----Estilos para la texto del velocimetro-------//
	    
	    textVelocityPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
	    textVelocityPaint.setAntiAlias(true);
	    textVelocityPaint.setStyle(Paint.Style.FILL);
	    textVelocityPaint.setTextAlign(Paint.Align.CENTER);
	    textVelocityPaint.setColor(Color.WHITE);
	    textVelocityPaint.setTextSize(Constants.SIZE_TEXT);
	    
	    //-----Estilos para la texto de la información-------//
	    
	    textInfoPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
	    textInfoPaint.setAntiAlias(true);
	    textInfoPaint.setStyle(Paint.Style.FILL);
	    textInfoPaint.setTextAlign(Paint.Align.RIGHT);
	    textInfoPaint.setColor(Color.BLACK);
	    textInfoPaint.setTypeface(myTypeface);
	    textInfoPaint.setTextSize(Constants.SIZE_TEXT_2);
	    
	    arrow = new Arrow(centerX, centerY, radioSpeed - 15);
	    
	    pointsVelocity = new ArrayList<Dial>();
	    textsVelocity = new ArrayList<Text>();
	    
		Dial dail;
		Text text;
		int distance;
		int valor;
		
		//Creamos el velocimetro
		for(int i = 0; i < 53; i++){
			dail = new Dial();
			
			valor = i * Constants.ADD_VELOCITY;
			if( valor % 20 == 0)
			{
				distance = 40;
				dail.setBackgroundSimple(backgroundSimple);
				dail.setBackgroundBlur(backgroundBlur);
				text = new Text();
				text.setPaint(textVelocityPaint);
				text.setX(centerX + (radioSpeed - Constants.SIZE_TEXT - 40) * (float)Math.cos(Math.toRadians(actualdegrees)));
				text.setY(centerY + (radioSpeed - Constants.SIZE_TEXT - 40) * (float)Math.sin(Math.toRadians(actualdegrees)));			
				text.setText("" + valor);
				textsVelocity.add(text);
			}
			else if( valor % 10 == 0)
			{
				distance = 20;
				dail.setBackgroundSimple(backgroundSimpleSamll);
				dail.setBackgroundBlur(backgroundBlurSamll);
			}
			else
			{
				distance = 10;
				dail.setBackgroundSimple(backgroundSimpleSamll);
				dail.setBackgroundBlur(backgroundBlurSamll);
			}
			dail.setXini(centerX + radioSpeed * (float)Math.cos(Math.toRadians(actualdegrees)));
			dail.setYini(centerY + radioSpeed * (float)Math.sin(Math.toRadians(actualdegrees)));
			
			dail.setXfin(centerX + (radioSpeed - distance) * (float)Math.cos(Math.toRadians(actualdegrees)));
			dail.setYfin(centerY + (radioSpeed - distance) * (float)Math.sin(Math.toRadians(actualdegrees)));

		
			
			actualdegrees += Constants.ADD_VELOCITY;
			pointsVelocity.add(dail);
		}
		
		maxdegress = actualdegrees - Constants.ADD_VELOCITY;
		actualdegrees = Constants.INIT_VELOCITY_DEGREES;
		
		//Creamos informacion del cuenta kilometros
		
		//
		// a----------b
		// |          |
		// |          |
		// d----------c
		//
		List<Point> points = new ArrayList<Point>();
		
		float xa = (centerX + (radioSpeed / 2) * (float)Math.cos(Math.toRadians(actualdegrees))) + 20;
		float ya = centerY + (radioSpeed / 2) * (float)Math.sin(Math.toRadians(actualdegrees));
		float xb = centerX + (radioSpeed / 2) * (float)Math.cos(Math.toRadians(45));
		float yc = (centerY + (radioSpeed) * (float)Math.sin(Math.toRadians(90))) - 70 ;		
		
		points.add(new Point((int)(xa), (int)(ya)));		
		points.add(new Point((int)(xb), (int)(ya)));	
		
		points.add(new Point((int)(xb), (int)(yc)));		
		points.add(new Point((int)(xa), (int)(yc)));
		
		info = new Polygon(points);		
		infoBackground = new Paint();
		infoBackground.setAntiAlias(true);
		infoBackground.setDither(true);
		infoBackground.setColor(Color.parseColor("#FF7fff00"));
		infoBackground.setStrokeWidth(1f);
		infoBackground.setStyle(Paint.Style.FILL_AND_STROKE);
		infoBackground.setStrokeJoin(Paint.Join.MITER);
		infoBackground.setStrokeCap(Paint.Cap.BUTT);
		info.setPaint(infoBackground);
		info.setShown(true);
		
		textInfoAcutalKm = new Text();
		textInfoAcutalKm.setPaint(textInfoPaint);
		textInfoAcutalKm.setText("220.1");
		textInfoAcutalKm.setX(xb);
		textInfoAcutalKm.setY(ya + ((yc - ya)/2));
		
		textInfoTotalKm = new Text();
		textInfoTotalKm.setPaint(textInfoPaint);
		textInfoTotalKm.setText("150220");
		textInfoTotalKm.setX(xb);
		textInfoTotalKm.setY(yc);
		
		
		//Creamos el marcado para el dedo de la velocidad
		points.clear();
		points.add(new Point(with, maxVelocity));
		points.add(new Point(with - Constants.MARGIN_VELOCITY, maxVelocity));
		points.add(new Point(with - Constants.MARGIN_VELOCITY, minVelocity));
		velocity = new Polygon(points);
		velocityBackground = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
		velocityBackground.setStyle(Paint.Style.FILL_AND_STROKE);
		velocityBackground.setShader(new LinearGradient(with - Constants.MARGIN_VELOCITY, minVelocity, 
														with - Constants.MARGIN_VELOCITY, maxVelocity, 														
														Color.WHITE, 
														Color.parseColor("#FF7fff00"), 
														Shader.TileMode.CLAMP));
		velocity.setPaint(velocityBackground);
		velocity.setShown(true);
	
	}

	
	public int getActualdegrees() {
		return actualdegrees;
	}
	
	public void setActualdegrees(int actualdegrees) {
		this.actualdegrees = actualdegrees;
	}
	
	public void setActualdegreesAdd(int actualdegrees) {
		this.actualdegrees += actualdegrees;
	}
	
	public void setActualdegreesDecrease(int actualdegrees) {
		this.actualdegrees -= actualdegrees;
	}
	
	public int getMaxdegress() {
		return maxdegress;
	}
	
	public void update(){
		arrow.update(actualdegrees);
	}
	
	private void drawVelocity(Canvas canvas){		
		for(Dial dial : pointsVelocity){
			dial.draw(canvas);
		}
	}
	
	private void drawTextVelocity(Canvas canvas){		
		for(Text text : textsVelocity){
			text.draw(canvas);
		}
	}
	
	public void draw(Canvas canvas){
		drawVelocity(canvas);
		canvas.drawCircle(centerX, centerY, radioSpeed, backgroundSimple);
		canvas.drawCircle(centerX, centerY, radioSpeed, backgroundBlur);		
		drawTextVelocity(canvas);
		arrow.draw(canvas);
		info.draw(canvas);
		textInfoAcutalKm.draw(canvas);
		textInfoTotalKm.draw(canvas);
		velocity.draw(canvas);
	}

}
