package it.etoken.componet.eosblock.facade;

import com.alibaba.fastjson.JSONObject;

import it.etoken.base.common.result.MLResultList;
import it.etoken.component.eosblock.mongo.model.Transactions;

public interface TransactionsFacadeAPI {


	public MLResultList<JSONObject> findByAccountAndActorNew(String last_id, int pageSize,String account,String actor,String code);
	
	public MLResultList<Transactions> findAccountCoins(String account,String actor);
	
	public MLResultList<JSONObject> findByAccountAndActor(int page, int pageSize,String account,String actor,String code);
	

}
