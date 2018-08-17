package it.etoken.componet.eosblock.facade;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;

import it.etoken.base.common.result.MLResultList;
import it.etoken.base.model.eosblock.entity.RamTradeLog;

public interface RamPriceFacadeAPI {

	public MLResultList<RamTradeLog> getNewTradeOrders();
	
	public MLResultList<RamTradeLog> getBigTradeOrders();
	
	public MLResultList<RamTradeLog> getNewTradeOrdersByAccountName(String accountName, int page, int pageSize);
	
	public MLResultList<RamTradeLog> getNewTradeOrdersByAccountNameNew(String accountName, int pageSize, String last_id);
	
	public MLResultList<JSONObject> getRamKLines(String dateType, int pageSize);
	
	public MLResultList<BasicDBObject> getRamHourLines(long start_date, long end_date);
}
