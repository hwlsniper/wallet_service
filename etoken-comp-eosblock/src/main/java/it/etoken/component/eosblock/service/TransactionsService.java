package it.etoken.component.eosblock.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

import it.etoken.component.eosblock.mongo.model.Transactions;


public interface TransactionsService {
	
	public List<JSONObject> findByAccountAndActorNew(String last_id, int pageSize,String account,String actor,String code);
	
	public List<Transactions> findAccountCoins(String account,String actor);
	
	public List<JSONObject> findByAccountAndActor(int page, int pageSize,String account,String actor,String code);

}
