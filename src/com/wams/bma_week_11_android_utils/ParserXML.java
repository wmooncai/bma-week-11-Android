/**
 * 
 */
package com.wams.bma_week_11_android_utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.Resources;
import android.util.Xml;

/**
 * @author W. Mooncai
 * 
 * RSS Code Example:
 * http://www.ibm.com/developerworks/opensource/library/x-android/
 *
 */
public abstract class ParserXML implements ParserXML_Interface, DebugInterface {
    
	// Debug Logging Class Identifier
    private static Debug d = new Debug(DEBUG_ON, DEBUG_LEVEL_DEBUG);
	protected static final String TAG = "ParserXML";
	
    static Context mContext;
    static Resources mRes;
    
    URL mFeedUrl;
    XmlPullParser mParser = Xml.newPullParser();
    InputStream mInputXml;

    // ########################################################################
    
	public ParserXML(Context ctxt, URL feedUrl) {

		mContext = ctxt;
		mRes = mContext.getResources();

        try {
        	if (feedUrl != null)
        		mInputXml = feedUrl.openConnection().getInputStream();
        		mParser.setInput(mInputXml, null);
        	
        } catch(SocketException e) {
        	d.toLog(TAG, DEBUG_LEVEL_DEBUG, " setParser() " + e.toString());
        } catch (IOException e) {
        	d.toLog(TAG, DEBUG_LEVEL_DEBUG, " setParser() " + e.toString());
        } catch (XmlPullParserException e) {
        	d.toLog(TAG, DEBUG_LEVEL_DEBUG, " setParser() " + e.toString());
			// e.printStackTrace();
		}
    }
    
    public ParserXML(Context ctxt, XmlPullParser xpp) {

		mContext = ctxt;
		mRes = mContext.getResources();
		
		mParser = xpp;
	}
    
}
