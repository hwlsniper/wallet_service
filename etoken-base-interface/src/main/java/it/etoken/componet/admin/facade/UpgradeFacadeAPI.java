package it.etoken.componet.admin.facade;

import it.etoken.base.common.result.MLResult;
import it.etoken.base.common.result.MLResultObject;
import it.etoken.base.model.admin.entity.Upgrade;

public interface UpgradeFacadeAPI {

	public MLResultObject<Upgrade> findByOS(String os);

	public MLResult saveUpdate(Upgrade upgrade);

}
