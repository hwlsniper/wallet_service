package it.etoken.component.admin.facade.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

import it.etoken.base.common.exception.MLException;
import it.etoken.base.common.result.MLResult;
import it.etoken.base.common.result.MLResultObject;
import it.etoken.base.model.admin.entity.Upgrade;
import it.etoken.component.admin.service.UpgradeService;
import it.etoken.componet.admin.facade.UpgradeFacadeAPI;

@Service(version = "1.0.0")
public class UpgradeFacadeAPIImpl implements UpgradeFacadeAPI {

	private final static Logger logger = LoggerFactory.getLogger(UpgradeFacadeAPIImpl.class);

	@Autowired
	UpgradeService upgradeService;

	@Override
	public MLResultObject<Upgrade> findByOS(String os) {
		try {
			Upgrade b = upgradeService.findByOs(os);
			return new MLResultObject<Upgrade>(b);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultObject<Upgrade>(e);
		}
	}

	@Override
	public MLResult saveUpdate(Upgrade upgrade) {
		try {
			upgradeService.update(upgrade);
			return new MLResult(true);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResult(e);
		}
	}

}
