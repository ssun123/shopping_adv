package com.kt.ai.commerce.buy.controller;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
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
import com.kt.ai.commerce.buy.mapper.BuyApiMapper;
import com.kt.ai.commerce.buy.vo.BuyVo;
import com.kt.ai.commerce.log.mapper.LogMapper;
import com.kt.ai.commerce.login.controller.LoginApi;

@Controller
public class BuyApi {

	static Logger log = Logger.getLogger(BuyApi.class);
	
	@Autowired
    private SqlSession sqlSession;
	
	// 구매목록
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/api/buyList.api",method={RequestMethod.POST,RequestMethod.GET},produces="application/json; charset=utf8")
	public void buyList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// requst
		Map reqMap = LoginApi.getRequestMap(request);
		
		// log 
		logInsert(request,reqMap,"구매목록",""); 
		
		// get
		BuyApiMapper mapper = sqlSession.getMapper(BuyApiMapper.class);
		reqMap.put("userKey",Integer.parseInt(reqMap.get("userKey").toString()));
		JsonObject resultObj =  new JsonObject();
		try {
			List<BuyVo> buyList = mapper.buyList(reqMap);
			resultObj.addProperty("resCode", "0");
			resultObj.addProperty("resMsg", "구매목록입니다.");
			
			// set
			JsonArray buyListJson = new JsonArray();
			for(int i = 0; i < buyList.size() ; i++) {
				BuyVo bVo = (BuyVo) buyList.get(i) ;
				JsonObject buyObj = new JsonObject();
				buyObj.addProperty("buyNo", bVo.getBuyNo());
				buyObj.addProperty("productId", bVo.getProductId());
				buyObj.addProperty("imgUrl", bVo.getImgUrl());
				buyObj.addProperty("productName", bVo.getProductName());
				buyObj.addProperty("productCost", bVo.getProductCost());
				buyObj.addProperty("favoriteYn", bVo.getFavoriteYn());
				buyObj.addProperty("detailUrl", bVo.getDetailUrl());
				buyObj.addProperty("deliveryState", bVo.getDeliveryState());
				buyListJson.add(buyObj);
			}
			resultObj.add("buyList", buyListJson);
			
		} catch (Exception e) {
			resultObj.addProperty("resCode", "1");
			resultObj.addProperty("resMsg", "시스템 오류입니다. 관리자에게 문의하세요");
			log.error("/api/buyList.api error");
		} finally {
			// result
			response.setContentType("application/json;charset=utf-8");
			Gson gson = new Gson();
			PrintWriter printWriter = response.getWriter();
			printWriter.print(gson.toJson(resultObj));
			printWriter.close();
		}
	}
	
	// 상품구매하기 선택시
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/api/buy.api",method={RequestMethod.POST,RequestMethod.GET},produces="application/json; charset=utf8")
	public void buy(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// requst
		Map reqMap = LoginApi.getRequestMap(request);
		
		// log
		logInsert(request,reqMap,"구매하기","상품목록"); 
		
		// get
		HashMap<String, String> resultMap = new HashMap<String, String>();
		BuyApiMapper mapper = sqlSession.getMapper(BuyApiMapper.class);
		
		reqMap.put("userKey",Integer.parseInt(reqMap.get("userKey").toString()));
		String userKey = (String) reqMap.get("userKey").toString();
		
		long time = System.currentTimeMillis();
		SimpleDateFormat dayTime = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String timeStr = dayTime.format(new Date(time));
		
		String buyNo = userKey+"_"+timeStr; // 구매번호 생성
		reqMap.put("buyNo", buyNo);
		//System.out.println("buyNo : " + buyNo);
		try {
			mapper.buy(reqMap);
			resultMap.put("resCode", "0");
			resultMap.put("resMsg", "구매 완료되었습니다.");
		} catch (Exception e) {
			resultMap.put("resCode", "1");
			resultMap.put("resMsg", "시스템 오류입니다. 관리자에게 문의하세요");
			log.error("/api/buy.api error");
		} finally {
			// result
			response.setContentType("application/json;charset=utf-8");
			Gson gson = new Gson();
			PrintWriter printWriter = response.getWriter();
			printWriter.print(gson.toJson(resultMap));
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

	
}//endClass
