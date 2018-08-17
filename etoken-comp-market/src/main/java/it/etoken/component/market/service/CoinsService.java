package it.etoken.component.market.service;

import java.util.List;

import com.github.pagehelper.Page;

import it.etoken.base.common.exception.MLException;
import it.etoken.base.model.market.entity.Coins;

public interface CoinsService {
	
	/**
	 * 添加币
	 * @param coins
	 * @return
	 */
	public Coins saveUpdate(Coins coins)throws MLException;
	
	/**
	 * 删除币
	 * @param id
	 * @return
	 */
	public void delete(Long id)throws MLException;
	
	/**
	 * 查詢币
	 * @param userId
	 * @return
	 */
	public Page<Coins> findAll()throws MLException;
	
	/**
	 * 查詢币
	 * @param id
	 * @return
	 */
	public Coins findById(Long id)throws MLException;
	
	/**
	 * 分页查询
	 * @param page
	 * @return
	 * @throws MLException
	 */
	public Page<Coins> findAllByPage(int page, String code) throws MLException;
	
	/**
	 * 根据Name查询
	 * @param name
	 * @return
	 * @throws MLException
	 */
	public Coins findByName(String name) throws MLException;
	
	/**
	 * 获取所有支持行情的coin
	 * @return
	 * @throws MLException
	 */
	public Page<Coins> findAllBy4Market() throws MLException;

	public List<Coins> findAllCoins();
}
