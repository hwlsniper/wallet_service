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
import it.etoken.base.model.market.entity.EosElector;
import it.etoken.base.model.market.entity.EosElectorExample;
import it.etoken.base.model.market.entity.EosElectorExample.Criteria;
import it.etoken.component.market.dao.mapper.EosElectorMapper;
import it.etoken.component.market.service.EosElectorService;

@Component
@Transactional
public class EosElectorServiceImpl implements EosElectorService {

	private final static Logger logger = LoggerFactory.getLogger(EosElectorServiceImpl.class);
	
	@Autowired
	private EosElectorMapper eosElectorMapper;
	
	@Override
	@CacheEvict(value="eosElectorCache",allEntries=true)
	public EosElector saveUpdate(EosElector eosElector) throws MLException {
		try{
			if(eosElector.getId()==null){
				eosElectorMapper.insert(eosElector);
			}else{
				eosElectorMapper.updateByPrimaryKey(eosElector);
			}
			return eosElector;
		}catch (MLException ex) {
			logger.error(ex.toString());
			throw ex;
		}catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}

	@Override
	@CacheEvict(value="eosElectorCache",allEntries=true)
	public void delete(Long id) throws MLException {
		try{
			eosElectorMapper.deleteByPrimaryKey(id);
		}catch (MLException ex) {
			logger.error(ex.toString());
			throw ex;
		}catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}

	@Override
	@Cacheable(value="eosElectorCache",keyGenerator="wiselyKeyGenerator") 
	public Page<EosElector> findAll() throws MLException {
		try{
			Page<EosElector> result = PageHelper.startPage(1,10000);  
			EosElectorExample example = new EosElectorExample();
			Criteria criteria = example.createCriteria();
			criteria.andDeletedEqualTo(0);
			eosElectorMapper.selectByExampleWithBLOBs(example);
			
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
	@Cacheable(value="eosElectorCache",keyGenerator="wiselyKeyGenerator") 
	public EosElector findById(Long id) throws MLException {
		try{
			return eosElectorMapper.selectByPrimaryKey(id);
		}catch (MLException ex) {
			logger.error(ex.toString());
			throw ex;
		}catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}
}
