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
	
	private static final Debug d = new Debug(DEBUG_ON, DEBUG_LEVEL_DEBUG);
	private static final String TAG = "SolarSystemParserXML";
	
	private static CelestialBody sSolarSystem;
	private static int sMaxNameLen;
	
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
	
	//#########################################################################
	
    public SolarSystemParserXML(Context context, int xmlFile) {
        super(context, context.getResources().getXml(xmlFile));
        
        sMaxNameLen = mRes.getInteger(R.integer.max_name_len);
        
        sSolarSystem = new CelestialBody(sSolarSystem, sMaxNameLen);
        sSolarSystem.setupSatellites(1);
        sSolarSystem.setName(mRes.getString(R.string.solar_system));
        sSolarSystem.setParent(null);
        // sSolarSystem.setParentName(mRes.getString(R.string.black_hole));
    }
    
    @Override
    public void parse() {

    	d.toLog(TAG, DEBUG_LEVEL_DEBUG, "parseDetails() start");

    	// Parse XML file containing solar system and celestial body information
    	parseSolarSystemXML();
    	
    	// Derive and calculate internal member variables for each celestial body recursively.
    	sSolarSystem.setMinsMaxs(null);
    	

    	d.toLog(TAG, DEBUG_LEVEL_DEBUG, "parseDetails() end");

    	/*
    	new Thread(new Runnable() {
			@Override
			public void run() {
		        
	        	Looper.prepare();
		            
	        	sSolarSystem.addSatellite(parseDetails(sSolarSystem));
			}    
		}).start();
		*/
    }
 
    private void parseSolarSystemXML() {
    	
    	CelestialBody celestialBody = new CelestialBody(sSolarSystem, sMaxNameLen);
    	
        try {

            boolean done = false;
            
            int eventType = mParser.getEventType();
            String element = null;
            
	        while ((eventType != XmlPullParser.END_DOCUMENT) && !done) {
	        	// element = null;
	        	
	            switch (eventType){
	                case XmlPullParser.START_DOCUMENT:
	                    // do nothing
	                    break;
	                case XmlPullParser.START_TAG:
	                    element = mParser.getName();
	                    
	                    d.toLog(TAG, DEBUG_LEVEL_DEBUG
	                    	, "parseDetails() - START_TAG Element: " + element);
	                    
	                    if (element.equalsIgnoreCase(PLANET_TAG)) {
	                    	celestialBody = new CelestialBody(sSolarSystem, sMaxNameLen);

	                    } else if (element.equalsIgnoreCase(NAME_TAG)) {
	                        	celestialBody.setName(mParser.nextText());
	                        	
                        }else if (element.equalsIgnoreCase(PARENT_NAME_TAG)) {
                        	
                        	celestialBody.setParentName(mParser.nextText());
                        	
                        	celestialBody.setParent(sSolarSystem
                        								.getCelestialBody(null
                        								, celestialBody
                        									.getParentName()));
                        	
                        } else if (element.equalsIgnoreCase(RADIUS_TAG)) {
                        	celestialBody.setRadius(Float.parseFloat(mParser.nextText()));
                        	
                        } else if (element.equalsIgnoreCase(COLOR_TAG)) {
                        	celestialBody.setColor(mParser.nextText());
                        	
                        } else if (element.equalsIgnoreCase(PARENTAL_DISTANCE_TAG)) {
                        	celestialBody.setParentalDistance(Float.parseFloat(mParser.nextText()));
                        	
                        } else if (element.equalsIgnoreCase(YEAR_TAG)) {
                        	celestialBody.setYear(Integer.parseInt(mParser.nextText()));
                        	
                        } else if (element.equalsIgnoreCase(ORBITAL_VELOCITY_TAG)) {
                        	celestialBody.setOrbitalVelocity(Float.parseFloat(mParser.nextText()));
                        	
                        } else if (element.equalsIgnoreCase(SATELLITES_TAG)) {
                        	celestialBody.setupSatellites(Integer.parseInt(mParser.nextText()));
                        }
	                    break;
	                    
	                case XmlPullParser.END_TAG:

	                    element = mParser.getName();
	                	
	                	d.toLog(TAG, DEBUG_LEVEL_DEBUG, "END_TAG Name: " + mParser.getName());
	                	d.toLog(TAG, DEBUG_LEVEL_DEBUG, celestialBody.toString());
	                	
	                    if (element.equalsIgnoreCase(SOLAR_SYSTEM_TAG)) {
	                        done = true;
	                    } else if (element.equalsIgnoreCase(PLANET_TAG)) {
	                    	
	                    	if (celestialBody.getParentName() != null)
	                    		if (celestialBody.getName().equals("Sun")) {
	                    			celestialBody.setParent(sSolarSystem);
	                    			sSolarSystem.addSatellite(celestialBody);
	                    		} else if (!celestialBody.getParentName().equals("Solar System")) { // Not "Sun" CB
	                    			celestialBody.setParent(sSolarSystem.getCelestialBody(sSolarSystem, celestialBody.getParentName()));
	                    			celestialBody.getParent().addSatellite(celestialBody);
	                    		} 
	                    	

	                    	d.toLog(TAG, DEBUG_LEVEL_DEBUG, "END_TAG PLANET_TAG: "
	                    			+ celestialBody.toString());
	                    }
	                    
	                    // eventType = mParser.next();
	                    break;
		            }
	            eventType = mParser.next();
	        }
	        if (mInputXml != null)
	        	mInputXml.close();
	
	    } catch (IOException e) {
	    	d.toLog(TAG, DEBUG_LEVEL_DEBUG, "parseDetails() - Exception: " + e);
	    	e.printStackTrace();
	    } catch (XmlPullParserException e) {
	    	d.toLog(TAG, DEBUG_LEVEL_DEBUG, "parseDetails() XmlPullParserException: " + e);
			e.printStackTrace();
		}
    }

    // ################################# GETTERS ##############################
    
    public CelestialBody getSolarSystem() { return sSolarSystem; }
    
} // SolarSystemParserXML CLASS
