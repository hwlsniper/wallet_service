package it.etoken.component.market.facade.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;

import it.etoken.base.common.exception.MLException;
import it.etoken.base.common.result.MLResult;
import it.etoken.base.common.result.MLResultList;
import it.etoken.base.model.market.entity.EosElector;
import it.etoken.component.market.service.EosElectorService;
import it.etoken.componet.coins.facade.EosElectorFacadeAPI;

@Service(version = "1.0.0")
public class EosElectorFacadeAPIImpl implements EosElectorFacadeAPI {

	private final static Logger logger = LoggerFactory.getLogger(EosElectorFacadeAPIImpl.class);

	@Autowired
	EosElectorService eosElectorService;

	@Override
	public MLResultList<EosElector> findAll(int page) {
		try {
			Page<EosElector> result = eosElectorService.findAll();
			return new MLResultList<EosElector>(result.getResult());
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultList<EosElector>(e);
		}
	}

	@Override
	public MLResult saveUpdate(EosElector eosElector) {
		try {
			eosElectorService.saveUpdate(eosElector);
			return new MLResult(true);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResult(e);
		}
	}

	@Override
	public MLResult delete(Long id) {
		try {
			eosElectorService.delete(id);
			return new MLResult(true);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResult(e);
		}
	}
}
