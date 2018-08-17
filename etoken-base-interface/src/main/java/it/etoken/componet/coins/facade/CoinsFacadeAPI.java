package it.etoken.componet.coins.facade;

import it.etoken.base.common.result.MLResult;
import it.etoken.base.common.result.MLResultList;
import it.etoken.base.common.result.MLResultObject;
import it.etoken.base.model.market.entity.Coins;

public interface CoinsFacadeAPI {

	public MLResultList<Coins> findAll(int page);

	public MLResult saveUpdate(Coins coins);

	public MLResult delete(Long id);
	
	public MLResultList<Coins> findAllByPage(int page, String code);
	
	public MLResultObject<Coins> findByName(String name);
	
	//查询所有的coins不分页
	public MLResultList<Coins> findAllCoins();
	
}
