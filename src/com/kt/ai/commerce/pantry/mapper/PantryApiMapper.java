package com.kt.ai.commerce.pantry.mapper;

import java.util.List;
import java.util.Map;

//import com.kt.ai.commerce.ai.vo.AiVo;
import com.kt.ai.commerce.pantry.vo.PantryVo;

public interface PantryApiMapper {

	public List<PantryVo> selectPantryCategoryList(Map reqMap);

	public List<PantryVo> selectPantryProdList(Map reqMap);
	
	public int pantryProdInsert(Map reqMap);
	public int pantryProdDelete(Map reqMap);
	
	public List<PantryVo> pantrySetList(Map reqMap);
	
}
