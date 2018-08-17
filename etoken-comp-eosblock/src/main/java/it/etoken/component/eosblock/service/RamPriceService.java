package it.etoken.component.eosblock.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;

import it.etoken.base.common.exception.MLException;
import it.etoken.base.model.eosblock.entity.RamTradeLog;

public interface RamPriceService {
	/**
	 * 获取内存信息和价格
	 * @return
	 */
	public JSONObject getRammarketInfoAndPrice();
	
	/**
	 * 获取内存基本信息
	 * @return
	 * @throws MLException
	 */
	public JSONObject getRamInfo() throws MLException;
	
	/**
	 * 构建趋势线数据
	 * @throws MLException
	 */
	public void buildLineData() throws MLException;
	
	/**
	 * 获取内存最新交易
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws MLException
	 */
	public List<RamTradeLog> getNewTradeOrders() throws MLException;
	
	/**
	 * 获取大单交易记录
	 * @param count
	 * @return
	 * @throws MLException
	 */
	public  List<RamTradeLog> getBigTradeOrders() throws MLException;
	
	/**
	 * 根据账号查看最新内存交易记录
	 * @param accountName
	 * @return
	 * @throws MLException
	 */
	public List<RamTradeLog> getNewTradeOrdersByAccountName(String accountName, int page, int pageSize) throws MLException;
	
	/**
	 * 计算24小时内资金分布
	 * @return
	 */
	public Map<String, Object> calculateAmountStatistics();
	
	/**
	 * 查询账号明细
	 * @param accountName
	 * @param pageSize
	 * @param last_id
	 * @return
	 */
	public List<RamTradeLog> getNewTradeOrdersByAccountNameNew(String accountName, int pageSize, String last_id);
	
	/**
	 * 获取一分内交易量
	 * @return
	 */
	public JSONObject getTradingVolumeByTimes(BigDecimal price);
	
	/**
	 * 根据时间获取保存k线数据
	 * @param dateType
	 * @return
	 * @throws MLException
	 */
	public BasicDBObject saveKLineData(String dateType) throws MLException;
	
	/**
	 * 获取k线数据
	 * @param dateType 
	 * @param start_date
	 * @param end_date
	 * @return
	 */
	public List<JSONObject> getRamKLines(String dateType, int pageSize);
	
	/**
	 * 获取内存时分线数据
	 * @param dateType 
	 * @param start_date
	 * @param end_date
	 * @return
	 */
	public List<BasicDBObject> getRamHourLines(long start_date, long end_date);
	
	
	/**
	 * 根据code和开始时间，获取开始时间到现在的k线信息
	 * @param code
	 * @param startDateTimes
	 * @return
	 */
	public JSONObject getLatestInfo(Long startDateTimes);
	
}
