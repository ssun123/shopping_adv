package com.kt.ai.commerce.login.mapper;

import java.util.Map;

import com.kt.ai.commerce.login.vo.LoginVo;

public interface LoginApiMapper {

	public LoginVo login(Map reqMap);
	
	public int deviceUpdatePush(Map reqMap);
	public int deviceInsert(Map reqMap);
	
	public int userCheck(Map reqMap);
	
	public int userInsert(Map reqMap);

	public int selectUserKey(Map reqMap);
	
}
