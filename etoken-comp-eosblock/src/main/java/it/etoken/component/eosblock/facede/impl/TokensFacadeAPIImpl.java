package it.etoken.component.eosblock.facede.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

import it.etoken.base.common.exception.MLException;
import it.etoken.base.common.result.MLResultList;
import it.etoken.base.common.result.MLResultObject;
import it.etoken.base.model.eosblock.entity.AccountsKeys;
import it.etoken.base.model.eosblock.entity.Tokens;
import it.etoken.component.eosblock.service.TokensService;
import it.etoken.componet.eosblock.facade.TokensFacadeAPI;
@Service(version = "1.0.0")
public class TokensFacadeAPIImpl implements TokensFacadeAPI{
	private final static Logger logger = LoggerFactory.getLogger(TokensFacadeAPIImpl.class);
	@Autowired
	TokensService tokensService;
	
	@Override
	public MLResultList<Tokens> findByAccountAndSymbol(String Account, String symbol) {
		try {
			List<Tokens> result= tokensService.findByAccountAndSymbol(Account, symbol);
			return new MLResultList<Tokens>(result);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultList<Tokens>(e);
		}
	}
}
