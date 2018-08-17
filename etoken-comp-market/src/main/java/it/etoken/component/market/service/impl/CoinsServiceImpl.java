package it.etoken.component.market.service.impl;

import java.util.List;

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
import it.etoken.base.model.market.entity.Coins;
import it.etoken.base.model.market.entity.CoinsExample;
import it.etoken.base.model.market.entity.CoinsExample.Criteria;
import it.etoken.component.market.dao.mapper.CoinsMapper;
import it.etoken.component.market.dao.mapper.CoinsNewMapper;
import it.etoken.component.market.service.CoinsService;

@Component
@Transactional
public class CoinsServiceImpl implements CoinsService {

	private final static Logger logger = LoggerFactory.getLogger(CoinsServiceImpl.class);
	
	@Autowired
	private CoinsMapper coinsMapper;
	
	@Autowired
	private CoinsNewMapper coinsNewMapper;
	
	@Override
	@CacheEvict(value="coinsCache",allEntries=true)
	public Coins saveUpdate(Coins coins) throws MLException {
		try{
			if(coins.getId()==null){
//				coinsMapper.insert(coins);
				coinsNewMapper.insertSelective(coins);
			}else{
				coinsNewMapper.updateByPrimaryKeySelective(coins);
//				coinsMapper.update(coins);
			}
			return coins;
		}catch (MLException ex) {
			logger.error(ex.toString());
			throw ex;
		}catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}

	@Override
	@CacheEvict(value="coinsCache",allEntries=true)
	public void delete(Long id) throws MLException {
		try{
			coinsNewMapper.deleteByPrimaryKey(id);
		}catch (MLException ex) {
			logger.error(ex.toString());
			throw ex;
		}catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}

	@Override
	@Cacheable(value="coinsCache",keyGenerator="wiselyKeyGenerator") 
	public Page<Coins> findAll() throws MLException {
		try{
			Page<Coins> result = PageHelper.startPage(1,100);  
			CoinsExample example = new CoinsExample();
			coinsNewMapper.selectByExample(example);
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
	@Cacheable(value="coinsCache",keyGenerator="wiselyKeyGenerator") 
	public Coins findById(Long id) throws MLException {
		try{
			return coinsNewMapper.selectByPrimaryKey(id);
		}catch (MLException ex) {
			logger.error(ex.toString());
			throw ex;
		}catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}
	
	@Override
	@Cacheable(value="coinsCache",keyGenerator="wiselyKeyGenerator") 
	public Page<Coins> findAllByPage(int page, String code) throws MLException {
		try{
			Page<Coins> result = PageHelper.startPage(page,100);  
			CoinsExample example = new CoinsExample();
			CoinsExample.Criteria criteria = example.createCriteria();
			if(null != code && !code.isEmpty()) {
				criteria.andCodeEqualTo(code);
			}
			coinsNewMapper.selectByExample(example);
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
	@Cacheable(value="coinsCache",keyGenerator="wiselyKeyGenerator") 
	public Coins findByName(String name) throws MLException {
		try{
			CoinsExample example = new CoinsExample();
			Criteria criteria = example.createCriteria();
			criteria.andNameEqualTo(name);
			
			List<Coins> coins =  coinsNewMapper.selectByExample(example);
			if(coins.isEmpty()) {
				return null;
			}
			return coins.get(0);
		}catch (MLException ex) {
			logger.error(ex.toString());
			throw ex;
		}catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}
	
	@Override
	@Cacheable(value="coinsCache",keyGenerator="wiselyKeyGenerator") 
	public Page<Coins> findAllBy4Market() throws MLException {
		try{
			CoinsExample example = new CoinsExample();
			Criteria criteria = example.createCriteria();
			criteria.andIsSupportMarketEqualTo("y");
			
			Page<Coins> result = PageHelper.startPage(1,1000); 
			
			coinsNewMapper.selectByExample(example);
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
	public List<Coins> findAllCoins() {
		try{
			CoinsExample example = new CoinsExample();
			List<Coins> list=coinsNewMapper.selectByExample(example);
			return list;
		}catch (MLException ex) {
			logger.error(ex.toString());
			throw ex;
		}catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}
}
