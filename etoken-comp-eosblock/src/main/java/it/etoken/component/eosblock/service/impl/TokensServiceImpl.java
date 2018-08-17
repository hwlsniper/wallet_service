package it.etoken.component.eosblock.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.etoken.base.common.exception.MLCommonException;
import it.etoken.base.common.exception.MLException;
import it.etoken.base.model.eosblock.entity.Tokens;
import it.etoken.base.model.eosblock.entity.TokensExample;
import it.etoken.component.eosblock.dao.mapper.TokensMapper;
import it.etoken.component.eosblock.service.TokensService;

@Component
@Transactional
public class TokensServiceImpl implements TokensService{
	
	private final static Logger logger = LoggerFactory.getLogger(AccountsKeysServiceImpl.class);
	@Autowired
	TokensMapper tokensMapper;
	
	@Override
	public List<Tokens> findByAccountAndSymbol(String Account, String symbol) {
		try {
			TokensExample example=new TokensExample();
			it.etoken.base.model.eosblock.entity.TokensExample.Criteria  criteria = example.createCriteria();
			criteria.andAccountEqualTo(Account);
			criteria.andSymbolEqualTo(symbol);
			List<Tokens> list=tokensMapper.selectByExample(example);
			return list;
		} catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}

	
}
