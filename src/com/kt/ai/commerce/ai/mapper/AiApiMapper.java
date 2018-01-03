package com.kt.ai.commerce.ai.mapper;

import java.util.List;
import java.util.Map;

import com.kt.ai.commerce.ai.vo.AiExVo;
import com.kt.ai.commerce.ai.vo.AiVo;
import com.kt.ai.commerce.ai.vo.DicVo;
import com.kt.ai.commerce.ai.vo.SessionVo;
import com.kt.ai.commerce.ai.vo.UserAiQaVo;
import com.kt.ai.commerce.tvApp.vo.prodListVo;

public interface AiApiMapper {
	
	void historyDelete_uaq(Map reqMap);
	
	void historyDelete_uac(Map reqMap);

	List<AiVo> hisList(Map reqMap);

	List<SessionVo> sessionList(Map reqMap);

	List<prodListVo> productListByHist(Map reqMap);

	List<prodListVo> prodListByOrder(Map reqMap);

	int sessionMax(Map reqMap);

	void userAiQaInsert(Map reqMap);

	UserAiQaVo aiMstSelect(Map reqMap);
	
	void serchResultInsert(Map reqMap);
	void serchResultInsertKeyWord(Map reqMap);
	
	void userRecommendInsert(Map reqMap);
	void talkRecommendInsert(Map reqMap);
	void bestRecommendInsert(Map reqMap);
	
	List<AiExVo> aiExList(Map reqMap);
	
	void findDic(Map reqMap);
	
	int findDicCount(Map reqMap);
	
	List<DicVo> searchDicList(Map reqMap);
	
	void userViewContentInsert(Map reqMap);
}
