package com.wams.bma_week_11_android;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.wams.bma_week_11_android_utils.*;

public class MainActivity extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                
        setContentView(new SolarSystemView(this
        		, R.xml.bma_week_11_planets));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
