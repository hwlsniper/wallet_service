package it.etoken.component.eosblock.facede.impl;

import com.alibaba.dubbo.config.annotation.Service;

import it.etoken.base.common.exception.MLException;
import it.etoken.base.common.result.MLResult;
import it.etoken.base.common.result.MLResultObject;
import it.etoken.base.model.eosblock.entity.Delegatebw;
import it.etoken.component.eosblock.service.DelegatebwService;
import it.etoken.componet.eosblock.facade.DelegatebwFacadeAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Service(version = "1.0.0")
public class DelegatebwFacadeAPIImpl implements DelegatebwFacadeAPI{
	private final static Logger logger = LoggerFactory.getLogger(DelegatebwFacadeAPIImpl.class);
	
	@Autowired
	DelegatebwService delegatebwService;

	@Override
	public MLResult save(Delegatebw delegatebw) {
		try {
			delegatebwService.save(delegatebw);
			return new MLResult(true);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResult(e);
		}
	}

	@Override
	public MLResultObject<Delegatebw> findByAccountName(String account_name) {
		try {
			Delegatebw result = delegatebwService.findByAccountName(account_name);
			return new MLResultObject<Delegatebw>(result);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultObject<Delegatebw>(e);
		}
	}

}
