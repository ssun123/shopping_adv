package com.kt.ai.commerce.memo.vo;

public class MemoVo {

	private String memo = "";	//메모
	private String brand ="";
	private String category="";
			

	//getter, setter	
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
}