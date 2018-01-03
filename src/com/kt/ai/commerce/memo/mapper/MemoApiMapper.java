package com.kt.ai.commerce.memo.mapper;

import java.util.List;
import java.util.Map;

import com.kt.ai.commerce.memo.vo.MemoVo;
//import com.kt.ai.commerce.ai.vo.AiVo;
import com.kt.ai.commerce.pantry.vo.PantryVo;
import com.kt.ai.commerce.tvApp.vo.prodListVo;

public interface MemoApiMapper {

	public int memoInsert(Map reqMap);
	public int memoDelete(Map reqMap);
	
	public List<MemoVo> memoList(Map reqMap);
	public List<prodListVo> memoProdSearch(Map reqMap);
	
	public int memoProdInsert(Map reqMap);
	public int memoProdDelete(Map reqMap);
	public List<prodListVo> memoProdList(Map reqMap);

}
