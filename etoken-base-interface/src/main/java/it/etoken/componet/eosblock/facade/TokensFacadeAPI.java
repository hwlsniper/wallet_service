package it.etoken.componet.eosblock.facade;

import it.etoken.base.common.result.MLResultList;
import it.etoken.base.common.result.MLResultObject;
import it.etoken.base.model.eosblock.entity.Tokens;

public interface TokensFacadeAPI {

	public MLResultList<Tokens> findByAccountAndSymbol(String Account,String symbol);
}
