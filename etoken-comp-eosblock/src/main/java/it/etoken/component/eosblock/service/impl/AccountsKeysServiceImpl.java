package it.etoken.component.eosblock.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.etoken.base.common.exception.MLCommonException;
import it.etoken.base.common.exception.MLException;
import it.etoken.base.model.eosblock.entity.AccountsKeys;
import it.etoken.base.model.eosblock.entity.AccountsKeysExample;
import it.etoken.base.model.eosblock.entity.AccountsKeysExample.Criteria;
import it.etoken.component.eosblock.dao.mapper.AccountsKeysMapper;
import it.etoken.component.eosblock.service.AccountsKeysService;


@Component
@Transactional
public class AccountsKeysServiceImpl implements AccountsKeysService{
	private final static Logger logger = LoggerFactory.getLogger(AccountsKeysServiceImpl.class);

	@Autowired
    AccountsKeysMapper accountsKeysMapper;

	@Override
	public List<AccountsKeys> findByPublicKey(String publicKey) {
		try {
			AccountsKeysExample example=new AccountsKeysExample();
			Criteria  criteria = example.createCriteria();
			criteria.andPublicKeyEqualTo(publicKey);
			List<AccountsKeys> accountsKeys= accountsKeysMapper.selectByExample(example);
			return accountsKeys;
		} catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
		
	}

	@Override
	public List<AccountsKeys> findByAccount(String account) {
		try {
			AccountsKeysExample example=new AccountsKeysExample();
			Criteria  criteria = example.createCriteria();
			criteria.andAccountEqualTo(account);
			List<AccountsKeys> accountsKeys= accountsKeysMapper.selectByExample(example);
			return accountsKeys;
		} catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}
	
   

}
 