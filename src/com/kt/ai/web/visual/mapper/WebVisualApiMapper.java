package com.kt.ai.web.visual.mapper;

import java.util.List;
import java.util.Map;

import com.kt.ai.commerce.tvApp.vo.prodListVo;
import com.kt.ai.web.visual.vo.WebVisualVo;

public interface WebVisualApiMapper {

	public List<WebVisualVo> selectVisualList(Map reqMap);

	public List<prodListVo> selectVisualResultList(Map reqMap);
	
	int visualMax(Map reqMap);

	public prodListVo prodListByRecommDetail(Map reqMap);
}
