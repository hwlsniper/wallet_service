package it.etoken.component.market.facade.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;

import it.etoken.base.common.exception.MLException;
import it.etoken.base.common.result.MLResult;
import it.etoken.base.common.result.MLResultList;
import it.etoken.base.common.result.MLResultObject;
import it.etoken.base.model.market.entity.Coins;
import it.etoken.component.market.service.CoinsService;
import it.etoken.componet.coins.facade.CoinsFacadeAPI;

@Service(version = "1.0.0")
public class CoinsFacadeAPIImpl implements CoinsFacadeAPI {

	private final static Logger logger = LoggerFactory.getLogger(CoinsFacadeAPIImpl.class);

	@Autowired
	CoinsService coinsService;

	@Override
	public MLResultList<Coins> findAll(int page) {
		try {
			Page<Coins> result = coinsService.findAll();
			return new MLResultList<Coins>(result.getResult());
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultList<Coins>(e);
		}
	}

	@Override
	public MLResult saveUpdate(Coins coins) {
		try {
			coinsService.saveUpdate(coins);
			return new MLResult(true);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResult(e);
		}
	}

	@Override
	public MLResult delete(Long id) {
		try {
			coinsService.delete(id);
			return new MLResult(true);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResult(e);
		}
	}
	
	@Override
	public MLResultList<Coins> findAllByPage(int page, String code) {
		try {
			Page<Coins> result = coinsService.findAllByPage(page, code);
			return new MLResultList<Coins>(result.getResult());
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultList<Coins>(e);
		}
	}
	
	@Override
	public MLResultObject<Coins> findByName(String name) {
		try {
			Coins coins = coinsService.findByName(name);
			return new MLResultObject<Coins>(coins);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultObject<Coins>(e);
		}
	}

	@Override
	public MLResultList<Coins> findAllCoins() {
		try {
			List<Coins> result = coinsService.findAllCoins();
			return new MLResultList<Coins>(result);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultList<Coins>(e);
		}
	}
}
