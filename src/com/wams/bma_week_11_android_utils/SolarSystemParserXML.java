/**
 * 
 */
package com.wams.bma_week_11_android_utils;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.wams.bma_week_11_android.R;

import android.content.Context;
// import android.os.Looper;

/**
 * @author W. Mooncai
 * @since 0.0
 * 
 * SolarSystemParserXML
 * 
 * Downloads or reads from a file, and Parses XML containing solar system information
 * 
 * References:
 *  - http://mobile.tutsplus.com/tutorials/android/android-fundamentals-downloading-data-with-services/
 *  - http://www.ibm.com/developerworks/opensource/library/x-android/
 * 
 */

public class SolarSystemParserXML extends ParserXML implements DebugInterface, ParserXML_Interface {
	
	private static final Debug d = new Debug(true, DEBUG_LEVEL_DEBUG);
	private static final String TAG = "SolarSystemParserXML";
	
    // XML tag constants
	private final static String SOLAR_SYSTEM_TAG = "solarSystem";
	private final static String PLANET_TAG = "planet";
	private final static String NAME_TAG = "name";
	private final static String PARENT_NAME_TAG = "parent";
	private final static String RADIUS_TAG = "radius";
	private final static String COLOR_TAG = "color";
	private final static String PARENTAL_DISTANCE_TAG = "parentalDistance";
	private final static String YEAR_TAG = "year";
	private final static String ORBITAL_VELOCITY_TAG = "orbitalVelocity";
	private final static String SATELLITES_TAG = "satellites";
	
	private CelestialBody mSolarSystem;
	
	private int mMaxNameLen;
	
	//#########################################################################
	
    public SolarSystemParserXML(Context context, int xmlFile) {
        super(context, context.getResources().getXml(xmlFile));
        
        mMaxNameLen = mRes.getInteger(R.integer.max_name_len);
        
        mSolarSystem = new CelestialBody(mMaxNameLen);
        mSolarSystem.setSatellites(1);
        mSolarSystem.setName(mRes.getString(R.string.solar_system));
        mSolarSystem.setParentName(mRes.getString(R.string.black_hole));
    }
    
    @Override
    public void parse() {

    	d.toLog(TAG, DEBUG_LEVEL_DEBUG, "parse() start");

    	mSolarSystem.addSatellite(parseDetails(mSolarSystem));
    	
    	/*
    	new Thread(new Runnable() {
			@Override
			public void run() {
		        
	        	Looper.prepare();
		            
	        	mSolarSystem.addSatellite(parseDetails(mSolarSystem));
			}    
		}).start();
		*/
    }
 
    private CelestialBody parseDetails(CelestialBody parentCB) {

    	CelestialBody celestialBody = new CelestialBody(mMaxNameLen);
    	
        try {        
            String eventName = new String();

            boolean done = false;
            
            int eventType = mParser.getEventType();
            
	        while ((eventType != XmlPullParser.END_DOCUMENT) && !done) {
	        	
	            switch (eventType){
	                case XmlPullParser.START_DOCUMENT:
	                    // do nothing
	                    break;
	                case XmlPullParser.START_TAG:
	                    eventName = mParser.getName();
	                    d.toLog(TAG, DEBUG_LEVEL_DEBUG
	                    	, "parse() - Element: " + eventName);
	                    if (eventName.equalsIgnoreCase(PLANET_TAG)) {
	                    	// do nothing
	                    } else {
	                        if (eventName.equalsIgnoreCase(NAME_TAG)) {
	                        	celestialBody.setName(mParser.nextText());
	                        	
	                        }else if (eventName.equalsIgnoreCase(PARENT_NAME_TAG)) {
	                        	celestialBody.setParentName(mParser.nextText());
	                        	celestialBody.setParent(celestialBody
	                        			.getCelestialBody(mSolarSystem
	                        								, celestialBody.getParentName()));
	                        	
	                        } else if (eventName.equalsIgnoreCase(RADIUS_TAG)) {
	                        	celestialBody.setRadius(Float.parseFloat(mParser.nextText()));
	                        	
	                        } else if (eventName.equalsIgnoreCase(COLOR_TAG)) {
	                        	celestialBody.setColor(mParser.nextText());
	                        	
	                        } else if (eventName.equalsIgnoreCase(PARENTAL_DISTANCE_TAG)) {
	                        	celestialBody.setParentalDistance(Float.parseFloat(mParser.nextText()));
	                        	
	                        } else if (eventName.equalsIgnoreCase(YEAR_TAG)) {
	                        	celestialBody.setYear(Integer.parseInt(mParser.nextText()));
	                        	
	                        } else if (eventName.equalsIgnoreCase(ORBITAL_VELOCITY_TAG)) {
	                        	celestialBody.setOrbitalVelocity(Float.parseFloat(mParser.nextText()));
	                        	
	                        } else if (eventName.equalsIgnoreCase(SATELLITES_TAG)) {
	                        	celestialBody.setSatellites(Integer.parseInt(mParser.nextText()));
	                        }
	                    }
	                    break;
	                    
	                case XmlPullParser.END_TAG:
	                	
	                    if (eventName.equalsIgnoreCase(SOLAR_SYSTEM_TAG)){
	                        done = true;
	                    } else if (eventName.equalsIgnoreCase(PLANET_TAG)) {
	                    	eventType = mParser.next();
	                    	
	                    	mSolarSystem.getCelestialBody(mSolarSystem, celestialBody.getParentName())
	                    		.addSatellite(celestialBody);
	                    }
	                    break;
		            }
	            eventType = mParser.next();
	        }
	        if (mInputXml != null) mInputXml.close();
	
	    } catch (IOException e) {
	    	d.toLog(TAG, DEBUG_LEVEL_DEBUG, "parse() - Exception: " + e);
	    	e.printStackTrace();
	    } catch (XmlPullParserException e) {
	    	d.toLog(TAG, DEBUG_LEVEL_DEBUG, "parse() XmlPullParserException: " + e);
			e.printStackTrace();
		}
        return celestialBody;
    }

    // ################################# GETTERS ##############################
    
    public CelestialBody getSolarSystem() { return mSolarSystem; }
    
} // SolarSystemParserXML CLASS
