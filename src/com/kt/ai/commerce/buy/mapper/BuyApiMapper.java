package com.kt.ai.commerce.buy.mapper;

import java.util.List;
import java.util.Map;

import com.kt.ai.commerce.buy.vo.BuyVo;

public interface BuyApiMapper {

	public List<BuyVo> buyList(Map reqMap);

	public void buy(Map reqMap);


}
