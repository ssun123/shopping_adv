package com.kt.ai.commerce.ai.vo;

public class AiVo {
	
	private int aiSessionNo; //  대화 세션 번호
	private String aiSessionType=""; // 대화 세션 타입(대화형:TK, 인기테마:BS)
	
	
	// getter, setter
	public int getAiSessionNo() {
		return aiSessionNo;
	}
	public void setAiSessionNo(int aiSessionNo) {
		this.aiSessionNo = aiSessionNo;
	}
	public String getAiSessionType() {
		return aiSessionType;
	}
	public void setAiSessionType(String aiSessionType) {
		this.aiSessionType = aiSessionType;
	}
	
}
