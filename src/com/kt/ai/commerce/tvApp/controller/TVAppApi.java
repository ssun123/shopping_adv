package com.kt.ai.commerce.tvApp.controller;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kt.ai.commerce.ai.mapper.AiApiMapper;
import com.kt.ai.commerce.log.mapper.LogMapper;
import com.kt.ai.commerce.login.controller.LoginApi;
import com.kt.ai.commerce.push.PushAndroidUtil;
import com.kt.ai.commerce.tvApp.mapper.TVAppApiMapper;
import com.kt.ai.commerce.tvApp.vo.PushVo;
import com.kt.ai.commerce.tvApp.vo.TVAppVo;
import com.kt.ai.commerce.tvApp.vo.prodListVo;

@Controller
public class TVAppApi {

	static Logger log = Logger.getLogger(TVAppApi.class);
	
	@Autowired
    private SqlSession sqlSession;
	
	// TV 추천 - TV앱 실행시 메인 노출 추천 상품 목록
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/api/tvRecommend.api",method={RequestMethod.POST,RequestMethod.GET},produces="application/json; charset=utf8")
	public void tvRecommend(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// requst
		Map reqMap = LoginApi.getRequestMap(request);
		
		// log
		logInsert(request,reqMap,"TV 추천",""); 
		
		//get
		TVAppApiMapper mapper = sqlSession.getMapper(TVAppApiMapper.class);
		reqMap.put("userKey",Integer.parseInt(reqMap.get("userKey").toString()));
		JsonObject resultObj =  new JsonObject();
		JsonArray reCommendData =  new JsonArray();
		try {
			
			List<TVAppVo> tVoList = mapper.recommInfo(reqMap);
			
			for(int i=0;i<tVoList.size();i++){
				TVAppVo tVo = tVoList.get(i);
				JsonObject tObj = new JsonObject();
				tObj.addProperty("reCommendName", tVo.getReCommendName());
				tObj.addProperty("reCommendColor", tVo.getReCommendColor());
				tObj.addProperty("reCommendType", tVo.getReCommendType());
				tObj.addProperty("reCommendVer", tVo.getReCommendVer());
				reqMap.put("reCommendType", tVo.getReCommendType());
				reqMap.put("reCommendVer", tVo.getReCommendVer());
				
				List<prodListVo> productList = null;

				if(tVo.getReCommendType().equals("TVHIGH")) {
					productList = mapper.prodListByRecommHigh(reqMap);
				} else if(tVo.getReCommendType().equals("TVLOW")) {
					productList = mapper.prodListByRecommLow(reqMap);
				} else if(tVo.getReCommendType().equals("TVPOP")) {
					reqMap.put("recommendId", "ALL");
					
					long time = System.currentTimeMillis();
					SimpleDateFormat dayTime = new SimpleDateFormat("HH");
					String timeStr = dayTime.format(new Date(time));
					reqMap.put("searchTime", timeStr);
					productList = mapper.prodListByRecomm(reqMap);
					
					reqMap.remove("searchTime");
					
				} else if(tVo.getReCommendType().equals("TVSTB")) {
					
					// tv id
					String tvId = mapper.selectTvId(reqMap);
					reqMap.put("recommendId", tvId);
					productList = mapper.prodListByRecomm(reqMap);
				} else if(tVo.getReCommendType().equals("TVUSR")) {
					reqMap.put("recommendId", reqMap.get("userKey").toString());
					productList = mapper.prodListByRecomm(reqMap);
				}
								
				
				JsonArray productListJson = prodListToJson(productList);
				
				tObj.add("productList", productListJson);
				reCommendData.add(tObj);
			}
			
			resultObj.add("reCommendData", reCommendData);
			resultObj.addProperty("resCode", "0");
			resultObj.addProperty("resMsg", "성공");
			
		} catch (Exception e) {
			resultObj.addProperty("resCode", "1");
			resultObj.addProperty("resMsg", "시스템 오류입니다. 관리자에게 문의하세요");
			log.error("/api/tvRecommend.api error");
		} finally {
			// result
			response.setContentType("application/json;charset=utf-8");
			Gson gson = new Gson();
			PrintWriter printWriter = response.getWriter();
			printWriter.print(gson.toJson(resultObj));
			printWriter.close();
		}
	}

	
	// TV 검색 - TV 앱에서 검색어를 전달하여 해당 상품 목록을 받는다
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/api/tvSearch.api",method={RequestMethod.POST,RequestMethod.GET},produces="application/json; charset=utf8")
	public void tvSearch(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// requst
		Map reqMap = LoginApi.getRequestMap(request);
		
		// log
		logInsert(request,reqMap,"TV 검색","검색어입력");
		
		// get
		TVAppApiMapper mapper = sqlSession.getMapper(TVAppApiMapper.class);
		reqMap.put("userKey",Integer.parseInt(reqMap.get("userKey").toString()));
		JsonObject resultObj =  new JsonObject();
		JsonArray reCommendData =  new JsonArray();
		try {
			
			List searchColor = new ArrayList();
			List searchStyle = new ArrayList();
			List searchCategory = new ArrayList();
			List searchGender = new ArrayList();
			String searchWord = "";
			
			// 대화 서버 전달 내용 파싱
			
			String gBoRetrunStr[] = ((String) reqMap.get("searchWord")).split(",");

			System.out.println("########### gBoRetrunStr length : "+gBoRetrunStr.length);
			
			for(int g=0; g<gBoRetrunStr.length;g++) {
				String subStr[] = gBoRetrunStr[g].split(":");

				System.out.println("########### subStr : "+subStr);

				
				if(subStr[0].equals("Color")) {
					searchColor.add(subStr[1].toString());
				} else if(subStr[0].equals("Gender")) {
					searchGender.add(subStr[1].toString());
				} else if(subStr[0].indexOf("category3")>=0) {
					searchCategory.add(subStr[1].toString());
				} else if(subStr[0].equals("uword")) {
					searchWord = subStr[1].toString();
				} else if(subStr[0].indexOf("style")>=0) {
					searchStyle.add(subStr[1].toString());
				} else if(subStr[0].indexOf("theme")>=0) {
					searchStyle.add(subStr[1].toString());
				} else {
					
				}
				
			}
			
			if(searchColor.size()> 0) {
				reqMap.put("searchColor",searchColor);
			}
			if(searchStyle.size()> 0) {
				reqMap.put("searchStyle",searchStyle);
				reqMap.put("searchStyleCount",searchStyle.size());
			}
			
			if(searchCategory.size()> 0) {
				reqMap.put("searchCategory",searchCategory);
			}
			if(searchGender.size()> 0) {
				reqMap.put("searchGender",searchGender);
			}			

			System.out.println("########### prodListBySearch : " + reqMap);

			List<prodListVo> productList = mapper.prodListBySearch(reqMap);
			System.out.println("########### productList : " + productList.size());
			JsonArray productListJson = prodListToJson(productList);
			//set
			JsonObject rObj = new JsonObject();
			rObj.addProperty("reCommendName", searchWord);
			rObj.addProperty("reCommendColor", "");
			rObj.addProperty("reCommendType", "");
			rObj.addProperty("reCommendVer", "");

			rObj.add("productList", productListJson);
			reCommendData.add(rObj);
						
			resultObj.add("reCommendData", reCommendData);
			resultObj.addProperty("resCode", "0");
			resultObj.addProperty("resMsg", "성공");
		} catch (Exception e) {
			resultObj.addProperty("resCode", "1");
			resultObj.addProperty("resMsg", "시스템 오류입니다. 관리자에게 문의하세요");
			log.error("/api/tvSearch.api error");
		} finally {
			// result
			response.setContentType("application/json;charset=utf-8");
			Gson gson = new Gson();
			PrintWriter printWriter = response.getWriter();
			printWriter.print(gson.toJson(resultObj));
			printWriter.close();
		}
	}


	// 모바일로 상세보기 API-TV App용 - TV앱에서 상품 상세 View에서 모바일로 상세보기를 선택시 호출
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/api/callContantDetail.api",method={RequestMethod.POST,RequestMethod.GET},produces="application/json; charset=utf8")
	public void callContantDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// requst
		Map reqMap = LoginApi.getRequestMap(request);
		
		// log
		LogMapper logMapper = sqlSession.getMapper(LogMapper.class);
		HashMap logMap = new HashMap();	
		logMap.put("userKey", Integer.parseInt(reqMap.get("userKey").toString()));
		logMap.put("deviceType",reqMap.get("deviceType"));
		logMap.put("nowPage","TV 상품 상세"); 
		logMap.put("beforPage","TV 메인"); 
		logMap.put("reqMsg", request.getRequestURI()+"###"+request.getQueryString());
		logMapper.logInsert(logMap);
		
		TVAppApiMapper mapper = sqlSession.getMapper(TVAppApiMapper.class);
				
		reqMap.put("userKey",Integer.parseInt(reqMap.get("userKey").toString()));
		
		JsonObject resultObj =  new JsonObject();
		try {
			
			List pushUserList = mapper.selectPushKey(reqMap);

			for(int i = 0; i < pushUserList.size() ; i++) {
			
				PushVo pVo = (PushVo)pushUserList.get(i);
				
				// push 전송 요청
				PushAndroidUtil pau = new PushAndroidUtil();
				pau.sendMessage(pVo.getPushKey(), pVo.getPushTitle(), pVo.getPushMsg(), pVo.getProdId(), pVo.getProdUrl(), 0);
				
				// push history 저장
				mapper.pushHistoryInsert(pVo) ;
			}		

			resultObj.addProperty("resCode", "0");
			resultObj.addProperty("resMsg", "성공");
		} catch (Exception e) {
			resultObj.addProperty("resCode", "1");
			resultObj.addProperty("resMsg", "시스템 오류입니다. 관리자에게 문의하세요");
			log.error("/api/callContantDetail.api error");
		} finally {
			// result
			response.setContentType("application/json;charset=utf-8");
			Gson gson = new Gson();
			PrintWriter printWriter = response.getWriter();
			printWriter.print(gson.toJson(resultObj));
			printWriter.close();
		}
	}

	
	// 모바일로 상세보기 API-TV 셋탑용 - TV 실시간 방송중 주문하기 를 하였을 경우
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/api/callContantDetailTV.api",method={RequestMethod.POST,RequestMethod.GET},produces="application/json; charset=utf8")
	public void callContantDetailTV(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// requst
		Map reqMap = LoginApi.getRequestMap(request);
		
		// log
		LogMapper logMapper = sqlSession.getMapper(LogMapper.class);
		HashMap logMap = new HashMap();	
		logMap.put("userKey", "");
		logMap.put("deviceType","T");
		logMap.put("nowPage","상세보기");  
		logMap.put("beforPage","주문하기");
		logMap.put("reqMsg", request.getRequestURI()+"###"+request.getQueryString());
		logMapper.logInsert(logMap);
		
		TVAppApiMapper mapper = sqlSession.getMapper(TVAppApiMapper.class);

		// 방송중인 상품 id 찾기
		String prod_id = mapper.airProdId(reqMap);
		
		reqMap.put("productId",prod_id);
		
		JsonObject resultObj =  new JsonObject();
		try {
			List pushUserList = mapper.selectPushKeyByDeviceId(reqMap);

			for(int i = 0; i < pushUserList.size() ; i++) {
			
				PushVo pVo = (PushVo)pushUserList.get(i);
				
				// push 전송 요청
				PushAndroidUtil pau = new PushAndroidUtil();
				pau.sendMessage(pVo.getPushKey(), pVo.getPushTitle(), pVo.getPushMsg() , pVo.getProdId(),  pVo.getProdUrl(), 0);
				
				// push history 저장
				mapper.pushHistoryInsert(pVo) ;
			}		
			resultObj.addProperty("resCode", "0");
			resultObj.addProperty("resMsg", "성공");
		} catch (Exception e) {
			resultObj.addProperty("resCode", "1");
			resultObj.addProperty("resMsg", "시스템 오류입니다. 관리자에게 문의하세요");
			log.error("/api/callContantDetailTV.api error");
		} finally {
			// result
			response.setContentType("application/json;charset=utf-8");
			Gson gson = new Gson();
			PrintWriter printWriter = response.getWriter();
			printWriter.print(gson.toJson(resultObj));
			printWriter.close();
		}
	}
	
	//
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/api/tvViewContent.api",method={RequestMethod.POST,RequestMethod.GET},produces="application/json; charset=utf8")
	public void tvViewContent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// requst
		Map reqMap = LoginApi.getRequestMap(request);
		
		// log
		logInsert(request, reqMap, "본 상품", "상세보기");
		
		// get
		HashMap<String, String> resultMap = new HashMap<String, String>();
		AiApiMapper mapper = sqlSession.getMapper(AiApiMapper.class);
		reqMap.put("userKey",Integer.parseInt(reqMap.get("userKey").toString()));
		try {
			mapper.userViewContentInsert(reqMap);
			resultMap.put("resCode", "0");
			resultMap.put("resMsg", "성공");
		} catch (Exception e) {
			resultMap.put("resCode", "1");
			resultMap.put("resMsg", "시스템 오류입니다. 관리자에게 문의하세요");
			log.error("/api/tvViewContent.api error");
		} finally {
			// result
			response.setContentType("application/json;charset=utf-8");
			Gson gson = new Gson();
			PrintWriter printWriter = response.getWriter();
			printWriter.print(gson.toJson(resultMap));
			printWriter.close();
		}
	}

	//
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/api/tvLiveContent.api",method={RequestMethod.POST,RequestMethod.GET},produces="application/json; charset=utf8")
	public void tvLiveContent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// requst
		Map reqMap = LoginApi.getRequestMap(request);
		
		// log
		logInsert(request, reqMap, "홈쇼핑 상품", "홈쇼핑 방송중인 상품");
		
		// get
		TVAppApiMapper mapper = sqlSession.getMapper(TVAppApiMapper.class);
		reqMap.put("userKey",Integer.parseInt(reqMap.get("userKey").toString()));
		JsonObject resultObj =  new JsonObject();
		JsonArray reCommendData =  new JsonArray();
		try {
			
			List<prodListVo> productList = mapper.liveContent(reqMap);
			JsonArray productListJson = prodListToJson(productList);
			//set
			JsonObject rObj = new JsonObject();
			rObj.addProperty("reCommendName", "방송중인 상품");
			rObj.addProperty("reCommendColor", "");
			rObj.addProperty("reCommendType", "");
			rObj.addProperty("reCommendVer", "");
			rObj.add("productList", productListJson);
			reCommendData.add(rObj);
						
			resultObj.add("reCommendData", reCommendData);
			resultObj.addProperty("resCode", "0");
			resultObj.addProperty("resMsg", "성공");
		} catch (Exception e) {
			resultObj.addProperty("resCode", "1");
			resultObj.addProperty("resMsg", "시스템 오류입니다. 관리자에게 문의하세요");
			log.error("/api/tvSearch.api error");
		} finally {
			// result
			response.setContentType("application/json;charset=utf-8");
			Gson gson = new Gson();
			PrintWriter printWriter = response.getWriter();
			printWriter.print(gson.toJson(resultObj));
			printWriter.close();
		}
	}
	
	
	//0510 TV 연예인 검색
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/api/tvSearchCeleb.api",method={RequestMethod.POST,RequestMethod.GET})
	public void tvSearchCeleb(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// requst
		Map reqMap = LoginApi.getRequestMap(request);
		
		// log
		//logInsert(request,reqMap,"TV 검색","연예인상품검색");
		
		// get
		TVAppApiMapper mapper = sqlSession.getMapper(TVAppApiMapper.class);
		reqMap.put("userKey",Integer.parseInt(reqMap.get("userKey").toString()));
		JsonObject resultObj =  new JsonObject();
		JsonArray reCommendData =  new JsonArray();
		try {
			//progTitle:도깨비, prod:써니 가방
			//List searchProgTitle = new ArrayList();
			String searchProgTitle = "";
			List searchProdType  = new ArrayList();
			//List searchCharacterName = new ArrayList();
			String searchCharacterName = "";
			String searchWord = "";
			
			// 대화 서버 전달 내용 파싱
			
			String gBoRetrunStr[] = ((String) reqMap.get("searchWord")).split(",");
			
			for(int g=0; g<gBoRetrunStr.length;g++) {
				String subStr[] = gBoRetrunStr[g].split(":");
				
				if(subStr[0].equals("program_title")) {
					searchProgTitle = subStr[1];
				} 
				else if(subStr[0].equals("tv_product_category")) {
					searchProdType.add(subStr[1]);
				}else if(subStr[0].equals("person")) {
					searchCharacterName = subStr[1];
				}else if(subStr[0].equals("unk")) {
					searchWord = subStr[1];
				}				
			}			
		
			if(!searchProgTitle.equals("")) {
				reqMap.put("searchProgTitle",searchProgTitle);
			}
			if(!searchProdType.equals("")) {
				reqMap.put("searchProdType",searchProdType);
			}
			if(!searchCharacterName.equals("")) {
				reqMap.put("searchCharacterName",searchCharacterName);
			}		
			
			List<prodListVo> productList = mapper.prodSearchCeleb(reqMap);
			JsonArray productListJson = prodListToJson(productList);
			//set
			resultObj.addProperty("resCode", "0");
			resultObj.addProperty("resMsg", "성공");
			JsonObject rObj = new JsonObject();
			rObj.addProperty("reCommendName", reqMap.get("searchWord").toString());
			rObj.addProperty("reCommendColor", "");
			rObj.addProperty("reCommendType", "");
			rObj.addProperty("reCommendVer", "");
			rObj.add("productList", productListJson);
			reCommendData.add(rObj);
			resultObj.add("reCommendData", reCommendData);
		} catch (Exception e) {
			resultObj.addProperty("resCode", "1");
			resultObj.addProperty("resMsg", "시스템 오류입니다. 관리자에게 문의하세요");
			//log.error("/api/tvSearch.api error");
		} finally {
			// result
			response.setContentType("application/json;charset=utf-8");
			Gson gson = new Gson();
			PrintWriter printWriter = response.getWriter();
			printWriter.print(gson.toJson(resultObj));
			printWriter.close();
		}
	}

	
	//TV 생필품 검색
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/api/tvPantryContent.api",method={RequestMethod.POST,RequestMethod.GET})
	public void tvPantryContent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// requst
		Map reqMap = LoginApi.getRequestMap(request);
		
		// log
		//logInsert(request,reqMap,"TV 검색","연예인상품검색");
		
		// get
		TVAppApiMapper mapper = sqlSession.getMapper(TVAppApiMapper.class);
		reqMap.put("userKey",Integer.parseInt(reqMap.get("userKey").toString()));
		JsonObject resultObj =  new JsonObject();
		JsonArray reCommendData =  new JsonArray();
		try {
			String pantryCategory = "";
			String pantryProduct = "";
			
			// 대화 서버 전달 내용 파싱
			
			String gBoRetrunStr[] = ((String) reqMap.get("pantryWord")).split(",");
			
			for(int g=0; g<gBoRetrunStr.length;g++) {
				String subStr[] = gBoRetrunStr[g].split(":");
				
				if(subStr[0].equals("pantry_category")) {
					pantryCategory = subStr[1];
				} 
				else if(subStr[0].equals("pantry_product")) {
					pantryProduct = subStr[1];
				}				
			}			
		
			if(!pantryCategory.equals("")) {
				reqMap.put("pantryCategory",pantryCategory);
			}

			if(!pantryProduct.equals("")) {
				reqMap.put("pantryProduct",pantryProduct);
			}		
			
			List<prodListVo> productList = mapper.pantryContent(reqMap);
			//set
			
			if(productList.size()<1) {
				resultObj.addProperty("resCode", "1");
				resultObj.addProperty("resMsg", "등록하신 물품이 없습니다. 물품을 등록하신 후에 이용해 주세요");
				
			} else {
				resultObj.addProperty("resCode", "0");
				resultObj.addProperty("resMsg", "성공");
				
			}			

			
			JsonObject rObj = new JsonObject();
			rObj.addProperty("reCommendName", reqMap.get("pantryWord").toString());
			rObj.addProperty("reCommendColor", "");
			rObj.addProperty("reCommendType", "");
			rObj.addProperty("reCommendVer", "");

			JsonArray productListJson = prodListToJson(productList);
			rObj.add("productList", productListJson);
			reCommendData.add(rObj);
			resultObj.add("reCommendData", reCommendData);
		} catch (Exception e) {
			resultObj.addProperty("resCode", "1");
			resultObj.addProperty("resMsg", "시스템 오류입니다. 관리자에게 문의하세요");
			//log.error("/api/tvSearch.api error");
		} finally {
			// result
			response.setContentType("application/json;charset=utf-8");
			Gson gson = new Gson();
			PrintWriter printWriter = response.getWriter();
			printWriter.print(gson.toJson(resultObj));
			printWriter.close();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void logInsert(HttpServletRequest request, Map reqMap, String nowPage, String beforPage) throws Exception {
		LogMapper logMapper = sqlSession.getMapper(LogMapper.class);
		HashMap logMap = new HashMap();	
		logMap.put("userKey", Integer.parseInt(reqMap.get("userKey").toString()));
		logMap.put("deviceType",reqMap.get("deviceType"));
		logMap.put("nowPage",nowPage);
		logMap.put("beforPage",beforPage); 
		logMap.put("reqMsg", request.getRequestURI()+"###"+request.getQueryString());
		logMapper.logInsert(logMap);
	}
	

	public JsonArray prodListToJson(List<prodListVo> productList) {
		JsonArray productListJson = new JsonArray();
		for(int i=0;i<productList.size();i++){
			prodListVo pVo = (prodListVo) productList.get(i) ;
			JsonObject pObj = new JsonObject();
			pObj.addProperty("productType", pVo.getProductType());
			pObj.addProperty("productId", pVo.getProductId());
			pObj.addProperty("imgUrl", pVo.getImgUrl());
			pObj.addProperty("productName", pVo.getProductName());
			pObj.addProperty("productCost", pVo.getProductCost());
			pObj.addProperty("channelName", pVo.getChannelName());
			pObj.addProperty("channelNo", pVo.getChannelNo());
			pObj.addProperty("brand", pVo.getBrand());
			pObj.addProperty("logoUrl", pVo.getLogoUrl());
			pObj.addProperty("productMake", pVo.getProductMake());
			pObj.addProperty("videoUrl", pVo.getVideoUrl());
			pObj.addProperty("airStartTime",pVo.getAirStartTime());
			pObj.addProperty("airEndTime", pVo.getAirEndTime());
			productListJson.add(pObj);
		}
		return productListJson;
	}
	
}//endClass
