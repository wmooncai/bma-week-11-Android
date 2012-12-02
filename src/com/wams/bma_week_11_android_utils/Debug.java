/**
 * 
 */
package com.wams.bma_week_11_android_utils;

import android.util.Log;

/**
 * @author W. Mooncai
 * 
 * @since 0.0
 * 
 * Debugging class to output troubleshooting output to Console
 * 
 * Logging Levels are based on Syslog
 *
 */

// import android.util.Log;

public class Debug implements DebugInterface {

	/** Toggle Debugging ON or OFF */
	private boolean mToggle = DEBUG_OFF;
	
	/** Level of detail for debugging:
	 * 0	Emergency
	 * 1	Alert
	 * 2	Critical
	 * 3	Error
	 * 4	Warning
	 * 5	Notice
	 * 6	Informational
	 * 7	Debug
	 */
	public static int mLevel = DEBUG_LEVEL_DEBUG;
	
	/**
	 * Constructor
	 * 
	 * @param mToggle	Globally mToggle logging On / Off 
	 * @param mLevel		Global logging mLevel.  Messages of this mLevel or lower
	 * 					will be output to log.
	 *  
	 */
	public Debug(boolean toggle, int level) {
		setToggle(toggle);
		setLevel(level);
	}
	
	// ******************************** METHODS *******************************
	
	/**
	 * Output error message if Debugging is ON and debugging is at least this
	 *  mLevel of detail.
	 * 
	 * @since 0.0
	 * 
	 * @param lvl		Debugging Level
	 * @param message	Error message to log
	 */
	public void toLog(int lvl, String message) {
		
		if (mToggle == DEBUG_ON) toLog(null, lvl, message);
	}
		
	public void toLog(String tag, int lvl, String message) {
		
		if (mToggle == DEBUG_ON) {
			switch (lvl) {

			case DEBUG_LEVEL_WARNING :
			case DEBUG_LEVEL_NOTICE :
			case DEBUG_LEVEL_INFORMATIONAL :
				Log.i(tag, message);
				break;
				
			case DEBUG_LEVEL_DEBUG :
				Log.d(tag, message);
				break;
				
			case DEBUG_LEVEL_ALERT : 
			case DEBUG_LEVEL_CRITICAL :
			case DEBUG_LEVEL_ERROR :
			case DEBUG_LEVEL_EMERGENCY :
			default : 
				Log.e(tag, message);
				break;
			}
		}
		
	}

	// ******************************** GETTERS *******************************

	/**
	 * @since 0.0
	 * 
	 * @return		Value of global debug On / Off mToggle.
	 */
	public boolean getToggle () { return mToggle; }
	
	/**
	 * @since 0.0
	 * 
	 * @return		Value of the global logging mLevel setting.
	 */
	public int getLevel() { return mLevel; }

	// ******************************** SETTERS *******************************

	/**
	 * @since 0.0
	 * 
	 * @param lvl	Global logging mLevel.
	 */
	public void setLevel(int lvl) {
	
		if ( (lvl < DEBUG_LEVEL_DEBUG) && (lvl > DEBUG_LEVEL_EMERGENCY) )
			mLevel = lvl;
	}
	
	/**
	 * @since 0.0
	 * 
	 * @param		Global logging mLevel.
	 */
	public void setToggle(boolean turn) { mToggle = turn; }
	

}
