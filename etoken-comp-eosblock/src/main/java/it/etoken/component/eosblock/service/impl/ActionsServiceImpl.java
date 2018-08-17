package it.etoken.component.eosblock.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.etoken.base.common.exception.MLCommonException;
import it.etoken.base.common.exception.MLException;
import it.etoken.base.model.eosblock.entity.Actions;
import it.etoken.base.model.eosblock.entity.ActionsExample;
import it.etoken.base.model.eosblock.entity.ActionsExample.Criteria;
import it.etoken.component.eosblock.dao.mapper.ActionsMapper;
import it.etoken.component.eosblock.service.ActionsService;
@Component
@Transactional
public class ActionsServiceImpl implements ActionsService{
	
	private final static Logger logger = LoggerFactory.getLogger(ActionsServiceImpl.class);
	
	@Autowired
	ActionsMapper actionsMapper;
	
	@Override
	public List<Actions> findByAccountAndSymbol(String account_name) {
		try {
			ActionsExample example=new ActionsExample();
			Criteria  criteria = example.createCriteria();
			criteria.andDataLike(account_name);
			criteria.andAccountBetween("eosio", "eosio.token");
			List<Actions> list=actionsMapper.selectByExample(example);
			return list;
		} catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}

}
