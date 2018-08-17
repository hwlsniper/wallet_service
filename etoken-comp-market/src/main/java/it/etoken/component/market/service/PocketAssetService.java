package it.etoken.component.market.service;

import com.github.pagehelper.Page;

import it.etoken.base.common.exception.MLException;
import it.etoken.base.model.market.entity.PocketAsset;

public interface PocketAssetService {
	
	/**
	 * 保存
	 * @param pocketAsset
	 * @return
	 * @throws MLException
	 */
	public PocketAsset saveUpdate(PocketAsset pocketAsset)throws MLException;
	
	/**
	 * 删除
	 * @param id
	 * @throws MLException
	 */
	public void delete(Long id)throws MLException;
	
	/**
	 * 查询
	 * @return
	 * @throws MLException
	 */
	public Page<PocketAsset> findAll(int page)throws MLException;
	
	/**
	 * 查看
	 * @param id
	 * @return
	 * @throws MLException
	 */
	public PocketAsset findById(Long id)throws MLException;
}
