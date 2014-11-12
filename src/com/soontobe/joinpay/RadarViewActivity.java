package com.soontobe.joinpay;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.util.EncodingUtils;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils.StringSplitter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.soontobe.joinpay.fragment.HistoryFragment;
import com.soontobe.joinpay.fragment.RequestFragment;
import com.soontobe.joinpay.fragment.SendFragment;
import com.soontobe.joinpay.model.UserInfo;
import com.soontobe.joinpay.widget.BigBubblePopupWindow;

/* TODO: 
 * 		1. Numeric Keyboard should contain arithmetic operations
 */

public class RadarViewActivity extends FragmentActivity 
implements OnTabChangeListener, SendFragment.OnFragmentInteractionListener
, RequestFragment.OnFragmentInteractionListener, HistoryFragment.OnFragmentInteractionListener {

	private TabHost mTabHost;
	private int mCurrentTab;
	private SendFragment mSendFragment;
	private RequestFragment mRequestFragment;
	private HistoryFragment mHistoryFragment;

	
	public static final String JUMP_KEY = "_jump";
	private static final String TAG = "RadarViewActivity";
	private static final String TAG_SEND = "tab_send";
	private static final String TAG_REQUEST = "tab_request";
	private static final String TAG_HISTORY = "tab_history";

	private static final int contactListRequestCode = 1;
	private static final int proceedToConfirmRequestCode = 2;
	public static final int historyRequestCode = 3;
	private static final int sendTab = 0;
	private static final int receiveTab = 1;
	private static final int historyTab = 2;

    private ArrayList<String[]> paymentInfo;

	public Map<String, Boolean> lockInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  //No Title Bar
		setContentView(R.layout.activity_radar_view);
		mSendFragment = new SendFragment();
		mRequestFragment = new RequestFragment();
		mHistoryFragment = new HistoryFragment();

		mTabHost = (TabHost)findViewById(android.R.id.tabhost);
		setupTabs();
		mTabHost.setOnTabChangedListener(this);
		
		//Receive jump command
		Intent intent = getIntent();
		int jump_target = intent.getIntExtra(JUMP_KEY, 0);
		mCurrentTab = sendTab;
		if(jump_target == historyRequestCode){
			mCurrentTab = historyTab;
		}
		
		mTabHost.setCurrentTab(mCurrentTab);
		getFragmentManager().beginTransaction().replace(R.id.tab_send, mSendFragment)
		.commit();

		lockInfo = new HashMap<String, Boolean>();
		lockInfo.put("total", false);
		setEventListeners();
	}

	@Override
	public View onCreateView(String name, Context context, AttributeSet attrs) {
		// TODO Auto-generated method stub
		return super.onCreateView(name, context, attrs);
	}

	private void setEventListeners() {
		Button btn = (Button) findViewById(R.id.btn_radar_view_back);
		btn.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Button btn = (Button) v;
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					btn.setBackgroundResource(R.drawable.arrow_active);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					btn.setBackgroundResource(R.drawable.arrow_normal);
				}
				return false;
			}
		});

		btn = (Button) findViewById(R.id.btn_radar_view_cross);
		btn.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Button btn = (Button) v;
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					btn.setBackgroundResource(R.drawable.cross_active);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					btn.setBackgroundResource(R.drawable.cross_normal);
				}
				return false;
			}
		});
	}

	private void setupTabs() {
		// Setup tabs
		mTabHost.setup();
		mTabHost.addTab(newTab(TAG_SEND, R.string.tab_send, R.id.tab_send));
		mTabHost.addTab(newTab(TAG_REQUEST, R.string.tab_request, R.id.tab_request));
		mTabHost.addTab(newTab(TAG_HISTORY, R.string.tab_history, R.id.tab_history));
	}

	private TabSpec newTab(String tag, int labelId, int tabContentId) {
//		Log.d(TAG, "buildTab(): tag=" + tag);

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
		//		Log.d(TAG, "onTabChanged(): tabId=" + tabId);
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
			fm.beginTransaction().replace(R.id.tab_history, mHistoryFragment)
			.commit();

		} else {
			Log.w("RadarViewActivity_onTabChanged", "Cannot find tab id=" + tabId);
		}

		// change history tab color. Should be refactored later.
		if (tabId.equals("tab_history")) {
			mTabHost.getTabWidget().getChildAt(mTabHost.getCurrentTab()).setBackgroundColor(Color.rgb(0xc2, 0xd4, 0x2d));
		} else {
			TabWidget tabWidget = mTabHost.getTabWidget();
			tabWidget.getChildAt(2).setBackgroundColor(Color.rgb(0xe6, 0xe6, 0xe6));
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
				Toast.makeText(this, data.getData().toString(), Toast.LENGTH_SHORT).show();
				switch(mCurrentTab){
				case 0:
					//Send
					String nameArray[] = data.getStringArrayExtra("name");
					for(String name: nameArray){
						mSendFragment.addContactToView(name);
					}
					//mSendFragment.addContactToView(data.getDataString());
					break;
				case 1:
					//Request
					break;
				default:
					break;
				}
				//TODO: Inform mSendFragment of mRequestFragment that we have new contact selected
				// switch case.: mCurrentTab
			} 
		} else if (requestCode == proceedToConfirmRequestCode) {
			if (resultCode == RESULT_OK) {
				paymentInfo = new ArrayList<String []>();
				String dataString = data.getData().toString();
//				Log.d("SendConfirm", dataString);
				String[] paymentStrings = dataString.split("\\|");
				for (int i = 0;i < paymentStrings.length;i++) {
//					Log.d("SendConfirm", dataString);
//					Log.d("SendConfirm", paymentStrings[i] + "------");
					String[] items = paymentStrings[i].split(",");
					paymentInfo.add(items);
					for (int j = 0;j < items.length;j++) {

//						Log.d("SendConfirm", items[j] + "-");
					}
				}
				mHistoryFragment.setNewRecordNotification(paymentInfo);
				mTabHost.setCurrentTab(historyTab);
			} 
		}
	}

	public void sendProceedToConfirm(View v) {
		Intent i = new Intent(this, SendConfirmActivity.class);
		ArrayList<String[]> paymentInfo = mSendFragment.getPaymentInfo();
		Bundle extras = new Bundle();
		extras.putSerializable("paymentInfo", paymentInfo);
		i.putExtras(extras);
		startActivityForResult(i, proceedToConfirmRequestCode);
	}

	public void contactButtonOnClick(View v) {
		//		Log.d("contactButtonOnClick", "clicked");
		startActivityForResult(new Intent(this, ContactListActivity.class), contactListRequestCode);
	}

//	/**
//	 * onClick function for the debug button `CALL_BIG_BUBBLE`
//	 * @param view
//	 */
//	public void debugCallBigBubble(View view){
//		View popupView = getLayoutInflater().inflate(R.layout.big_bubble, null);
//		mBigBubble = new BigBubblePopupWindow(popupView, null);
//		mBigBubble.setTouchable(true);
//		mBigBubble.setBackgroundDrawable(new BitmapDrawable()); //Outside disimss-able
//
//		ArrayList<Float> dataList = new ArrayList<Float>();
//		dataList.add(1.05f);
//		dataList.add(2.55f);
//
//		mBigBubble.setDonutChartData(dataList);
//
//		//Make up some user info...
//		UserInfo userInfo = new UserInfo();
//		userInfo.setLocked(true);
//		userInfo.setUserName("Test User Name");
//		userInfo.setAmountOfMoney(52.06f);
//		userInfo.setPublicNote("Helloween party");
//
//		mBigBubble.setUserInfo(userInfo);
//		mBigBubble.showUserInfo();
//		mBigBubble.setOnDismissListener(new OnBigBubbleDismissListener());
//
//		mBigBubble.showAtLocation(findViewById(R.id.btn_radar_view_back), Gravity.CENTER_VERTICAL, 0, 50);
//	}
//
//
//
//
//	private class OnBigBubbleDismissListener implements OnDismissListener {
//
//		@Override
//		public void onDismiss() {
//			UserInfo userInfo = mBigBubble.getUserInfo();
//			//TODO: Refresh UI.
//			Log.d("OnBigBubbleDismissListener", userInfo.toString());
//		}
//
//	}

	public void setSendTotalLock(View v) {
		//TODO:Move to SendFragment
		ImageView iv = (ImageView) v;
		if (lockInfo.get("total")) {
			iv.setImageResource(R.drawable.unlocked_darkgreen);
			lockInfo.put("total", false);
			findViewById(R.id.edit_text_total_amount).setEnabled(true);
			// TODO
		} else {
			iv.setImageResource(R.drawable.locked_darkgreen);
			lockInfo.put("total", true);
			findViewById(R.id.edit_text_total_amount).setEnabled(false);
		}
	}
	
	public void onClickBackButton(View v){
		Intent i = new Intent(this, MainActivity.class);
		startActivity(i);
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			onClickBackButton(getCurrentFocus());
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	
	
}
