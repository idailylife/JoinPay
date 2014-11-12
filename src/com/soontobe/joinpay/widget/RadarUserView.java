package com.soontobe.joinpay.widget;

import com.soontobe.joinpay.R;
import com.soontobe.joinpay.model.UserInfo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.ViewGroup;

/**
 * Customized widget in radar view
 * contains 1 center button and 4 side buttons.
 * 
 * CAUTION: This widget has the size 108dp*108dp whether the side buttons show or not.
 * The center part of this widget have the size 96dp*96dp.
 * @author ��ΰ
 *
 */
public class RadarUserView extends FrameLayout {
	private static int[] CENTER_BUTTON_BKG_ID = {R.drawable.shape_circle_white_w_border, R.drawable.shape_circle_green_w_border};
	
	private boolean mIsPanelExpanded;	//Whether the 4 side buttons are shown.
	private boolean mIsMoneyLocked;		
	private boolean mIsUserSelected;
	private boolean mIsContact;
	
	private ImageView mYellowCircle;
	//private ImageView mGreenCircle[] = {null, null, null, null}; 	// 0-Top, 1-Left, 2-Bottom, 3-Right
	private ImageButton	mSideButtons[] = {null, null, null, null}; 	// 0-Top, 1-Left, 2-Bottom, 3-Right
	private Button mCenterButton;
	private TextView mNameText;
	private TextView mMoneyText;
	
	private ViewGroup.LayoutParams nameTextParams;
	
	OnLockButtonClickedListener lockBtnClickedListener = null;
	OnEditButtonClickedListener editBtnClickedListener = null;
	OnAddContactClickedListener addBtnClickedListener = null;
	OnCenterButtonClickedListener centerBtnClickedListener = null;
	OnDeselectButtonClickedListener deselectBtnClickedListener = null;
	
	
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
	
	public void setDeselectBtnClickedListener(
			OnDeselectButtonClickedListener deselectBtnClickedListener) {
		this.deselectBtnClickedListener = deselectBtnClickedListener;
	}

	public RadarUserView(Context context) {
		super(context);
		
		LayoutInflater.from(context).inflate(R.layout.adjust_panel, this);
		mIsPanelExpanded = false;
		mIsUserSelected = false;
		init();
		switchExpandPanel(false);
	}
	
	@SuppressLint("NewApi")
	public RadarUserView(Context context, AttributeSet attrs, int defStyleAttr,
			int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		LayoutInflater.from(context).inflate(R.layout.adjust_panel, this);
		mIsPanelExpanded = false;
		mIsUserSelected = false;
		init();
		switchExpandPanel(false);
	}

	public RadarUserView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		LayoutInflater.from(context).inflate(R.layout.adjust_panel, this);
		mIsPanelExpanded = false;
		mIsUserSelected = false;
		init();
		switchExpandPanel(false);
	}

	public RadarUserView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.adjust_panel, this);
		mIsPanelExpanded = false;
		mIsUserSelected = false;
		init();
		switchExpandPanel(false);
	}

	/**
	 * Must be called after construction method
	 * to update user info.
	 * @param userInfo
	 */
	public void setUserInfo(UserInfo userInfo){
		if(null != userInfo){
			setUserName(userInfo.getUserName());
			setMoneyAmount(userInfo.getAmountOfMoney());
			
			mIsContact = userInfo.isContact();
			if(userInfo.isContact()){
				setCenterButtonBackgroundState(true);
			}
			
			if(userInfo.isLocked()){
				changeLockState(true);
				//mIsMoneyLocked = true;
			} else {
				mIsMoneyLocked = false;
			}
		}
	}

	private void init() {
		mYellowCircle = (ImageView)findViewById(R.id.imgview_adjpanel_yellow);

		mSideButtons[0] = (ImageButton)findViewById(R.id.button_adjpanel_top);
		mSideButtons[0].setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d("AdjPanel", "MoneyLockState=" + !mIsMoneyLocked);
				changeLockState(!mIsMoneyLocked);

				if(lockBtnClickedListener != null)
					lockBtnClickedListener.OnClick(v, mIsMoneyLocked);
			}
		});
		
		mSideButtons[1] = (ImageButton)findViewById(R.id.button_adjpanel_left);
		mSideButtons[1].setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(editBtnClickedListener != null){
					editBtnClickedListener.OnClick(v);
				}
			}
		});
		
		mSideButtons[2] = (ImageButton)findViewById(R.id.button_adjpanel_bottom);
		mSideButtons[2].setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(addBtnClickedListener != null){
					addBtnClickedListener.OnClick(v);
				}
			}
		});
		
		mSideButtons[3] = (ImageButton)findViewById(R.id.button_adjpanel_right);
		mSideButtons[3].setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Deselect and close expand panel
				setSelectState(false);
				switchExpandPanel(false);
				if(null != deselectBtnClickedListener){
					deselectBtnClickedListener.OnClick(v);
				}
			}
		});
		
		mCenterButton = (Button)findViewById(R.id.button_adjpanel_center);
		mCenterButton.setOnClickListener(new CenterBtnClickListener());
		
		mNameText = (TextView)findViewById(R.id.textview_adjpanel_name);
		nameTextParams = mNameText.getLayoutParams();
		mMoneyText = (TextView)findViewById(R.id.textview_adjpanel_money);
	}

	private void setUserName(String userName) {
		mNameText.setText(userName);		
	}
	
	/**
	 * Set amount of money
	 * 
	 * @param amountOfMoney If this value is 0.0f, the amount of money will be hided.
	 */
	
	public void setMoneyAmount(float amountOfMoney) {
		if(Float.valueOf(amountOfMoney).equals(0.0f)){
			//Hide money amount and reset view
			mMoneyText.setVisibility(View.GONE);
			mNameText.setTextSize(22.0f);	//enlarge text
			
			FrameLayout.LayoutParams params = new LayoutParams(mNameText.getLayoutParams());
			params.height = params.height + 20;
			params.gravity = Gravity.CENTER;
			params.setMargins(0, 0, 0, 0);
			mNameText.setLayoutParams(params);
		} else {
			mMoneyText.setText(String.valueOf(amountOfMoney));
			//Restore default layout with mondy amount
			mMoneyText.setVisibility(View.VISIBLE);
			mNameText.setTextSize(14.0f);
			mNameText.setLayoutParams(nameTextParams);
		}	
	}
	
	/**
	 * Set whether a user is a contact of mine.
	 * This will change the background color of center button.
	 * @param isContact
	 */
	private void setCenterButtonBackgroundState(boolean isContact){
		if (isContact){
			mCenterButton.setBackgroundResource(CENTER_BUTTON_BKG_ID[1]);
			mNameText.setTextColor(Color.parseColor("#FFFFFF"));
			mMoneyText.setTextColor(Color.parseColor("#FFFFFF"));
		} else {
			mCenterButton.setBackgroundResource(CENTER_BUTTON_BKG_ID[0]);
			mNameText.setTextColor(Color.parseColor("#000000"));
			mMoneyText.setTextColor(Color.parseColor("#000000"));
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
//				mGreenCircle[i].setVisibility(View.INVISIBLE);
				mSideButtons[i].setVisibility(View.INVISIBLE);
			}
			mIsPanelExpanded = false;
		} else {
			mYellowCircle.setVisibility(View.VISIBLE);
			for (int i=0; i<mSideButtons.length; i++){
//				mGreenCircle[i].setVisibility(View.VISIBLE);
				mSideButtons[i].setVisibility(View.VISIBLE);
			}
			mIsPanelExpanded = true;
		}
		
	}
	
	public void changeLockState (boolean isLocked){
		if(isLocked){
			mSideButtons[0].setImageResource(R.drawable.locked_white);
			mIsMoneyLocked = true;
		} else {
			mSideButtons[0].setImageResource(R.drawable.unlocked_white);
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
	

	
	
	@Override
	public boolean isInEditMode() {
		// TODO Auto-generated method stub
		return true;
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
		public void OnClick(View v, boolean isSelected);
	}
	
	public interface OnDeselectButtonClickedListener {
		public void OnClick(View v);
	}
	
	/**
	 * Actions:
	 * [1] Unselected --Click--> Selected;
	 * [2] Selected&Unexpanded <--Click--> Selected&Expanded
	 * 
	 * @author ��ΰ
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
			if(centerBtnClickedListener != null){
				centerBtnClickedListener.OnClick(v, mIsUserSelected);
			}
		}
	}
}
