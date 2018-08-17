package it.etoken.componet.coins.facade;

import it.etoken.base.common.result.MLResult;
import it.etoken.base.common.result.MLResultList;
import it.etoken.base.model.market.entity.EosElector;

public interface EosElectorFacadeAPI {

	public MLResultList<EosElector> findAll(int page);

	public MLResult saveUpdate(EosElector eosElector);

	public MLResult delete(Long id);
}
