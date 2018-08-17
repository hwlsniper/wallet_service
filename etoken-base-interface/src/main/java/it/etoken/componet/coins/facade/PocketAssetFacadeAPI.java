package it.etoken.componet.coins.facade;

import it.etoken.base.common.result.MLResult;
import it.etoken.base.common.result.MLResultList;
import it.etoken.base.model.market.entity.PocketAsset;

public interface PocketAssetFacadeAPI {

	public MLResultList<PocketAsset> findAll(int page);

	public MLResult saveUpdate(PocketAsset pocketAsset);

	public MLResult delete(Long id);
}
