package com.kt.ai.commerce.tvApp.vo;

public class PushVo {
	
	int userKey ;
	String deviceId ;
	String pushKey ;
	String pushTitle ;
	String pushMsg ;
	String prodId ;
	String prodUrl ;
	
	
	
	public String getProdUrl() {
		return prodUrl;
	}
	public void setProdUrl(String prodUrl) {
		this.prodUrl = prodUrl;
	}
	public int getUserKey() {
		return userKey;
	}
	public void setUserKey(int userKey) {
		this.userKey = userKey;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getPushKey() {
		return pushKey;
	}
	public void setPushKey(String pushKey) {
		this.pushKey = pushKey;
	}
	public String getPushTitle() {
		return pushTitle;
	}
	public void setPushTitle(String pushTitle) {
		this.pushTitle = pushTitle;
	}
	public String getPushMsg() {
		return pushMsg;
	}
	public void setPushMsg(String pushMsg) {
		this.pushMsg = pushMsg;
	}
	public String getProdId() {
		return prodId;
	}
	public void setProdId(String prodId) {
		this.prodId = prodId;
	}
	
	
}
