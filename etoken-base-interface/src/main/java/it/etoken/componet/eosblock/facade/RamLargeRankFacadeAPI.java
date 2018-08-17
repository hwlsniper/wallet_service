package it.etoken.componet.eosblock.facade;

import it.etoken.base.common.result.MLResultList;
import it.etoken.base.model.eosblock.entity.RamTradeLog;
import it.etoken.component.eosblock.mongo.model.RamLargeRank;

public interface RamLargeRankFacadeAPI {

	public MLResultList<RamLargeRank> getNewestRank();
	
}
