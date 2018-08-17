package it.etoken.component.eosblock.facede.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

import it.etoken.base.common.exception.MLException;
import it.etoken.base.common.result.MLResultList;
import it.etoken.base.model.eosblock.entity.Actions;
import it.etoken.base.model.eosblock.entity.Tokens;
import it.etoken.component.eosblock.service.ActionsService;
import it.etoken.componet.eosblock.facade.ActionsFacadeAPI;
@Service(version = "1.0.0")
public class ActionsFacadeAPIImpl implements ActionsFacadeAPI{
	
	private final static Logger logger = LoggerFactory.getLogger(ActionsFacadeAPIImpl.class);
	
	@Autowired
	ActionsService actionsService;
	
	@Override
	public MLResultList<Actions> findByAccountAndSymbol(String account_name) {
		try {
			List<Actions> result= actionsService.findByAccountAndSymbol(account_name);
			return new MLResultList<Actions>(result);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultList<Actions>(e);
		}
	}
	

}
