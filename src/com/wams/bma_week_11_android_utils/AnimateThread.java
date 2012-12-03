/**
 * UpdateThread
 */
package com.wams.bma_week_11_android_utils;

import android.graphics.Canvas;
import android.view.SurfaceHolder;



/**
 * @author W. Mooncai
 * @since 0.0
 * 
 * Thread to update graphics objects.
 *
 */
public class AnimateThread extends Thread implements DebugInterface {
	
	private static final Debug d = new Debug(DEBUG_ON, DEBUG_LEVEL_DEBUG);
	private static final String TAG = "AnimateThread";
	
	public static final boolean THREAD_STOP = false;
	public static final boolean THREAD_GO = true;
	
	private static final int mFps = 20;
	
	private static long mTime;
	private static boolean mToRun = false;
	private static SolarSystemView mSolarSystemView;
	private static SurfaceHolder mSurfaceHolder;

	// ########################## CONSTRUCTORS #################################

	public AnimateThread(SolarSystemView solarSystemView) {
		
		mSolarSystemView = solarSystemView;
		mSurfaceHolder = mSolarSystemView.getHolder();
	}
	
	// ############################# SETTERS ##################################

	public void setRunning(boolean run) {
		
		mToRun = run;
	}
	
	// ############################# METHODS ##################################

	@Override
	public void run() {
		
		try {
			Canvas canvas;
	
			while (mToRun == THREAD_GO) {
				
				long cTime = System.currentTimeMillis();
				if ((cTime = mTime) <= (1000 / mFps)) {
					canvas = null;
					try {
						canvas = mSurfaceHolder.lockCanvas(null);
						mSolarSystemView.updateView(canvas);
						mSolarSystemView.onDraw(canvas);
					} finally {
						if (canvas != null) {
							mSurfaceHolder.unlockCanvasAndPost(canvas);
						}
					}
				}
				mTime = cTime;
			}
		} catch (Exception e) {
			d.toLog(TAG, DEBUG_LEVEL_DEBUG, "run() thread Exception: " + e.toString());
		}
	}
	
} // AnimateThread CLASS
