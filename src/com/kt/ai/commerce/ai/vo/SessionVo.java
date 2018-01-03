package com.kt.ai.commerce.ai.vo;

public class SessionVo {
	
	private String aiNo ="";
	private String qaType =""; // 대화주체 : 1(AI) ,2(USER)
	private String answerMsg = ""; // 대화내용
	
	public String getAiNo() {
		return aiNo;
	}
	public void setAiNo(String aiNo) {
		this.aiNo = aiNo;
	}
	public String getQaType() {
		return qaType;
	}
	public void setQaType(String qaType) {
		this.qaType = qaType;
	}
	public String getAnswerMsg() {
		return answerMsg;
	}
	public void setAnswerMsg(String answerMsg) {
		this.answerMsg = answerMsg;
	}
	
	
}
