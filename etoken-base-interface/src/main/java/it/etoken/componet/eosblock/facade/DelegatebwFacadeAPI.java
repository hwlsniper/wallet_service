package it.etoken.componet.eosblock.facade;

import it.etoken.base.common.result.MLResult;
import it.etoken.base.common.result.MLResultObject;
import it.etoken.base.model.eosblock.entity.Delegatebw;

public interface DelegatebwFacadeAPI {
	
	public MLResult save(Delegatebw delegatebw);
	
	public MLResultObject<Delegatebw> findByAccountName(String account_name);

}
