/**
 * 
 */
package com.wams.bma_week_11_android_utils;

import static java.lang.Math.*;

/**
 * @author W. Mooncai
 * @since 0.0
 * 
 * Basic solar system object.
 * 
 * Source of astronomical data:
 *  - http://solarsystem.nasa.gov/planets/
 *  
 *  Reducing use of Getters and Setters:
 *  - http://developer.android.com/training/articles/perf-tips.html#GettersSetters
 *
 */

// TODO There are so many getters and setters, we may want to make some of the members public and directly access them to increase performance.

public class CelestialBody implements DebugInterface {

	private static Debug d = new Debug(true, DEBUG_LEVEL_DEBUG);
	private static final String TAG = "CelestialBody";
	
	public static final String NAME_SOLAR_SYSTEM = "Solar System";
	public static final String NAME_SUN = "SUN";
	
	private static CelestialBody sSolarSystem;
	private static int sMaxNameLen = 9;
	
	private String mColor;
	public float mLocationX = 0;
	public float mLocationY = 0;
	public String mName;
	public float mOrbitalVelocity = 0;
	public CelestialBody mParent;
	public float mParentalDistance = 0;
	public String mParentName;
	private float mRadius = 0;
	private int mYear = 0;
	
	public CelestialBody[] mSatellites;
	public int mFirstAvailableSatellite = 0;

	// Raw Min / Max variables derived from other member variables
	public int mSatMinRadius = 0;
	public int mSatMaxRadius = 0;
	public int mSatMinParentalDistance = 0;
	public int mSatMaxParentalDistance = 0;
	public int mSatMinVelocity = 0;
	
	// Calculated Min / Max variables (G for graphics)
	public int mRadiusG = 0;
	public int mParentalDistanceG = 0;
	public int mOrbitalVelocityG = 0;
	

	// ########################### CONSTRUCTORS ###############################

	/**
	 * Constructor
	 * 
	 * @since 0.0
	 * 
	 * @param solarSystem		Pointer to the Solar System object (for top-down searching).
	 * @param maxNameLen		Maximum length of celestial body name.
	 * 
	 */
	public CelestialBody(CelestialBody solarSystem, int maxNameLen) {
		
		sMaxNameLen = maxNameLen;
		sSolarSystem = solarSystem;

		d.toLog(TAG, DEBUG_LEVEL_DEBUG, "CelestialBody() instantiated.");
	}
	
	// ############################# SETTERS ##################################

	
	/**
	 * Set the color of this celestial body.
	 * 
	 * @since 0.0
	 * 
	 * @param color		Color represented as ARGB (#AARRGGBB).
	 * 
	 */
	public void setColor(String color) {
		
		//TODO Implement setColor() either with colors in XML or via resources
		mColor = color;
		
	}
	
	/**
	 * Set the location of this celestial body on the screen
	 * 
	 * @since 0.0
	 * 
	 * @param x		X coordinate component of the location.
	 * @param y		Y coordinate component of the location.
	 * 
	 */
	public void setLocation(float x, float y) {
		mLocationX = x;
		mLocationY = y;
	}
	
	/**
	 * Set the name of this celestial body.
	 * 
	 * @since 0.0
	 * 
	 * @param name		The name for this celestial body.
	 * 
	 */
	public void setName(String name) {
		mName = name.substring(0
			, (sMaxNameLen > name.length()) ? name.length() : sMaxNameLen);
		}
	
	/**
	 * Sets the parent celestial body for this one.
	 * 
	 * @since 0.0
	 * 
	 * @param cb	The celestial body to set as parent.
	 * 
	 */
	public void setParent(CelestialBody cb) { if (cb != null) mParent = cb; }
	
	/**
	 * Sets this celestial objects distance (radius) from its parent
	 * 
	 * @since 0.0
	 * 
	 * @param dist		Distance from parent.
	 * 
	 */
	public void setParentalDistance(float dist) { mParentalDistance = dist; }

	/**
	 * Set name of parent celestial body.
	 * 
	 * @param parent	Name of parent celestial body.
	 */
	public void setParentName(String parent) {
		if ( (parent != null) && (parent.length() > 0) )
			mParentName = parent.substring(0
				, (sMaxNameLen > parent.length()) ? parent.length() : sMaxNameLen);
		}

	/**
	 * Sets the radius of the celestial body in kilometers.
	 * 
	 * @since 0.0
	 * 
	 * @param radius	The radius of the celestial body in kilometers.
	 * 
	 */
	public void setRadius(float radius) { mRadius = radius; }
	
	/**
	 * Set's the length of the celestial body's year in Earth days.
	 * 
	 *  @since 0.0
	 *  
	 * @param year	Number of Earth days in this celestial body's year.
	 * 
	 */
	public void setYear(int year) { mYear = year; }
	
	/**
	 * Set the orbital velocity of this celestial body.
	 * 
	 * @since 0.0
	 * 
	 * @param velocity		The velocity of this celestial body in kilometers.
	 * 
	 */
	public void setOrbitalVelocity(float velocity) { mOrbitalVelocity = velocity; }
	
	public void setOrbitalVelocityG(int vel) { mOrbitalVelocityG = vel; }
	
	public void setParentalDistanceG(int dist) { mParentalDistanceG = dist; }
	
	public void setRadiusG(int radius) { mRadiusG = radius; }

	public void setSatMinRadius(float radius) { mSatMinRadius = round(radius); }
	
	public void setSatMinParentalDistance(float distance) {
		mSatMinParentalDistance = round(distance);
		}
	
	public void setSatMaxParentalDistance(float distance) {
		mSatMaxParentalDistance = round(distance);
		}

	public void setSatMinYear(float radius) { mSatMinRadius = round(radius); }

	// ------------------------ INTERNAL SETTERS ------------------------------
	
	/**
	 * Set the Min / Max internal derived member variables based on input celestial body values.
	 * 
	 * @since 0.0
	 * 
	 * @param cb	Starting celestial body from which to recurse.
	 * 
	 */
	public void setMinsMaxs(CelestialBody cb) {
				
		if (cb == null) cb = sSolarSystem;
		
		if (cb.getFirstAvailableSatellite() > 0) {
			
			int index = 0;
			int lastCb = cb.getFirstAvailableSatellite();
					
			CelestialBody[] satellites = cb.getSatellites();
			
			mSatMinRadius = round(mRadius);
			mSatMaxParentalDistance = round(mSatMaxParentalDistance);
			mSatMinParentalDistance = round(mSatMinParentalDistance);
			
			while (index < lastCb) {
				
				if (mSatMinRadius == 0) cb.setSatMinRadius(satellites[index].getRadius());
					else cb.setSatMinRadius(
							min(mSatMinRadius, satellites[index].getRadius()));
				
				if (mSatMaxParentalDistance == 0) cb.setSatMaxParentalDistance(satellites[index].getParentalDistance());
					else cb.setSatMaxParentalDistance(max(mSatMaxParentalDistance, satellites[index].getParentalDistance()));
				
				if (mSatMinParentalDistance == 0) cb.setSatMinParentalDistance(satellites[index].getParentalDistance());
					else cb.setSatMinParentalDistance(min(mSatMinParentalDistance, satellites[index].getParentalDistance()));
				
				// Recurse...
				if (satellites[index].getFirstAvailableSatellite() > 0) setMinsMaxs(satellites[index]);
				
				index++;
			}
		}
	}
	
	/**
	 * Calculate the internal graphics member variables.
	 * 
	 * @since 0.0
	 * 
	 * @param cbParent	Starting celestial body from which to recurse.
	 * 
	 */
	public void setCalcedVarsG(CelestialBody cb, float density) {
		
		
		cb = ( (cb == null) ? sSolarSystem : cb );
		
		if ( (!cb.getName().equalsIgnoreCase(NAME_SOLAR_SYSTEM))
				&&  (!cb.getName().equalsIgnoreCase(NAME_SUN)) ) {
			
			// Calculate for this celestial body
			/*
			private int mRadiusG = 0;
			private int mParentalDistanceG = 0;
			private int mOrbitalVelocityG = 0;
			*/
			
			float radiusRelParent = round( cb.getRadius()
					/ ((cb.getSatMinRadius() == 0) ? cb.getParent().getRadiusG() : cb.getSatMinRadius()) * density );
			cb.setRadiusG( (radiusRelParent < 1) ? 1 : round(radiusRelParent) );
			
			// TODO fix these calcs:
			cb.setParentalDistanceG( round( cb.getParentalDistance() / cb.getSatMinParentalDistance() * density ) );
			cb.setOrbitalVelocityG( round( mOrbitalVelocity / cb.getSatMinYear() ) );
		}
		
		// Recurse...
		if (cb.getFirstAvailableSatellite() > 0) {
			int index = 0;
			int lastCb = cb.getFirstAvailableSatellite();
					
			CelestialBody[] satellites = cb.getSatellites();
			
			while (index < lastCb) {
	
				if (satellites[index].getFirstAvailableSatellite() > 0)
					setCalcedVarsG(satellites[index], density);
				
				index++;
			}
		}
		
	}
	// ############################# GETTERS ##################################

	/**
	 * Get a celestial body subordinate to the input celestial body.
	 * If input celestial body is omitted with a null,
	 * the whole solar system will be searched.
	 * 
	 * @since 0.0
	 * 
	 * @param celestialBody		Search downward for celestial body satellites to this one.
	 * @param name				Search for the celestial body with this name.
	 * @return					The celestial body with name, "name".
	 * 
	 */
	public CelestialBody getCelestialBody(CelestialBody cb, String name) {
		
		CelestialBody celestialBody = (cb == null) ? sSolarSystem : cb;
		CelestialBody result = null;

		if (celestialBody.getFirstAvailableSatellite() > 0) {
			
			CelestialBody[] satellites = celestialBody.getSatellites();

			int index = 0;
			
			while ( (result == null) && (index < celestialBody.getFirstAvailableSatellite()) )  {
				if  (satellites == null) {
					index = celestialBody.getFirstAvailableSatellite();
				} else {
					
					if ( satellites[index].getName().equalsIgnoreCase(name) ) {
						result = satellites[index];
					} else if (satellites[index].getFirstAvailableSatellite() > 0)
					result = getCelestialBody(satellites[index], name);
				}
				index++;
			}
		}
		return result;
	}
	
	/**
	 * Get the color of this celestial body in ARGB (#AARRGGBB)
	 * 
	 * @since 0.0
	 * 
	 * @return	The color of this celestial body in ARGB(#AARRGGBB).
	 * 
	 */
	public String getColor() { return(mColor); }

	/**
	 * Get the array index of the next available satellite in the satellites array.
	 * 
	 * @since 0.0
	 * 
	 * @return	The array index of the next available satellite in the satellites array.
	 * 
	 */
	public int getFirstAvailableSatellite() { return mFirstAvailableSatellite; }
	
	/**
	 * Get the X component of the location coordinate for this celestial body.
	 * 
	 * @since 0.0
	 * 
	 * @return	The X component of the location coordinate for this celestial body.
	 * 
	 */
	public float getLocationX() { return mLocationX; }
	
	/**
	 * Get the Y component of the location coordinate for this celestial body.
	 * 
	 * @since 0.0
	 * 
	 * @return	The Y component of the location coordinate for this celestial body.
	 * 
	 */
	public float getLocationY() { return mLocationY; }
	
	/**
	 * Get the name of this celestial body.
	 * 
	 * @since 0.0
	 * 
	 * @return	The name of this celestial body.
	 * 
	 */
	public String getName() { return(mName); }

	/**
	 * Get the orbital velocity of this celestial body in kilometers.
	 * 
	 * @since 0.0
	 * 
	 * @return	The orbital velocity of this celestial body.
	 * 
	 */
	public float getOrbitalVelocity() { return(mOrbitalVelocity); }
	
	public int getOrbitalVelocityG() { return(mOrbitalVelocityG); }
	
	/**
	 * Get the orbital distance between this and parental celestial bodies in kilometers.
	 * 
	 * @since 0.0
	 * 
	 * @return	The orbital distance between this and parental celestial bodies in kilometers.
	 * 
	 */
	public float getParentalDistance() { return(mParentalDistance); }
	
	public float getParentalDistanceG() { return mParentalDistanceG; }
	
	/**
	 * Get the parent of this celestial body; and find it if currently unknown.
	 * 
	 * @since 0.0
	 * 
	 * @return
	 */
	public CelestialBody getParent() {
		
		if (mParent == null) {
			return(mParent);
		} else return getCelestialBody(sSolarSystem, mParentName);
	}

	/**
	 * Get parent's name
	 * 
	 * @since 0.0
	 * 
	 * @return	Name of parent.
	 * 
	 */
	public String getParentName() { return(mParentName); }
	
	/**
	 * Get the radius of the celestial body in kilometers.
	 * 
	 * @since 0.0
	 * 
	 * @return		The radius of the celestial body in kilometers.
	 */
	public float getRadius() { return(mRadius); }
	
	public int getRadiusG() { return(mRadiusG); }
	
	/**
	 * Get an array containing pointers to this celestial body's satellites.
	 * 
	 * @since 0.0
	 * 
	 * @return		An array containing pointers to this celestial body's satellites.
	 */
	public CelestialBody[] getSatellites() { return(mSatellites); }

	/**
	 * Get the length of a year in Earth days.
	 * 
	 * @since 0.0
	 * 
	 * @return	The length of a year in Earth days.
	 * 
	 */
	public int getYear() { return(mYear); }
	
	/**
	 * Get the number of satellites in the satellites array.
	 * 
	 * @return	The number of satellites in the satellites array.
	 */
	public int getSatelliteQty() { return(mFirstAvailableSatellite); }
	
	/**
	 * Get a direct satellite of this celestial body.
	 * 
	 * @since 0.0
	 * 
	 * @param name	The name of the satellite to get.
	 * 
	 * @return		The satellite celestial body object.
	 * 
	 */
	public CelestialBody getSatellite(String name) {
		for(CelestialBody satellite : mSatellites) {

			if (satellite == null)
				return null;
			else if (satellite.mName.equals(name)) return satellite;
		}
		return null;
	}
	
	public int getSatMinRadius() { return mSatMinRadius; }
	
	public int getSatMaxParentalDistance() { return mSatMaxParentalDistance; }

	public int getSatMinParentalDistance() { return mSatMinParentalDistance; }
	
	public int getSatMinYear() { return mSatMinVelocity; }
	
	// ############################# METHODS ##################################

	/**
	 * Initializes an array to hold satellite celestial body objects.
	 * 
	 * @since 0.0
	 * 
	 * @param qty	The number of satellites this celestial body has, used to initialize the array.
	 * 
	 */
	public void setupSatellites(int qty) { if (qty > 0) mSatellites
															= new CelestialBody[qty]; }
	
	/**
	 * Add a satellite to this celestial body.  Not all satellites will be added to this list.
	 * Some satellites are too small and some planets have 50+ satellites!
	 * 
	 * @since 0.0
	 * 
	 * @param cb	The celestial body to add as a satellite.
	 * 
	 */
	public void addSatellite(CelestialBody cb) {
		
		mSatellites[mFirstAvailableSatellite] = cb;
		mFirstAvailableSatellite++;
	}
	
	public String toString() {
		return "CelestialBody.toString() - mName: " + mName + " | mParentName: " + mParentName 
				+ " | mParent.Name: " + ((getParent() == null) ? "" : getParent().getName());
	}
}
