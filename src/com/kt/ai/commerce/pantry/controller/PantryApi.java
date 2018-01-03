package com.kt.ai.commerce.pantry.controller;

import java.io.IOException;
import java.io.PrintWriter;
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
import com.kt.ai.commerce.favorite.mapper.FavoriteApiMapper;
import com.kt.ai.commerce.favorite.vo.FavoriteVo;
import com.kt.ai.commerce.log.mapper.LogMapper;
import com.kt.ai.commerce.login.controller.LoginApi;
import com.kt.ai.commerce.pantry.mapper.PantryApiMapper;
import com.kt.ai.commerce.pantry.vo.PantryVo;

@Controller
public class PantryApi {

	static Logger log = Logger.getLogger(PantryApi.class);
	
	@Autowired
    private SqlSession sqlSession;
	
	// 간편구매 카테고리 - 간편구매가 가능한 카테고리 목록을 요청(생수 등 10개)
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/api/selectPantryCategoryList.api",method={RequestMethod.POST,RequestMethod.GET})
	public void pantryCategoryList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// request
		Map reqMap = LoginApi.getRequestMap(request);
		
		// log
		LogMapper logMapper = sqlSession.getMapper(LogMapper.class);
		HashMap logMap = new HashMap();	
		logMap.put("userKey", Integer.parseInt(reqMap.get("userKey").toString()));
		logMap.put("deviceType",reqMap.get("deviceType"));
		logMap.put("nowPage","간편구매 카테고리 선택"); 
		///꼭 있어야 하나? logMap.put("beforPage","메인"); 
		logMap.put("reqMsg", request.getRequestURI()+"###"+request.getQueryString());
		logMapper.logInsert(logMap);
		
		// get
		PantryApiMapper mapper = sqlSession.getMapper(PantryApiMapper.class);
		reqMap.put("userKey",Integer.parseInt(reqMap.get("userKey").toString()));
		
		List pantryCateList = mapper.selectPantryCategoryList(reqMap);
		
		JsonObject resultObj =  new JsonObject();
		try {

			resultObj.addProperty("resCode", "0");
			resultObj.addProperty("resMsg", "성공");
			
			JsonArray pantryCateListJson = new JsonArray();
			for(int i = 0; i < pantryCateList.size() ; i++) {
			
				PantryVo pVo = (PantryVo)pantryCateList.get(i);
				
				JsonObject pantryCateObj = new JsonObject();
				pantryCateObj.addProperty("category", pVo.getCategory());
				pantryCateObj.addProperty("ImgUrl", pVo.getImgUrl());

				pantryCateListJson.add(pantryCateObj);
			}
			resultObj.add("pantryCateList", pantryCateListJson);
			
		} catch (Exception e) {
			resultObj.addProperty("resCode", "1");
			resultObj.addProperty("resMsg", "시스템 오류입니다. 관리자에게 문의하세요");
			log.error("/api/selectPantryCategoryList.api error");
		} finally {
			// result
			response.setContentType("application/json;charset=utf-8");
			Gson gson = new Gson();
			PrintWriter printWriter = response.getWriter();
			printWriter.print(gson.toJson(resultObj));
			printWriter.close();
		}
	}

	// 간편구매 카테고리 선택 후 상품목록(생수 선택 -> 생수 상품 선택)
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/api/selectPantryProdList.api",method={RequestMethod.POST,RequestMethod.GET})
	public void pantryProdList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// request
		Map reqMap = LoginApi.getRequestMap(request);
		
		// log
		LogMapper logMapper = sqlSession.getMapper(LogMapper.class);
		HashMap logMap = new HashMap();	
		logMap.put("userKey", Integer.parseInt(reqMap.get("userKey").toString()));
		logMap.put("deviceType",reqMap.get("deviceType"));
		logMap.put("nowPage","간편구매 "+reqMap.get("category")+" 상품 선택"); 
		logMap.put("beforPage","간편구매 카테고리 선택"); 
		logMap.put("reqMsg", request.getRequestURI()+"###"+request.getQueryString());
		logMapper.logInsert(logMap);
		
		// get
		PantryApiMapper mapper = sqlSession.getMapper(PantryApiMapper.class);
		reqMap.put("userKey",Integer.parseInt(reqMap.get("userKey").toString()));
		reqMap.put("category",reqMap.get("category"));
		
		List pantryProdList = mapper.selectPantryProdList(reqMap);
		JsonObject resultObj =  new JsonObject();
		try {
			resultObj.addProperty("resCode", "0");
			resultObj.addProperty("resMsg", "성공");
			
			JsonArray pantryProdListJson = new JsonArray();
			for(int i = 0; i < pantryProdList.size() ; i++) {
			
				PantryVo pVo = (PantryVo)pantryProdList.get(i);
				
				JsonObject pantryProdObj = new JsonObject();
				pantryProdObj.addProperty("prodId", pVo.getProdId());
				pantryProdObj.addProperty("prodName", pVo.getProdName());
				pantryProdObj.addProperty("prodPrice", pVo.getProdPrice());
				pantryProdObj.addProperty("imgUrl", pVo.getImgUrl());

				pantryProdListJson.add(pantryProdObj);
			}
			resultObj.add("pantryProdList", pantryProdListJson);
		} catch (Exception e) {
			resultObj.addProperty("resCode", "1");
			resultObj.addProperty("resMsg", "시스템 오류입니다. 관리자에게 문의하세요");
			log.error("/api/selectPantryProdList.api error");
		} finally {
			// result
			response.setContentType("application/json;charset=utf-8");
			Gson gson = new Gson();
			PrintWriter printWriter = response.getWriter();
			printWriter.print(gson.toJson(resultObj));
			printWriter.close();
		}
	}
	
	//간편구매 상품 설정(등록/삭제)
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/api/pantryProdSet.api")
	public void pantryProdSet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		Map reqMap = LoginApi.getRequestMap(request);
		
		LogMapper logMapper = sqlSession.getMapper(LogMapper.class);
		HashMap logMap = new HashMap();	
		logMap.put("userKey", Integer.parseInt(reqMap.get("userKey").toString()));
		logMap.put("deviceType",reqMap.get("deviceType"));
		logMap.put("nowPage","간편구매상품설정");
		logMap.put("beforPage","");
		logMap.put("reqMsg", request.getRequestURI()+"###"+request.getQueryString());
		logMapper.logInsert(logMap);

		PantryApiMapper mapper = sqlSession.getMapper(PantryApiMapper.class);
		
		HashMap<String, String> resultMap = new HashMap<String, String>();
		reqMap.put("userKey", Integer.parseInt(reqMap.get("userKey").toString()));
		//reqMap.put("prodId", Integer.parseInt(reqMap.get("prodId").toString()));
		if(reqMap.get("setYn").equals("Y")) {
			// 간편구매 등록
			int res = mapper.pantryProdInsert(reqMap);
			if(res==0) {
				resultMap.put("resCode", "1");
				resultMap.put("resMsg", "시스템 오류입니다. 관리자에게 문의하세요");				
			} else {
				resultMap.put("resCode", "0");
				resultMap.put("resMsg", "성공");
			}
		} else if(reqMap.get("setYn").equals("N")) {
			// 간편구매 삭제
			int res = mapper.pantryProdDelete(reqMap);
			if(res==0) {
				resultMap.put("resCode", "1");
				resultMap.put("resMsg", "시스템 오류입니다. 관리자에게 문의하세요");				
			} else {
				resultMap.put("resCode", "0");
				resultMap.put("resMsg", "성공");
			}
		}

		response.setContentType("application/json;charset=utf-8");
		Gson gson = new Gson();

		PrintWriter printWriter = response.getWriter();
		printWriter.print(gson.toJson(resultMap));
		printWriter.close();

	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/api/pantrySetList.api")
	public void pantrySetList(HttpServletRequest request, HttpServletResponse response) throws IOException {

		Map reqMap = LoginApi.getRequestMap(request);
		
		LogMapper logMapper = sqlSession.getMapper(LogMapper.class);
		HashMap logMap = new HashMap();	
		logMap.put("userKey", Integer.parseInt(reqMap.get("userKey").toString()));
		logMap.put("deviceType",reqMap.get("deviceType"));
		logMap.put("nowPage","myPantryList");
		logMap.put("beforPage","");
		logMap.put("reqMsg", request.getRequestURI()+"###"+request.getQueryString());
		
		logMapper.logInsert(logMap);
		
		PantryApiMapper mapper = sqlSession.getMapper(PantryApiMapper.class);
		
		reqMap.put("userKey",Integer.parseInt(reqMap.get("userKey").toString()));
		
		List pantrySetList = mapper.pantrySetList(reqMap);
		JsonObject resultObj =  new JsonObject();
		
		resultObj.addProperty("resCode", "0");
		resultObj.addProperty("resMsg", "성공");
		
		JsonArray pantrySetListJson = new JsonArray();

		for(int i = 0; i < pantrySetList.size() ; i++) {
			PantryVo pVo = (PantryVo)pantrySetList.get(i) ;
			
			JsonObject pantryProdObj = new JsonObject();
			pantryProdObj.addProperty("category", pVo.getCategory());
			pantryProdObj.addProperty("prodId", pVo.getProdId());
			pantryProdObj.addProperty("prodName", pVo.getProdName());
			pantryProdObj.addProperty("prodPrice", pVo.getProdPrice());
			pantryProdObj.addProperty("imgUrl", pVo.getImgUrl());
			
			pantrySetListJson.add(pantryProdObj);
		}
		
		resultObj.add("pantrySetList", pantrySetListJson);

		response.setContentType("application/json;charset=utf-8");
		Gson gson = new Gson();

		PrintWriter printWriter = response.getWriter();
		printWriter.print(gson.toJson(resultObj));
		printWriter.close();

	}
}//endClass
