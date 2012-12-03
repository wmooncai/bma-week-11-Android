/**
 * Movement
 */
package com.wams.bma_week_11_android_utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * @author W. Mooncai
 * @since 0.0
 * 
 * 
 * Reference:
 *  - http://mobile.tutsplus.com/tutorials/android/android-sdk-achieving-movement/
 *  - http://stackoverflow.com/questions/9478192/android-tap-image-to-have-it-scale-and-optionally-rotate-to-fill-screen
 *  - http://stackoverflow.com/questions/3051643/how-to-rotate-particular-image-among-multiple-images-drawn-to-canvas-in-android
 *  
 */
public class SolarSystemView extends SurfaceView implements SurfaceHolder.Callback, DebugInterface {

	private Debug d = new Debug(DEBUG_ON, DEBUG_LEVEL_DEBUG);
	private static final String TAG = "SolarSystemView";
	
	// Substring index locations for Hex color String
	private static final int AS = 0;
	private static final int AE = 2;
	private static final int RS = 2;
	private static final int RE = 4;
	private static final int GS = 4;
	private static final int GE = 6;
	private static final int BS = 6;
	private static final int BE = 8;
	
	public static final String NAME_SUN = "Sun";
	
	private static int mXmlPlanetFile;
	
	private static float mDensity = 0;
	
	private static CelestialBody sSolarSystem;
	
	private int mScreenWidth;
	private int mScreenHeight;
	private float mPositionX = 150.0f;
	private float mPositionY = 150.0f;
	private float mRotateDegreesVelocity = 0.0f;
	private float mCelestialBodyRadius = 10.0f;
	
	private static Paint mPaint;
	private static String mColorString;
	
	AnimateThread animateThread;
	
	// Matrix matrix = new Matrix();
	
	// ########################## CONSTRUCTORS #################################

	public SolarSystemView(Context context) { super(context); }
	
	public SolarSystemView(Context context, int xmlFile) {
		super(context);
		
		mXmlPlanetFile = xmlFile;
		
        SolarSystemParserXML solarSystemParserXML
    		= new SolarSystemParserXML(context, mXmlPlanetFile);
	    solarSystemParserXML.parse();
	    
	    sSolarSystem = solarSystemParserXML.getSolarSystem();
	    
	    sSolarSystem.setCalcedVarsG(null, mDensity);
	    
	    mPaint = new Paint();
/*
		if (sSolarSystem.getName() == NAME_SUN ) {
			sSolarSystem.setLocation(mScreenWidth/2, mScreenHeight/2);
			sSolarSystem.setRadius((mScreenWidth/4) - sSolarSystem.getRadius());
			d.toLog(TAG, DEBUG_LEVEL_DEBUG, "SolarSystem setLocation()");

		} else {
*/
		getHolder().addCallback(this);

		
//		d.toLog(TAG, DEBUG_LEVEL_DEBUG, "mScreenWidth: " + mScreenWidth + " / mScreenHeight: " + mScreenHeight);

//		}
	}
	
	// ############################## GETTERS #################################

	public int getXmlPlanetFile() { return mXmlPlanetFile; }
	
	public int getScreenHeight() { return mScreenHeight; }
	
	public int getScreenWidth() { return mScreenWidth; }

	// ############################## METHODS #################################

	@Override
	protected void onDraw(Canvas canvas) {

		super.onDraw(canvas);
		
		canvas.drawColor(Color.BLACK);

		CelestialBody sun = sSolarSystem.getCelestialBody(null, NAME_SUN);
		
		// Calculate internal member graphics variables for each celestial body recursively.
		sSolarSystem.setCalcedVarsG(null, mDensity);
		
		canvas.rotate(mRotateDegreesVelocity, sun.getLocationX(), sun.getLocationY());

		// canvas.translate(50.0f, 50.0f);
		// mCelestialBodyRadius = sun.getRadiusG();
		
		mColorString = sun.getColor();
		mPaint.setColor(Color.argb(Integer.parseInt(mColorString.substring(AS, AE), 16)
									, Integer.parseInt(mColorString.substring(RS, RE), 16)
									, Integer.parseInt(mColorString.substring(GS, GE), 16)
									, Integer.parseInt(mColorString.substring(BS, BE), 16)));
		
		canvas.drawCircle(sun.getLocationX(), sun.getLocationY(), ((sun.getRadiusG() > 0) ? sun.getRadiusG() : 20), mPaint);
		d.toLog(TAG, DEBUG_LEVEL_DEBUG, "***** Sun: X: " + sun.getLocationX() + " | Y: " + sun.getLocationY()
				+ " | R: " + sun.getRadiusG() + " | ParentalDist: " + sun.getParentalDistanceG());
		
		
		// Baseline debugging Sun
		mPaint.setColor(Color.BLUE);
		canvas.drawCircle(200, 200, 10, mPaint);
		
		mPaint.setColor(Color.LTGRAY);
		canvas.drawCircle(150, 150, 5, mPaint);

	}
	
	public void updateView(Canvas canvas) {
		
		if (mRotateDegreesVelocity == 360) mRotateDegreesVelocity = 1;
			else mRotateDegreesVelocity += 2 ;
		
//		mPositionX += mVelocity;
//		mPositionY += mVelocity;
	}
	
	public void surfaceCreated(SurfaceHolder holder) {
		
		Rect surfaceFrame = holder.getSurfaceFrame();
		
		mScreenWidth = surfaceFrame.width();
		mScreenHeight = surfaceFrame.height();
		
		mPositionX = mScreenWidth / 2;
		mPositionY = mScreenHeight / 2;
		
		animateThread = new AnimateThread(this);
		animateThread.setRunning(true);
		animateThread.start();
	}
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		
	}
	
	public void surfaceDestroyed(SurfaceHolder holder) {
		
		boolean retry = true;
		animateThread.setRunning(false);
		while (retry) {
			try {
				animateThread.join();
				retry = false;
			} catch (InterruptedException e) {
			}
		}
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {

		super.onSizeChanged(w, h, oldw, oldh);
			
		mScreenWidth = w;
		mScreenHeight = h;
	
		mDensity = getResources().getDisplayMetrics().density;
		
		sSolarSystem.getCelestialBody(null, NAME_SUN).setLocation(mScreenWidth/2, mScreenWidth/2);
		d.toLog(TAG,  DEBUG_LEVEL_DEBUG, "onSizeChanged() - sun.locX: "
				+ sSolarSystem.getCelestialBody(null, "Sun").getLocationX() + " | sun.locY: "
				+ sSolarSystem.getCelestialBody(null, "Sun").getLocationY());

		
		d.toLog(TAG, DEBUG_LEVEL_INFORMATIONAL
				, "onSizeChanged(): width " + mScreenWidth
				+ " / mScreenHeight: " + mScreenHeight);
	}
	
}
