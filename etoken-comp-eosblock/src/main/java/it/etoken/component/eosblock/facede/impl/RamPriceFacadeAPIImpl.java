package it.etoken.component.eosblock.facede.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;

import it.etoken.base.common.exception.MLException;
import it.etoken.base.common.result.MLResultList;
import it.etoken.base.model.eosblock.entity.RamTradeLog;
import it.etoken.component.eosblock.service.RamPriceService;
import it.etoken.componet.eosblock.facade.RamPriceFacadeAPI;
@Service(version = "1.0.0")
public class RamPriceFacadeAPIImpl implements RamPriceFacadeAPI{
	
	private final static Logger logger = LoggerFactory.getLogger(RamPriceFacadeAPIImpl.class);
	
	@Autowired
	RamPriceService ramPriceService;
	
	@Override
	public MLResultList<RamTradeLog> getNewTradeOrders() {
		try {
			List<RamTradeLog> result= ramPriceService.getNewTradeOrders();
			return new MLResultList<RamTradeLog>(result);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultList<RamTradeLog>(e);
		}
	}
	
	@Override
	public MLResultList<RamTradeLog> getBigTradeOrders() {
		try {
			List<RamTradeLog> result= ramPriceService.getBigTradeOrders();
			return new MLResultList<RamTradeLog>(result);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultList<RamTradeLog>(e);
		}
	}
	
	@Override
	public MLResultList<RamTradeLog> getNewTradeOrdersByAccountName(String accountName, int page, int pageSize) {
		try {
			List<RamTradeLog> result= ramPriceService.getNewTradeOrdersByAccountName(accountName, page, pageSize);
			return new MLResultList<RamTradeLog>(result);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultList<RamTradeLog>(e);
		}
	}
	
	@Override
	public MLResultList<RamTradeLog> getNewTradeOrdersByAccountNameNew(String accountName, int pageSize, String last_id) {
		try {
			List<RamTradeLog> result= ramPriceService.getNewTradeOrdersByAccountNameNew(accountName, pageSize, last_id);
			return new MLResultList<RamTradeLog>(result);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultList<RamTradeLog>(e);
		}
	}

	@Override
	public MLResultList<JSONObject> getRamKLines(String dateType, int pageSize) {
		try {
			List<JSONObject> result= ramPriceService.getRamKLines(dateType, pageSize);
			return new MLResultList<JSONObject>(result);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultList<JSONObject>(e);
		}
	}

	@Override
	public MLResultList<BasicDBObject> getRamHourLines(long start_date, long end_date) {
		try {
			List<BasicDBObject> result= ramPriceService.getRamHourLines(start_date, end_date);
			return new MLResultList<BasicDBObject>(result);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultList<BasicDBObject>(e);
		}
	}
}
