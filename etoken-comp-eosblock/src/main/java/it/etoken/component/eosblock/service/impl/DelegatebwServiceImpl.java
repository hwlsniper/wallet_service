package it.etoken.component.eosblock.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.etoken.base.common.exception.MLCommonException;
import it.etoken.base.common.exception.MLException;
import it.etoken.base.model.eosblock.entity.Delegatebw;
import it.etoken.base.model.eosblock.entity.DelegatebwExample;
import it.etoken.base.model.eosblock.entity.DelegatebwExample.Criteria;
import it.etoken.component.eosblock.dao.mapper.DelegatebwMapper;
import it.etoken.component.eosblock.service.DelegatebwService;
@Component
@Transactional
public class DelegatebwServiceImpl implements DelegatebwService{
	
	private final static Logger logger = LoggerFactory.getLogger(DelegatebwServiceImpl.class);

	@Autowired
	DelegatebwMapper delegatebwMapper;

	@Override
	public void save(Delegatebw delegatebw) {
		try {
			delegatebwMapper.insert(delegatebw);
		} catch (MLException ex) {
			logger.error(ex.toString());
			throw ex;
		} catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}

	@Override
	public Delegatebw findByAccountName(String account_name) {
		try {
			DelegatebwExample example=new DelegatebwExample();
		    Criteria  criteria = example.createCriteria();
		    criteria.andAccountNameEqualTo(account_name);
			List<Delegatebw> delegatebws= delegatebwMapper.selectByExample(example);
			if(delegatebws.size()>0) {
				return delegatebws.get(0);
			}else {
				return null;
			}
		} catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}

}
