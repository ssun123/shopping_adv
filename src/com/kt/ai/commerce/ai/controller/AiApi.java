package com.kt.ai.commerce.ai.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
import com.kt.ai.commerce.ai.vo.AiExVo;
import com.kt.ai.commerce.ai.vo.AiVo;
import com.kt.ai.commerce.ai.vo.DicVo;
import com.kt.ai.commerce.ai.vo.SessionVo;
import com.kt.ai.commerce.ai.vo.UserAiQaVo;
import com.kt.ai.commerce.common.utils.StringUtil;
import com.kt.ai.commerce.log.mapper.LogMapper;
import com.kt.ai.commerce.login.controller.LoginApi;
import com.kt.ai.commerce.tvApp.vo.prodListVo;

@Controller
public class AiApi {

	static Logger log = Logger.getLogger(AiApi.class);
	
	@Autowired
    private SqlSession sqlSession;
	
	// 대화내역요청 - 대화 쇼핑 진입시 호출하여 대화쇼핑 내역
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/api/aiShoppingHistory.api",method={RequestMethod.POST,RequestMethod.GET},produces="application/json; charset=utf8")
	public void aiShoppingHistory(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// requst
		Map reqMap = LoginApi.getRequestMap(request);
		
		// log
		logInsert(request, reqMap, "대화쇼핑", "메인");
		
		// get 
		AiApiMapper mapper = sqlSession.getMapper(AiApiMapper.class);
		reqMap.put("userKey",Integer.parseInt(reqMap.get("userKey").toString()));
		JsonObject resultObj =  new JsonObject();
		
		try {
			JsonArray historyListJson = new JsonArray();
			List<AiVo> hisList = mapper.hisList(reqMap);
			for(int i=0;i<hisList.size();i++){
				AiVo av = hisList.get(i);
				JsonObject addObj =  new JsonObject();
				addObj.addProperty("aiSessionNo", av.getAiSessionNo());
				addObj.addProperty("aiSessionType", av.getAiSessionType());
				reqMap.put("aiSessionNo",av.getAiSessionNo());
				List<SessionVo> sList = mapper.sessionList(reqMap);
				JsonArray sesstionListJson = new JsonArray();
				for(int j=0;j<sList.size();j++){
					SessionVo sv = sList.get(j);
					JsonObject addObj2 =  new JsonObject();
					addObj2.addProperty("qaType", sv.getQaType());
					addObj2.addProperty("answerMsg", sv.getAnswerMsg());
					reqMap.put("aiNo", Integer.parseInt(sv.getAiNo()));
					List<prodListVo> pList = mapper.productListByHist(reqMap);
					JsonArray productListJson = new JsonArray();
					for(int k=0;k<pList.size();k++){
						prodListVo pv = pList.get(k);
						JsonObject addObj3 =  new JsonObject();
						addObj3.addProperty("productId", pv.getProductId());
						addObj3.addProperty("imgUrl", pv.getImgUrl());
						addObj3.addProperty("productName", pv.getProductName());
						addObj3.addProperty("productCost", pv.getProductCost());
						addObj3.addProperty("detailUrl", pv.getDetailUrl());
						addObj3.addProperty("favoriteYn", pv.getFavoriteYn());
						productListJson.add(addObj3);
					}
					addObj2.add("productList", productListJson);
					sesstionListJson.add(addObj2);
				}
				addObj.add("sessionList", sesstionListJson);
				historyListJson.add(addObj);
			}
			
			// set
			resultObj.addProperty("resCode", "0");
			resultObj.addProperty("resMsg", "대화 내용입니다.");
			resultObj.add("historyList", historyListJson);
			
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
	
	// 대화 내역 삭제
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/api/aiHistoryDelete.api",method={RequestMethod.POST,RequestMethod.GET},produces="application/json; charset=utf8")
	public void aiHistoryDelete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// requst
		Map reqMap = LoginApi.getRequestMap(request);
		
		// log
		logInsert(request, reqMap, "대화내역삭제", "대화쇼핑");
		
		// get
		HashMap<String, String> resultMap = new HashMap<String, String>();
		AiApiMapper mapper = sqlSession.getMapper(AiApiMapper.class);
		reqMap.put("userKey",Integer.parseInt(reqMap.get("userKey").toString()));
		try {
			mapper.historyDelete_uaq(reqMap);
			mapper.historyDelete_uac(reqMap);
			resultMap.put("resCode", "0");
			resultMap.put("resMsg", "대화가 삭제 되었습니다.");
		} catch (Exception e) {
			resultMap.put("resCode", "1");
			resultMap.put("resMsg", "시스템 오류입니다. 관리자에게 문의하세요");
			log.error("/api/aiHistoryDelete.api error");
		} finally {
			// result
			response.setContentType("application/json;charset=utf-8");
			Gson gson = new Gson();
			PrintWriter printWriter = response.getWriter();
			printWriter.print(gson.toJson(resultMap));
			printWriter.close();
		}
	}

	
	// 대화 - 사용자와 대화시 호출 XXX
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/api/aiTalk.api",method={RequestMethod.POST,RequestMethod.GET},produces="application/json; charset=utf8")
	public void aiTalk(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// requst
		Map reqMap = LoginApi.getRequestMap(request);
		
		// log
		logInsert(request, reqMap, "대화쇼핑", "대화쇼핑");
		
		System.out.println("################################# Log Insert End");
		
		// get
		JsonObject resultMap =  new JsonObject();
		//HashMap<String, String> resultMap = new HashMap<String, String>();
		AiApiMapper mapper = sqlSession.getMapper(AiApiMapper.class);
		reqMap.put("userKey",Integer.parseInt(reqMap.get("userKey").toString()));
		try {
			
			System.out.println("################################# Start");
			String aiSessionNo = (String) reqMap.get("aiSessionNo");
			String aiNo = (String) reqMap.get("aiNo");
			String aiCategory = (String) reqMap.get("aiCategory");
			String answerCode = (String) reqMap.get("answerCode");
			String qaType = (String) reqMap.get("qaType");

			int sNo = 0;

			HashMap uaqVoReq = new HashMap();

			int checkUserQa = 0;
			
			// 사용자 응답 user_ai_qa 저장
			if(aiSessionNo.equals("")||aiSessionNo==null||(aiNo.equals("1")&&qaType.equals("U") )||(aiNo.equals("8")&&qaType.equals("U") )){
				// 처음 진입이므로 user_ai_qa에 저장하지 않음.
				System.out.println("################################# Start 1");
			} else {
				// 사용자 응답 user_ai_qa 저장
				
				//uaqVo.put("aiNo",Integer.parseInt((String)reqMap.get("aiNo")));
				uaqVoReq.put("aiNo",Integer.parseInt(reqMap.get("aiNo").toString()));
				uaqVoReq.put("aiSubNo",1);
				uaqVoReq.put("aiType","");
				uaqVoReq.put("answerCode",(String) reqMap.get("answerCode"));
				uaqVoReq.put("answerMsg",(String) reqMap.get("answerMsg"));
				uaqVoReq.put("answerValue","");
				uaqVoReq.put("qaType",(String) reqMap.get("qaType"));
				uaqVoReq.put("aiSessionNo",Integer.parseInt(reqMap.get("aiSessionNo").toString()));
				uaqVoReq.put("userKey",Integer.parseInt(reqMap.get("userKey").toString()));
				
				checkUserQa = 1;
				System.out.println("################################# Start 1-e");

				reqMap.put("aiSessionNo",Integer.parseInt(reqMap.get("aiSessionNo").toString()));
			}

			
			// 처음 시작시 
			if(aiSessionNo.equals("")||aiNo.equals("2")||aiSessionNo==null){
				sNo = mapper.sessionMax(reqMap); // max session no
				reqMap.put("aiSessionNo",sNo);
				System.out.println("################################# Start 2");
			} else {
				reqMap.put("aiSessionNo",Integer.parseInt(reqMap.get("aiSessionNo").toString()));
			}

			if(aiNo.equals("")||aiNo==null) {
				reqMap.put("aiNo",0);
				aiNo = "0";
				System.out.println("################################# Start 3");
			} else {
				reqMap.put("aiNo",Integer.parseInt(reqMap.get("aiNo").toString()));
			}

			if(aiCategory.equals("")||aiCategory==null) {
				reqMap.put("aiCategory","00000");	
				System.out.println("################################# Start 4");
			}

			if(aiNo.equals("0")) {
				// ai_sub_no 만들기 
				int subNo = 1 + (int)(Math.random()*3) ;
				reqMap.put("aiSubNo",subNo);	
				System.out.println("################################# Start 5");
				
			} else {
				reqMap.put("aiSubNo",1);	
				System.out.println("################################# Start 5-1");
			}
			
			// ai serch
			if(aiNo.equals("2")&&(answerCode.equals("")||answerCode==null)){
				//검색 처리
				reqMap.put("aiNo",11);  // 검색 결과 ai_mst 번호	

				//System.out.println("################################# Search 1");
				UserAiQaVo aimst = mapper.aiMstSelect(reqMap);
				resultMap.addProperty("resCode", "0");
				resultMap.addProperty("resMsg", "검색 결과입니다.");
				resultMap.addProperty("aiSessionNo", aimst.getUserAiSession());
				resultMap.addProperty("aiSessionType", aimst.getAiType());
				resultMap.addProperty("qaType", aimst.getQaType());
				resultMap.addProperty("aiNo", "2");
				resultMap.addProperty("aiCategory", aimst.getAiCategory());
				resultMap.addProperty("answerMsg", aimst.getAnswerMsg().replace("###",(String) reqMap.get("answerMsg")));
				resultMap.addProperty("nextTarget", aimst.getNextTarget());
				//System.out.println("################################# Search 2");

				reqMap.put("aiNo",Integer.parseInt(aimst.getAiNo()));
				
				HashMap uaqVo = new HashMap();
				uaqVo.put("aiNo",Integer.parseInt(aimst.getAiNo()));
				uaqVo.put("aiSubNo",Integer.parseInt(aimst.getAiSubNo()));
				uaqVo.put("aiType",aimst.getAiType());
				uaqVo.put("answerCode","");
				uaqVo.put("answerMsg",aimst.getAnswerMsg().replace("###",(String) reqMap.get("answerMsg")));
				uaqVo.put("answerValue","");
				uaqVo.put("qaType",aimst.getQaType());
				uaqVo.put("aiSessionNo",Integer.parseInt(aimst.getUserAiSession()));
				uaqVo.put("userKey",reqMap.get("userKey"));
				//System.out.println("################################# Search 3");
				
				mapper.userAiQaInsert(uaqVo);
				//System.out.println("################################# Search 4");

				//검색 찾기
				// 1. 검색어 분리(space로 분리함).
				System.out.println("################################# Search 5");				
				String[] searchWord = ((String) reqMap.get("answerMsg")).split(" ");

				System.out.println("################################# Search 6");
				System.out.println("################################# searchWord.length :" + searchWord.length);
				
				List searchWordList = new ArrayList();
				
				for(int sw = 0; sw< searchWord.length;sw++) {
					System.out.println("################################# searchWord :" + searchWord[sw] );
					searchWordList.add(searchWord[sw].toString());
				}
				
				reqMap.put("searchWord", searchWordList) ;
				List<DicVo> dicList = mapper.searchDicList(reqMap);
				//System.out.println("################################# Search 5");
				System.out.println("################################# dicList :" + dicList);
				System.out.println("################################# dicList :" + dicList.size());
				
				if(dicList.size() == 0) {
					
					mapper.serchResultInsertKeyWord(reqMap);
					//System.out.println("################################# Search 6");
					
				} else {
					
					List searchColor = new ArrayList();
					List searchStyle = new ArrayList();
					//List searchCategory = new ArrayList();
					//List searchGender = new ArrayList();
					String searchCategory = "";
					String searchGender = "";
	
					int checkCate = 0;
					int checkGender = 0;
					
					for(int s=0;s<dicList.size();s++){
						DicVo dv = dicList.get(s);
						
						if(dv.getDicType().equals("color")) {
							searchColor.add(dv.getDicResult().toString());
						} else if(dv.getDicType().equals("gender") && checkGender == 0) {
							//searchGender.add(dv.getDicResult());
							searchGender = dv.getDicResult() ;
							System.out.println("dicType : " +dv.getDicType() + "###:" +dv.getDicResult());
							checkGender = 1;
						} else if(dv.getDicType().equals("style")) {
							searchStyle.add(dv.getDicResult().toString());
						} else if(dv.getDicType().equals("category3") && checkCate == 0) {
							//searchCategory.add(dv.getDicResult());
							searchCategory = dv.getDicResult() ;
							System.out.println("dicType : " +dv.getDicType() + "###:" +dv.getDicResult());
							checkCate = 1 ;
						}
	
					}

					if(searchColor.size()> 0) {
						reqMap.put("searchColor",searchColor);
					}
					if(searchStyle.size()> 0) {
						reqMap.put("searchStyle",searchStyle);
						reqMap.put("searchStyleCount",searchStyle.size());
						
					}
					
					if(!searchCategory.equals("")) {
						reqMap.put("searchCategory",searchCategory);
					}
					if(!searchGender.equals("")) {
						reqMap.put("searchGender",searchGender);
					}
					//System.out.println("################################# Search 7");
									
					mapper.serchResultInsert(reqMap);
					//System.out.println("################################# Search 8");

				}
				
				List<prodListVo> pList = mapper.productListByHist(reqMap);
				JsonArray productListJson = new JsonArray();
				//System.out.println("################################# Search 9");
				
				if(pList != null) {

					for(int k=0;k<pList.size();k++){
						prodListVo pv = pList.get(k);
						JsonObject addObj3 =  new JsonObject();
						addObj3.addProperty("productId", pv.getProductId());
						addObj3.addProperty("imgUrl", pv.getImgUrl());
						addObj3.addProperty("productName", pv.getProductName());
						addObj3.addProperty("productCost", pv.getProductCost());
						addObj3.addProperty("detailUrl", pv.getDetailUrl());
						addObj3.addProperty("favoriteYn", pv.getFavoriteYn());
						productListJson.add(addObj3);
					}
					
					resultMap.add("productList", productListJson);
				} else {
					resultMap.add("productList", null);
				}
					
					
				JsonArray aiExListJson = new JsonArray();
				aiExListJson = null;
				resultMap.add("aiExList", aiExListJson);
				
			} else {
				
				// 사용자 응답 결과 
				int findDic = 1;
				System.out.println("################################# talk 1");
				
				if(aiNo.equals("4")||aiNo.equals("5")||aiNo.equals("6")||aiNo.equals("7")) {
					if(aiNo.equals("4")) {
						reqMap.put("dicType","category3");	
					}
					if(aiNo.equals("5")) {
						reqMap.put("dicType","gender");	
					}
					if(aiNo.equals("6")) {
						reqMap.put("dicType","color");	
					}
					if(aiNo.equals("7")) {
						reqMap.put("dicType","style");	
					}
					System.out.println("################################# talk 2");

					reqMap.put("aiTypeTk","TK");
					
					findDic = mapper.findDicCount(reqMap);
					
					if(findDic > 0){
						mapper.findDic(reqMap);
					}
					System.out.println("################################# talk 3 findDic :"+findDic);
				}
				
				if(findDic > 0 ) {
					//
					if(checkUserQa>0) {
						mapper.userAiQaInsert(uaqVoReq);
					}
					
					System.out.println("################################# aiNo :"+aiNo);
					
					if(aiNo.equals("9")) {
						// 정렬
						resultMap.addProperty("resCode", "0");
						resultMap.addProperty("resMsg", "대화 내용입니다.");
						resultMap.addProperty("aiSessionNo", aiSessionNo);
						resultMap.addProperty("aiSessionType", "TK");
						resultMap.addProperty("qaType", qaType);
						resultMap.addProperty("aiNo", "9");
						resultMap.addProperty("aiCategory", aiCategory);
						resultMap.addProperty("answerMsg", "");
						resultMap.addProperty("nextTarget", "US");
						
						String orderType = reqMap.get("answerCode").toString();
						
						reqMap.put("orderType",orderType);
						
						List<prodListVo> pList = mapper.prodListByOrder(reqMap);

						JsonArray productListJson = new JsonArray();
						
						if(pList != null) {
							for(int k=0;k<pList.size();k++){
								prodListVo pv = pList.get(k);
								JsonObject addObj3 =  new JsonObject();
								addObj3.addProperty("productId", pv.getProductId());
								addObj3.addProperty("imgUrl", pv.getImgUrl());
								addObj3.addProperty("productName", pv.getProductName());
								addObj3.addProperty("productCost", pv.getProductCost());
								addObj3.addProperty("detailUrl", pv.getDetailUrl());
								addObj3.addProperty("favoriteYn", pv.getFavoriteYn());
								productListJson.add(addObj3);
							}
						resultMap.add("productList", productListJson);
						} else {
							resultMap.add("productList", null);
						}

						resultMap.add("aiExList", null);
						
					} else {
					
						UserAiQaVo aimst = mapper.aiMstSelect(reqMap);
						resultMap.addProperty("resCode", "0");
						resultMap.addProperty("resMsg", "대화 내용입니다.");
						resultMap.addProperty("aiSessionNo", aimst.getUserAiSession());
						resultMap.addProperty("aiSessionType", aimst.getAiType());
						resultMap.addProperty("qaType", aimst.getQaType());
						resultMap.addProperty("aiNo", aimst.getAiNo());
						resultMap.addProperty("aiCategory", aimst.getAiCategory());
						resultMap.addProperty("answerMsg", aimst.getAnswerMsg().replace("###",(String) reqMap.get("answerMsg")));
						resultMap.addProperty("nextTarget", aimst.getNextTarget());
		
						reqMap.put("aiNo",Integer.parseInt(aimst.getAiNo()));	
		
						HashMap uaqVo = new HashMap();
						uaqVo.put("aiNo", Integer.parseInt(aimst.getAiNo()));
						
						uaqVo.put("aiSubNo",Integer.parseInt(aimst.getAiSubNo()));
						uaqVo.put("aiType",aimst.getAiType());
						uaqVo.put("answerCode","");
						uaqVo.put("answerMsg",aimst.getAnswerMsg().replace("###",(String) reqMap.get("answerMsg")));
						uaqVo.put("answerValue","");
						uaqVo.put("qaType",aimst.getQaType());
						uaqVo.put("aiSessionNo",Integer.parseInt(aimst.getUserAiSession()));
						uaqVo.put("userKey",reqMap.get("userKey"));
	
						mapper.userAiQaInsert(uaqVo);
						
						List<prodListVo> pList = null;
		
						//상품 찾기
						if(aimst.getAiType().equals("DF") && aimst.getProdYn().equals("Y") ) {
							//개인 맞춤 결과 상품 리스트
							//System.out.println("################################# talk p 1");
							
							reqMap.put("recommendId", reqMap.get("userKey").toString());
							mapper.userRecommendInsert(reqMap);
							//System.out.println("################################# talk p 1-2");
							pList = mapper.productListByHist(reqMap);
							//System.out.println("################################# talk p 1-3");
							
							if(pList.size() < 1) {
								reqMap.put("recommendId", "0");
								mapper.userRecommendInsert(reqMap);
								pList = mapper.productListByHist(reqMap);
							}
							
	
						} else if(aimst.getAiType().equals("TK") && aimst.getProdYn().equals("Y") ){
							// 대화 결과 상품 리스트
							//System.out.println("################################# talk p 2");
							mapper.talkRecommendInsert(reqMap);
							//System.out.println("################################# talk p 2-1");
							pList = mapper.productListByHist(reqMap);
							//System.out.println("################################# talk p 2-2");
	
						} else if(aimst.getAiType().equals("BS") && aimst.getProdYn().equals("Y") ){
							// 인기 결과 상품 리스트
							//System.out.println("################################# talk p 3");
							mapper.bestRecommendInsert(reqMap);
							//System.out.println("################################# talk p 3-1");
							pList = mapper.productListByHist(reqMap);
							//System.out.println("################################# talk p 3-2");
						} 
						
						JsonArray productListJson = new JsonArray();
						
						if(pList != null) {
							for(int k=0;k<pList.size();k++){
								prodListVo pv = pList.get(k);
								JsonObject addObj3 =  new JsonObject();
								addObj3.addProperty("productId", pv.getProductId());
								addObj3.addProperty("imgUrl", pv.getImgUrl());
								addObj3.addProperty("productName", pv.getProductName());
								addObj3.addProperty("productCost", pv.getProductCost());
								addObj3.addProperty("detailUrl", pv.getDetailUrl());
								addObj3.addProperty("favoriteYn", pv.getFavoriteYn());
								productListJson.add(addObj3);
							}
						resultMap.add("productList", productListJson);
						} else {
							resultMap.add("productList", null);
						}
						
						JsonArray aiExListJson = new JsonArray();
						List<AiExVo> aiExList = mapper.aiExList(reqMap);
						
						for(int ex=0;ex<aiExList.size();ex++) {
							AiExVo aev = aiExList.get(ex) ;
							JsonObject addObjEx = new JsonObject();
							addObjEx.addProperty("aiExCode", aev.getAiExCode());
							addObjEx.addProperty("aiExValue", aev.getAiExValue());
							addObjEx.addProperty("aiExImgUrl", aev.getAiExImgUrl());
							aiExListJson.add(addObjEx);
						}
						
						resultMap.add("aiExList", aiExListJson);
					}
				} else {
					
					resultMap.addProperty("resCode", "0");
					resultMap.addProperty("resMsg", "일치하는 것이 없습니다. 다시 입력해 주세요");
					resultMap.addProperty("aiSessionNo", aiSessionNo);
					resultMap.addProperty("aiSessionType", "TK");
					resultMap.addProperty("qaType", "A");
					resultMap.addProperty("aiNo", reqMap.get("aiNo").toString());
					resultMap.addProperty("aiCategory", reqMap.get("aiCategory").toString());
					resultMap.addProperty("answerMsg", "일치하는 것이 없습니다. 다시 입력해 주세요");
					resultMap.addProperty("nextTarget", "US");
					
				}
			}
			
			
		} catch (Exception e) {
			resultMap.addProperty("resCode", "1");
			resultMap.addProperty("resMsg", "시스템 오류입니다. 관리자에게 문의하세요");
			log.error("/api/aiTalk.api error");
		} finally {
			// result
			response.setContentType("application/json;charset=utf-8");
			Gson gson = new Gson();
			PrintWriter printWriter = response.getWriter();
			printWriter.print(gson.toJson(resultMap));
			printWriter.close();
		}
	}

	
	// 추천 결과 정렬 - 대화의 추천 결과를 정렬 XXX
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/api/productOrder.api",method={RequestMethod.POST,RequestMethod.GET},produces="application/json; charset=utf8")
	public void productOrder(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// requst
		Map reqMap = LoginApi.getRequestMap(request);
		
		// log
		LogMapper logMapper = sqlSession.getMapper(LogMapper.class);
		HashMap logMap = new HashMap();	
		logMap.put("userKey", Integer.parseInt(reqMap.get("userKey").toString()));
		logMap.put("deviceType",reqMap.get("deviceType"));
		logMap.put("nowPage","대화쇼핑"); 
		logMap.put("beforPage","대화쇼핑"); 
		logMap.put("reqMsg", request.getRequestURI()+"###"+request.getQueryString());
		logMapper.logInsert(logMap);
		
		// get
		JsonObject resultMap =  new JsonObject();
		//HashMap<String, String> resultMap = new HashMap<String, String>();
		AiApiMapper mapper = sqlSession.getMapper(AiApiMapper.class);
		reqMap.put("userKey",Integer.parseInt(reqMap.get("userKey").toString()));
		reqMap.put("aiSessionNo",Integer.parseInt(reqMap.get("aiSessionNo").toString()));
		try {
			// 정렬방식? st001?
			String orderType = reqMap.get("orderType").toString();
			
			List<prodListVo> pList = mapper.prodListByOrder(reqMap);

			JsonArray productListJson = new JsonArray();
			
			if(pList != null) {
				for(int k=0;k<pList.size();k++){
					prodListVo pv = pList.get(k);
					JsonObject addObj3 =  new JsonObject();
					addObj3.addProperty("productId", pv.getProductId());
					addObj3.addProperty("imgUrl", pv.getImgUrl());
					addObj3.addProperty("productName", pv.getProductName());
					addObj3.addProperty("productCost", pv.getProductCost());
					addObj3.addProperty("detailUrl", pv.getDetailUrl());
					addObj3.addProperty("favoriteYn", pv.getFavoriteYn());
					productListJson.add(addObj3);
				}
			resultMap.add("productList", productListJson);
			} else {
				resultMap.add("productList", null);
			}

			resultMap.addProperty("resCode", "0");
			resultMap.addProperty("resMsg", "상품이 리스트입니다.");
			
		} catch (Exception e) {
			resultMap.addProperty("resCode", "1");
			resultMap.addProperty("resMsg", "시스템 오류입니다. 관리자에게 문의하세요");
			log.error("/api/productOrder.api error");
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
