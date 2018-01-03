package com.kt.ai.commerce.memo.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
import com.kt.ai.commerce.memo.mapper.MemoApiMapper;
import com.kt.ai.commerce.memo.vo.MemoVo;
import com.kt.ai.commerce.pantry.mapper.PantryApiMapper;
import com.kt.ai.commerce.pantry.vo.PantryVo;
import com.kt.ai.commerce.tvApp.mapper.TVAppApiMapper;
import com.kt.ai.commerce.tvApp.vo.prodListVo;

@Controller
public class MemoApi {

	static Logger log = Logger.getLogger(MemoApi.class);
	
	@Autowired
    private SqlSession sqlSession;
	
	//메모 설정(등록/삭제)
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/api/memoSet.api")
	public void memoSet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		Map reqMap = LoginApi.getRequestMap(request);
		
		LogMapper logMapper = sqlSession.getMapper(LogMapper.class);
		HashMap logMap = new HashMap();	
		logMap.put("userKey", Integer.parseInt(reqMap.get("userKey").toString()));
		logMap.put("deviceType",reqMap.get("deviceType"));
		logMap.put("nowPage","메모 입력/삭제");
		logMap.put("beforPage","");
		logMap.put("reqMsg", request.getRequestURI()+"###"+request.getQueryString());
		logMapper.logInsert(logMap);

		MemoApiMapper mapper = sqlSession.getMapper(MemoApiMapper.class);
		
		HashMap<String, String> resultMap = new HashMap<String, String>();
		reqMap.put("userKey", Integer.parseInt(reqMap.get("userKey").toString()));
	//	reqMap.put("memo", reqMap.get("brand")+reqMap.get("category"));

		//reqMap.put("prodId", Integer.parseInt(reqMap.get("prodId").toString()));
		if(reqMap.get("setYn").equals("Y")) {
			// 메모 등록
			int res = mapper.memoInsert(reqMap);
			if(res==0) {
				resultMap.put("resCode", "1");
				resultMap.put("resMsg", "시스템 오류입니다. 관리자에게 문의하세요");				
			} else {
				resultMap.put("resCode", "0");
				resultMap.put("resMsg", "성공");
			}
		} else if(reqMap.get("setYn").equals("N")) {
			// 메모 삭제
			int res = mapper.memoDelete(reqMap);
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
	//쇼핑리스트 메모 조회
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/api/memoList.api")
	public void memoList(HttpServletRequest request, HttpServletResponse response) throws IOException {

		Map reqMap = LoginApi.getRequestMap(request);
		
		LogMapper logMapper = sqlSession.getMapper(LogMapper.class);
		HashMap logMap = new HashMap();	
		logMap.put("userKey", Integer.parseInt(reqMap.get("userKey").toString()));
		logMap.put("deviceType",reqMap.get("deviceType"));
		logMap.put("nowPage","myMemo");
		logMap.put("beforPage","");
		logMap.put("reqMsg", request.getRequestURI()+"###"+request.getQueryString());
		
		logMapper.logInsert(logMap);
		
		MemoApiMapper mapper = sqlSession.getMapper(MemoApiMapper.class);
		
		reqMap.put("userKey",Integer.parseInt(reqMap.get("userKey").toString()));
		
		List memoList = mapper.memoList(reqMap);
		JsonObject resultObj =  new JsonObject();
		
		resultObj.addProperty("resCode", "0");
		resultObj.addProperty("resMsg", "성공");
		///////////////18.01.03 paging 처리 메모 6개씩 조회
		int pageNum = Integer.parseInt(reqMap.get("pageNum").toString());
		int memoSize = memoList.size();
		int page = (memoSize/6)+1;
		int pageData = 6;
		resultObj.addProperty("resPage", page);
		JsonArray memoListJson = new JsonArray();
		//6개씩
		if(memoSize < (pageNum*pageData)) {
			for(int i=(pageNum-1)*pageData; i < memoSize ; i++) {
				MemoVo pVo = (MemoVo)memoList.get(i) ;
				
				JsonObject memoObj = new JsonObject(); //
				memoObj.addProperty("memo", pVo.getMemo());
				memoObj.addProperty("category", pVo.getCategory());
				memoObj.addProperty("brand", pVo.getBrand());

				memoListJson.add(memoObj);
			}
		}
		else {
			for(int i=(pageNum-1)*pageData; i < pageNum*pageData ; i++) {
				MemoVo pVo = (MemoVo)memoList.get(i) ;
				
				JsonObject memoObj = new JsonObject(); //
				memoObj.addProperty("memo", pVo.getMemo());
				memoObj.addProperty("category", pVo.getCategory());
				memoObj.addProperty("brand", pVo.getBrand());

				memoListJson.add(memoObj);
			}			
		}
		
		
	/*	for(int i = 0; i < memoList.size() ; i++) {
			MemoVo pVo = (MemoVo)memoList.get(i) ;
			
			JsonObject memoObj = new JsonObject(); //
			memoObj.addProperty("memo", pVo.getMemo());
			memoObj.addProperty("category", pVo.getCategory());
			memoObj.addProperty("brand", pVo.getBrand());

			memoListJson.add(memoObj);
		}
		*/
		resultObj.add("memoList", memoListJson);

		response.setContentType("application/json;charset=utf-8");
		Gson gson = new Gson();

		PrintWriter printWriter = response.getWriter();
		printWriter.print(gson.toJson(resultObj));
		printWriter.close();

	}
	//쇼핑리스트 상품검색
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/api/memoSearch.api",method={RequestMethod.POST,RequestMethod.GET},produces="application/json; charset=utf8")
	public void memoSearch(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// request
		Map reqMap = LoginApi.getRequestMap(request);
		

		// get
		MemoApiMapper mapper = sqlSession.getMapper(MemoApiMapper.class);
		reqMap.put("userKey",Integer.parseInt(reqMap.get("userKey").toString()));
		//String orderType = reqMap.get("orderType").toString();
		JsonObject resultObj =  new JsonObject();

		try {
			
	//		List searchCategory = new ArrayList();
	//		List searchBrand = new ArrayList();
			//String searchWord = "";
						
			System.out.println("########### prodListBySearch : " + reqMap);

			List<prodListVo> productList = mapper.memoProdSearch(reqMap);
			System.out.println("########### productList : " + productList.size());
			
			JsonArray productListJson = new JsonArray();
			for(int i = 0; i < productList .size() ; i++) {
				prodListVo pVo = (prodListVo) productList.get(i) ;
				JsonObject pObj = new JsonObject();
				pObj.addProperty("productId", pVo.getProductId());
				pObj.addProperty("imgUrl", pVo.getImgUrl());
				pObj.addProperty("productUrl", pVo.getProductUrl());
				pObj.addProperty("productName", pVo.getProductName());
				pObj.addProperty("productCost", pVo.getProductCost());
				pObj.addProperty("partnerName", pVo.getPartnerName());
				pObj.addProperty("brand", pVo.getBrand());
				
				productListJson.add(pObj);
			}					
			resultObj.addProperty("resCode", "0");
			resultObj.addProperty("resMsg", "성공");
			resultObj.add("productList", productListJson);
				
		} catch (Exception e) {
			resultObj.addProperty("resCode", "1");
			resultObj.addProperty("resMsg", "시스템 오류입니다. 관리자에게 문의하세요");
			log.error("/api/memoSearch.api error");
		} finally {
			// result
			response.setContentType("application/json;charset=utf-8");
			Gson gson = new Gson();
			PrintWriter printWriter = response.getWriter();
			printWriter.print(gson.toJson(resultObj));
			printWriter.close();
		}
	}
	
	//단골상품 설정(등록/삭제)
		@SuppressWarnings({ "rawtypes", "unchecked" })
		@RequestMapping("/api/memoProdSet.api")
		public void memoProdSet(HttpServletRequest request, HttpServletResponse response) throws IOException {

			Map reqMap = LoginApi.getRequestMap(request);
			
			LogMapper logMapper = sqlSession.getMapper(LogMapper.class);
			HashMap logMap = new HashMap();	
			logMap.put("userKey", Integer.parseInt(reqMap.get("userKey").toString()));
			logMap.put("deviceType",reqMap.get("deviceType"));
			logMap.put("nowPage","단골상품 입력/삭제");
			logMap.put("beforPage","");
			logMap.put("reqMsg", request.getRequestURI()+"###"+request.getQueryString());
			logMapper.logInsert(logMap);

			MemoApiMapper mapper = sqlSession.getMapper(MemoApiMapper.class);
			
			HashMap<String, String> resultMap = new HashMap<String, String>();
			reqMap.put("userKey", Integer.parseInt(reqMap.get("userKey").toString()));
		//	reqMap.put("memo", reqMap.get("brand")+reqMap.get("category"));

			//reqMap.put("prodId", Integer.parseInt(reqMap.get("prodId").toString()));
			if(reqMap.get("setYn").equals("Y")) {
				// 메모 등록
				int res = mapper.memoProdInsert(reqMap);
				if(res==0) {
					resultMap.put("resCode", "1");
					resultMap.put("resMsg", "시스템 오류입니다. 관리자에게 문의하세요");				
				} else {
					resultMap.put("resCode", "0");
					resultMap.put("resMsg", "성공");
				}
			} else if(reqMap.get("setYn").equals("N")) {
				// 메모 삭제
				int res = mapper.memoProdDelete(reqMap);
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
		
		//단골상품 조회
		@SuppressWarnings({ "rawtypes", "unchecked" })
		@RequestMapping(value="/api/memoProdList.api",method={RequestMethod.POST,RequestMethod.GET},produces="application/json; charset=utf8")
		public void memoProdList(HttpServletRequest request, HttpServletResponse response) throws Exception {
			
			// request
			Map reqMap = LoginApi.getRequestMap(request);
			

			// get
			MemoApiMapper mapper = sqlSession.getMapper(MemoApiMapper.class);
			reqMap.put("userKey",Integer.parseInt(reqMap.get("userKey").toString()));
			//String orderType = reqMap.get("orderType").toString();
			JsonObject resultObj =  new JsonObject();

			try {
				
		//		List searchCategory = new ArrayList();
		//		List searchBrand = new ArrayList();
				//String searchWord = "";							

				List<prodListVo> productList = mapper.memoProdList(reqMap);
				
				JsonArray productListJson = new JsonArray();
				for(int i = 0; i < productList .size() ; i++) {
					prodListVo pVo = (prodListVo) productList.get(i) ;
					JsonObject pObj = new JsonObject();
					pObj.addProperty("memo", pVo.getMemo());
					pObj.addProperty("productId", pVo.getProductId());
					pObj.addProperty("imgUrl", pVo.getImgUrl());
					pObj.addProperty("productName", pVo.getProductName());
					pObj.addProperty("productCost", pVo.getProductCost());
					pObj.addProperty("partnerName", pVo.getPartnerName());
									
					productListJson.add(pObj);
				}					
				resultObj.addProperty("resCode", "0");
				resultObj.addProperty("resMsg", "성공");
				resultObj.add("productList", productListJson);
					
			} catch (Exception e) {
				resultObj.addProperty("resCode", "1");
				resultObj.addProperty("resMsg", "시스템 오류입니다. 관리자에게 문의하세요");
				log.error("/api/memoProdList.api error");
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
