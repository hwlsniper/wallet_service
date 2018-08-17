package it.etoken.component.eosblock.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import it.etoken.base.common.exception.MLException;
import it.etoken.base.common.result.MLResultList;
import it.etoken.base.model.eosblock.entity.RamTradeLog;
import it.etoken.component.eosblock.mongo.model.RamLargeRank;

public interface RamLargeRankService {
	
	public List<RamLargeRank> getNewestRank();
	
	//获取内存大户排行并且放到mongodb中
	public void getLargeRank();
}
