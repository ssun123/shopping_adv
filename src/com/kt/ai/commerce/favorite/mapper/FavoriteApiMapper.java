package com.kt.ai.commerce.favorite.mapper;

import java.util.List;
import java.util.Map;

import com.kt.ai.commerce.favorite.vo.FavoriteVo;

public interface FavoriteApiMapper {

	public List<FavoriteVo> favoriteList(Map reqMap);

	public int favoriteDelete(Map reqMap);

	public int favoriteInsert(Map reqMap);

}
