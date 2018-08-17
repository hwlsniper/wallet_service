package it.etoken.component.eosblock.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import it.etoken.component.eosblock.mongo.model.EOSAction;
import it.etoken.component.eosblock.service.EOSActionService;

@Component
@Transactional
public class EOSActionServiceImpl implements EOSActionService {
	@Autowired
	MongoOperations mongoTemplate;

	@Override
	public void save(EOSAction eOSAction) {
		mongoTemplate.save(eOSAction);
	}

	@Override
	public void remove(String id) {
		mongoTemplate.remove(new Query(Criteria.where("_id").is(id)), EOSAction.class);
	}

	@Override
	public void update(String id, String key, String value) {
		mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(id)), Update.update(key, value), EOSAction.class);
	}

	@Override
	public List<EOSAction> list4Page(int page, int pageSize) {
		
		DBObject dbObject = new BasicDBObject();
		dbObject.put("name", "buyrambytes");  //查询条件
		
		Query query = new Query(Criteria.where("name").is("buyrambytes"));
		
		Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC,"action_num"));
		query = new Query().with(sort).skip((page - 1) * pageSize).limit(pageSize);
		
		List<EOSAction> result = mongoTemplate.find(query, EOSAction.class);

		return result;
	}
}
