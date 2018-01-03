package com.kt.ai.commerce.login.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import org.apache.ibatis.session.SqlSession;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kt.ai.commerce.log.mapper.LogMapper;
import com.kt.ai.commerce.login.mapper.LoginApiMapper;
import com.kt.ai.commerce.login.vo.LoginVo;

@Controller

public class LoginApi {

	static Logger log = Logger.getLogger(LoginApi.class);
	
	@Autowired
    private SqlSession sqlSession;
	
	@RequestMapping("/api/login.api")
	public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {

		Map reqMap = getRequestMap(request);
		
		LogMapper logMapper = sqlSession.getMapper(LogMapper.class);
		HashMap logMap = new HashMap();	
		logMap.put("userKey", 0); 
		logMap.put("deviceType",reqMap.get("deviceType"));
		logMap.put("nowPage","login");
		logMap.put("beforPage","");
		logMap.put("reqMsg", request.getRequestURI()+"###"+request.getQueryString());
		
		logMapper.logInsert(logMap); 
		
		
		LoginApiMapper mapper = sqlSession.getMapper(LoginApiMapper.class);
		
		LoginVo loginCheck = mapper.login(reqMap);

		HashMap<String, String> resultMap = new HashMap<String, String>();
		
		if(loginCheck==null) {
			resultMap.put("resCode", "1");
			resultMap.put("resMsg", "이메일 또는 비밀번호를 확인해 주세요!");
			
		} else {
			resultMap.put("resCode", "0");
			resultMap.put("resMsg", "로그인 되었습니다.");
			resultMap.put("userKey", loginCheck.getUserKey());
			resultMap.put("userGender", loginCheck.getUserGender());
			
			// 단말정보 확인
			
			reqMap.put("userKey", Integer.parseInt(loginCheck.getUserKey()));
			
			int device = mapper.deviceUpdatePush(reqMap);
			
			if(device < 1) {
				device = mapper.deviceInsert(reqMap);
			}
			
		}
		response.setContentType("application/json;charset=utf-8");
		Gson gson = new Gson();
		PrintWriter printWriter = response.getWriter();
		printWriter.print(gson.toJson(resultMap));
		printWriter.close();
	}	

	@RequestMapping("/api/userReg.api")
	public void userReg(HttpServletRequest request, HttpServletResponse response) throws IOException {

		Map reqMap = getRequestMap(request);

		LogMapper logMapper = sqlSession.getMapper(LogMapper.class);
		HashMap logMap = new HashMap();	
		logMap.put("userKey", 0); 
		logMap.put("deviceType",reqMap.get("deviceType"));
		logMap.put("nowPage","userReg");
		logMap.put("beforPage","login");
		logMap.put("reqMsg", request.getRequestURI()+"###"+request.getQueryString());
		
		logMapper.logInsert(logMap);

		LoginApiMapper mapper = sqlSession.getMapper(LoginApiMapper.class);
		
		int userCheck = mapper.userCheck(reqMap);

		HashMap<String, String> resultMap = new HashMap<String, String>();

		if(userCheck>0) {
			resultMap.put("resCode", "1");
			resultMap.put("resMsg", "이미 사용중인 아이디입니다.");
			
		} else {
			resultMap.put("resCode", "0");
			resultMap.put("resMsg", "회원가입 되셨습니다.");
			
			// 사용자 저장
			int userInsert = mapper.userInsert(reqMap);
			int userKey = mapper.selectUserKey(reqMap);
			resultMap.put("userKey", Integer.toString(userKey));
			
			if(userInsert < 1) {
				resultMap.put("resCode", "1");
				resultMap.put("resMsg", "관리자에게 문의해 주세요.");
			} else {
				// 단말정보 확인
				reqMap.put("userKey", userKey);
				
				int device = mapper.deviceUpdatePush(reqMap);
				if(device < 1) {
					System.out.println("userKey : " + userKey);
					device = mapper.deviceInsert(reqMap);
				}
			}
		}
		response.setContentType("application/json;charset=utf-8");
		Gson gson = new Gson();
		PrintWriter printWriter = response.getWriter();
		printWriter.print(gson.toJson(resultMap));
		printWriter.close();

	}	

	public static Map getRequestMap(HttpServletRequest request) {
        HashMap map = new HashMap();
        try {
        	Map parameter = request.getParameterMap();
	        if (parameter == null) return null;
	        		        
	        Iterator it = parameter.keySet().iterator();
	        Object paramKey = null;
	        String[] paramValue = null;
	        
	        while (it.hasNext()) { 
	            paramKey = it.next();
	            paramValue = (String[]) parameter.get(paramKey);	
	            String strKey = paramKey.toString();
	            if (paramValue.length > 1 ) {
	                map.put(strKey,paramValue);
	                log.debug("reqMap:key=["+strKey+"],value=["+paramValue+"]");
	            } else {
	                map.put(strKey, (paramValue[0] == null) ? "" : paramValue[0].trim());
		            log.debug("reqMap:key=["+strKey+"],value=["+paramValue[0].trim()+"]");
	            }
	        }    
	        return map; 
	        
        } catch (Exception e){
            log.error("getRequestMap() >> " + e.toString());
            return null;
        }
    }
	
}
