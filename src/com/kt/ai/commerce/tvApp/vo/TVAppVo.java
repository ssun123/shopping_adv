package com.kt.ai.commerce.tvApp.vo;

public class TVAppVo {
	
	private String reCommendType = ""; 
	private String reCommendVer = ""; 
	private String reCommendName = ""; // 추천 제목
	private String reCommendColor = ""; // 추천 컬러 값

	
	//getter, setter
	public String getReCommendName() {
		return reCommendName;
	}

	public String getReCommendType() {
		return reCommendType;
	}

	public void setReCommendType(String reCommendType) {
		this.reCommendType = reCommendType;
	}

	public String getReCommendVer() {
		return reCommendVer;
	}

	public void setReCommendVer(String reCommendVer) {
		this.reCommendVer = reCommendVer;
	}

	public void setReCommendName(String reCommendName) {
		this.reCommendName = reCommendName;
	}

	public String getReCommendColor() {
		return reCommendColor;
	}

	public void setReCommendColor(String reCommendColor) {
		this.reCommendColor = reCommendColor;
	}

}
