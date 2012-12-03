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
	
	private static final String NAME_SUN = "Sun";
	
	private static int mXmlPlanetFile;
	
	private CelestialBody mSolarSystem;
	
	private int mScreenWidth;
	private int mScreenHeight;
	private float mPositionX = 150.0f;
	private float mPositionY = 150.0f;
	private float mRotateDegreesVelocity = 0.0f;
	private float mCelestialBodyRadius = 10.0f;
	private Paint CelestialBodyPaint;
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
	    
	    mSolarSystem = solarSystemParserXML.getSolarSystem();

		if (mSolarSystem.getName() == NAME_SUN ) {
			mSolarSystem.setLocation(mScreenWidth/2, mScreenHeight/2);
			mSolarSystem.setRadius((mScreenWidth/4) - mSolarSystem.getRadius());
			d.toLog(TAG, DEBUG_LEVEL_DEBUG, "SolarSystem setLocation()");

		} else {
		getHolder().addCallback(this);
		mCelestialBodyRadius = 10.0f;
		CelestialBodyPaint = new Paint();
		CelestialBodyPaint.setColor(Color.WHITE);
		
		d.toLog(TAG, DEBUG_LEVEL_DEBUG, "mScreenWidth: " + mScreenWidth + " / mScreenHeight: " + mScreenHeight);

		}
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

		canvas.rotate(mRotateDegreesVelocity, mPositionX, mPositionY);

		// canvas.translate(50.0f, 50.0f);
		canvas.drawCircle(0, 0, mCelestialBodyRadius, CelestialBodyPaint);
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
	
		d.toLog(TAG, DEBUG_LEVEL_INFORMATIONAL
				, "onSizeChanged(): width " + mScreenWidth
				+ " / mScreenHeight: " + mScreenHeight);
	}
	
}
