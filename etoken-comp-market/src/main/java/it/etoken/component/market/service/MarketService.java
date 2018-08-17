package it.etoken.component.market.service;

import java.util.List;
import java.util.Map;

import it.etoken.base.common.exception.MLException;
import it.etoken.base.model.market.entity.Coins;
import it.etoken.base.model.market.vo.CoinTicker;

public interface MarketService {
	
	/**
	 * 获取价格信息
	 * @param coins
	 * @return
	 */
	public void ticker(Coins coins)throws MLException;
	
	/**
	 * 获取美元汇率
	 * @param coins
	 * @return
	 */
	public void exchange()throws MLException;
	
	/**
	 * 查詢币
	 * @param id
	 * @return
	 */
	public List<CoinTicker> getTicker()throws MLException;
	
	/**
	 * 
	 * @return
	 * @throws MLException
	 */
	public Map getLine(String coin,String type)throws MLException;
	
	/**
	 * 获取0时价格
	 * @param coins
	 */
	public void morningPrice(Coins coins) throws MLException;

}
