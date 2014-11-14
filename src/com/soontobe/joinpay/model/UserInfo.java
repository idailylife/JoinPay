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
	private boolean isMyself;
	private boolean isContact;
	private boolean isLocked;
	private boolean isSelecetd;
	
	public UserInfo() {
		isContact = false;
		isMyself = false;
		amountOfMoney = 0.0f;
		userName = "";
		publicNote = "";
		personalNote = "";
	}
	
	
	
	public boolean isMyself() {
		return isMyself;
	}



	public void setMyself(boolean isMyself) {
		this.isMyself = isMyself;
	}



	public boolean isSelecetd() {
		return isSelecetd;
	}

	public void setSelecetd(boolean isSelecetd) {
		this.isSelecetd = isSelecetd;
	}

	
	public boolean isContact() {
		return isContact;
	}

	public void setContactState(boolean isContact) {
		this.isContact = isContact;
	}

	public float getChangedMoney() {
		return changedMoney;
	}

	public void setChangedMoney(float changedMoney) {
		this.changedMoney = changedMoney;
	}

	
	
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
