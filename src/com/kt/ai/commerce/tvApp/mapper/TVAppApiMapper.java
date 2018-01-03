package com.kt.ai.commerce.tvApp.mapper;

import java.util.List;
import java.util.Map;

import com.kt.ai.commerce.tvApp.vo.PushVo;
import com.kt.ai.commerce.tvApp.vo.TVAppVo;
import com.kt.ai.commerce.tvApp.vo.prodListVo;

public interface TVAppApiMapper {

	public List<PushVo> selectPushKey(Map reqMap);

	public int pushHistoryInsert(PushVo pVo);

	public List<PushVo> selectPushKeyByDeviceId(Map reqMap);
	
	public String airProdId(Map reqMap);

	public List<prodListVo> prodListBySearch(Map reqMap);

	public List<TVAppVo> recommInfo(Map reqMap);

	public List<prodListVo> prodListByRecomm(Map reqMap);
	public List<prodListVo> prodListByRecommHigh(Map reqMap);
	public List<prodListVo> prodListByRecommLow(Map reqMap);
	
	public String selectTvId(Map reqMap);

	public prodListVo prodListByRecommDetail(Map reqMap);

	public prodListVo prodListByRecommHighDetail(Map reqMap);
	
	public List<prodListVo> liveContent(Map reqMap);
	
	public List<prodListVo> prodSearchCeleb(Map reqMap);

	public List<prodListVo> pantryContent(Map reqMap);
	
	public String recommInfoOne(Map reqMap);

	public prodListVo prodListByRecommLowDetail(Map reqMap);

	public List<prodListVo> prodListBySearchText(Map reqMap);

	public prodListVo prodListBySearchTextDetail(Map reqMap);

	public List<prodListVo> imgListByRecommTrend(Map reqMap);

	public List<prodListVo> prodListByRecommTrend(Map reqMap);


}
