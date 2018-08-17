package it.etoken.component.eosblock.service;

import java.util.List;

import it.etoken.base.model.eosblock.entity.Actions;

public interface ActionsService {

	public List<Actions> findByAccountAndSymbol(String account_name);

}
