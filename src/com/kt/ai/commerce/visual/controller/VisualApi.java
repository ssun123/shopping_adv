package com.kt.ai.commerce.visual.controller;

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
import com.kt.ai.commerce.log.mapper.LogMapper;
import com.kt.ai.commerce.login.controller.LoginApi;
import com.kt.ai.commerce.tvApp.vo.prodListVo;
import com.kt.ai.commerce.visual.mapper.VisualApiMapper;
import com.kt.ai.commerce.visual.vo.VisualVo;

@Controller
public class VisualApi {

	static Logger log = Logger.getLogger(VisualApi.class);
	
	@Autowired
    private SqlSession sqlSession;
	
	// 비쥬얼 쇼핑 카테고리 - 비쥬얼 쇼핑을 할 수 있는 카테고리 목록을 요청
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/api/visualShoppingCategoryList.api",method={RequestMethod.POST,RequestMethod.GET})
	public void visualShoppingCategoryList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// requst
		Map reqMap = LoginApi.getRequestMap(request);
		
		// log
		LogMapper logMapper = sqlSession.getMapper(LogMapper.class);
		HashMap logMap = new HashMap();	
		logMap.put("userKey", Integer.parseInt(reqMap.get("userKey").toString()));
		logMap.put("deviceType",reqMap.get("deviceType"));
		logMap.put("nowPage","비쥬얼 쇼핑 카테고리 선택"); 
		logMap.put("beforPage","메인"); 
		logMap.put("reqMsg", request.getRequestURI()+"###"+request.getQueryString());
		logMapper.logInsert(logMap);
		
		// get
		VisualApiMapper mapper = sqlSession.getMapper(VisualApiMapper.class);
		reqMap.put("userKey",Integer.parseInt(reqMap.get("userKey").toString()));
		reqMap.put("depthNo",0);
		reqMap.put("parentsVisualId","000");
		
		List visualList = mapper.selectVisualList(reqMap);
		
		JsonObject resultObj =  new JsonObject();
		try {

			resultObj.addProperty("resCode", "0");
			resultObj.addProperty("resMsg", "카테고리 리스트입니다.");
			resultObj.addProperty("nextStep", "1");

			JsonArray visualListJson = new JsonArray();
			for(int i = 0; i < visualList.size() ; i++) {
			
				VisualVo vVo = (VisualVo)visualList.get(i);
				
				JsonObject visualObj = new JsonObject();
				visualObj.addProperty("categoryId", vVo.getVisualId());
				visualObj.addProperty("categoryImgUrl", vVo.getVisualImgUrl());

				visualListJson.add(visualObj);
			}
			resultObj.add("visualCategoryList", visualListJson);
			
		} catch (Exception e) {
			resultObj.addProperty("resCode", "1");
			resultObj.addProperty("resMsg", "시스템 오류입니다. 관리자에게 문의하세요");
			log.error("/api/visualShoppingCategoryList.api error");
		} finally {
			// result
			response.setContentType("application/json;charset=utf-8");
			Gson gson = new Gson();
			PrintWriter printWriter = response.getWriter();
			printWriter.print(gson.toJson(resultObj));
			printWriter.close();
		}
	}

	// 비쥬얼 쇼핑 STEP - 비쥬얼 쇼핑 단계 진행시
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/api/visualShoppingStep.api",method={RequestMethod.POST,RequestMethod.GET})
	public void visualShoppingStep(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// requst
		Map reqMap = LoginApi.getRequestMap(request);
		
		// log
		LogMapper logMapper = sqlSession.getMapper(LogMapper.class);
		HashMap logMap = new HashMap();	
		logMap.put("userKey", Integer.parseInt(reqMap.get("userKey").toString()));
		logMap.put("deviceType",reqMap.get("deviceType"));
		logMap.put("nowPage","비쥬얼 쇼핑 "+reqMap.get("step").toString()); 
		logMap.put("beforPage","비쥬얼 쇼핑 " + String.valueOf(Integer.parseInt(reqMap.get("step").toString())-1) ); 
		logMap.put("reqMsg", request.getRequestURI()+"###"+request.getQueryString());
		logMapper.logInsert(logMap);
		
		// get
		VisualApiMapper mapper = sqlSession.getMapper(VisualApiMapper.class);
		reqMap.put("userKey",Integer.parseInt(reqMap.get("userKey").toString()));
		reqMap.put("depthNo",Integer.parseInt(reqMap.get("step").toString()));
		reqMap.put("parentsVisualId",reqMap.get("imgId").toString());
		
		List visualList = mapper.selectVisualList(reqMap);
		JsonObject resultObj =  new JsonObject();
		try {
			resultObj.addProperty("resCode", "0");
			resultObj.addProperty("resMsg", "카테고리 이미지 리스트입니다.");
			
			int visualMax = mapper.visualMax(reqMap);
			
			if(Integer.parseInt(reqMap.get("step").toString())+1>visualMax) {
				resultObj.addProperty("nextStep", "E");
			} else {
				resultObj.addProperty("nextStep", Integer.parseInt(reqMap.get("step").toString())+1);
			}
			JsonArray visualListJson = new JsonArray();
			for(int i = 0; i < visualList.size() ; i++) {
			
				VisualVo vVo = (VisualVo)visualList.get(i);
				
				JsonObject visualObj = new JsonObject();
				visualObj.addProperty("imgId", vVo.getVisualId());
				visualObj.addProperty("imgUrl", vVo.getVisualImgUrl());

				visualListJson.add(visualObj);
			}
			resultObj.add("imgList", visualListJson);
		} catch (Exception e) {
			resultObj.addProperty("resCode", "1");
			resultObj.addProperty("resMsg", "시스템 오류입니다. 관리자에게 문의하세요");
			log.error("/api/visualShoppingStep.api error");
		} finally {
			// result
			response.setContentType("application/json;charset=utf-8");
			Gson gson = new Gson();
			PrintWriter printWriter = response.getWriter();
			printWriter.print(gson.toJson(resultObj));
			printWriter.close();
		}
	}
	
	
	// 비쥬얼 쇼핑 추천 - 비쥬얼 쇼핑 결과 추천 상품 목록
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/api/visualShoppingResult.api",method={RequestMethod.POST,RequestMethod.GET})
	public void visualShoppingResult(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// requst
		Map reqMap = LoginApi.getRequestMap(request);
		
		// log
		LogMapper logMapper = sqlSession.getMapper(LogMapper.class);
		HashMap logMap = new HashMap();	
		logMap.put("userKey", Integer.parseInt(reqMap.get("userKey").toString()));
		logMap.put("deviceType",reqMap.get("deviceType"));
		logMap.put("nowPage","비쥬얼 쇼핑 결과"); 
		logMap.put("beforPage","비쥬얼 쇼핑 "); 
		logMap.put("reqMsg", request.getRequestURI()+"###"+request.getQueryString());
		logMapper.logInsert(logMap);

		// get
		VisualApiMapper mapper = sqlSession.getMapper(VisualApiMapper.class);
		reqMap.put("userKey",Integer.parseInt(reqMap.get("userKey").toString()));
		
		List visualResultList = mapper.selectVisualResultList(reqMap);
		
		JsonObject resultObj =  new JsonObject();
		try {
			resultObj.addProperty("resCode", "0");
			resultObj.addProperty("resMsg", "비쥬얼쇼핑 결과입니다.");

			JsonArray visualResultListJson = new JsonArray();
			for(int i = 0; i < visualResultList.size() ; i++) {
			
				prodListVo pVo = (prodListVo)visualResultList.get(i);
				
				JsonObject visualObj = new JsonObject();
				visualObj.addProperty("productId", pVo.getProductId());
				visualObj.addProperty("imgUrl", pVo.getImgUrl());
				visualObj.addProperty("productName", pVo.getProductName());
				visualObj.addProperty("productCost", pVo.getProductCost());
				visualObj.addProperty("detailUrl", pVo.getDetailUrl());
				visualObj.addProperty("favoriteYn", pVo.getFavoriteYn());

				visualResultListJson.add(visualObj);
			}
			resultObj.add("productList", visualResultListJson);
		} catch (Exception e) {
			resultObj.addProperty("resCode", "1");
			resultObj.addProperty("resMsg", "시스템 오류입니다. 관리자에게 문의하세요");
			log.error("/api/visualShoppingResult.api error");
		} finally {
			// result
			response.setContentType("application/json;charset=utf-8");
			Gson gson = new Gson();
			PrintWriter printWriter = response.getWriter();
			printWriter.print(gson.toJson(resultObj));
			printWriter.close();
		}
	}
	
	
	
	
	
}//endClass
