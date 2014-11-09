package com.soontobe.joinpay;


import java.util.ArrayList;

import com.soontobe.joinpay.fragment.RequestFragment;
import com.soontobe.joinpay.fragment.SendFragment;
import com.soontobe.joinpay.model.UserInfo;
import com.soontobe.joinpay.widget.BigBubblePopupWindow;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;

public class RadarViewActivity extends FragmentActivity 
			implements OnTabChangeListener, SendFragment.OnFragmentInteractionListener
			, RequestFragment.OnFragmentInteractionListener{
	
	private TabHost mTabHost;
	private int mCurrentTab;
	private SendFragment mSendFragment;
	private RequestFragment mRequestFragment;
	private BigBubblePopupWindow mBigBubble;
	
	private static final String TAG = "RadarViewActivity";
	private static final String TAG_SEND = "tab_send";
	private static final String TAG_REQUEST = "tab_request";
	private static final String TAG_HISTORY = "tab_history";
	
	private static final int contactListRequestCode = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  //No Title Bar
		setContentView(R.layout.activity_radar_view);
		
		mSendFragment = new SendFragment();
		mRequestFragment = new RequestFragment();
		
		mTabHost = (TabHost)findViewById(android.R.id.tabhost);
		setupTabs();
		mTabHost.setOnTabChangedListener(this);
		mCurrentTab = 0;
		mTabHost.setCurrentTab(mCurrentTab);
		getFragmentManager().beginTransaction().replace(R.id.tab_send, new SendFragment())
							.commit();
	}
	
    public void contactButtonOnClick(View v) {
		//		Log.d("contactButtonOnClick", "clicked");
		startActivityForResult(new Intent(this, ContactListActivity.class), contactListRequestCode);
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
			fm.beginTransaction().replace(R.id.tab_send, mSendFragment)
								.commit();
			
		} else if (TAG_REQUEST.equals(tabId)){
			mCurrentTab = 1;
			fm.beginTransaction().replace(R.id.tab_request, mRequestFragment)
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
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == contactListRequestCode) { 
			if (resultCode == RESULT_OK) {
				Toast.makeText(this,data.getData().toString(), Toast.LENGTH_SHORT).show();
				
				//TODO: Inform mSendFragment of mRequestFragment that we have new contact selected
				// switch case.: mCurrentTab
			} 
		}
	}
	
	/**
	 * onClick function for the debug button `CALL_BIG_BUBBLE`
	 * @param view
	 */
	public void debugCallBigBubble(View view){
		View popupView = getLayoutInflater().inflate(R.layout.big_bubble, null);
		mBigBubble = new BigBubblePopupWindow(popupView, null);
		mBigBubble.setTouchable(true);
		mBigBubble.setBackgroundDrawable(new BitmapDrawable()); //Outside disimss-able
		
		
		ArrayList<Float> dataList = new ArrayList<Float>();
		dataList.add(1.05f);
		dataList.add(2.55f);
		
		mBigBubble.setDonutChartData(dataList);
		
		//Make up some user info...
		UserInfo userInfo = new UserInfo();
		userInfo.setLocked(true);
		userInfo.setUserName("Test User Name");
		userInfo.setAmountOfMoney(52.06f);
		userInfo.setPublicNote("Helloween party");
		
		mBigBubble.setUserInfo(userInfo);
		mBigBubble.showUserInfo();
		mBigBubble.setOnDismissListener(new OnBigBubbleDismissListener());
		
		mBigBubble.showAtLocation(findViewById(R.id.btn_radar_view_back), Gravity.CENTER_VERTICAL, 0, 50);
	}
	
	
	private class OnBigBubbleDismissListener implements OnDismissListener {

		@Override
		public void onDismiss() {
			UserInfo userInfo = mBigBubble.getUserInfo();
			//TODO: 
			Log.d("OnBigBubbleDismissListener", userInfo.toString());
		}
		
	}

}
