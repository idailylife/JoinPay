package com.soontobe.joinpay.model;

import java.util.ArrayList;

/**
 * User information model for exchanging between RadarView and BigBubble
 * @author ��ΰ
 *
 */
public class UserInfo {
	private int userId;
	private String userName;
	private String publicNote;
	private String personalNote;
	private float amountOfMoney;
	private float changedMoney;	//Amount of money changed when editing TextEdit
	
	public float getChangedMoney() {
		return changedMoney;
	}

	public void setChangedMoney(float changedMoney) {
		this.changedMoney = changedMoney;
	}

	private boolean isLocked;
	
	public String getPublicNote() {
		return publicNote;
	}

	public void setPublicNote(String publicNote) {
		this.publicNote = publicNote;
	}

	public String getPersonalNote() {
		return personalNote;
	}

	public void setPersonalNote(String personalNote) {
		this.personalNote = personalNote;
	}

	public boolean isLocked() {
		return isLocked;
	}

	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}

	public UserInfo() {
		
	}
	
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}


	public float getAmountOfMoney() {
		return amountOfMoney;
	}

	public void setAmountOfMoney(float amountOfMoney) {
		this.amountOfMoney = amountOfMoney;
	}

	@Override
	public String toString() {
		String str = "UserInfo:";
		str += "Name = " + userName;
		return str;
	}
	
}
