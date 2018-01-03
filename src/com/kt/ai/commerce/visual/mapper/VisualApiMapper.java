package com.kt.ai.commerce.visual.mapper;

import java.util.List;
import java.util.Map;

import com.kt.ai.commerce.ai.vo.AiVo;
import com.kt.ai.commerce.tvApp.vo.prodListVo;
import com.kt.ai.commerce.visual.vo.VisualVo;

public interface VisualApiMapper {

	public List<VisualVo> selectVisualList(Map reqMap);

	public List<prodListVo> selectVisualResultList(Map reqMap);
	
	int visualMax(Map reqMap);
}
