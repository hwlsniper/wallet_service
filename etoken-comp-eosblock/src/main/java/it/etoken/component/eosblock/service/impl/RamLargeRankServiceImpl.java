package it.etoken.component.eosblock.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import it.etoken.base.common.exception.MLCommonException;
import it.etoken.base.common.exception.MLException;
import it.etoken.component.eosblock.mongo.model.RamLargeRank;
import it.etoken.component.eosblock.service.RamLargeRankService;
import it.etoken.base.common.utils.HttpClientUtils;

@Component
@Transactional
public class RamLargeRankServiceImpl implements RamLargeRankService{

	@Autowired
	MongoOperations mongoTemplate;
	
	
	@Override
	public List<RamLargeRank> getNewestRank() {
		Query query = new Query();
		query = query.with(new Sort(new Order(Direction.DESC, "ramQuota")));
		query = query.limit(20);
		List<RamLargeRank> RamLargeRankList = mongoTemplate.find(query, RamLargeRank.class);
		return RamLargeRankList;
	}

	@Override
	public void getLargeRank() {
		try {
			  String url = "http://api.ram.southex.com/v1/getram/rank";
		      String result = HttpClientUtils.doPostJson(url, "");
		      JSONObject json=JSONObject.parseObject(result); 
		      Update update=new Update();
		      update.set("ramProportion100", json.get("ramProportion100"));
		      update.set("ramProportion200", json.get("ramProportion200"));
		      update.set("lastUpdateAt", json.get("lastUpdateAt"));
		      JSONArray jsonarray = json.getJSONArray("users");
		      if(jsonarray.size()==0) {
		    	  return;
		      }
		      for(int i=0;i<jsonarray.size();i++){
		    	  JSONObject user = jsonarray.getJSONObject(i); // 遍历 jsonarray 数组，把每一个对象转成 json 对象
		    	  Double ramQuota=user.getDouble("ramQuota");
		    	  update.set("totalRamSelled", user.get("totalRamSelled"));
		    	  update.set("totalEosEarn", user.get("totalEosEarn"));
		    	  update.set("totalProfit", user.get("totalProfit"));
		    	  update.set("ramQuota",ramQuota);//可售内存
		    	  update.set("holdCost", user.get("holdCost"));
		    	  update.set("totalEosCost", user.get("totalEosCost"));
		    	  update.set("historyAverageCost", user.get("historyAverageCost"));
		    	  update.set("profit", user.get("profit"));
		    	  update.set("account", user.get("account"));
		    	  update.set("ramValue", user.get("ramValue"));
		    	  update.set("createdAt", System.currentTimeMillis());
		    	  update.set("updatedAt", System.currentTimeMillis());
			      //查询user.get("account")是否存在存在就更新，不存在就新增。
			      Query query = new Query(Criteria.where("account").is(user.get("account")));
				  mongoTemplate.upsert(query, update, RamLargeRank.class);
		    	  }  
		} catch (Exception e) {
			e.printStackTrace();
			throw new MLException(MLCommonException.system_err);
		}
		
	}

}
