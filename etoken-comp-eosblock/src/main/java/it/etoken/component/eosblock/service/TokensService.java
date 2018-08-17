package it.etoken.component.eosblock.service;

import java.util.List;

import it.etoken.base.model.eosblock.entity.Tokens;

public interface TokensService {

	public List<Tokens> findByAccountAndSymbol(String Account,String symbol);
}
