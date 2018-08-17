package it.etoken.component.market.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import it.etoken.base.common.exception.MLCommonException;
import it.etoken.base.common.exception.MLException;
import it.etoken.base.model.market.entity.PocketAsset;
import it.etoken.base.model.market.entity.PocketAssetExample;
import it.etoken.component.market.dao.mapper.PocketAssetMapper;
import it.etoken.component.market.service.PocketAssetService;

@Component
@Transactional
public class PocketAssetServiceImpl implements PocketAssetService {

	private final static Logger logger = LoggerFactory.getLogger(PocketAssetServiceImpl.class);
	
	@Autowired
	private PocketAssetMapper pocketAssetMapper;
	
	@Override
	@CacheEvict(value="pocketAssetCache",allEntries=true)
	public PocketAsset saveUpdate(PocketAsset pocketAsset) throws MLException {
		try{
			if(pocketAsset.getId()==null){
				pocketAssetMapper.insertSelective(pocketAsset);
			}else{
				pocketAssetMapper.updateByPrimaryKey(pocketAsset);
			}
			return pocketAsset;
		}catch (MLException ex) {
			logger.error(ex.toString());
			throw ex;
		}catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}

	@Override
	@CacheEvict(value="pocketAssetCache",allEntries=true)
	public void delete(Long id) throws MLException {
		try{
			pocketAssetMapper.deleteByPrimaryKey(id);
		}catch (MLException ex) {
			logger.error(ex.toString());
			throw ex;
		}catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}

	@Override
	@Cacheable(value="pocketAssetCache",keyGenerator="wiselyKeyGenerator") 
	public Page<PocketAsset> findAll(int page) throws MLException {
		try{
			Page<PocketAsset> result = PageHelper.startPage(page,100);  
			PocketAssetExample example = new PocketAssetExample();
			pocketAssetMapper.selectByExampleWithBLOBs(example);
			return result;
		}catch (MLException ex) {
			logger.error(ex.toString());
			throw ex;
		}catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}

	@Override
	@Cacheable(value="pocketAssetCache",keyGenerator="wiselyKeyGenerator") 
	public PocketAsset findById(Long id) throws MLException {
		try{
			return pocketAssetMapper.selectByPrimaryKey(id);
		}catch (MLException ex) {
			logger.error(ex.toString());
			throw ex;
		}catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}
}
