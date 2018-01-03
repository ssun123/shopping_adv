package com.kt.ai.commerce.pantry.vo;

public class PantryVo {

	private String category = "";	//카테고리명
	private String prodId = "";	//상품 ID
	private String prodName = "";	//상품명
	private String prodPrice = "";	//상품가격
	private String imgUrl = "";	//이미지 URL 
	
	//getter, setter
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getProdId() {
		return prodId;
	}
	public void setProdId(String prodId) {
		this.prodId = prodId;
	}
	public String getProdName() {
		return prodName;
	}
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}
	public String getProdPrice() {
		return prodPrice;
	}
	public void setProdPrice(String prodPrice) {
		this.prodPrice = prodPrice;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	
}