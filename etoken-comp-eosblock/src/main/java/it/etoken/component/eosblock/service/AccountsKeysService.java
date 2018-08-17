package it.etoken.component.eosblock.service;

import java.util.List;

import it.etoken.base.model.eosblock.entity.AccountsKeys;

public interface AccountsKeysService {
	
    public List<AccountsKeys> findByPublicKey(String publicKey);
	
	public List<AccountsKeys> findByAccount(String account);

}
 