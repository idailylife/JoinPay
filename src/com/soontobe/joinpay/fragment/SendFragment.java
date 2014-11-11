package com.soontobe.joinpay.fragment;

import java.util.ArrayList;
import java.util.Random;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.PopupWindow.OnDismissListener;

import com.soontobe.joinpay.PositionHandler;
import com.soontobe.joinpay.R;
import com.soontobe.joinpay.Utility;
import com.soontobe.joinpay.model.UserInfo;
import com.soontobe.joinpay.widget.BigBubblePopupWindow;
import com.soontobe.joinpay.widget.RadarUserView;
import com.soontobe.joinpay.widget.RadarUserView.OnEditButtonClickedListener;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment
 * must implement the {@link SendFragment.OnFragmentInteractionListener}
 * interface to handle interaction events. Use the
 * {@link SendFragment#newInstance} factory method to create an instance of
 * this fragment.
 * 
 * This is the base(parent) fragment for Send/Request fragment.
 *
 */
public class SendFragment extends Fragment 
implements LoaderCallbacks<Void>{



	private OnFragmentInteractionListener mListener;
	private static final int contactListRequestCode = 1;
	
	private FrameLayout mBubbleFrameLayout;
	private ArrayList<RadarUserView> mUserBubbles;
	private RadarUserView mSelfBubble;
	private View mCurrentView;
	private BigBubblePopupWindow mBigBubble;
	
	private UserInfo myUserInfo;
	private ArrayList<UserInfo> mUserInfoList;	//User info list except me myself
	
	private PositionHandler mPositionHandler;	

	public static SendFragment newInstance(String param1, String param2) {
		SendFragment fragment = new SendFragment();
//		Bundle args = new Bundle();
//		args.putString(ARG_PARAM1, param1);
//		args.putString(ARG_PARAM2, param2);
//		fragment.setArguments(args);
		return fragment;
	}

	public SendFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		if (getArguments() != null) {
//			mParam1 = getArguments().getString(ARG_PARAM1);
//			mParam2 = getArguments().getString(ARG_PARAM2);
//		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		mCurrentView = inflater.inflate(R.layout.fragment_send, container, false);
		Utility.setupKeyboardAutoHidden(mCurrentView, getActivity());
		
		mBubbleFrameLayout = (FrameLayout)mCurrentView.findViewById(R.id.layout_send_frag_bubbles);
		mBubbleFrameLayout.getViewTreeObserver().addOnGlobalLayoutListener(new MyOnGlobalLayoutChgListener());
		mSelfBubble = (RadarUserView)mCurrentView.findViewById(R.id.user_bubble_myself);
		
		myUserInfo = new UserInfo();
		myUserInfo.setUserId(new Random().nextInt());
		myUserInfo.setUserName("Itziar");
		myUserInfo.setContactState(true);
		mSelfBubble.setUserInfo(myUserInfo);
		mSelfBubble.setEditBtnClickedListener(new OnEditButtonClickedListener() {
			
			@Override
			public void OnClick(View v) {
				// TODO Auto-generated method stub
				showBigBubble(myUserInfo);
			}
		});
//		myUserInfo.setAmountOfMoney(50.6f);
//		mSelfBubble.setUserInfo(myUserInfo);
		//TODO: Random generated small bubbles~ 
		mUserInfoList = new ArrayList<UserInfo>();
		mUserBubbles = new ArrayList<RadarUserView>();
		mPositionHandler = new PositionHandler();
		
		
		return mCurrentView;
	}
	
	/**
	 * Generate user bubbles.
	 * @param qty Amount of users to be generated.
	 */
	public void generateBubbles(int qty){
		if(qty > PositionHandler.MAX_USER_SUPPORTED){
			Log.e("SendFragment::generateBubbles", "Maximum user quantity exceed!");
			return;
		}
		//TODO: Generate randomly
		int frameHeight = mBubbleFrameLayout.getHeight();
		int frameWidth = mBubbleFrameLayout.getWidth();
		int widgetWidth = mSelfBubble.getWidth();
		Random random = new Random();
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(mSelfBubble.getLayoutParams());
		for (int i=0; i<qty; i++){
			float pos[] = {PositionHandler.RAND_BUBBLE_CENTER_POS_X[i],
					PositionHandler.RAND_BUBBLE_CENTER_POS_Y[i]			};
			pos[0] = pos[0] * frameWidth - widgetWidth/2;
			pos[1] = pos[1] * frameHeight - widgetWidth/2;
			Log.d("Bubble pos", "x=" + pos[0] + ", y=" + pos[1]);
			params = new FrameLayout.LayoutParams(mSelfBubble.getLayoutParams());
			params.gravity = Gravity.LEFT | Gravity.TOP;
			params.setMargins((int)pos[0], (int)pos[1], 0, 0);
			RadarUserView ruv = new RadarUserView(getActivity());
			mUserBubbles.add(ruv);
			mBubbleFrameLayout.addView(mUserBubbles.get(i), params);
			
			//TODO:Set onClick listener
			UserInfo info = new UserInfo();
			info.setUserName("Ano" + i);
			info.setUserId(random.nextInt());
			mUserInfoList.add(info);
			mUserBubbles.get(i).setUserInfo(info);
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		/**
		 * NOTE.........
		 * 
		 * The code below was temporarily commented in case of weird crash.
		 */
//		setEventListeners();
//		Button sendMoneyNextButton = (Button) getActivity().findViewById(R.id.send_money_next);
//		
//		
//		OnTouchListener buttonOnTouchListener = new OnTouchListener() {
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				Button btn = (Button) v;
//				// TODO Auto-generated method stub
//				if (event.getAction() == MotionEvent.ACTION_DOWN) {
//					//CAUSE OF　CRASH。 Temporarily removed.
//					//btn.setBackgroundResource(R.drawable.button_active);
//				} else if (event.getAction() == MotionEvent.ACTION_UP) {
//					//CAUSE OF　CRASH。 Temporarily removed.
//					//btn.setBackgroundResource(R.drawable.button_normal);
//				}
//				return false;
//			}
//		};
//		sendMoneyNextButton.setOnTouchListener(buttonOnTouchListener);
		//generateBubbles(2);
	}
	
	private void setEventListeners() {
		
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}


	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		public void onFragmentInteraction(Uri uri);
	}

	@Override
	public Loader<Void> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Void> arg0, Void arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoaderReset(Loader<Void> arg0) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * Show big bubble
	 * @param userInfo
	 */
	public void showBigBubble(UserInfo userInfo){
		View popupView = getActivity().getLayoutInflater().inflate(R.layout.big_bubble, null);
		mBigBubble = new BigBubblePopupWindow(popupView, null);
		mBigBubble.setTouchable(true);
		mBigBubble.setBackgroundDrawable(new BitmapDrawable()); //Outside disimss-able

		ArrayList<Float> dataList = new ArrayList<Float>();
		dataList.add(1.05f);
		dataList.add(2.55f);

		mBigBubble.setDonutChartData(dataList);

		mBigBubble.setUserInfo(userInfo);
		mBigBubble.showUserInfo();
		mBigBubble.setOnDismissListener(new OnBigBubbleDismissListener());

		mBigBubble.showAtLocation(getActivity().findViewById(R.id.btn_radar_view_back), Gravity.CENTER|Gravity.TOP, 0, 200);
	}
	
	private class OnBigBubbleDismissListener implements OnDismissListener {

		@Override
		public void onDismiss() {
			UserInfo userInfo = mBigBubble.getUserInfo();
			int uid = userInfo.getUserId();
			int index = findUserIndexById(uid);
			if(index == -1){
				//TODO: Refresh UI.
				myUserInfo = userInfo;
				mSelfBubble.setUserInfo(myUserInfo);
			} else if(index == -2) {
				Log.w("OnBigBubbleDismissListener", "Could not find user id=" + userInfo.getUserId());
			} else {
				//TODO: Refresh UI.
				mUserInfoList.set(index, userInfo);
				mUserBubbles.get(index).setUserInfo(userInfo);
			}
			
			Log.d("OnBigBubbleDismissListener", userInfo.toString());
		}

	}
	
	/**
	 * 
	 * @param userId
	 * @return index of UserInfo object in ArrayList, Myserlf = -1, not found = -2
	 */
	public int findUserIndexById(int userId){
		if (myUserInfo.getUserId() == userId)
			return -1;
		for (int i=0; i<mUserInfoList.size(); i++){
			UserInfo info = mUserInfoList.get(i);
			if(info.getUserId() == userId)
				return i;
		}
		return -2;
	}
	
	private class MyOnGlobalLayoutChgListener implements ViewTreeObserver.OnGlobalLayoutListener{

		@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
		@Override
		public void onGlobalLayout() {
			// TODO Auto-generated method stub
			generateBubbles(3);
			
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
				mBubbleFrameLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
			else
				mBubbleFrameLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
		}
		
	}
}
