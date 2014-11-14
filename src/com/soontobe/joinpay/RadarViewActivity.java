package com.soontobe.joinpay;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

import com.soontobe.joinpay.fragment.TransactionFragment;
import com.soontobe.joinpay.fragment.HistoryFragment;
import com.soontobe.joinpay.fragment.RequestFragment;
import com.soontobe.joinpay.fragment.SendFragment;
import com.soontobe.joinpay.model.UserInfo;
import com.soontobe.joinpay.widget.BigBubblePopupWindow;

/* TODO: 
 * 		1. Numeric Keyboard should contain arithmetic operations
 */

public class RadarViewActivity extends FragmentActivity 
implements OnTabChangeListener, TransactionFragment.OnFragmentInteractionListener,
HistoryFragment.OnFragmentInteractionListener {

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

	WebConnector webConnector;
	private ArrayList<String> fileNameList; // posttestserver
	private int visitedFilesCount = 0; // posttestserver
	private Set<String> onlineNameList = new HashSet<String>();


	WebConnector WebConnector;

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
		runPostTestServer();
	}

	private void runPostTestServer() {
		webConnector = new WebConnector(Constants.userName);
		Log.d("RadarViewActivity", "onCreate");

		new Thread() {
			@Override
			public void run() {
				Log.d("RadarViewActivity", "run");
				webConnector.onlineSignIn(Constants.urlForPostingToFolder);
				while (true) {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					fileNameList = webConnector.getFileNameList(Constants.urlPrefix, visitedFilesCount);
					for (String i : fileNameList) {
						String newFile = webConnector.getFile(Constants.urlPrefix + "/" + i);
						for (String name : Constants.deviceNameList) {
							if (onlineNameList.contains(name) || name.equals(Constants.userName)) continue;
							if (newFile.contains(name + "IsOnline")) {
								onlineNameList.add(name);
							}
						}

						if (newFile.contains(Constants.transactionIntiatorTag + Constants.userName)) {
							continue;
						}

						int idx1 = newFile.indexOf(Constants.transactionBeginTag);
						int idx2 = newFile.indexOf(Constants.transactionEndTag);
						if (idx1 >= 0 && idx2 >= 0) {
							int st = idx1 + Constants.transactionBeginTag.length();
							int ed = idx2;
							String data = newFile.substring(st, ed);
							//							Log.d("RadarViewActivity paymentInfo from web", newFile.substring(st, ed));

							ArrayList<String []> paymentInfoFromWeb = new ArrayList<String []>();
							String[] paymentStrings = data.split("\\|");
							String [] relevantItem = {};
							String [] groupNote = {};
							String [] summary = {};
							for (int k = 0;k < paymentStrings.length;k++) {
								String[] items = paymentStrings[k].split(",");
								//								paymentInfoFromWeb.add(items);
								if (items.length >= 4) {
									if (items[2].equals(Constants.userName)) {  	//	payer
										relevantItem = items;
									} else if (items[3].equals(Constants.userName)) {	//	payee
										relevantItem = items;
									}
								}

								if (items[0].equals("group_note")) {
									groupNote = items;
								}

								if (items[0].equals("summary")) {
									summary = items;
								}
							}

							if (relevantItem.length > 0) {
								ArrayList<String []> popupPaymentInfo = new ArrayList<String[]>();
								popupPaymentInfo.add(relevantItem);
								popupPaymentInfo.add(groupNote);
								popupPaymentInfo.add(summary);
								
//								String ssss = "";
//								for (int m = 0;m < popupPaymentInfo.size();m++) {
//									for (String s : popupPaymentInfo.get(m)) {
//										Log.d("popupPaymentInfo", s);
//										ssss += " " + s;
//										
//									}
//								}
								//final String finalSSSS = ssss;
								final ArrayList<String []> finalInfo = popupPaymentInfo;
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
//										Toast.makeText(getBaseContext(),
//												finalSSSS, Toast.LENGTH_SHORT).show();
										
										//mHistoryFragment.addTransactionItem(finalInfo);
										mHistoryFragment.addPendingTransItem(finalInfo);
										mCurrentTab = 2;	//jump to history view
										mTabHost.setCurrentTab(mCurrentTab);
									}
								});
							}



						}


					}
					visitedFilesCount += fileNameList.size();
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							for (String i : onlineNameList) {
								Log.d("onlineNameList", i);
								if (mCurrentTab == 0) {
									mSendFragment.addUserToView(i);
								} else if (mCurrentTab == 1) {
									mRequestFragment.addUserToView(i);
								}
							}
						}
					});

					Log.d("visitedFilesCount", "" + visitedFilesCount);
				}
			}
		}.start();
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
		
		mTabHost.setCurrentTab(1);
		mTabHost.setCurrentTab(2);
		mTabHost.setCurrentTab(0);
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
	
	private boolean mFragmentInitState[] = {true, false, false};
	@Override
	public void onTabChanged(String tabId) {
		//		Log.d(TAG, "onTabChanged(): tabId=" + tabId);
		FragmentManager fm = getFragmentManager();
		if(TAG_SEND.equals(tabId)){
				fm.beginTransaction().replace(R.id.tab_send, mSendFragment)
					.commit();
				mFragmentInitState[0] = true;

			mCurrentTab = 0;
			mSendFragment.setMyName(Constants.userName);
		} else if (TAG_REQUEST.equals(tabId)){
				fm.beginTransaction().replace(R.id.tab_request, mRequestFragment)
					.commit();
				mFragmentInitState[1] = true;

			mCurrentTab = 1;
			mRequestFragment.setMyName(Constants.userName);
		} else if (TAG_HISTORY.equals(tabId)){
				fm.beginTransaction().replace(R.id.tab_history, mHistoryFragment)
				.commit();

			mCurrentTab = 2;
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
				//				Toast.makeText(this, data.getData().toString(), Toast.LENGTH_SHORT).show();
				String nameArray[];
				switch(mCurrentTab){
				case 0:
					//Send
					nameArray = data.getStringArrayExtra("name");
					for(String name: nameArray){
						mSendFragment.addContactToView(name);
					}
					//mSendFragment.addContactToView(data.getDataString());
					break;
				case 1:
					//Request
					nameArray = data.getStringArrayExtra("name");
					for(String name: nameArray){
						mRequestFragment.addContactToView(name);
					}
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
				String[] paymentStrings = dataString.split("\\|");
				for (int i = 0;i < paymentStrings.length;i++) {
					String[] items = paymentStrings[i].split(",");
					paymentInfo.add(items);
				}

				mHistoryFragment.setNewRecordNotification(paymentInfo);
				mTabHost.setCurrentTab(historyTab);
			} 
		}
	}

	public void proceedToConfirm(View v) {
		Intent i = new Intent(this, SendConfirmActivity.class);
		ArrayList<String[]> paymentInfo = new ArrayList<String[]>();
		switch(mCurrentTab) {
		case 0:
			paymentInfo = mSendFragment.getPaymentInfo();
			i.putExtra("transactionType", "Send");
			break;
		case 1:
			paymentInfo = mRequestFragment.getPaymentInfo();
			i.putExtra("transactionType", "Request");
			break;
		default:
			break;
		}
		Bundle extras = new Bundle();
		extras.putSerializable("paymentInfo", paymentInfo);
		i.putExtras(extras);
		startActivityForResult(i, proceedToConfirmRequestCode);
	}

	public void contactButtonOnClick(View v) {
		//		Log.d("contactButtonOnClick", "clicked");
		startActivityForResult(new Intent(this, ContactListActivity.class), contactListRequestCode);
	}


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

	public void onClickClearButton(View v){
		switch(mCurrentTab){
		case 0:
			mSendFragment.clearUserMoneyAmount();
			break;
		case 1:
			mRequestFragment.clearUserMoneyAmount();
			break;
		}
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
