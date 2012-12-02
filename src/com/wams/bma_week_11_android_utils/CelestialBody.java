/**
 * 
 */
package com.wams.bma_week_11_android_utils;

/**
 * @author W. Mooncai
 * @since 0.0
 * 
 * Basic solar system object.
 *
 */
public class CelestialBody implements DebugInterface {

	/**
	 * Source of astronomical data:
	 * http://solarsystem.nasa.gov/planets/
	 * 
	 */
	
	private String mColor;
	private float mLocationX = 0;
	private float mLocationY = 0;
	private String mName;
	private float mOrbitalVelocity = 0;
	private CelestialBody mParent;
	private float mParentalDistance = 0;
	private String mParentName;
	private float mRadius = 0;
	// private int mSatelliteQty = 0;
	private int mYear = 0;
	
	private CelestialBody[] mSatellites;
	private int mFirstAvailableSatellite = 0;
	
	private int mMaxNameLen = 9;
	
	/*
	private int mSatMinRadius = 0;
	private int mSatMinParentalDistance = 0;

	*/
	
	private Debug d = new Debug(true, DEBUG_LEVEL_DEBUG);

	// ########################### CONSTRUCTORS ###############################

	public CelestialBody(int maxNameLen) {

		d.toLog(DEBUG_LEVEL_DEBUG, "CelestialBody() instantiated.");
	}
	
	// ############################# SETTERS ##################################

	public void addSatellite(CelestialBody cb) {
		
		mSatellites[mFirstAvailableSatellite] = cb;
		mFirstAvailableSatellite++;
	}
	
	public void setColor(String color) {
		
		//TODO Implement setColor() either with colors in XML or via resources
		mColor = color;
		
	}
	
	public void setLocation(float x, float y) {
		mLocationX = x;
		mLocationY = y;
	}
	
	public void setName(String name) { mName = name.substring(0
			, (mMaxNameLen > name.length()) ? name.length() : mMaxNameLen); }
		
	public void setParent(CelestialBody cb) { mParent = cb; }
	
	public void setParentalDistance(float dist) { mParentalDistance = dist; }

	public void setParentName(String parent) { mParentName = parent.substring(0
			, (mMaxNameLen > parent.length()) ? parent.length() : mMaxNameLen); }
		
	public void setRadius(float radius) { mRadius = radius; }
		
	public void setYear(int year) { mYear = year; }
	
	public void setOrbitalVelocity(float velocity) { mOrbitalVelocity = velocity; }

	public void setSatellites(int qty) { mSatellites = new CelestialBody[qty]; }
	
	// ############################# GETTERS ##################################

	public CelestialBody getCelestialBody(CelestialBody parent, String name) {
		
		if (parent.getSatellites().length == 0) {
			
			if (parent.getFirstAvailableSatellite() > 0) {
				CelestialBody[] satellites = parent.getSatellites();
				for(CelestialBody cb : satellites) {
					if (cb.getFirstAvailableSatellite() > 0)
						return getCelestialBody(cb, name);
				}
			} return null;
		}
		return parent;
	}
	
	public int getFirstAvailableSatellite() { return mFirstAvailableSatellite; }
	
	public float getLocationX() { return mLocationX; }
	
	public float getLocationY() { return mLocationY; }
	
	public String getName() { return(mName); }

	public float getOrbitalVelocity() { return(mOrbitalVelocity); }
	
	public float getParentalDistance() { return(mParentalDistance); }
	
	public CelestialBody getParent() { return(mParent); }

	public String getParentName() { return(mParentName); }
		
	public float getRadius() { return(mRadius); }
	
	public CelestialBody[] getSatellites() { return(mSatellites); }

	public int getYear() { return(mYear); }
		
	// public int getSatelliteQty() { return(mSatelliteQty); }
	
	public String getColor(String color) { return(mColor); }
	
	public CelestialBody getSatellite(String name) {
		for(CelestialBody satellite : mSatellites) {

			if (satellite == null)
				return null;
			else if (satellite.mName.equals(name)) return satellite;
		}
		return null;
	}
	
	// ############################# METHODS ##################################

}
