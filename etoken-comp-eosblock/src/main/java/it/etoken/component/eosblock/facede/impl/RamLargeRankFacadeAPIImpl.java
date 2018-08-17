package it.etoken.component.eosblock.facede.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

import it.etoken.base.common.exception.MLException;
import it.etoken.base.common.result.MLResultList;
import it.etoken.base.model.eosblock.entity.RamTradeLog;
import it.etoken.component.eosblock.mongo.model.RamLargeRank;
import it.etoken.component.eosblock.service.RamLargeRankService;
import it.etoken.component.eosblock.service.RamPriceService;
import it.etoken.componet.eosblock.facade.RamLargeRankFacadeAPI;
@Service(version = "1.0.0")
public class RamLargeRankFacadeAPIImpl implements RamLargeRankFacadeAPI{
	
	private final static Logger logger = LoggerFactory.getLogger(RamLargeRankFacadeAPIImpl.class);
	
	@Autowired
	RamLargeRankService ramLargeRankService;

	@Override
	public MLResultList<RamLargeRank> getNewestRank() {
		try {
			List<RamLargeRank> result= ramLargeRankService.getNewestRank();
			return new MLResultList<RamLargeRank>(result);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultList<RamLargeRank>(e);
		}
	}
	
	
}
