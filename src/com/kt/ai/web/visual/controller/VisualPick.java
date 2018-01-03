package com.kt.ai.web.visual.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.plaf.synth.SynthSpinnerUI;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.kt.ai.commerce.log.mapper.LogMapper;
import com.kt.ai.commerce.login.controller.LoginApi;
import com.kt.ai.commerce.tvApp.vo.prodListVo;
import com.kt.ai.web.visual.mapper.WebVisualApiMapper;

@Controller
public class VisualPick {

	static Logger log = Logger.getLogger(VisualPick.class);
	
	@Autowired
    private SqlSession sqlSession;
	

		// 비쥬얼 쇼핑 STEP - 비쥬얼 쇼핑 단계 진행시
		@SuppressWarnings({ "rawtypes", "unchecked" })
		@RequestMapping(value="/web/visualShoppingStep.do",method={RequestMethod.POST,RequestMethod.GET})
		public ModelAndView webVisualShoppingStep(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
			
			ModelAndView mav = new ModelAndView("Web_Visual_Pick");
			int depthNo = 1;
			String parentsVisualId = "C01";
			
			// requst
			Map reqMap = LoginApi.getRequestMap(request);
			
			// log
			logInsert(request, reqMap, "비쥬얼 쇼핑", "메인");
			
			try {
				// get
				WebVisualApiMapper mapper = sqlSession.getMapper(WebVisualApiMapper.class);
				reqMap.put("userKey",Integer.parseInt(reqMap.get("userKey").toString()));
				if(reqMap.get("depthNo") != null && reqMap.get("depthNo").equals("2")){
					depthNo = 2;
				}
				reqMap.put("depthNo",depthNo);
				
				if(reqMap.get("selId") != null && !reqMap.get("selId").equals("")){
					parentsVisualId = reqMap.get("selId").toString();
					session.setAttribute("firstResultId", parentsVisualId);
				}
				
				reqMap.put("parentsVisualId",parentsVisualId);
				List visualList = mapper.selectVisualList(reqMap);
				
				mav.addObject("userKey",reqMap.get("userKey"));
				mav.addObject("deviceType",reqMap.get("deviceType"));
				mav.addObject("depthNo",depthNo);
				mav.addObject("visualList",visualList);
				
			} catch (Exception e) {
				
				log.error("/web/visualShoppingStep.do error");
			} 
			
			return mav;
		}
		
		
		// 비쥬얼 쇼핑 추천 - 비쥬얼 쇼핑 결과 추천 상품 목록
		@SuppressWarnings({ "rawtypes", "unchecked" })
		@RequestMapping(value="/web/visualShoppingResult.do",method={RequestMethod.POST,RequestMethod.GET})
		public ModelAndView webVisualShoppingResult(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
			
			ModelAndView mav = new ModelAndView("Web_Detail");
			
			// requst
			Map reqMap = LoginApi.getRequestMap(request);
			
			// log
			logInsert(request, reqMap, "비쥬얼 쇼핑 결과", "비쥬얼 쇼핑");
			
			try {
				// get
				prodListVo productDetail = null;
				
				WebVisualApiMapper mapper = sqlSession.getMapper(WebVisualApiMapper.class);
				reqMap.put("userKey",Integer.parseInt(reqMap.get("userKey").toString()));
				
				String imgIdStr =""; 
				String firstResultId =  session.getAttribute("firstResultId").toString();
				String selId = reqMap.get("selId").toString();
				imgIdStr = firstResultId+selId;
				reqMap.put("imgIdStr", imgIdStr);
				
				List<prodListVo> visualResultList = mapper.selectVisualResultList(reqMap);
				
				if(reqMap.get("prodId") != null && !reqMap.get("prodId").toString().equals("")){
					reqMap.put("prodId", reqMap.get("prodId"));	
				}else{
					reqMap.put("prodId", visualResultList.get(0).getProductId());
				}
				
				productDetail = mapper.prodListByRecommDetail(reqMap);
				
				mav.addObject("selId",selId);
				mav.addObject("deviceType",reqMap.get("deviceType"));
				mav.addObject("userKey",reqMap.get("userKey"));
				mav.addObject("productDetail", productDetail);
				mav.addObject("productList", new Gson().toJson(visualResultList));
				mav.addObject("searchStr","비주얼쇼핑");
				
			} catch (Exception e) {
				log.error("/api/visualShoppingResult.do error");
			}
			
			return mav;
		}
	
		// 비쥬얼 쇼핑 추천 - ajax
		@SuppressWarnings({ "rawtypes", "unchecked" })
		@RequestMapping(value="/web/visualShoppingResultAjax.do",method={RequestMethod.POST,RequestMethod.GET})
		public ModelAndView webVisualShoppingResultAjax(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
			
			ModelAndView mav = new ModelAndView("Web_Detail_Temp");
			
			// requst
			Map reqMap = LoginApi.getRequestMap(request);
			try {
				// get
				prodListVo productDetail = null;
				
				WebVisualApiMapper mapper = sqlSession.getMapper(WebVisualApiMapper.class);
				reqMap.put("userKey",Integer.parseInt(reqMap.get("userKey").toString()));
				
				String imgIdStr =""; 
				String firstResultId =  session.getAttribute("firstResultId").toString();
				String selId = reqMap.get("selId").toString();
				imgIdStr = firstResultId+selId;
				reqMap.put("imgIdStr", imgIdStr);
				
				productDetail = mapper.prodListByRecommDetail(reqMap);
				
				mav.addObject("selId",selId);
				mav.addObject("deviceType",reqMap.get("deviceType"));
				mav.addObject("userKey",reqMap.get("userKey"));
				mav.addObject("productDetail", productDetail);
				
			} catch (Exception e) {
				log.error("/api/visualShoppingResultAjax.do error");
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
	
	
}//endClass
