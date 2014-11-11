package com.soontobe.joinpay;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MessageRetrievalService extends Service {
		
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO something useful
		return super.onStartCommand(intent, flags, startId);
	}




	@Override
	public IBinder onBind(Intent arg0) {
		
		return null;
	}
	
	
	
	
	public class JPMessage {
		private int messageId;
		private int sourceUserId;
		private int targetUserId;
		private int messageType; // 0- Source->Target(Send); 1-Target->Source(Request)
		private String publicMessage;
		private String privateMessage;
		
		public JPMessage(){

		}
		
		
		public int getSourceUserId() {
			return sourceUserId;
		}
		public void setSourceUserId(int sourceUserId) {
			this.sourceUserId = sourceUserId;
		}
		public int getTargetUserId() {
			return targetUserId;
		}
		public void setTargetUserId(int targetUserId) {
			this.targetUserId = targetUserId;
		}
		public int getMessageType() {
			return messageType;
		}
		public void setMessageType(int messageType) {
			this.messageType = messageType;
		}
		public String getPublicMessage() {
			return publicMessage;
		}
		public void setPublicMessage(String publicMessage) {
			this.publicMessage = publicMessage;
		}
		public String getPrivateMessage() {
			return privateMessage;
		}
		public void setPrivateMessage(String privateMessage) {
			this.privateMessage = privateMessage;
		}
		public int getMessageId() {
			return messageId;
		}
		
		
	}
}
