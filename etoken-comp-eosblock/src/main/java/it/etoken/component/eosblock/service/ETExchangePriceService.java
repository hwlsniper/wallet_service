package it.etoken.component.eosblock.service;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;

import it.etoken.base.common.exception.MLException;
import it.etoken.base.model.eosblock.entity.ETTradeLog;

public interface ETExchangePriceService {
	/**
	 * 获取所有交易对信息和价格信息和价格
	 * @return
	 */
	public JSONArray getEtExchangeMarketInfoAndPrice();
	
	/**
	 * 获取所有价格并保存
	 * @return
	 * @throws MLException
	 */
	public JSONArray getEtPrices() throws MLException;
	
	/**
	 * 获取最新交易订单
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws MLException
	 */
	public List<ETTradeLog> getNewTradeOrdersByCode(String code) throws MLException;
	
	/**
	 * 获取大单交易记录
	 * @param count
	 * @return
	 * @throws MLException
	 */
	public List<ETTradeLog> getBigTradeOrdersByCode(String code) throws MLException;
	
//	/**
//	 * 计算24小时内资金分布
//	 * @return
//	 */
//	public Map<String, Object> calculateAmountStatistics();
	
	/**
	 * 查询账号明细
	 * @param accountName
	 * @param pageSize
	 * @param last_id
	 * @return
	 */
	public List<ETTradeLog> getNewTradeOrdersByCodeAndAccountName(String code, String accountName, int pageSize, String last_id);
	
	/**
	 * 获取一分内交易量
	 * @return
	 */
	JSONObject getTradingVolumeByCode(String code, BigDecimal price);
	
	/**
	 * 构建分时图
	 * @throws MLException
	 */
	public void buildLineData() throws MLException;
	
	/**
	 * 根据时间获取保存k线数据
	 * @param dateType
	 * @return
	 * @throws MLException
	 */
	public BasicDBObject saveKLineData(String code, String dateType) throws MLException;
	
	/**
	 * 获取k线数据
	 * @param dateType 
	 * @param start_date
	 * @param end_date
	 * @return
	 */
	public List<JSONObject> getKLines(String code, String dateType, int count);
	
	/**
	 * 获取时分数据
	 * @param code
	 * @param count
	 * @return
	 */
	public JSONArray getTimeLines(String code, int count);
	
	/**
	 * 根据code和开始时间，获取开始时间到现在的k线信息
	 * @param code
	 * @param startDateTimes
	 * @return
	 */
	public JSONObject getLatestInfo(String code, Long startDateTimes);
	
	/**
	 * 根据code查询当天K信息
	 * @param code
	 * @return
	 */
	public JSONObject getTodayKInfo(String code);
}
