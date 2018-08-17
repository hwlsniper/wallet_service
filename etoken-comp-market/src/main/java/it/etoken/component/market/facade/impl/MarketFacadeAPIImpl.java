package it.etoken.component.market.facade.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

import it.etoken.base.common.exception.MLException;
import it.etoken.base.common.result.MLResultList;
import it.etoken.base.common.result.MLResultObject;
import it.etoken.base.model.market.entity.Coins;
import it.etoken.base.model.market.vo.CoinTicker;
import it.etoken.component.market.service.CoinsService;
import it.etoken.component.market.service.MarketService;
import it.etoken.componet.market.facede.MarketFacadeAPI;

@Service(version="1.0.0")
public class MarketFacadeAPIImpl implements MarketFacadeAPI {

	private final static Logger logger = LoggerFactory.getLogger(MarketFacadeAPIImpl.class);

	@Autowired
	MarketService marketService;
	
	@Autowired
	CoinsService coinsService;

	@Override
	public MLResultList<CoinTicker> getTicker() {
		try{
			return new MLResultList<CoinTicker>(marketService.getTicker());
		}catch (MLException e) {
			logger.error(e.toString());
			return new MLResultList<CoinTicker>(e);
		}
	}

	@Override
	public MLResultObject<Coins> saveUpdate(Coins coins) {
		try{
			return new MLResultObject<Coins>(coinsService.saveUpdate(coins));
		}catch (MLException e) {
			logger.error(e.toString());
			return new MLResultObject<Coins>(e);
		}
	}

	@Override
	public MLResultObject<Map> getLine(String coins,String type) {
		try{
			return new MLResultObject<Map>(marketService.getLine(coins, type));
		}catch (MLException e) {
			logger.error(e.toString());
			return new MLResultObject<Map>(e);
		}
	}

	@Override
	public MLResultObject<Coins> findById(Long id) {
		try{
			return new MLResultObject<Coins>(coinsService.findById(id));
		}catch (MLException e) {
			logger.error(e.toString());
			return new MLResultObject<Coins>(e);
		}
	}
}
