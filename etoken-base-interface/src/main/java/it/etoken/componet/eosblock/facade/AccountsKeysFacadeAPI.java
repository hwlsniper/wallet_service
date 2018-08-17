package it.etoken.componet.eosblock.facade;

import it.etoken.base.common.result.MLResultList;
import it.etoken.base.common.result.MLResultObject;
import it.etoken.base.model.eosblock.entity.AccountsKeys;


public interface AccountsKeysFacadeAPI {
	
	public MLResultList<AccountsKeys> findByPublicKey(String publicKey);
	
	public MLResultList<AccountsKeys> findByAccount(String account);
}
