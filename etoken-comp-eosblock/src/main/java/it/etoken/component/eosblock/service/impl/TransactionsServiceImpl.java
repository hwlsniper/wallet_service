package it.etoken.component.eosblock.service.impl;


import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

import it.etoken.component.eosblock.mongo.model.RamPriceInfo;
import it.etoken.component.eosblock.mongo.model.Transactions;
import it.etoken.component.eosblock.service.TransactionsService;

@Component
@Transactional
public class TransactionsServiceImpl implements TransactionsService{
	
	@Autowired
	MongoOperations mongoTemplate;
	
	
	
	public BigDecimal getRamPriceByTimes(Long times) {
		Query query = new Query(Criteria.where("record_date").is(times));
		List<RamPriceInfo> result = mongoTemplate.find(query, RamPriceInfo.class);
		
		if(null != result && !result.isEmpty()) {
			BasicDBObject temp = result.get(0);
			String priceString = temp.getString("price");
			return BigDecimal.valueOf(Double.parseDouble(priceString));
		}else{
			return BigDecimal.valueOf(0.32115);
		}
	}
	
	@Override
	@Deprecated
	public List<JSONObject> findByAccountAndActor(int page, int pageSize, String account, String actor,String code) {
		Criteria accountCriteria = null;
		if(code.equalsIgnoreCase("eos")){
			Object[] accountNames = new Object[] { "eosio", "eosio.token"};
			accountCriteria = Criteria.where("actions.account").in(accountNames);
		}else if(null != actor || !"".equals(actor)){
			accountCriteria = Criteria.where("actions.account").is(account);
		}
		
		Criteria[] actorCriterias = new Criteria[3];
		actorCriterias[0] = Criteria.where("actions.authorization.actor").is(actor);
		actorCriterias[1] = Criteria.where("actions.data.receiver").is(actor);
		actorCriterias[2] = Criteria.where("actions.data.to").is(actor);
		
		Criteria actorCriteria = new Criteria();
		actorCriteria.orOperator(actorCriterias);
		
		Criteria codeCriteria = new Criteria();
//		Object[] actionstNames = new Object[] { "delegatebw", "sellram","undelegatebw"};
		Pattern pattern=Pattern.compile("^.*"+code+".*$", Pattern.CASE_INSENSITIVE);
		codeCriteria.orOperator(Criteria.where("actions.data.quantity").regex(pattern),
				Criteria.where("actions.name").is("delegatebw"),
				Criteria.where("actions.name").is("sellram"),
				Criteria.where("actions.name").is("undelegatebw")
//				Criteria.where("actions.name").is("newaccount")
//                Criteria.where("actions.data.stake_cpu_quantit").regex(pattern),
//                Criteria.where("actions.data.quant").regex(pattern),
               );
		
		Criteria blockIdCriteria =Criteria.where("block_id").exists(true);
		
		
		Criteria criteria = new Criteria();
		if(accountCriteria!=null) {
		   criteria.andOperator(accountCriteria,actorCriteria,codeCriteria,blockIdCriteria);
		   System.out.println(criteria.getCriteriaObject());
		}else {
		   criteria.andOperator(actorCriteria,codeCriteria,blockIdCriteria);
		   System.out.println(criteria.getCriteriaObject());
		}
	    Query query = new Query(criteria);
	    query = query.with(new Sort(new Order(Direction.DESC, "createdAt")));
	    query = query.limit(pageSize);
	    query = query.skip((page - 1) * pageSize);

	    List<Transactions> transactionsList = mongoTemplate.find(query, Transactions.class);
	    
	    List<JSONObject> list=new ArrayList<JSONObject>();
		for (BasicDBObject thisBasicDBObject :transactionsList) {
			String type="";
			String to="";
			String from="";
			String quantity="0.0";
			String memo="";
			String description="";
			String code_new="";
			String transactionId=thisBasicDBObject.getString("trx_id");
			String blockNum=thisBasicDBObject.getString("block_num");
			Date  blockTime=thisBasicDBObject.getDate("createdAt");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        	Long times = 0l;
			BigDecimal price = BigDecimal.ZERO;
			try {
				times = sdf.parse(sdf.format(blockTime)).getTime();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			price = this.getRamPriceByTimes(times);
			BasicDBList actions = (BasicDBList) thisBasicDBObject.get("actions");
			Object[] thisActions = actions.toArray();
			for(Object thisAction : thisActions) {
				BasicDBObject action = (BasicDBObject)thisAction;
				BasicDBObject data = (BasicDBObject) action.get("data");
				String actionName = action.getString("name");
				 if(actionName.equalsIgnoreCase("transfer")) {
		            	description="转账";
		            	to=data.getString("to");
		            	from=data.getString("from");
		            	if(from.equals(actor)) {
		 			    	type="转出";
		 			    }
		 			    if(to.equals(actor)) {
		 			    	type="转入";
		 			    }
		            	quantity=data.getString("quantity");
		             	String[] quantity_arra= quantity.split(" ");
		             	quantity=quantity_arra[0];
		             	code_new=quantity_arra[1];
		            	memo=data.getString("memo");
		            }else if(actionName.equalsIgnoreCase("newaccount")) {
		            	memo="";
		            	from=data.getString("creator");
		            	to=data.getString("name");
		            	type="创建账号";
		            	description="创建账号";
		            	quantity="0.0";
		            }else if(actionName.equalsIgnoreCase("delegatebw")) {
		            	memo="";
		            	from=data.getString("from");
		            	to=data.getString("receiver");
		            	String stake_net_quantity=data.getString("stake_net_quantity");
		            	String[] stake_net_quantity_array= stake_net_quantity.split(" ");
		            	String stake_cpu_quantity=data.getString("stake_cpu_quantity");
		            	String[] stake_cpu_quantity_array= stake_cpu_quantity.split(" ");
		            	BigDecimal netQuantity= new  BigDecimal(stake_net_quantity_array[0]);
		            	BigDecimal cpuQuantity= new  BigDecimal(stake_cpu_quantity_array[0]);
		            	BigDecimal quantitys = null; 
		            	code_new=stake_cpu_quantity_array[1];
		            	type="转出";
		            	description="抵押";
		            	quantitys=netQuantity.add(cpuQuantity);
		            	quantity=quantitys.toString();
		            }else if(actionName.equalsIgnoreCase("undelegatebw")) {
		            	memo="";
		            	from=data.getString("from");
		            	to=data.getString("receiver");
		            	String unstake_net_quantity=data.getString("unstake_net_quantity");
		            	String[] unstake_net_quantity_array= unstake_net_quantity.split(" ");
		            	String unstake_cpu_quantity=data.getString("unstake_cpu_quantity");
		            	String[] unstake_cpu_quantity_array= unstake_cpu_quantity.split(" ");
		            	BigDecimal netQuantity= new  BigDecimal(unstake_net_quantity_array[0]);
		            	BigDecimal cpuQuantity= new  BigDecimal(unstake_cpu_quantity_array[0]);
		            	code_new=unstake_cpu_quantity_array[1];
		            	BigDecimal quantitys = null; 
		            	type="转入";
	            		description="赎回";
	            		quantitys=netQuantity.add(cpuQuantity);
		            	quantity=quantitys.toString();
		            }else if(actionName.equalsIgnoreCase("buyram")) {
		            	description="内存购买";
		            	memo="";
		            	type="转出";
		            	from=data.getString("payer");
		             	to=data.getString("receiver");
		            	quantity=data.getString("quant");
		            	String[] quantity_arra= quantity.split(" ");
		             	quantity=quantity_arra[0];
		             	code_new=quantity_arra[1];
		            }else if(actionName.equalsIgnoreCase("sellram")) {
		            	description="内存出售";
		            	memo="";
		            	type="转入";
		            	to="";
		            	from=data.getString("account");
		            	Long bytes = data.getLong("bytes");
						BigDecimal bytesK = BigDecimal.valueOf(bytes).divide(BigDecimal.valueOf(1024l), 2, BigDecimal.ROUND_HALF_UP);
						BigDecimal eos_qty = bytesK.multiply(price);
						eos_qty.setScale(4, BigDecimal.ROUND_HALF_UP);
						quantity=eos_qty.toString();
						code_new=code;
		            }else if(actionName.equalsIgnoreCase("issue")) {
		            	description="发行";
		            	memo="";
		            	type="转入";
		            	to=data.getString("to");
		            	from="";
		            	quantity=data.getString("quantity");
		            	String[] quantity_arra= quantity.split(" ");
		             	quantity=quantity_arra[0];
		             	code_new=quantity_arra[1];
		            }else {
		            	continue;
					}
					    JSONObject jsonObjects = new JSONObject();
					    jsonObjects.put("quantity", quantity+code_new);//code_new是单位如EOS,MSP	
						jsonObjects.put("description", description);
						jsonObjects.put("memo",memo);
						jsonObjects.put("from", from);
						jsonObjects.put("blockNum", blockNum);
						jsonObjects.put("blockTime", sdf.format(blockTime));
						jsonObjects.put("to", to);
						jsonObjects.put("type", type);
						jsonObjects.put("transactionId", transactionId);
						if (null == code || code.isEmpty()) {
						jsonObjects.put("code", code_new);	
						}else {
						jsonObjects.put("code", code);		
						}
				        list.add(jsonObjects);
			}	
		}
		return list;
	}

	@Override
	public List<JSONObject> findByAccountAndActorNew(String last_id, int pageSize, String account, String actor,String code) {
		Date startDate = null;
		if (null != last_id && !last_id.isEmpty()) {
			Query query = new Query(Criteria.where("_id").is(new ObjectId(last_id)));
			List<BasicDBObject> existTransactionsList = mongoTemplate.find(query, BasicDBObject.class, "transactions");
			if (null != existTransactionsList && !existTransactionsList.isEmpty()) {
				startDate = existTransactionsList.get(0).getDate("createdAt");
			}
		}
		Criteria accountCriteria = null;
		if(code.equalsIgnoreCase("eos")){
			Object[] accountNames = new Object[] { "eosio", "eosio.token"};
			accountCriteria = Criteria.where("actions.account").in(accountNames);
		}else if(null != actor || !"".equals(actor)){
			accountCriteria = Criteria.where("actions.account").is(account);
		}
		
		Criteria[] actorCriterias = new Criteria[3];
		actorCriterias[0] = Criteria.where("actions.authorization.actor").is(actor);
		actorCriterias[1] = Criteria.where("actions.data.receiver").is(actor);
		actorCriterias[2] = Criteria.where("actions.data.to").is(actor);
		
		Criteria actorCriteria = new Criteria();
		actorCriteria.orOperator(actorCriterias);
		
		Criteria codeCriteria = new Criteria();
//		Object[] actionstNames = new Object[] { "delegatebw", "sellram","undelegatebw"};
		Pattern pattern=Pattern.compile("^.*"+code+".*$", Pattern.CASE_INSENSITIVE);
		codeCriteria.orOperator(Criteria.where("actions.data.quantity").regex(pattern),
				Criteria.where("actions.name").is("delegatebw"),
				Criteria.where("actions.name").is("sellram"),
				Criteria.where("actions.name").is("undelegatebw")
//				Criteria.where("actions.name").is("newaccount")
//                Criteria.where("actions.data.stake_cpu_quantit").regex(pattern),
//                Criteria.where("actions.data.quant").regex(pattern),
               );
		
		//Criteria blockIdCriteria =Criteria.where("block_id").exists(true);
		
		
		Criteria criteria = new Criteria();
		if(accountCriteria!=null) {
		   criteria.andOperator(accountCriteria,actorCriteria,codeCriteria);
		   System.out.println(criteria.getCriteriaObject());
		}else {
		   criteria.andOperator(actorCriteria,codeCriteria);
		   System.out.println(criteria.getCriteriaObject());
		}
		Map<String, String> existMap = new HashMap<String, String>();
		List<JSONObject> list=new ArrayList<JSONObject>();
		boolean haveList = true;
		int countN = 0;
		do {
		    Query query = new Query(criteria);
		    query = query.with(new Sort(new Order(Direction.DESC, "createdAt")));
		    query = query.limit(pageSize);
		    //query = query.skip((page - 1) * pageSize);
		    if (null != startDate) {
				query = query.addCriteria(Criteria.where("createdAt").lt(startDate));
			}else {
				query = query.addCriteria(Criteria.where("createdAt").exists(true));
			}

		   // List<Transactions> transactionsList = mongoTemplate.find(query, Transactions.class);
		    List<BasicDBObject> transactionsList = mongoTemplate.find(query, BasicDBObject.class, "transactions");
			if(null == transactionsList || transactionsList.isEmpty()) {
				haveList = false;
				break;
			}
			startDate = transactionsList.get(transactionsList.size()-1).getDate("createdAt");
			for (BasicDBObject thisBasicDBObject :transactionsList) {
				String transactionId=thisBasicDBObject.getString("trx_id");
				if (existMap.containsKey(transactionId)) {
					continue;
				}
				String blockNum=thisBasicDBObject.getString("block_num");
				if(blockNum==null || blockNum.isEmpty()) {
					Date time=thisBasicDBObject.getDate("createdAt");
					Date newDate=new Date();
					if(newDate.getTime()-time.getTime()>10*60*1000) {
						continue;
					}
					Query queryBlockNum = new Query(Criteria.where("trx_id").is(transactionId));
					queryBlockNum = queryBlockNum.addCriteria(Criteria.where("block_id").exists(true));
					queryBlockNum = queryBlockNum.with(new Sort(new Order(Direction.DESC, "updatedAt")));
					queryBlockNum = queryBlockNum.limit(1);
					List<BasicDBObject> existTransactionsList = mongoTemplate.find(queryBlockNum, BasicDBObject.class, "transactions");
					if (null != existTransactionsList && !existTransactionsList.isEmpty()) {
						thisBasicDBObject=existTransactionsList.get(0);
						blockNum=thisBasicDBObject.getString("block_num");
					}
				}
				String type="";
				String to="";
				String from="";
				String quantity="0.0";
				String memo="";
				String description="";
				String code_new="";
				String _id=thisBasicDBObject.getString("_id");
				Date  blockTime=thisBasicDBObject.getDate("createdAt");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	        	Long times = 0l;
				BigDecimal price = BigDecimal.ZERO;
				try {
					times = sdf.parse(sdf.format(blockTime)).getTime();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				price = this.getRamPriceByTimes(times);
				BasicDBList actions = (BasicDBList) thisBasicDBObject.get("actions");
				Object[] thisActions = actions.toArray();
				for(Object thisAction : thisActions) {
					BasicDBObject action = (BasicDBObject)thisAction;
					BasicDBObject data = (BasicDBObject) action.get("data");
					String actionName = action.getString("name");
					 if(actionName.equalsIgnoreCase("transfer")) {
			            	description="转账";
			            	to=data.getString("to");
			            	from=data.getString("from");
			            	if(from.equals(actor)) {
			 			    	type="转出";
			 			    }
			 			    if(to.equals(actor)) {
			 			    	type="转入";
			 			    }
			            	quantity=data.getString("quantity");
			             	String[] quantity_arra= quantity.split(" ");
			             	quantity=quantity_arra[0];
			             	code_new=quantity_arra[1];
			            	memo=data.getString("memo");
			            }else if(actionName.equalsIgnoreCase("newaccount")) {
			            	memo="";
			            	from=data.getString("creator");
			            	to=data.getString("name");
			            	type="创建账号";
			            	description="创建账号";
			            	quantity="0.0";
			            }else if(actionName.equalsIgnoreCase("delegatebw")) {
			            	memo="";
			            	from=data.getString("from");
			            	to=data.getString("receiver");
			            	String stake_net_quantity=data.getString("stake_net_quantity");
			            	String[] stake_net_quantity_array= stake_net_quantity.split(" ");
			            	String stake_cpu_quantity=data.getString("stake_cpu_quantity");
			            	String[] stake_cpu_quantity_array= stake_cpu_quantity.split(" ");
			            	BigDecimal netQuantity= new  BigDecimal(stake_net_quantity_array[0].trim());
			            	BigDecimal cpuQuantity= new  BigDecimal(stake_cpu_quantity_array[0].trim());
			            	BigDecimal quantitys = null; 
			            	code_new=stake_cpu_quantity_array[1];
			            	type="转出";
			            	description="抵押";
			            	quantitys=netQuantity.add(cpuQuantity);
			            	quantity=quantitys.toString();
			            }else if(actionName.equalsIgnoreCase("undelegatebw")) {
			            	memo="";
			            	from=data.getString("from");
			            	to=data.getString("receiver");
			            	String unstake_net_quantity=data.getString("unstake_net_quantity");
			            	String[] unstake_net_quantity_array= unstake_net_quantity.split(" ");
			            	String unstake_cpu_quantity=data.getString("unstake_cpu_quantity");
			            	String[] unstake_cpu_quantity_array= unstake_cpu_quantity.split(" ");
			            	BigDecimal netQuantity= new  BigDecimal(unstake_net_quantity_array[0]);
			            	BigDecimal cpuQuantity= new  BigDecimal(unstake_cpu_quantity_array[0]);
			            	code_new=unstake_cpu_quantity_array[1];
			            	BigDecimal quantitys = null; 
			            	type="转入";
		            		description="赎回";
		            		quantitys=netQuantity.add(cpuQuantity);
			            	quantity=quantitys.toString();
			            }else if(actionName.equalsIgnoreCase("buyram")) {
			            	description="内存购买";
			            	memo="";
			            	type="转出";
			            	from=data.getString("payer");
			            	to=data.getString("receiver");
			            	quantity=data.getString("quant");
			            	String[] quantity_arra= quantity.split(" ");
			             	quantity=quantity_arra[0];
			             	code_new=quantity_arra[1];
			            }else if(actionName.equalsIgnoreCase("sellram")) {
			            	description="内存出售";
			            	memo="";
			            	type="转入";
			            	to="";
			            	from=data.getString("account");
			            	Long bytes = data.getLong("bytes");
							BigDecimal bytesK = BigDecimal.valueOf(bytes).divide(BigDecimal.valueOf(1024l), 2, BigDecimal.ROUND_HALF_UP);
							BigDecimal eos_qty = bytesK.multiply(price);
							eos_qty.setScale(4, BigDecimal.ROUND_HALF_UP);
							quantity=eos_qty.toString();
							code_new=code;
			            }else if(actionName.equalsIgnoreCase("issue")) {
			            	description="发行";
			            	memo="";
			            	type="转入";
			            	to=data.getString("to").trim();
			            	if(!actor.equals(to)) {
			            		continue;
			            	}
			            	from="";
			            	quantity=data.getString("quantity");
			            	String[] quantity_arra= quantity.split(" ");
			             	quantity=quantity_arra[0];
			             	code_new=quantity_arra[1];
			            }else {
			            	continue;
						}
						    JSONObject jsonObjects = new JSONObject();
						    jsonObjects.put("_id", _id);
						    jsonObjects.put("quantity", quantity+code_new);//code_new是单位如EOS,MSP	
							jsonObjects.put("description", description);
							jsonObjects.put("memo",memo);
							jsonObjects.put("from", from);
							jsonObjects.put("blockNum", blockNum);
							jsonObjects.put("blockTime", sdf.format(blockTime));
							jsonObjects.put("to", to);
							jsonObjects.put("type", type);
							jsonObjects.put("transactionId", transactionId);
							if (null == code || code.isEmpty()) {
							jsonObjects.put("code", code_new);	
							}else {
							jsonObjects.put("code", code);		
							}
							existMap.put(transactionId, transactionId);
					        list.add(jsonObjects);
					    	countN++;
							if(countN == pageSize) {
								existMap.clear();
								return list;
							}
				}
		    }
        } while (haveList);
		existMap.clear();
		return list;
	}

	@Override
	public List<Transactions> findAccountCoins(String account, String actor) {
		try {
			Criteria  accountCriteria = Criteria.where("actions.account").is(actor);//合约账号
			Criteria[] actorCriterias = new Criteria[3];
			actorCriterias[0] = Criteria.where("actions.authorization.actor").is(account);
			actorCriterias[1] = Criteria.where("actions.data.receiver").is(account);
			actorCriterias[2] = Criteria.where("actions.data.to").is(account);
			Criteria actorCriteria = new Criteria();
			actorCriteria.orOperator(actorCriterias);
			Criteria criteria = new Criteria();
			criteria.andOperator(actorCriteria,accountCriteria);
			System.out.println(criteria);
			Query query = new Query(criteria);
		    query = query.limit(1);
		    List<Transactions> transactionsList = mongoTemplate.find(query, Transactions.class);
		    return transactionsList;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
}
