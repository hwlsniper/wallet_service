package it.etoken.component.eosblock.service;

import java.util.List;

import it.etoken.component.eosblock.mongo.model.EOSAction;

public interface EOSActionService {
	public void save(EOSAction eOSAction);

	public void remove(String id);

	public void update(String id, String key, String value);

	public List<EOSAction> list4Page(int page, int pageSize);
}
