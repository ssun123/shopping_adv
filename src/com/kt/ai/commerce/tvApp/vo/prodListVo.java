package com.kt.ai.commerce.tvApp.vo;

public class prodListVo {
	private String productType = ""; //상품 구분(방송, 쇼핑몰)
	private String productId = ""; //  상품 아이디
	private String imgUrl = ""; // 상품 이미지 주소
	private String productName = ""; // 상품명
	private String productCost = ""; // 상품 가격
	private String channelName = ""; // 채널 또는 쇼핑몰 명
	private String brand = ""; // 브랜드명
	private String logoUrl = ""; // 로고 이미지
	private String productMake = ""; // 생산지
	private String airStartTime = ""; // 방송 시작 일시
	private String airEndTime = ""; // 방송 종료 일시
	private String detailUrl = "";
	private String videoUrl = "";
	
	private String favoriteYn = "";
	private String channelNo = ""; // 채널 번호
	
	
	private String airReadyYn =""; // 방송예정 여부
	//12.1 추가 쇼핑 리스트
	private String productUrl = ""; // 상품URL
	private String partnerName = ""; // 쇼핑몰 명
	private String memo ="";		//memo
	
	//getter,setter
	
	
	public String getProductType() {
		return productType;
	}
	public String getAirReadyYn() {
		return airReadyYn;
	}
	public void setAirReadyYn(String airReadyYn) {
		this.airReadyYn = airReadyYn;
	}
	public String getChannelNo() {
		return channelNo;
	}
	public void setChannelNo(String channelNo) {
		this.channelNo = channelNo;
	}
	public String getFavoriteYn() {
		return favoriteYn;
	}
	public void setFavoriteYn(String favoriteYn) {
		this.favoriteYn = favoriteYn;
	}
	public String getVideoUrl() {
		return videoUrl;
	}
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
	public String getDetailUrl() {
		return detailUrl;
	}
	public void setDetailUrl(String detailUrl) {
		this.detailUrl = detailUrl;
	}
	public void setProductType(String productType) {
		this.productType = productType;
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
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getLogoUrl() {
		return logoUrl;
	}
	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}
	public String getProductMake() {
		return productMake;
	}
	public void setProductMake(String productMake) {
		this.productMake = productMake;
	}
	public String getAirStartTime() {
		return airStartTime;
	}
	public void setAirStartTime(String airStartTime) {
		this.airStartTime = airStartTime;
	}
	public String getAirEndTime() {
		return airEndTime;
	}
	public void setAirEndTime(String airEndTime) {
		this.airEndTime = airEndTime;
	}
	

	public String getProductUrl() {
		return productUrl;
	}
	public void setProductUrl(String productUrl) {
		this.productUrl = productUrl;
	}
	public String getPartnerName() {
		return partnerName;
	}
	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
}

