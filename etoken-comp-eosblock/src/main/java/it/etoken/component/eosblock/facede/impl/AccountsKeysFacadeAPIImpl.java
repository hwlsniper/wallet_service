package it.etoken.component.eosblock.facede.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

import it.etoken.base.common.exception.MLException;
import it.etoken.base.common.result.MLResultList;
import it.etoken.base.model.eosblock.entity.AccountsKeys;
import it.etoken.component.eosblock.service.AccountsKeysService;
import it.etoken.componet.eosblock.facade.AccountsKeysFacadeAPI;
@Service(version = "1.0.0")
public class AccountsKeysFacadeAPIImpl  implements AccountsKeysFacadeAPI{
	private final static Logger logger = LoggerFactory.getLogger(AccountsKeysFacadeAPIImpl.class);

	@Autowired
	AccountsKeysService accountsKeysService;
	
	@Override
	public MLResultList<AccountsKeys> findByPublicKey(String publicKey) {
		try {
			List<AccountsKeys> result= accountsKeysService.findByPublicKey(publicKey);
			return new MLResultList<AccountsKeys>(result);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultList<AccountsKeys>(e);
		}
	}

	@Override
	public MLResultList<AccountsKeys> findByAccount(String account) {
		try {
			List<AccountsKeys> result=accountsKeysService.findByAccount(account);
			return new MLResultList<AccountsKeys>(result);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultList<AccountsKeys>(e);
		}
	}

}
