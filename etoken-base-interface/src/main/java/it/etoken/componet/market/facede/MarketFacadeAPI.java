package it.etoken.componet.market.facede;

import java.util.Map;

import it.etoken.base.common.result.MLResultList;
import it.etoken.base.common.result.MLResultObject;
import it.etoken.base.model.market.entity.Coins;
import it.etoken.base.model.market.vo.CoinTicker;

public interface MarketFacadeAPI {
	
	/**
	 * 获得行情
	 * @return
	 */
	public MLResultList<CoinTicker> getTicker();
	
	/**
	 * 获得行情
	 * @return
	 */
	public MLResultObject<Coins> saveUpdate(Coins coin);
	
	/**
	 * 获得行情
	 * @return
	 */
	public MLResultObject<Coins> findById(Long id);
	
	/**
	 * 獲得k綫
	 * @param coins
	 * @param type
	 * @return
	 */
	public MLResultObject<Map> getLine(String coins,String type);
}
