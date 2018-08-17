package it.etoken.componet.eosblock.facade;

import it.etoken.base.common.result.MLResultList;
import it.etoken.base.model.eosblock.entity.Actions;

public interface ActionsFacadeAPI {
	//查询用户所有交易记录
	public MLResultList<Actions> findByAccountAndSymbol(String account_name);

}
