package it.etoken.component.eosblock.service;

import it.etoken.base.model.eosblock.entity.Delegatebw;

public interface DelegatebwService {

    public void save(Delegatebw delegatebw);
	
	public Delegatebw findByAccountName(String account_name);
}
