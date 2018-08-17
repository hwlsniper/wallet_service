package it.etoken.component.eosblock.facede.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import it.etoken.base.common.exception.MLException;
import it.etoken.base.common.result.MLResultList;
import it.etoken.base.common.result.MLResultObject;
import it.etoken.base.model.eosblock.entity.ETTradeLog;
import it.etoken.component.eosblock.service.ETExchangePriceService;
import it.etoken.componet.eosblock.facade.ETExchangePriceFacadeAPI;

@Service(version = "1.0.0")
public class ETExchangePriceFacadeAPIImpl implements ETExchangePriceFacadeAPI {

	private final static Logger logger = LoggerFactory.getLogger(ETExchangePriceFacadeAPIImpl.class);

	@Autowired
	ETExchangePriceService eTExchangePriceService;

	public MLResultList<ETTradeLog> getNewTradeOrdersByCode(String code) {
		try {
			List<ETTradeLog> result = eTExchangePriceService.getNewTradeOrdersByCode(code);

			return new MLResultList<ETTradeLog>(result);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultList<ETTradeLog>(e);
		}
	}

	public MLResultList<ETTradeLog> getBigTradeOrdersByCode(String code) {
		try {
			List<ETTradeLog> result = eTExchangePriceService.getBigTradeOrdersByCode(code);

			return new MLResultList<ETTradeLog>(result);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultList<ETTradeLog>(e);
		}
	}

	@Override
	public MLResultList<ETTradeLog> getNewTradeOrdersByCodeAndAccountName(String code, String accountName, int pageSize,
			String last_id) {
		try {
			List<ETTradeLog> result = eTExchangePriceService.getNewTradeOrdersByCodeAndAccountName(code, accountName,
					pageSize, last_id);

			return new MLResultList<ETTradeLog>(result);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultList<ETTradeLog>(e);
		}
	}
	
	@Override
	public MLResultObject<JSONArray> getTimeLines(String code, int count) {
		try {
			JSONArray result = eTExchangePriceService.getTimeLines(code, count);

			return new MLResultObject<JSONArray>(result);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultObject<JSONArray>(e);
		}
	}
	
	@Override
	public MLResultList<JSONObject> getKLines(String code, String dateType, int count) {
		try {
			List<JSONObject> result = eTExchangePriceService.getKLines(code, dateType, count);

			return new MLResultList<JSONObject>(result);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultList<JSONObject>(e);
		}
	}
}
