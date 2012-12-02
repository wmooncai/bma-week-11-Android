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
public class AnimateThread extends Thread {

	private long mTime;
	private final int mFps = 10;
	private boolean mToRun = false;
	private SolarSystemView mSolarSystemView;
	private SurfaceHolder mSurfaceHolder;

	// ########################## CONSTRUCTORS #################################

	public AnimateThread(SolarSystemView rSolarSystemView) {
		
		mSolarSystemView = rSolarSystemView;
		mSurfaceHolder = mSolarSystemView.getHolder();
	}
	
	// ############################# SETTERS ##################################

	public void setRunning(boolean run) {
		
		mToRun = run;
	}
	
	// ############################# METHODS ##################################

	@Override
	public void run() {
		
		Canvas canvas;
		
		while (mToRun) {
			
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
		
	}
	
} // AnimateThread CLASS
