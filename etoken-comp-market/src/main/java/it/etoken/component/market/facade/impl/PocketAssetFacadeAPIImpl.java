package it.etoken.component.market.facade.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;

import it.etoken.base.common.exception.MLException;
import it.etoken.base.common.result.MLResult;
import it.etoken.base.common.result.MLResultList;
import it.etoken.base.model.market.entity.PocketAsset;
import it.etoken.component.market.service.PocketAssetService;
import it.etoken.componet.coins.facade.PocketAssetFacadeAPI;

@Service(version = "1.0.0")
public class PocketAssetFacadeAPIImpl implements PocketAssetFacadeAPI {

	private final static Logger logger = LoggerFactory.getLogger(PocketAssetFacadeAPIImpl.class);

	@Autowired
	PocketAssetService pocketAssetService;

	@Override
	public MLResultList<PocketAsset> findAll(int page) {
		try {
			Page<PocketAsset> result = pocketAssetService.findAll(page);
			return new MLResultList<PocketAsset>(result.getResult());
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultList<PocketAsset>(e);
		}
	}

	@Override
	public MLResult saveUpdate(PocketAsset pocketAsset) {
		try {
			pocketAssetService.saveUpdate(pocketAsset);
			return new MLResult(true);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResult(e);
		}
	}

	@Override
	public MLResult delete(Long id) {
		try {
			pocketAssetService.delete(id);
			return new MLResult(true);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResult(e);
		}
	}
}
