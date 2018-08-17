package it.etoken.component.market.service;

import com.github.pagehelper.Page;

import it.etoken.base.common.exception.MLException;
import it.etoken.base.model.market.entity.Coins;
import it.etoken.base.model.market.entity.EosElector;

public interface EosElectorService {
	
	/**
	 * 保存候选人
	 * @param eosSelector
	 * @return
	 * @throws MLException
	 */
	public EosElector saveUpdate(EosElector eosSelector)throws MLException;
	
	/**
	 * 删除候选人
	 * @param id
	 * @throws MLException
	 */
	public void delete(Long id)throws MLException;
	
	/**
	 * 查询候选人
	 * @return
	 * @throws MLException
	 */
	public Page<EosElector> findAll()throws MLException;
	
	/**
	 * 根据ID查询候选人
	 * @param id
	 * @return
	 * @throws MLException
	 */
	public EosElector findById(Long id)throws MLException;
}
