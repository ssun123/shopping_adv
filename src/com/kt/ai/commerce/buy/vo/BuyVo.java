package com.kt.ai.commerce.buy.vo;

public class BuyVo {

	private String buyNo=""; // 구매번호
	private String productId=""; // 상품아이디
	private String imgUrl=""; // 상품이미지주소
	private String productName=""; // 상품명
	private String productCost=""; // 상품가격
	private String favoriteYn=""; // 즐겨찾기여부
	private String detailUrl=""; // 상세주소
	private String deliveryState =""; // 배송 상태
	
	
	// getter , setter
	
	public String getBuyNo() {
		return buyNo;
	}
	public String getDeliveryState() {
		return deliveryState;
	}
	public void setDeliveryState(String deliveryState) {
		this.deliveryState = deliveryState;
	}
	public void setBuyNo(String buyNo) {
		this.buyNo = buyNo;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductCost() {
		return productCost;
	}
	public void setProductCost(String productCost) {
		this.productCost = productCost;
	}
	public String getFavoriteYn() {
		return favoriteYn;
	}
	public void setFavoriteYn(String favoriteYn) {
		this.favoriteYn = favoriteYn;
	}
	public String getDetailUrl() {
		return detailUrl;
	}
	public void setDetailUrl(String detailUrl) {
		this.detailUrl = detailUrl;
	}
}
