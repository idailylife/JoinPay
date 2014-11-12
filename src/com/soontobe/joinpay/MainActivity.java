package com.soontobe.joinpay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends Activity {
	private boolean mIsServiceStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mIsServiceStarted = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void onButtonClick(View view){
    	Log.d("button", "click");
    	startActivity(new Intent(this, RadarViewActivity.class));
    	finish(); //Close current activity
    }
    
    public void onStartServiceClick(View v){
    	
    	Intent i = new Intent(getBaseContext(), MessageRetrievalService.class);;
    	if(!mIsServiceStarted){
        	startService(i);
        	Log.d("Service", "started");
        	mIsServiceStarted = true;
    	} else {
    		stopService(i);
    		Log.d("Service", "stopped");
    		mIsServiceStarted = false;
    	}
    	
    }
}
