package com.soontobe.joinpay.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.soontobe.joinpay.model.UserInfo;

public class SendFragment extends TransactionFragment {
	public ArrayList<String[]> getPaymentInfo() {
		ArrayList<String[]> paymentInfo = new ArrayList<String[]>();
		for (UserInfo info : mUserInfoList) {
			if (info.isSelecetd()) {
				String[] item = {"normal", info.getPersonalNote(), myUserInfo.getUserName(), info.getUserName(), "$ " + String.format("%.2f",info.getAmountOfMoney()), "notPending"};
				paymentInfo.add(item);
//				Log.d(" paymentInfo", item[2]);
			}
		}
		
		/* Doesn't make sense to send money to ourselves */
//		if (myUserInfo.isSelecetd()) {
//			String[] item = {"normal", myUserInfo.getPersonalNote(), myUserInfo.getUserName(), myUserInfo.getUserName(), "$ " + String.format("%.2f",myUserInfo.getAmountOfMoney())};
//			paymentInfo.add(item);
////			Log.d(" paymentInfo", item[2]);
//		}

		String[] groupNote = {"group_note", mGroupNote.getText().toString() };
		if (groupNote[1].length() > 0) paymentInfo.add(groupNote);


		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy  HH:mm:ss");
		String strDate = sdf.format(c.getTime());

		String[] summary = {"summary", strDate, String.valueOf(getSelectedUserSize()), "$ " + mTotalAmount.getText().toString()};
		paymentInfo.add(summary);
//		Log.d(" paymentInfo", summary[3]);
		/* example */
//		{
//			{"normal", "", "Luna", "Itziar", "$ 500", ""},
//			{"normal", "Pay one extra beer", "Patrick", "Itziar", "$ 30", ""},   //	name, amount, personal note
//			{"normal", "", "asd", "Itziar", "$ 20", ""},
//			{"normal", "", "Itziar", "Itziar", "$ 20", ""},
//			{"group_note", "This is a group note"},
//			{"summary", "2014-11-14", "5", "$ 130"}
//		}
		return paymentInfo;
	}
}

//package com.soontobe.joinpay.fragment;
//
//import java.sql.Date;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Random;
//
//import android.annotation.TargetApi;
//import android.app.Activity;
//import android.app.Fragment;
//import android.graphics.drawable.BitmapDrawable;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.v4.app.LoaderManager.LoaderCallbacks;
//import android.support.v4.content.Loader;
//import android.text.format.DateFormat;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.View.OnFocusChangeListener;
//import android.view.ViewGroup;
//import android.view.ViewTreeObserver;
//import android.widget.EditText;
//import android.widget.FrameLayout;
//import android.widget.PopupWindow.OnDismissListener;
//import android.widget.TextView;
//
//import com.soontobe.joinpay.PositionHandler;
//import com.soontobe.joinpay.R;
//import com.soontobe.joinpay.RadarViewActivity;
//import com.soontobe.joinpay.Utility;
//import com.soontobe.joinpay.model.UserInfo;
//import com.soontobe.joinpay.widget.BigBubblePopupWindow;
//import com.soontobe.joinpay.widget.RadarUserView;
//import com.soontobe.joinpay.widget.RadarUserView.OnCenterButtonClickedListener;
//import com.soontobe.joinpay.widget.RadarUserView.OnDeselectButtonClickedListener;
//import com.soontobe.joinpay.widget.RadarUserView.OnEditButtonClickedListener;
//import com.soontobe.joinpay.widget.RadarUserView.OnLockButtonClickedListener;
//
///**
// * A simple {@link Fragment} subclass. Activities that contain this fragment
// * must implement the {@link SendFragment.OnFragmentInteractionListener}
// * interface to handle interaction events. Use the
// * {@link SendFragment#newInstance} factory method to create an instance of
// * this fragment.
// * 
// * This is the base(parent) fragment for Send/Request fragment.
// *
// */
//public class SendFragment extends Fragment 
//implements LoaderCallbacks<Void>{
//
//
//
//	private OnFragmentInteractionListener mListener;
//	private static final int contactListRequestCode = 1;
//
//	private FrameLayout mBubbleFrameLayout;
//	private ArrayList<RadarUserView> mUserBubbles;
//	private RadarUserView mSelfBubble;
//	private View mCurrentView;
//	private BigBubblePopupWindow mBigBubble;
//	private TextView mSelectCountText;	//Number of selected user
//	private EditText mTotalAmount;
//	private EditText mGroupNote;
//
//	private UserInfo myUserInfo;
//	private ArrayList<UserInfo> mUserInfoList;	//User info list except for myself
//	private float mOldMoneyAmount;
//
//
//	public static SendFragment newInstance(String param1, String param2) {
//		SendFragment fragment = new SendFragment();
//		return fragment;
//	}
//
//	public SendFragment() {
//		// Required empty public constructor
//	}
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//	}
//
//
//
//	@Override
//	public void onStop() {
//		//TODO: Save current status
//		super.onStop();
//	}
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		// Inflate the layout for this fragment
//		mCurrentView = inflater.inflate(R.layout.fragment_send, container, false);
//		Utility.setupKeyboardAutoHidden(mCurrentView, getActivity());
//
//		mBubbleFrameLayout = (FrameLayout)mCurrentView.findViewById(R.id.layout_send_frag_bubbles);
//		mBubbleFrameLayout.getViewTreeObserver().addOnGlobalLayoutListener(new MyOnGlobalLayoutChgListener());
//		mBubbleFrameLayout.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				//TODO:Un-expand small bubbles
//				mSelfBubble.switchExpandPanel(false);
//				for(RadarUserView ruv: mUserBubbles){
//					ruv.switchExpandPanel(false);
//				}
//
//				//Catch the focus (from TotalAmout EditText)
//				mBubbleFrameLayout.requestFocus();
//			}
//		});
//
//		mSelfBubble = (RadarUserView)mCurrentView.findViewById(R.id.user_bubble_myself);
//		mSelectCountText = (TextView)mCurrentView.findViewById(R.id.send_num_of_people);
//		mTotalAmount = (EditText)mCurrentView.findViewById(R.id.edit_text_total_amount);
//		mGroupNote = (EditText)mCurrentView.findViewById(R.id.group_note);
//
//
//
//		myUserInfo = new UserInfo();
//		myUserInfo.setUserId(new Random().nextInt());
//		myUserInfo.setUserName("Itziar");
//		myUserInfo.setContactState(true);
//		mSelfBubble.setUserInfo(myUserInfo);
//		mSelfBubble.setEditBtnClickedListener(new OnEditButtonClickedListener() {
//			@Override
//			public void OnClick(View v) {
//
//				showBigBubble(myUserInfo);
//			}
//		});
//		mSelfBubble.setCenterBtnClickedListener(new OnCenterButtonClickedListener() {	
//			@Override
//			public void OnClick(View v, boolean isSelected) {
//				myUserInfo.setSelecetd(isSelected);
//				updateSelectedUserNumber();
//			}
//		});
//		mSelfBubble.setDeselectBtnClickedListener(new OnDeselectButtonClickedListener() {
//			@Override
//			public void OnClick(View v) {
//				myUserInfo.setSelecetd(false);
//				updateSelectedUserNumber();
//			}
//		});
//
//		//TODO: Randomly generated small bubbles~ 
//		mUserInfoList = new ArrayList<UserInfo>();
//		mUserBubbles = new ArrayList<RadarUserView>();
//		//mPositionHandler = new PositionHandler();
//
//
//		mTotalAmount.setOnFocusChangeListener(new OnTotalMoneyFocusChangeListener());
//
//		mGroupNote.setOnFocusChangeListener(new OnGroupNoteFocusChangeListener());
//
//		return mCurrentView;
//	}
//
//	/**
//	 * Add a contact to view
//	 * @param contactName
//	 */
//	public void addContactToView(String contactName){
//		generateBubbles(1);
//		int index = mUserInfoList.size() -1;
//		mUserInfoList.get(index).setContactState(true);
//		mUserInfoList.get(index).setUserName(contactName);
//		mUserBubbles.get(index).setUserInfo(mUserInfoList.get(index));
//	}
//	
//	/**
//	 * Generate user bubbles.
//	 * @param qty Amount of users to be generated.
//	 */
//	public void generateBubbles(int qty){
//		int posOffset = mUserInfoList.size();
//		if(qty + posOffset > PositionHandler.MAX_USER_SUPPORTED){
//			Log.e("SendFragment::generateBubbles", "Maximum user quantity exceed!");
//			return;
//		}
//		//TODO: Generate randomly
//		int frameHeight = mBubbleFrameLayout.getHeight();
//		int frameWidth = mBubbleFrameLayout.getWidth();
//		int widgetWidth = mSelfBubble.getWidth();
//		Random random = new Random();
//		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(mSelfBubble.getLayoutParams());
//		for (int i=posOffset; i<qty+posOffset; i++){
//			
//			float pos[] = {PositionHandler.RAND_BUBBLE_CENTER_POS_X[i],
//					PositionHandler.RAND_BUBBLE_CENTER_POS_Y[i]			};
//			pos[0] = pos[0] * frameWidth - widgetWidth/2;
//			pos[1] = pos[1] * frameHeight - widgetWidth/2;
//			Log.d("Bubble pos", "x=" + pos[0] + ", y=" + pos[1]);
//			params = new FrameLayout.LayoutParams(mSelfBubble.getLayoutParams());
//			params.gravity = Gravity.LEFT | Gravity.TOP;
//			params.setMargins((int)pos[0], (int)pos[1], 0, 0);
//			RadarUserView ruv = new RadarUserView(getActivity());
//			mUserBubbles.add(ruv);
//			mBubbleFrameLayout.addView(mUserBubbles.get(i), params);
//
//			UserInfo info = new UserInfo();
//			info.setUserName("Ano" + i);
//			info.setUserId(random.nextInt());
//			mUserInfoList.add(info);
//			mUserBubbles.get(i).setUserInfo(info);
//			mUserBubbles.get(i).setEditBtnClickedListener(new EditButtonOnClickListener(i));
//			mUserBubbles.get(i).setLockBtnClickedListener(new LockButtonOnClickListener(i));
//			mUserBubbles.get(i).setCenterBtnClickedListener(new SelectUserOnClickListener(i));
//			mUserBubbles.get(i).setDeselectBtnClickedListener(new DeselectUserOnClickListener(i));
//		}
//	}
//
//	@Override
//	public void onActivityCreated(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onActivityCreated(savedInstanceState);
//	}
//
//
//	// TODO: Rename method, update argument and hook method into UI event
//	public void onButtonPressed(Uri uri) {
//		if (mListener != null) {
//			mListener.onFragmentInteraction(uri);
//		}
//	}
//
//	@Override
//	public void onAttach(Activity activity) {
//		super.onAttach(activity);
//		try {
//			mListener = (OnFragmentInteractionListener) activity;
//		} catch (ClassCastException e) {
//			throw new ClassCastException(activity.toString()
//					+ " must implement OnFragmentInteractionListener");
//		}
//	}
//
//
//	@Override
//	public void onDetach() {
//		super.onDetach();
//		mListener = null;
//	}
//
//	/**
//	 * This interface must be implemented by activities that contain this
//	 * fragment to allow an interaction in this fragment to be communicated to
//	 * the activity and potentially other fragments contained in that activity.
//	 * <p>
//	 * See the Android Training lesson <a href=
//	 * "http://developer.android.com/training/basics/fragments/communicating.html"
//	 * >Communicating with Other Fragments</a> for more information.
//	 */
//	public interface OnFragmentInteractionListener {
//		// TODO: Update argument type and name
//		public void onFragmentInteraction(Uri uri);
//	}
//
//	@Override
//	public Loader<Void> onCreateLoader(int arg0, Bundle arg1) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void onLoadFinished(Loader<Void> arg0, Void arg1) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onLoaderReset(Loader<Void> arg0) {
//		// TODO Auto-generated method stub
//
//	}
//
//	/**
//	 * Show big bubble
//	 * @param userInfo
//	 */
//	public void showBigBubble(UserInfo userInfo){
//		View popupView = getActivity().getLayoutInflater().inflate(R.layout.big_bubble, null);
//		mBigBubble = new BigBubblePopupWindow(popupView, null);
//		mBigBubble.setTouchable(true);
//		mBigBubble.setBackgroundDrawable(new BitmapDrawable()); //Outside disimss-able
//
//		ArrayList<Float> dataList = new ArrayList<Float>();
//		dataList.add(1.05f);
//		dataList.add(2.55f);
//
//		mBigBubble.setDonutChartData(dataList);
//		mBigBubble.setUserInfo(userInfo);
//		mBigBubble.showUserInfo();
//		mBigBubble.setOnDismissListener(new OnBigBubbleDismissListener());
//
//		mOldMoneyAmount = userInfo.getAmountOfMoney();
//
//		mBigBubble.showAtLocation(getActivity().findViewById(R.id.btn_radar_view_back), Gravity.CENTER|Gravity.TOP, 0, 200);
//	}
//
//
//	private class OnBigBubbleDismissListener implements OnDismissListener {
//
//		@Override
//		public void onDismiss() {
//			UserInfo userInfo = mBigBubble.getUserInfo();
//			int uid = userInfo.getUserId();
//			int index = findUserIndexById(uid);
//			if(index == -1){
//				//TODO: Refresh UI.
//				//				float oldAmount = myUserInfo.getAmountOfMoney();
//				myUserInfo = userInfo;
//				mSelfBubble.setUserInfo(myUserInfo);
//				applyFurtherMoneyChange(index, mOldMoneyAmount, myUserInfo.getAmountOfMoney());
//			} else if(index == -2) {
//				Log.w("OnBigBubbleDismissListener", "Could not find user id=" + userInfo.getUserId());
//			} else {
//				//TODO: Refresh UI.
//				//				float oldAmount = mUserInfoList.get(index).getAmountOfMoney();
//				mUserInfoList.set(index, userInfo);
//				mUserBubbles.get(index).setUserInfo(userInfo);
//				applyFurtherMoneyChange(index, mOldMoneyAmount, mUserInfoList.get(index).getAmountOfMoney());
//			}
//
//			Log.d("OnBigBubbleDismissListener", userInfo.toString());
//
//		}
//
//		private void applyFurtherMoneyChange(int indexOfUser, float oldAmount,
//				float currentAmount) {
//			if(!getTotalLockState()){
//				//Total amount is not locked
//				float moneyChanged = currentAmount - oldAmount;
//				float oldTotalAmount = 0;
//				try{
//					oldTotalAmount = Float.valueOf(mTotalAmount.getEditableText().toString());
//				} catch(NumberFormatException e){
//					;
//				}
//				float newAmount = oldTotalAmount + moneyChanged;
//				mTotalAmount.setText(String.format("%.2f", newAmount));
//			} else {
//				//Total amount is locked, split the balance to (unlocked && selected) users
//				float moneyChanged = currentAmount - oldAmount;
//				ArrayList<Integer> unlockedSelectedUserIndexList = 
//						getUnlockedSelectedUserIndex();
//				int size = unlockedSelectedUserIndexList.size() - 1; //except me
//				float moneyToSplit = moneyChanged / (float)size;
//				for(int index: unlockedSelectedUserIndexList){
//					if(indexOfUser == index)
//						continue;
//					if(-1 == index){
//						float old = myUserInfo.getAmountOfMoney();
//						myUserInfo.setAmountOfMoney(old - moneyToSplit);
//						mSelfBubble.setUserInfo(myUserInfo);
//						continue;
//					}
//
//					float old = mUserInfoList.get(index).getAmountOfMoney();
//					mUserInfoList.get(index).setAmountOfMoney(old - moneyToSplit);
//					mUserBubbles.get(index).setUserInfo(mUserInfoList.get(index));
//				}
//			}
//
//		}
//
//	}
//
//
//
//	/**
//	 * 
//	 * @param userId
//	 * @return index of UserInfo object in ArrayList, Myserlf = -1, not found = -2
//	 */
//	public int findUserIndexById(int userId){
//		if (myUserInfo.getUserId() == userId)
//			return -1;
//		for (int i=0; i<mUserInfoList.size(); i++){
//			UserInfo info = mUserInfoList.get(i);
//			if(info.getUserId() == userId)
//				return i;
//		}
//		return -2;
//	}
//
//	private class MyOnGlobalLayoutChgListener implements ViewTreeObserver.OnGlobalLayoutListener{
//
//		@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//		@Override
//		public void onGlobalLayout() {
//			
//			generateBubbles(2);
//			
//			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
//				mBubbleFrameLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//			else
//				mBubbleFrameLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//		}
//
//	}
//
//	private class EditButtonOnClickListener implements OnEditButtonClickedListener{
//		int indexOfBubble;
//
//		public EditButtonOnClickListener(int index){
//			indexOfBubble = index;
//		}
//
//		@Override
//		public void OnClick(View v) {
//			showBigBubble(mUserInfoList.get(indexOfBubble));
//
//		}
//
//	}
//
//	private class LockButtonOnClickListener implements OnLockButtonClickedListener{
//		int indexOfBubble;
//
//		public LockButtonOnClickListener(int index) {
//			indexOfBubble = index;
//		}
//
//		@Override
//		public void OnClick(View v, boolean isLocked) {
//			mUserInfoList.get(indexOfBubble).setLocked(isLocked);
//			Log.d(getTag(), "User" + indexOfBubble + " lock state = " + isLocked);
//		}
//
//	}
//
//	private class SelectUserOnClickListener implements OnCenterButtonClickedListener{
//		int indexOfBubble;
//
//		public SelectUserOnClickListener(int index) {
//			indexOfBubble = index;
//		}
//
//		@Override
//		public void OnClick(View v, boolean isSelected) {
//			mUserInfoList.get(indexOfBubble).setSelecetd(isSelected);
//			Log.d(getTag(), "User" + indexOfBubble + " select state = " + isSelected);
//			updateSelectedUserNumber();
//		}
//	}
//
//	private class DeselectUserOnClickListener implements OnDeselectButtonClickedListener{
//		int indexOfBubble;
//		public DeselectUserOnClickListener(int index) {
//			indexOfBubble = index;
//		}
//
//		@Override
//		public void OnClick(View v) {
//			mUserInfoList.get(indexOfBubble).setSelecetd(false);
//			Log.d(getTag(), "User" + indexOfBubble + " deselected");
//			updateSelectedUserNumber();
//		}
//
//	}
//
//	public ArrayList<String[]> getPaymentInfo() {
//		ArrayList<String[]> paymentInfo = new ArrayList<String[]>();
//		for (UserInfo info : mUserInfoList) {
//			if (info.isSelecetd()) {
//				String[] item = {"normal", info.getPersonalNote(), info.getUserName(), myUserInfo.getUserName(), "$ " + String.format("%.2f",info.getAmountOfMoney())};
//				paymentInfo.add(item);
////				Log.d(" paymentInfo", item[2]);
//			}
//		}
//		if (myUserInfo.isSelecetd()) {
//			String[] item = {"normal", myUserInfo.getPersonalNote(), myUserInfo.getUserName(), myUserInfo.getUserName(), "$ " + String.format("%.2f",myUserInfo.getAmountOfMoney())};
//			paymentInfo.add(item);
////			Log.d(" paymentInfo", item[2]);
//		}
//
//		String[] groupNote = {"group_note", ((EditText) getActivity().findViewById(R.id.group_note)).getText().toString() };
//		if (groupNote[1].length() > 0) paymentInfo.add(groupNote);
//
//
//		Calendar c = Calendar.getInstance();
//		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy  HH:mm:ss");
//		String strDate = sdf.format(c.getTime());
//
//		String[] summary = {"summary", strDate, String.valueOf(getSelectedUserSize()), "$ " + mTotalAmount.getText().toString()};
//		paymentInfo.add(summary);
////		Log.d(" paymentInfo", summary[3]);
//		/* example */
////		{
////			{"normal", "", "Luna", "Itziar", "$ 500"},
////			{"normal", "Pay one extra beer", "Patrick", "Itziar", "$ 30"},   //	name, amount, personal note
////			{"normal", "", "asd", "Itziar", "$ 20"},
////			{"normal", "", "Itziar", "Itziar", "$ 20"},
////			{"group_note", "This is a group note"},
////			{"summary", "2014-11-14", "5", "$ 130"}
////		}
//
//		for (String[] x : paymentInfo) {
//			for (int i = 0;i < x.length;i++) {
//				Log.d("return paymentInfo", x[i]);
//			}
//		}
//		return paymentInfo;
//	}
//
//	private class OnTotalMoneyFocusChangeListener implements OnFocusChangeListener{
//		private float oldAmount;
//		@Override
//		public void onFocusChange(View v, boolean hasFocus) {
//			if(hasFocus){
//				oldAmount = 0.0f;
//				try{
//					oldAmount = Float.valueOf(((EditText)v).getEditableText().toString());
//				} catch (NumberFormatException e){
//					oldAmount = 0.0f;
//				}
//				Log.d("TotalMoneyAmount", "" + oldAmount);
//				return;
//			}
//			float currentAmount = Float.valueOf(((EditText)v).getEditableText().toString());
//			ArrayList<Integer> targetUserIndex = getUnlockedSelectedUserIndex();
//			int size = targetUserIndex.size();
//			float splitAmount = (currentAmount - oldAmount) / (float)size;
//			for(Integer index: targetUserIndex){
//				if(index == -1){
//					float oldUserAmount = myUserInfo.getAmountOfMoney();
//					myUserInfo.setAmountOfMoney(oldUserAmount + splitAmount);
//					mSelfBubble.setUserInfo(myUserInfo);
//				} else {
//					float oldUserAmount = mUserInfoList.get(index).getAmountOfMoney();
//					mUserInfoList.get(index).setAmountOfMoney(oldUserAmount + splitAmount);
//					mUserBubbles.get(index).setUserInfo(mUserInfoList.get(index));
//				}
//			}
//
//			oldAmount = currentAmount;
//		}
//	}
//
//	private class OnGroupNoteFocusChangeListener implements OnFocusChangeListener{
//
//		@Override
//		public void onFocusChange(View v, boolean hasFocus) {
//			if(hasFocus)
//				return;
//			String groupNote = mGroupNote.getEditableText().toString();
//			myUserInfo.setPersonalNote(groupNote);
//			mSelfBubble.setUserInfo(myUserInfo);
//			for(int i=0; i<mUserInfoList.size(); i++){
//				mUserInfoList.get(i).setPublicNote(groupNote);
//				mUserBubbles.get(i).setUserInfo(mUserInfoList.get(i));
//			}
//
//		}
//
//	}
//
//	/**
//	 * Get selected user index
//	 * @return Index array. The index of myself = -1;
//	 */
//	public ArrayList<Integer> getSelectedUserIndex() {
//		//TODO:
//		ArrayList<Integer> retList = new ArrayList<Integer>();
//		if(myUserInfo.isSelecetd()){
//			retList.add(-1);
//		}
//
//		int userSize = mUserInfoList.size();
//		for (int i=0; i<userSize; i++){
//			if(mUserInfoList.get(i).isSelecetd())
//				retList.add(i);
//		}
//		return retList;
//	}
//
//	public int getSelectedUserSize(){
//		return getSelectedUserIndex().size();
//	}
//
//	public void updateSelectedUserNumber() {
//		int selectedUserNum = getSelectedUserSize();
//		mSelectCountText.setText(String.valueOf(selectedUserNum));
//		if(selectedUserNum > 0 && !getTotalLockState()){
//			mTotalAmount.setEnabled(true);
//		} else {
//			mTotalAmount.setEnabled(false);
//		}
//	}
//
//	public boolean getTotalLockState() {
//		RadarViewActivity activity = (RadarViewActivity)getActivity();
//		Boolean b =  activity.lockInfo.get("total");
//		return b;
//	}
//
//	/**
//	 * Get unlocked && selected user index
//	 * @return	Index array. The index of myself = -1;
//	 */
//	public ArrayList<Integer> getUnlockedSelectedUserIndex(){
//		ArrayList<Integer> retList = new ArrayList<Integer>();
//		if(myUserInfo.isSelecetd() && !myUserInfo.isLocked()){
//			retList.add(-1);
//		}
//		int userSize = mUserInfoList.size();
//		for (int i=0; i<userSize; i++){
//			if(mUserInfoList.get(i).isSelecetd()
//					&& !mUserInfoList.get(i).isLocked())
//				retList.add(i);
//		}
//		return retList;
//	}
//
//}
