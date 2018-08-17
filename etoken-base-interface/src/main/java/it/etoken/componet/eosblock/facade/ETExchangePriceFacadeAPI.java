package it.etoken.componet.eosblock.facade;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import it.etoken.base.common.result.MLResultList;
import it.etoken.base.common.result.MLResultObject;
import it.etoken.base.model.eosblock.entity.ETTradeLog;

public interface ETExchangePriceFacadeAPI {

	public MLResultList<ETTradeLog> getNewTradeOrdersByCode(String code);
	
	public MLResultList<ETTradeLog> getBigTradeOrdersByCode(String code);
	
	public MLResultList<ETTradeLog> getNewTradeOrdersByCodeAndAccountName(String code, String accountName, int pageSize, String last_id);
	
	public MLResultObject<JSONArray> getTimeLines(String code, int count);
	
	public MLResultList<JSONObject> getKLines(String code, String dateType, int count);
}
