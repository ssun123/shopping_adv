package com.kt.ai.commerce.favorite.controller;

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

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kt.ai.commerce.favorite.mapper.FavoriteApiMapper;
import com.kt.ai.commerce.favorite.vo.FavoriteVo;
import com.kt.ai.commerce.log.mapper.LogMapper;
import com.kt.ai.commerce.login.controller.LoginApi;

@Controller
public class FavoriteApi {

	static Logger log = Logger.getLogger(FavoriteApi.class);
	
	@Autowired
    private SqlSession sqlSession;
	
	@RequestMapping("/api/favoriteList.api")
	public void favoriteList(HttpServletRequest request, HttpServletResponse response) throws IOException {

		Map reqMap = LoginApi.getRequestMap(request);
		
		LogMapper logMapper = sqlSession.getMapper(LogMapper.class);
		HashMap logMap = new HashMap();	
		logMap.put("userKey", Integer.parseInt(reqMap.get("userKey").toString()));
		logMap.put("deviceType",reqMap.get("deviceType"));
		logMap.put("nowPage","myFavoriteList");
		logMap.put("beforPage","main");
		logMap.put("reqMsg", request.getRequestURI()+"###"+request.getQueryString());
		
		logMapper.logInsert(logMap);
		
		FavoriteApiMapper mapper = sqlSession.getMapper(FavoriteApiMapper.class);
		
		reqMap.put("userKey",Integer.parseInt(reqMap.get("userKey").toString()));
		List favoriteList = mapper.favoriteList(reqMap);
		
		JsonObject resultObj =  new JsonObject();
		
		resultObj.addProperty("resCode", "0");
		resultObj.addProperty("resMsg", "찜 목록입니다.");
		
		JsonArray favoriteListJson = new JsonArray();

		for(int i = 0; i < favoriteList.size() ; i++) {
			FavoriteVo fVo = (FavoriteVo)favoriteList.get(i) ;
			
			JsonObject favoriteObj = new JsonObject();
			favoriteObj.addProperty("productId", fVo.getProductId());
			favoriteObj.addProperty("shopName", fVo.getShopName());
			favoriteObj.addProperty("imgUrl", fVo.getImgUrl());
			favoriteObj.addProperty("productName", fVo.getProductName());
			favoriteObj.addProperty("productCost", fVo.getProductCost());
			favoriteObj.addProperty("favoriteYn", fVo.getFavoriteYn());
			favoriteObj.addProperty("detailUrl", fVo.getDetailUrl());
			
			favoriteListJson.add(favoriteObj);
		}
		
		resultObj.add("favoriteList", favoriteListJson);

		response.setContentType("application/json;charset=utf-8");
		Gson gson = new Gson();

		PrintWriter printWriter = response.getWriter();
		printWriter.print(gson.toJson(resultObj));
		printWriter.close();

	}
	
	@RequestMapping("/api/favorite.api")
	public void favorite(HttpServletRequest request, HttpServletResponse response) throws IOException {

		Map reqMap = LoginApi.getRequestMap(request);
		
		LogMapper logMapper = sqlSession.getMapper(LogMapper.class);
		HashMap logMap = new HashMap();	
		logMap.put("userKey", Integer.parseInt(reqMap.get("userKey").toString()));
		logMap.put("deviceType",reqMap.get("deviceType"));
		logMap.put("nowPage","");
		logMap.put("beforPage","");
		logMap.put("reqMsg", request.getRequestURI()+"###"+request.getQueryString());
		logMapper.logInsert(logMap);

		FavoriteApiMapper mapper = sqlSession.getMapper(FavoriteApiMapper.class);

		HashMap<String, String> resultMap = new HashMap<String, String>();
		reqMap.put("userKey", Integer.parseInt(reqMap.get("userKey").toString()));
		if(reqMap.get("favoriteYn").equals("Y")) {
			// 찜하기 추가
			int res = mapper.favoriteInsert(reqMap);
			if(res==0) {
				resultMap.put("resCode", "1");
				resultMap.put("resMsg", "시스템 오류입니다. 관리자에게 문의하세요");				
			} else {
				resultMap.put("resCode", "0");
				resultMap.put("resMsg", "저장 되었습니다.");
			}
		} else {
			// 찜하기 제거
			int res = mapper.favoriteDelete(reqMap);
			if(res==0) {
				resultMap.put("resCode", "1");
				resultMap.put("resMsg", "시스템 오류입니다. 관리자에게 문의하세요");				
			} else {
				resultMap.put("resCode", "0");
				resultMap.put("resMsg", "삭제 되었습니다.");
			}
		}


		response.setContentType("application/json;charset=utf-8");
		Gson gson = new Gson();

		PrintWriter printWriter = response.getWriter();
		printWriter.print(gson.toJson(resultMap));
		printWriter.close();

	}
	
}
