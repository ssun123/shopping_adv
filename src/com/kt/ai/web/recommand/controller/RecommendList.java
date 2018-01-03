package com.kt.ai.web.recommand.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.aspectj.asm.internal.RelationshipMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.kt.ai.commerce.log.mapper.LogMapper;
import com.kt.ai.commerce.login.controller.LoginApi;
import com.kt.ai.commerce.tvApp.mapper.TVAppApiMapper;
import com.kt.ai.commerce.tvApp.vo.TVAppVo;
import com.kt.ai.commerce.tvApp.vo.prodListVo;

@Controller
public class RecommendList {

	static Logger log = Logger.getLogger(RecommendList.class);
	
	@Autowired
    private SqlSession sqlSession;
	
	
	// 추천상품 리스트
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/web/recommendList.do",method={RequestMethod.POST,RequestMethod.GET},produces="application/json; charset=utf8")
	public ModelAndView recommendList(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		
		ModelAndView mav = new ModelAndView("Web_Recommend");
		
		// requst
		Map reqMap = LoginApi.getRequestMap(request);
		String reCommendType = reqMap.get("reCommendType").toString(); // 페이지 분기
		String reCommendVer = reqMap.get("reCommendVer").toString();
		String deviceId = reqMap.get("deviceId").toString(); // device ID 
		
		// log		
		String logStr = logInsertMakeStr(reCommendType);
		logInsert(request, reqMap, logStr, "");
								
		// get
		TVAppApiMapper mapper = sqlSession.getMapper(TVAppApiMapper.class);
		reqMap.put("userKey",Integer.parseInt(reqMap.get("userKey").toString()));
		session.setAttribute("userKey", reqMap.get("userKey"));
		
		try {
			
			System.out.println("reCommendVer : " + reCommendVer);
			
			List<prodListVo> productList = null;
			
			if(reCommendType.equals("TVSTB")){  // 우리집 추천상품
				reqMap.put("recommendId", deviceId);
				productList = mapper.prodListByRecomm(reqMap);
			}
			else if(reCommendType.equals("TVPOP")){ // 지니쇼핑 베스트
				reqMap.put("recommendId", "ALL");
				long time = System.currentTimeMillis();
				SimpleDateFormat dayTime = new SimpleDateFormat("HH");
				String timeStr = dayTime.format(new Date(time));
				reqMap.put("searchTime", timeStr);
				productList = mapper.prodListByRecomm(reqMap);
				reqMap.remove("searchTime");
			}	
			else if(reCommendType.equals("TVUSR")){ // 나에게 딱
				reqMap.put("recommendId", reqMap.get("userKey").toString());
				productList = mapper.prodListByRecomm(reqMap);
			}	
			else if(reCommendType.equals("TVHIGH")){ // 자주 구매 상품
				productList = mapper.prodListByRecommHigh(reqMap);
			}
			
			//System.out.println("reqMap.get(userKey) : " + reqMap.get("userKey"));
			
			mav.addObject("userKey",reqMap.get("userKey"));
			mav.addObject("productList",new Gson().toJson(productList));
			mav.addObject("reCommendType", reCommendType);
			mav.addObject("reCommendVer", reCommendVer);
			mav.addObject("deviceId",deviceId);
			
		} catch (Exception e) {
			log.error("/web/recommendList.do error");
		}
		return mav;
	}
	
	
	// 상품 디테일
	@SuppressWarnings({ "rawtypes", "unchecked" }) 
	@RequestMapping(value="/web/recommendDetail.do",method={RequestMethod.POST,RequestMethod.GET},produces="application/json; charset=utf8")
	public ModelAndView recommendDetail(HttpServletRequest request, HttpServletResponse response, HttpSession session
			,@RequestParam(value="searchText",required=false) String searchText) throws Exception {
		
		ModelAndView mav = new ModelAndView("Web_Detail");		
		Map reqMap = LoginApi.getRequestMap(request);

		String deviceId = reqMap.get("deviceId").toString();  
		String prodId = reqMap.get("prodId").toString();
		String userKey =  reqMap.get("userKey").toString();
		String reCommendType = reqMap.get("reCommendType").toString();
		String reCommendVer = reqMap.get("reCommendVer").toString();
		
		// get
		TVAppApiMapper mapper = sqlSession.getMapper(TVAppApiMapper.class);
		reqMap.put("userKey",Integer.parseInt(userKey));
		reqMap.put("prodId", prodId);
		//reqMap.put("deviceId", deviceId);
		
		try {
			
			List<prodListVo> productList = null;
			prodListVo productDetail = null;
		
			// log		
			String logStr = logInsertMakeStr(reCommendType);
			logInsert(request, reqMap, "상품 상세", logStr);
		
			
			if(reCommendType.equals("TVSTB")){  // 우리집 추천상품
				reqMap.put("recommendId", deviceId);
				productList = mapper.prodListByRecomm(reqMap);
				productDetail = mapper.prodListByRecommDetail(reqMap);
			}
			else if(reCommendType.equals("TVPOP")){ // 지니쇼핑 베스트		
				reqMap.put("recommendId", "ALL");
				long time = System.currentTimeMillis();
				SimpleDateFormat dayTime = new SimpleDateFormat("HH");
				String timeStr = dayTime.format(new Date(time));
				reqMap.put("searchTime", timeStr);
				productList = mapper.prodListByRecomm(reqMap);
				productDetail = mapper.prodListByRecommDetail(reqMap);
				reqMap.remove("searchTime");
			}	
			else if(reCommendType.equals("TVUSR")){ // 나에게 딱
				reqMap.put("recommendId", reqMap.get("userKey").toString());
				productList = mapper.prodListByRecomm(reqMap);
				productDetail = mapper.prodListByRecommDetail(reqMap);
			}	
			else if(reCommendType.equals("TVHIGH")){ // 자주 구매 상품
				productList = mapper.prodListByRecommHigh(reqMap);
				productDetail = mapper.prodListByRecommHighDetail(reqMap);
			}
			else if(reCommendType.equals("TVLOW")){ // 저관여 상품
				productList = mapper.prodListByRecommLow(reqMap);
				productDetail = mapper.prodListByRecommLowDetail(reqMap);
			}
			else if(reCommendType.equals("TREND_IMG")){ // 트렌드 추천 상품
				
			}
			else if(reCommendType.equals("COORDI")){ // 트렌드 추천 코디
//				System.out.println("prodId : " + prodId);
				productList = mapper.prodListByRecommTrend(reqMap);
				reqMap.put("prodId", productList.get(0).getProductId());
				productDetail = mapper.prodListBySearchTextDetail(reqMap);
				logStr = "트렌드 코디 추천 상품";
			}
			
			mav.addObject("userKey",userKey);
			mav.addObject("reCommendType",reCommendType);
			mav.addObject("reCommendVer",reCommendVer);
			mav.addObject("deviceId",deviceId);
			mav.addObject("deviceType",reqMap.get("deviceType"));
			mav.addObject("productList",new Gson().toJson(productList));
			mav.addObject("productDetail",productDetail);
			mav.addObject("reCommendType", reCommendType);
			mav.addObject("searchStr",logStr);
			
		} catch (Exception e) {
			log.error("/web/recommendList.do error");
		}
		
		return mav;
	}
	
	
	// VOD 트렌드 추천 리스트
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/web/recommendTrendCoordi.do",method={RequestMethod.POST,RequestMethod.GET},produces="application/json; charset=utf8")
	public ModelAndView recommendTrendCoordi(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		
		ModelAndView mav = new ModelAndView("Web_Trend_Coordi");
		// get
		TVAppApiMapper mapper = sqlSession.getMapper(TVAppApiMapper.class);
		
		Map reqMap = LoginApi.getRequestMap(request);
		String deviceId = reqMap.get("deviceId").toString();  // app req  
		String deviceType = reqMap.get("deviceType").toString(); // app req
		String userKey =  reqMap.get("userKey").toString(); // app req
		String prodId = reqMap.get("prodId").toString();
		String reCommendType = "COORDI";
		reqMap.put("reCommendType", reCommendType);
		String reCommendVer = "000000";
		
		// log		
		//logInsert(request, reqMap, "코디 상품추천", "메인");
		
		try {
			
			List<prodListVo> productList = null;
			productList = mapper.imgListByRecommTrend(reqMap);
			
			mav.addObject("prodId",prodId);
			mav.addObject("productList",productList);
			mav.addObject("deviceId", deviceId);
			mav.addObject("deviceType", deviceType);
			mav.addObject("userKey", userKey);
			mav.addObject("reCommendType", reCommendType);
			mav.addObject("reCommendVer", reCommendVer);
			
		} catch (Exception e) {
			System.out.println("recommendTrendCoordi.do error");
		}
	
		
		return mav;
	}
	
	
//	// VOD 추천 리스트
//	@SuppressWarnings("rawtypes")
//	@RequestMapping(value="/web/recommendTrendVod.do",method={RequestMethod.POST,RequestMethod.GET},produces="application/json; charset=utf8")
//	public ModelAndView recommendTrendVod(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
//		
//		Map reqMap = LoginApi.getRequestMap(request);
//		String deviceId = reqMap.get("deviceId").toString();  // app req  
//		String deviceType = reqMap.get("deviceType").toString(); // app req
//		String userKey =  reqMap.get("userKey").toString(); // app req
//		String reCommendType = "TREND_IMG";
//		String reCommendVer = "NOVER";
//		// log		
//		logInsert(request, reqMap, "트렌드 상품추천", "메인");
//		
//		//System.out.println("test");
//		ModelAndView mav = new ModelAndView("Web_Trend_Vod");		
//		//Map reqMap = LoginApi.getRequestMap(request);
//		
//		mav.addObject("deviceId", deviceId);
//		mav.addObject("deviceType", deviceType);
//		mav.addObject("userKey", userKey);
//		mav.addObject("reCommendType", reCommendType);
//		mav.addObject("reCommendVer", reCommendVer);
//		
//		
//		return mav;
//	}

	
	
	// 상품 디테일 음성 검색시 
	@SuppressWarnings({ "rawtypes", "unchecked" }) 
	@RequestMapping(value="/web/recommendDetailSearch.do",method={RequestMethod.POST,RequestMethod.GET},produces="application/json; charset=utf8")
	public ModelAndView recommendDetailSearch(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		
		ModelAndView mav = new ModelAndView("Web_Detail");	
		
		Map reqMap = LoginApi.getRequestMap(request);
//		String userKey =  reqMap.get("userKey").toString();
//		String deviceType =  reqMap.get("deviceType").toString();
		
		// get
		TVAppApiMapper mapper = sqlSession.getMapper(TVAppApiMapper.class);
		
		try {
			
			List<prodListVo> productList = null;
			prodListVo productDetail = null;
			
			// log		
			logInsert(request, reqMap, "상품 상세", "메인");
			
			List searchColor = new ArrayList();
			List searchStyle = new ArrayList();
			List searchCategory = new ArrayList();
			List searchGender = new ArrayList();
			String searchText = "";
			
			// 대화 서버 전달 내용 파싱
			String gBoRetrunStr[] = ((String) reqMap.get("searchText")).split(",");
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
				} else if(subStr[0].equals("unk")) {  // 검색 내용
					searchText = subStr[1].toString();
				} else if(subStr[0].indexOf("style")>=0) {
					searchStyle.add(subStr[1].toString());
				} else if(subStr[0].indexOf("theme")>=0) {
		               searchStyle.add(subStr[1].toString());
				}else if(subStr[0].indexOf("brand")>=0) {
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

			productList = mapper.prodListBySearch(reqMap);
			
			if(reqMap.get("prodId") != null && !reqMap.get("prodId").equals("")){
				 reqMap.put("prodId", reqMap.get("prodId"));
			}else{
				 reqMap.put("prodId", productList.get(0).getProductId());
			}
			productDetail = mapper.prodListBySearchTextDetail(reqMap);
		    String reCommendType = "search";
			
		    //mav.addObject("pageReqNum",reqMap.get("pageReqNum"));
		    
		    if(reqMap.get("pageReqNum") != null && !reqMap.get("pageReqNum").equals("")){
		    	mav.addObject("pageReqNum",reqMap.get("pageReqNum"));
		    }
		    
		    
			mav.addObject("reCommendType",reCommendType);
			mav.addObject("productList",new Gson().toJson(productList));
			mav.addObject("productDetail",productDetail);
			mav.addObject("reCommendType", reCommendType);
			mav.addObject("searchStr",searchText);
			
		} catch (Exception e) {
			log.error("/web/recommendList.do error");
		}
		
		return mav;
	}
	
	
	// 상품 디테일 ajax
	@SuppressWarnings({"unchecked","rawtypes"})
	@RequestMapping(value="/web/recommendDetailAjax.do",method={RequestMethod.POST,RequestMethod.GET},produces="application/json; charset=utf8")
	public ModelAndView recommendDetailAjax(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		
		System.out.println("test");
		
		ModelAndView mav = new ModelAndView("Web_Detail_Temp");		
		Map reqMap = LoginApi.getRequestMap(request);
		
		String deviceId = reqMap.get("deviceId").toString();  
		String prodId = reqMap.get("prodId").toString();
		String userKey =  reqMap.get("userKey").toString();
		String reCommendType = reqMap.get("reCommendType").toString();
		String reCommendVer = reqMap.get("reCommendVer").toString();
		
		// get
		TVAppApiMapper mapper = sqlSession.getMapper(TVAppApiMapper.class);
		reqMap.put("userKey",Integer.parseInt(userKey));
		reqMap.put("prodId", prodId);
		//reqMap.put("deviceId", deviceId);
		
		try {
			
			prodListVo productDetail = null;
			
			if(reCommendType.equals("TVSTB")){  // 우리집 추천상품
				reqMap.put("recommendId", deviceId);
				productDetail = mapper.prodListByRecommDetail(reqMap);
			}
			else if(reCommendType.equals("TVPOP")){ // 지니쇼핑 베스트		
				reqMap.put("recommendId", "ALL");
				long time = System.currentTimeMillis();
				SimpleDateFormat dayTime = new SimpleDateFormat("HH");
				String timeStr = dayTime.format(new Date(time));
				reqMap.put("searchTime", timeStr);
				productDetail = mapper.prodListByRecommDetail(reqMap);
				reqMap.remove("searchTime");
			}	
			else if(reCommendType.equals("TVUSR")){ // 나에게 딱
				reqMap.put("recommendId", reqMap.get("userKey").toString());
				productDetail = mapper.prodListByRecommDetail(reqMap);
			}	
			else if(reCommendType.equals("TVHIGH")){ // 자주 구매 상품
				productDetail = mapper.prodListByRecommHighDetail(reqMap);
			}
			else if(reCommendType.equals("TVLOW")){
				productDetail = mapper.prodListByRecommLowDetail(reqMap);
			}
			else if(reCommendType.equals("search") || reCommendType.equals("TREND_IMG") || reCommendType.equals("COORDI")){
				productDetail = mapper.prodListBySearchTextDetail(reqMap);
			}
			
			
			mav.addObject("reCommendType",reCommendType);
			mav.addObject("reCommendVer",reCommendVer);
			mav.addObject("deviceId",deviceId);
			mav.addObject("productDetail",productDetail);
			mav.addObject("reCommendType", reCommendType);
			
		} catch (Exception e) {
			log.error("/web/recommendDetailAjax.do error");
		}
		return mav;
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
	
	public String logInsertMakeStr(String reCommendType) throws Exception {
		String logStr = "";
		if(reCommendType.equals("TVSTB"))logStr = "우리집 추천상품";
		else if(reCommendType.equals("TVPOP")) logStr = "지니쇼핑 베스트";	
		else if(reCommendType.equals("TVUSR")) logStr = "나에게 딱";	
		else if(reCommendType.equals("TVHIGH")) logStr = "자주 구매 상품";
		else if(reCommendType.equals("TREND")) logStr = "트렌드 상품추천";
		return logStr; 
	}	
	
}//endClass
