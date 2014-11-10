package com.soontobe.joinpay.widget;

import com.soontobe.joinpay.R;
import com.soontobe.joinpay.model.UserInfo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Customized widget in radar view
 * contains 1 center button and 4 side buttons.
 * 
 * CAUTION: This widget has the size 96dp*96dp whether the side buttons show or not.
 * The center part of this widget have the size 80dp*80dp.
 * @author ²©Î°
 *
 */
public class RadarUserView extends FrameLayout {
	private static int[] CENTER_BUTTON_BKG_ID = {R.drawable.shape_circle_white_w_border, R.drawable.shape_circle_green_w_border};
	
	private boolean mIsPanelExpanded;	//Whether the 4 side buttons are shown.
	private boolean mIsMoneyLocked;		
	private boolean mIsUserSelected;
	private boolean mIsContact;
	
	private ImageView mYellowCircle;
	private ImageView mGreenCircle[]; 	// 0-Top, 1-Left, 2-Bottom, 3-Right
	private Button	mSideButtons[]; 	// 0-Top, 1-Left, 2-Bottom, 3-Right
	private Button mCenterButton;
	private TextView mNameText;
	private TextView mMoneyText;
	
	OnLockButtonClickedListener lockBtnClickedListener = null;
	OnEditButtonClickedListener editBtnClickedListener = null;
	OnAddContactClickedListener addBtnClickedListener = null;
	OnCenterButtonClickedListener centerBtnClickedListener = null;
	public void setLockBtnClickedListener(
			OnLockButtonClickedListener lockBtnClickedListener) {
		this.lockBtnClickedListener = lockBtnClickedListener;
	}

	public void setEditBtnClickedListener(
			OnEditButtonClickedListener editBtnClickedListener) {
		this.editBtnClickedListener = editBtnClickedListener;
	}

	public void setAddBtnClickedListener(
			OnAddContactClickedListener addBtnClickedListener) {
		this.addBtnClickedListener = addBtnClickedListener;
	}

	public void setCenterBtnClickedListener(
			OnCenterButtonClickedListener centerBtnClickedListener) {
		this.centerBtnClickedListener = centerBtnClickedListener;
	}

	public RadarUserView(Context context, UserInfo userInfo) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.adjust_panel, this);
		init();
		
		switchExpandPanel(false);
		mIsPanelExpanded = false;
		mIsUserSelected = false;
		
		if(null != userInfo){
			setUserName(userInfo.getUserName());
			setMoneyAmount(userInfo.getAmountOfMoney());
			
			mIsContact = userInfo.isContact();
			if(userInfo.isContact()){
				setCenterButtonBackgroundState(true);
			}
			
			if(userInfo.isLocked()){
				changeLockState(true);
				mIsMoneyLocked = true;
			} else {
				mIsMoneyLocked = false;
			}
		}
	}

	private void init() {
		mYellowCircle = (ImageView)findViewById(R.id.imgview_adjpanel_yellow);
		
		mGreenCircle[0] = (ImageView)findViewById(R.id.imgview_adjpanel_top);
		mGreenCircle[1] = (ImageView)findViewById(R.id.imgview_adjpanel_left);
		mGreenCircle[2] = (ImageView)findViewById(R.id.imgview_adjpanel_bottom);
		mGreenCircle[3] = (ImageView)findViewById(R.id.imgview_adjpanel_right);
		
		mSideButtons[0] = (Button)findViewById(R.id.button_adjpanel_top);
		mSideButtons[0].setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				changeLockState(!mIsMoneyLocked);
				Log.d("AdjPanel", "MoneyLockState=" + !mIsMoneyLocked);
				mIsMoneyLocked = !mIsMoneyLocked;
				//Invoke further listener!
				lockBtnClickedListener.OnClick(v, mIsMoneyLocked);
			}
		});
		mSideButtons[1] = (Button)findViewById(R.id.button_adjpanel_left);
		mSideButtons[2] = (Button)findViewById(R.id.button_adjpanel_bottom);
		mSideButtons[3] = (Button)findViewById(R.id.button_adjpanel_right);
		mSideButtons[3].setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Deselect and close expand panel
				setSelectState(false);
				switchExpandPanel(false);
			}
		});
		
		mCenterButton = (Button)findViewById(R.id.button_adjpanel_center);
		mCenterButton.setOnClickListener(new CenterBtnClickListener());
		
		mNameText = (TextView)findViewById(R.id.textview_adjpanel_name);
		mMoneyText = (TextView)findViewById(R.id.textview_adjpanel_money);
	}

	private void setUserName(String userName) {
		mNameText.setText(userName);		
	}
	
	private void setMoneyAmount(float amountOfMoney) {
		mMoneyText.setText(String.valueOf(amountOfMoney));		
	}
	
	/**
	 * Set whether a user is a contact of mine.
	 * This will change the background color of center button.
	 * @param isContact
	 */
	private void setCenterButtonBackgroundState(boolean isContact){
		if (isContact){
			mCenterButton.setBackgroundResource(CENTER_BUTTON_BKG_ID[1]);
		} else {
			mCenterButton.setBackgroundResource(CENTER_BUTTON_BKG_ID[0]);
		}
	}

	public boolean isPanelExpanded() {
		return mIsPanelExpanded;
	}
	
	public boolean isUserSelected() {
		return mIsUserSelected;
	}

	/**
	 * Switch the visibility of 4 side buttons
	 * @param b
	 */
	private void switchExpandPanel(boolean visible) {
		if(!visible){
			mYellowCircle.setVisibility(View.INVISIBLE);
			for (int i=0; i<mSideButtons.length; i++){
				mGreenCircle[i].setVisibility(View.INVISIBLE);
				mSideButtons[i].setVisibility(View.INVISIBLE);
			}
			mIsPanelExpanded = false;
		} else {
			mYellowCircle.setVisibility(View.VISIBLE);
			for (int i=0; i<mSideButtons.length; i++){
				mGreenCircle[i].setVisibility(View.VISIBLE);
				mSideButtons[i].setVisibility(View.VISIBLE);
			}
			mIsPanelExpanded = true;
		}
		
	}
	
	public void changeLockState (boolean isLocked){
		if(isLocked){
			mSideButtons[0].setBackgroundResource(R.drawable.locked_white);
			mIsMoneyLocked = true;
		} else {
			mSideButtons[0].setBackgroundResource(R.drawable.unlocked_white);
			mIsMoneyLocked = false;
		}
	}
	
	/**
	 * Set weather a user is selected, 
	 * click center button of a selected user will expand the side buttons.
	 * @param isSelected
	 */
	public void setSelectState(boolean isSelected){
		mIsUserSelected = isSelected;
		int resId;
		if(isSelected){
			if(mIsContact){
				resId = R.drawable.shape_circle_green_w_boldborder;
			} else {
				resId = R.drawable.shape_circle_white_w_boldborder;
			}
		} else {
			if(mIsContact){
				resId = R.drawable.shape_circle_green_w_border;
			} else {
				resId = R.drawable.shape_circle_white_w_border;
			}
		}
		mCenterButton.setBackgroundResource(resId);
		mIsUserSelected = isSelected;
	}
	
	
	public interface OnLockButtonClickedListener {
		public void OnClick(View v, boolean isLocked);
	}
	
	public interface OnEditButtonClickedListener {
		public void OnClick(View v);
	}
	
	public interface OnAddContactClickedListener {
		public void OnClick(View v);
	}
	
	public interface OnCenterButtonClickedListener {
		public void OnClick(View v);
	}
	
	/**
	 * Actions:
	 * [1] Unselected --Click--> Selected;
	 * [2] Selected&Unexpanded <--Click--> Selected&Expanded
	 * 
	 * @author ²©Î°
	 *
	 */
	private class CenterBtnClickListener implements View.OnClickListener{
		
		@Override
		public void onClick(View v) {
			if(!mIsUserSelected){
				setSelectState(true);
			} else if (!mIsPanelExpanded){
				switchExpandPanel(true);
			} else {
				switchExpandPanel(false);
			}
		}
		
	}
}
