package com.soontobe.joinpay;


import com.soontobe.joinpay.fragment.RequestFragment;
import com.soontobe.joinpay.fragment.SendFragment;


import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;

public class RadarViewActivity extends FragmentActivity 
			implements OnTabChangeListener, SendFragment.OnFragmentInteractionListener
			, RequestFragment.OnFragmentInteractionListener{
	
	private TabHost mTabHost;
	private int mCurrentTab;
	private static final String TAG = "RadarViewActivity";
	private static final String TAG_SEND = "tab_send";
	private static final String TAG_REQUEST = "tab_request";
	private static final String TAG_HISTORY = "tab_history";
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  //No Title Bar
		setContentView(R.layout.activity_radar_view);
		
		mTabHost = (TabHost)findViewById(android.R.id.tabhost);
		setupTabs();
		mTabHost.setOnTabChangedListener(this);
		mCurrentTab = 0;
		mTabHost.setCurrentTab(mCurrentTab);
		getFragmentManager().beginTransaction().replace(R.id.tab_send, new SendFragment())
							.commit();
	}
	
	

	private void setupTabs() {
		// Setup tabs
		mTabHost.setup();
		mTabHost.addTab(newTab(TAG_SEND, R.string.tab_send, R.id.tab_send));
		mTabHost.addTab(newTab(TAG_REQUEST, R.string.tab_request, R.id.tab_request));
		mTabHost.addTab(newTab(TAG_HISTORY, R.string.tab_history, R.id.tab_history));
	}
	
	private TabSpec newTab(String tag, int labelId, int tabContentId) {
		Log.d(TAG, "buildTab(): tag=" + tag);

		View indicator = LayoutInflater.from(this).inflate(
				R.layout.tab,
				(ViewGroup) findViewById(android.R.id.tabs), false);
		((TextView) indicator.findViewById(R.id.tab_text)).setText(labelId);

		TabSpec tabSpec = mTabHost.newTabSpec(tag);
		tabSpec.setIndicator(indicator);
		tabSpec.setContent(tabContentId);
		return tabSpec;
	}

	@Override
	public void onTabChanged(String tabId) {
		Log.d(TAG, "onTabChanged(): tabId=" + tabId);
		FragmentManager fm = getFragmentManager();
		if(TAG_SEND.equals(tabId)){
			mCurrentTab = 0;
			fm.beginTransaction().replace(R.id.tab_send, new SendFragment())
								.commit();
			
		} else if (TAG_REQUEST.equals(tabId)){
			mCurrentTab = 1;
			fm.beginTransaction().replace(R.id.tab_request, new RequestFragment())
						.commit();
		} else if (TAG_HISTORY.equals(tabId)){
			mCurrentTab = 2;
			
		} else {
			Log.w("RadarViewActivity_onTabChanged", "Cannot find tab id=" + tabId);
		}
	}



	@Override
	public void onFragmentInteraction(Uri uri) {
		Log.d(TAG, uri.toString());		
	}
	


}
