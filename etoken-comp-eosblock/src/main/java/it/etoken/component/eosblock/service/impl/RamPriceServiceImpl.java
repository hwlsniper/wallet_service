package it.etoken.component.eosblock.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import it.etoken.base.common.exception.MLCommonException;
import it.etoken.base.common.exception.MLException;
import it.etoken.base.common.utils.DateUtils;
import it.etoken.base.common.utils.HttpClientUtils;
import it.etoken.base.model.eosblock.entity.RamTradeLog;
import it.etoken.cache.service.CacheService;
import it.etoken.component.eosblock.mongo.model.RamPriceInfo;
import it.etoken.component.eosblock.mongo.model.Transactions;
import it.etoken.component.eosblock.service.RamPriceService;

@Component
@Transactional
public class RamPriceServiceImpl implements RamPriceService {
	private final int[] lines_hour = new int[] {1, 2, 6, 24, 48};
	private final long BIG_BILLS_AMMOUNT = 2000;
	private final long MID_BILLS_AMMOUNT = 500;
	
	@Value("${nodeos.path.chain}")
	String URL_CHAIN;
	@Value("${nodeos.path.chain.backup}")
	String URL_CHAIN_BACKUP;
	
	@Autowired
	MongoOperations mongoTemplate;

	@Autowired
	CacheService cacheService;

	@Override
	public JSONObject getRamInfo() throws MLException {
		// 获取内存信息
		JSONObject result = this.getRammarketInfoAndPrice();
		// 获取全局信息
		JSONObject globalInfo = this.getGlobalInfo();

		// 总资金
		BigDecimal total_ram_stake = globalInfo.getJSONArray("rows").getJSONObject(0).getBigDecimal("total_ram_stake");
		total_ram_stake = total_ram_stake.divide(BigDecimal.valueOf(10000), 4, BigDecimal.ROUND_HALF_UP);
		result.put("total_eos", total_ram_stake.doubleValue());

		// 已占用内存
		BigDecimal total_ram_bytes_reserved = globalInfo.getJSONArray("rows").getJSONObject(0)
				.getBigDecimal("total_ram_bytes_reserved");
		total_ram_bytes_reserved = total_ram_bytes_reserved.divide(BigDecimal.valueOf(1024 * 1024 * 1024), 2,
				BigDecimal.ROUND_HALF_UP);
		result.put("usage_ram", total_ram_bytes_reserved.doubleValue());

		// 总发行内存
		BigDecimal max_ram_size = globalInfo.getJSONArray("rows").getJSONObject(0).getBigDecimal("max_ram_size");
		max_ram_size = max_ram_size.divide(BigDecimal.valueOf(1024 * 1024 * 1024), 2, BigDecimal.ROUND_HALF_UP);
		result.put("max_ram", max_ram_size.doubleValue());

		// 已占用内存百分比
		BigDecimal usage_ram_percent = total_ram_bytes_reserved.divide(max_ram_size, 4, BigDecimal.ROUND_HALF_UP);
		result.put("usage_ram_percent", usage_ram_percent.doubleValue());

		Long chineseTimes = DateUtils.getUtcTimes()+8*60*60*1000;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Date localDate = new Date(chineseTimes);

		long open_record_date = 0;
		try {
			open_record_date = sdf.parse(sdf.format(localDate)).getTime()-8*60*60*1000;
		} catch (Exception e) {

		}

		// 取零点
		DBObject dbObject = new BasicDBObject();
		dbObject.put("record_date", open_record_date);

		Query query = new Query(Criteria.where("record_date").is(open_record_date));
		
		List<RamPriceInfo> openRamPriceInfos = mongoTemplate.find(query, RamPriceInfo.class);
		BigDecimal open = BigDecimal.valueOf(0.19147);
		if(null !=openRamPriceInfos && !openRamPriceInfos.isEmpty()) {
			BasicDBObject openRamPriceInfo = openRamPriceInfos.get(0);
			if (null != openRamPriceInfo) {
				String temPrice = openRamPriceInfo.getString("price");
				if (null != temPrice && !temPrice.equalsIgnoreCase("0")) {
					Double temPriceDouble = Double.parseDouble(temPrice);
					open = BigDecimal.valueOf(temPriceDouble);
				}
			}
		}
		

		long utcTimes = DateUtils.getUtcTimes();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		long times = 0;
		try {
			times = sf.parse(sf.format(new Date(utcTimes))).getTime();
		} catch (Exception e) {

		}
		
		BigDecimal price = new BigDecimal(result.getString("price"));
		
		RamPriceInfo ramPriceInfo = new RamPriceInfo();
		ramPriceInfo.put("record_date", times);
		ramPriceInfo.put("price", price.doubleValue());
		ramPriceInfo.put("total_eos", result.getDouble("total_eos"));
		ramPriceInfo.put("open", open.doubleValue());
		ramPriceInfo.put("usage_ram", result.getDouble("usage_ram"));
		ramPriceInfo.put("usage_ram_percent", result.getDouble("usage_ram_percent"));
		ramPriceInfo.put("total_ram", result.getDouble("max_ram"));

		BigDecimal increase = BigDecimal.valueOf(ramPriceInfo.getDouble("price") - ramPriceInfo.getDouble("open"))
				.divide(BigDecimal.valueOf(ramPriceInfo.getDouble("open")), 4, BigDecimal.ROUND_HALF_UP);
		ramPriceInfo.put("increase", increase.doubleValue());
		
		//获取交易量
		JSONObject tradingVolumeResult = this.getTradingVolumeByTimes(price);
		
		ramPriceInfo.put("trading_volum", tradingVolumeResult.getDouble("trading_volum"));
		ramPriceInfo.put("buy_volum", tradingVolumeResult.getDouble("buy_volum"));
		ramPriceInfo.put("sell_volum", tradingVolumeResult.getDouble("sell_volum"));
		ramPriceInfo.put("trading_eos", tradingVolumeResult.getDouble("trading_eos"));
		ramPriceInfo.put("buy_eos", tradingVolumeResult.getDouble("buy_eos"));
		ramPriceInfo.put("sell_eos", tradingVolumeResult.getDouble("sell_eos"));

		
		Query queryExists = new Query(Criteria.where("record_date").is(times));
		BasicDBObject existsRamPriceInfo = mongoTemplate.findOne(queryExists, BasicDBObject.class, "ram_price");
		if(existsRamPriceInfo == null) {
			mongoTemplate.save(ramPriceInfo);
			cacheService.set("ram_price_info", ramPriceInfo);
		}

		return result;
	}

	@Override
	public JSONObject getRammarketInfoAndPrice() {
		// 首先获取usage_ram 已使用内存total_ram 总内存 total_eos 总共抵押的eos （总资金）
		JSONObject result = new JSONObject();
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("json", true);
			jsonObject.put("code", "eosio");
			jsonObject.put("scope", "eosio");
			jsonObject.put("table", "rammarket");
			String resultStr = HttpClientUtils.doPostJson(URL_CHAIN + "get_table_rows",
					jsonObject.toString());
			if(null == resultStr || resultStr.isEmpty()) {
				resultStr = HttpClientUtils.doPostJson(URL_CHAIN_BACKUP + "get_table_rows",
						jsonObject.toString());
			}

			JSONObject jo = JSONObject.parseObject(resultStr);
			String baseBalanceStr = jo.getJSONArray("rows").getJSONObject(0).getJSONObject("base").getString("balance");
			baseBalanceStr = baseBalanceStr.replace("RAM", "").trim();
			BigDecimal baseBalance = new BigDecimal(baseBalanceStr);
			baseBalance = baseBalance.divide(BigDecimal.valueOf(1024), 4, BigDecimal.ROUND_HALF_UP);

			String quoteBalanceStr = jo.getJSONArray("rows").getJSONObject(0).getJSONObject("quote")
					.getString("balance");
			quoteBalanceStr = quoteBalanceStr.replace("EOS", "").trim();
			BigDecimal quoteBalance = new BigDecimal(quoteBalanceStr);

			BigDecimal price = quoteBalance.divide(baseBalance, 5, BigDecimal.ROUND_HALF_UP);

			result.put("baseBalance", baseBalance);
			result.put("quoteBalance", quoteBalance);
			result.put("price", price);
			return result;
		} catch (Exception e2) {
			e2.printStackTrace();
			throw new MLException(MLCommonException.system_err);
		}
	}
	
	@Override
	public JSONObject getTradingVolumeByTimes(BigDecimal price){
		BigDecimal buytradingVolum = BigDecimal.ZERO;
		BigDecimal selltradingVolum = BigDecimal.ZERO;
		BigDecimal tradingVolum =  BigDecimal.ZERO;
		
		BigDecimal buytradingEos = BigDecimal.ZERO;
		BigDecimal selltradingEos = BigDecimal.ZERO;
		BigDecimal tradingEos =  BigDecimal.ZERO;
		
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		long timesx = 0;
		try {
			timesx = sf.parse(sf.format(new Date())).getTime();
		} catch (Exception e) {

		}
		long startTimes = timesx - 1*60*1000;
		Date startDate = new Date(startTimes);
		Date endDate = new Date(timesx);
		
		Object[] actionsNames = new Object[] { "buyram", "sellram" };
		Query query = new Query(Criteria.where("actions.name").in(actionsNames).and("block_id").exists(false));
		Criteria createDateCriteria = new Criteria();
		createDateCriteria.andOperator(
				Criteria.where("createdAt").exists(true),
				Criteria.where("createdAt").gte(startDate),
				Criteria.where("createdAt").lt(endDate)
				);
		query.addCriteria(createDateCriteria);
		List<BasicDBObject> transactionsList = mongoTemplate.find(query, BasicDBObject.class, "transactions");
		
		Map<String, String> existMap = new HashMap<String, String>();
		for (BasicDBObject thisBasicDBObject : transactionsList) {
			BasicDBList actions = (BasicDBList) thisBasicDBObject.get("actions");
			String trx_id = thisBasicDBObject.getString("trx_id");
			if(existMap.containsKey(trx_id)) {
				continue;
			}
			Object[] thisActions = actions.toArray();
			for (Object thisAction : thisActions) {
				BasicDBObject action = (BasicDBObject) thisAction;
				String actionName = action.getString("name");
				if (!actionName.equalsIgnoreCase("sellram") && !actionName.equalsIgnoreCase("buyram")) {
					continue;
				}

				BasicDBObject data = (BasicDBObject) action.get("data");

				if (actionName.equalsIgnoreCase("buyram")) {
					Double quant = Double.parseDouble(data.getString("quant").replace("EOS", "").trim());

					BigDecimal ram_qty = BigDecimal.ZERO;
					if (price.compareTo(BigDecimal.ZERO) != 0) {
						ram_qty = BigDecimal.valueOf(quant).divide(price, 2, BigDecimal.ROUND_HALF_UP);
					}
					buytradingVolum = buytradingVolum.add(ram_qty);
					buytradingEos = buytradingEos.add(BigDecimal.valueOf(quant));
				} else if (actionName.equalsIgnoreCase("sellram")) {
					Long bytes = data.getLong("bytes");
					BigDecimal bytesK = BigDecimal.valueOf(bytes).divide(BigDecimal.valueOf(1024l), 2,
							BigDecimal.ROUND_HALF_UP);
					BigDecimal eos_qty = bytesK.multiply(price);
					eos_qty = eos_qty.setScale(4, BigDecimal.ROUND_HALF_UP);
					selltradingVolum = selltradingVolum.add(bytesK);
					selltradingEos = selltradingEos.add(eos_qty);
				}
			}
			existMap.put(trx_id, trx_id);
		}
		
		tradingVolum = buytradingVolum.add(selltradingVolum);
		tradingEos = buytradingEos.add(selltradingEos);
		
		JSONObject result = new JSONObject();
		result.put("trading_volum", tradingVolum);
		result.put("buy_volum", buytradingVolum);
		result.put("sell_volum", selltradingVolum);
		
		result.put("trading_eos", tradingEos);
		result.put("buy_eos", buytradingEos);
		result.put("sell_eos", selltradingEos);
		
		return result;
	}

	public JSONObject getGlobalInfo() {
		JSONObject jsonObject = new JSONObject();
		JSONObject result = null;
		try {
			jsonObject.put("json", true);
			jsonObject.put("code", "eosio");
			jsonObject.put("scope", "eosio");
			jsonObject.put("table", "global");
			String resultStr = HttpClientUtils.doPostJson(URL_CHAIN + "get_table_rows",
					jsonObject.toString());
			if(null == resultStr || resultStr.isEmpty()) {
				resultStr = HttpClientUtils.doPostJson(URL_CHAIN_BACKUP + "get_table_rows",
						jsonObject.toString());
			}
			result = JSONObject.parseObject(resultStr);
			return result;
		} catch (Exception e2) {
			e2.printStackTrace();
			throw new MLException(MLCommonException.system_err);
		}
	}

	@Override
	public void buildLineData() throws MLException {
		long utcTimes = DateUtils.getUtcTimes();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		long times = 0;
		try {
			times = sdf.parse(sdf.format(new Date(utcTimes))).getTime();
		} catch (Exception e) {

		}
		
		for (int thisLineHour : this.lines_hour) {
			long start_time = 0;
			start_time = times - thisLineHour * 60 * 60 * 1000;

			Query query = new Query(Criteria.where("record_date").gte(start_time));
			query = query.with(new Sort(new Order(Direction.ASC, "record_date")));

			List<RamPriceInfo> ramPriceInfoList = mongoTemplate.find(query, RamPriceInfo.class);

			cacheService.set("ram_price_hours_" + thisLineHour, ramPriceInfoList);
		}
	}

	public BigDecimal getRamPriceByTimes(Long times) {
		Query query = new Query(Criteria.where("record_date").is(times));
		List<RamPriceInfo> result = mongoTemplate.find(query, RamPriceInfo.class);

		BigDecimal price = BigDecimal.ZERO;
		if (null != result && !result.isEmpty()) {
			BasicDBObject temp = result.get(0);
			String priceString = temp.getString("price");
			price = BigDecimal.valueOf(Double.parseDouble(priceString));
		}
		return price;
	}

	@Override
	public List<RamTradeLog> getNewTradeOrders() throws MLException {
		Object[] actionsNames = new Object[] { "buyram", "sellram" };
		Query query = new Query(Criteria.where("actions.name").in(actionsNames).and("createdAt").exists(true));
		int page = 1;
		int pageSize = 100;
		query = query.with(new Sort(new Order(Direction.DESC, "createdAt")));
		query = query.limit(pageSize);
		query = query.skip((page - 1) * pageSize);

		List<Transactions> transactionsList = mongoTemplate.find(query, Transactions.class);
		
		Map<String, String> existMap = new HashMap<String, String>();
		List<RamTradeLog> result = new ArrayList<RamTradeLog>();
		
		for (BasicDBObject thisBasicDBObject : transactionsList) {
			BasicDBList actions = (BasicDBList) thisBasicDBObject.get("actions");
			Object[] thisActions = actions.toArray();
			String trx_id = thisBasicDBObject.getString("trx_id");
			if(existMap.size()==20) {
				break;
			}
			if(existMap.containsKey(trx_id)) {
				continue;
			}
			String blockNum=thisBasicDBObject.getString("block_num");
			if(blockNum==null || blockNum.isEmpty()) {
				Date time=thisBasicDBObject.getDate("createdAt");
				Date newDate=new Date();
				if(newDate.getTime()-time.getTime()>10*60*1000) {
					continue;
				}
				Query queryBlockNum = new Query(Criteria.where("trx_id").is(trx_id));
				queryBlockNum = queryBlockNum.addCriteria(Criteria.where("block_id").exists(true));
				queryBlockNum = queryBlockNum.with(new Sort(new Order(Direction.DESC, "updatedAt")));
				queryBlockNum = queryBlockNum.limit(1);
				List<BasicDBObject> existTransactionsList = mongoTemplate.find(queryBlockNum, BasicDBObject.class, "transactions");
				if (null != existTransactionsList && !existTransactionsList.isEmpty()) {
					thisBasicDBObject=existTransactionsList.get(0);
				}
			}
			for (Object thisAction : thisActions) {
				BasicDBObject action = (BasicDBObject) thisAction;
				String actionName = action.getString("name");
				if (!actionName.equalsIgnoreCase("sellram") && !actionName.equalsIgnoreCase("buyram")) {
					continue;
				}

				BasicDBObject data = (BasicDBObject) action.get("data");

				Date createdAt = thisBasicDBObject.getDate("createdAt");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

				Long times = 0l;
				BigDecimal price = BigDecimal.ZERO;
				try {
					times = sdf.parse(sdf.format(createdAt)).getTime();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				price = this.getRamPriceByTimes(times);

				RamTradeLog ramTradeLog = new RamTradeLog();
				ramTradeLog.set_id(thisBasicDBObject.getString("_id"));
				ramTradeLog.setTrx_id(trx_id);
				ramTradeLog.setBlock_id(thisBasicDBObject.getString("block_id"));
				ramTradeLog.setBlock_num(thisBasicDBObject.getString("block_num"));
				
				ramTradeLog.setRecord_date(sdf2.format(createdAt));
				ramTradeLog.setAction_name(actionName);
				ramTradeLog.setPrice(price);
				if (actionName.equalsIgnoreCase("buyram")) {
					Double quant = Double.parseDouble(data.getString("quant").replace("EOS", "").trim());

					BigDecimal ram_qty = BigDecimal.ZERO;
					if(price.compareTo(BigDecimal.ZERO)!=0) {
						ram_qty = BigDecimal.valueOf(quant).divide(price, 2, BigDecimal.ROUND_HALF_UP);
					}

					ramTradeLog.setPayer(data.getString("payer"));
					ramTradeLog.setReceiver(data.getString("receiver"));
					ramTradeLog.setEos_qty(data.getString("quant"));
					ramTradeLog.setRam_qty(ram_qty + " KB");
				} else if (actionName.equalsIgnoreCase("sellram")) {
					Long bytes = data.getLong("bytes");
					BigDecimal bytesK = BigDecimal.valueOf(bytes).divide(BigDecimal.valueOf(1024l), 2,
							BigDecimal.ROUND_HALF_UP);
					BigDecimal eos_qty = bytesK.multiply(price);
					eos_qty = eos_qty.setScale(4, BigDecimal.ROUND_HALF_UP);

					ramTradeLog.setPayer(data.getString("account"));
					ramTradeLog.setReceiver(data.getString("receiver"));
					ramTradeLog.setEos_qty(eos_qty + " EOS");
					ramTradeLog.setRam_qty(bytesK + " KB");
				}
				existMap.put(trx_id, trx_id);
				result.add(ramTradeLog);
				
			}
		}
		existMap.clear();
		cacheService.set("getNewTradeOrders", result);

		return result;
	}

	@Override
	public List<RamTradeLog> getBigTradeOrders() throws MLException {
		
		Object[] actionsNames = new Object[] { "buyram", "sellram" };
		Query query = new Query(Criteria.where("actions.name").in(actionsNames).and("block_id").exists(false).and("createdAt").exists(true));
		int page = 1;
		int pageSize = 1000;

		Map<String, String> existMap = new HashMap<String, String>();
		List<RamTradeLog> result = new ArrayList<RamTradeLog>();
		do {
			query = query.with(new Sort(new Order(Direction.DESC, "createdAt")));
			query = query.limit(pageSize);
			query = query.skip((page - 1) * pageSize);

			List<Transactions> transactionsList = mongoTemplate.find(query, Transactions.class);

			for (BasicDBObject thisBasicDBObject : transactionsList) {
				BasicDBList actions = (BasicDBList) thisBasicDBObject.get("actions");
				Object[] thisActions = actions.toArray();
				String trx_id = thisBasicDBObject.getString("trx_id");
				if (existMap.size() == 20) {
					break;
				}
				if (existMap.containsKey(trx_id)) {
					continue;
				}
				String blockNum=thisBasicDBObject.getString("block_num");
				if(blockNum==null || blockNum.isEmpty()) {
					Date time=thisBasicDBObject.getDate("createdAt");
					Date newDate=new Date();
					if(newDate.getTime()-time.getTime()>10*60*1000) {
						continue;
					}
					Query queryBlockNum = new Query(Criteria.where("trx_id").is(trx_id));
					queryBlockNum = queryBlockNum.addCriteria(Criteria.where("block_id").exists(true));
					queryBlockNum = queryBlockNum.with(new Sort(new Order(Direction.DESC, "updatedAt")));
					queryBlockNum = queryBlockNum.limit(1);
					List<BasicDBObject> existTransactionsList = mongoTemplate.find(queryBlockNum, BasicDBObject.class, "transactions");
					if (null != existTransactionsList && !existTransactionsList.isEmpty()) {
						thisBasicDBObject=existTransactionsList.get(0);
					}
				}
				for (Object thisAction : thisActions) {
					BasicDBObject action = (BasicDBObject) thisAction;
					String actionName = action.getString("name");
					if (!actionName.equalsIgnoreCase("sellram") && !actionName.equalsIgnoreCase("buyram")) {
						continue;
					}

					BasicDBObject data = (BasicDBObject) action.get("data");

					Date createdAt = thisBasicDBObject.getDate("createdAt");
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

					Long times = 0l;
					BigDecimal price = BigDecimal.ZERO;
					try {
						times = sdf.parse(sdf.format(createdAt)).getTime();
					} catch (ParseException e) {
						e.printStackTrace();
					}
					price = this.getRamPriceByTimes(times);

					RamTradeLog ramTradeLog = new RamTradeLog();

					ramTradeLog.set_id(thisBasicDBObject.getString("_id"));
					ramTradeLog.setTrx_id(trx_id);
					ramTradeLog.setBlock_id(thisBasicDBObject.getString("block_id"));
					ramTradeLog.setBlock_num(thisBasicDBObject.getString("block_num"));

					ramTradeLog.setRecord_date(sdf2.format(createdAt));
					ramTradeLog.setAction_name(actionName);
					ramTradeLog.setPrice(price);
					if (actionName.equalsIgnoreCase("buyram")) {
						Double quant = Double.parseDouble(data.getString("quant").replace("EOS", "").trim());

						BigDecimal ram_qty = BigDecimal.ZERO;
						if(price.compareTo(BigDecimal.ZERO)!=0) {
							ram_qty = BigDecimal.valueOf(quant).divide(price, 2, BigDecimal.ROUND_HALF_UP);
						}

						ramTradeLog.setPayer(data.getString("payer"));
						ramTradeLog.setReceiver(data.getString("receiver"));
						ramTradeLog.setEos_qty(data.getString("quant"));
						ramTradeLog.setRam_qty(ram_qty + " KB");

						if (quant < 1000) {
							continue;
						}
					} else if (actionName.equalsIgnoreCase("sellram")) {
						Long bytes = data.getLong("bytes");
						BigDecimal bytesK = BigDecimal.valueOf(bytes).divide(BigDecimal.valueOf(1024l), 2,
								BigDecimal.ROUND_HALF_UP);
						BigDecimal eos_qty = bytesK.multiply(price);
						eos_qty = eos_qty.setScale(4, BigDecimal.ROUND_HALF_UP);

						ramTradeLog.setPayer(data.getString("account"));
						ramTradeLog.setReceiver(data.getString("receiver"));
						ramTradeLog.setEos_qty(eos_qty + " EOS");
						ramTradeLog.setRam_qty(bytesK + " KB");

						if (eos_qty.compareTo(BigDecimal.valueOf(1000)) < 0) {
							continue;
						}
					}
					existMap.put(trx_id, trx_id);
					result.add(ramTradeLog);
				}
			}
			page++;
		} while (existMap.size() < 20);

		existMap.clear();
		cacheService.set("getBigTradeOrders", result);

		return result;

	}
	
	
	public List<RamTradeLog> getNewTradeOrdersByAccountNameNew(String accountName, int pageSize, String last_id)
			throws MLException {
		Object[] actionsNames = new Object[] { "buyram", "sellram" };

		Date startDate = null;
		if (null != last_id && !last_id.isEmpty()) {
			Query query = new Query(Criteria.where("_id").is(new ObjectId(last_id)));
			List<BasicDBObject> existTransactionsList = mongoTemplate.find(query, BasicDBObject.class, "transactions");
			if (null != existTransactionsList && !existTransactionsList.isEmpty()) {
				startDate = existTransactionsList.get(0).getDate("createdAt");
			}
		}

		//Criteria[] actorCriterias = new Criteria[3];
		//actorCriterias[0] = Criteria.where("actions.authorization.actor").is(accountName);
		//actorCriterias[1] = Criteria.where("actions.data.receiver").is(accountName);
		//actorCriterias[2] = Criteria.where("actions.data.to").is(accountName);

		//Criteria actorCriteria = new Criteria();
		//actorCriteria.orOperator(actorCriterias);
		Criteria actorCriteria =Criteria.where("actions.authorization.actor").is(accountName);

		Criteria actionsNameCriteria = Criteria.where("actions.name").in(actionsNames);

		List<RamTradeLog> result = new ArrayList<RamTradeLog>();
		boolean haveList = true;
		Map<String, String> existMap = new HashMap<String, String>();
		int countN = 0;
		do {
			Query query = new Query(actorCriteria);

			query = query.addCriteria(actionsNameCriteria);
			

			query = query.with(new Sort(new Order(Direction.DESC, "createdAt")));
			query = query.limit(pageSize);
			if (null != startDate) {
				query = query.addCriteria(Criteria.where("createdAt").lt(startDate));
			}else {
				query = query.addCriteria(Criteria.where("createdAt").exists(true));
			}

			List<BasicDBObject> transactionsList = mongoTemplate.find(query, BasicDBObject.class, "transactions");
			if(null == transactionsList || transactionsList.isEmpty()) {
				haveList = false;
				break;
			}
			startDate = transactionsList.get(transactionsList.size()-1).getDate("createdAt");
			
			for (BasicDBObject thisBasicDBObject : transactionsList) {
				BasicDBList actions = (BasicDBList) thisBasicDBObject.get("actions");
				String trx_id = thisBasicDBObject.getString("trx_id");
				if(existMap.containsKey(trx_id)) {
					continue;
				}
				String blockNum=thisBasicDBObject.getString("block_num");
				if(blockNum==null || blockNum.isEmpty()) {
					Date time=thisBasicDBObject.getDate("createdAt");
					Date newDate=new Date();
					if(newDate.getTime()-time.getTime()>10*60*1000) {
						continue;
					}
					Query queryBlockNum = new Query(Criteria.where("trx_id").is(trx_id));
					queryBlockNum = queryBlockNum.addCriteria(Criteria.where("block_id").exists(true));
					queryBlockNum = queryBlockNum.with(new Sort(new Order(Direction.DESC, "updatedAt")));
					queryBlockNum = queryBlockNum.limit(1);
					List<BasicDBObject> existTransactionsList = mongoTemplate.find(queryBlockNum, BasicDBObject.class, "transactions");
					if (null != existTransactionsList && !existTransactionsList.isEmpty()) {
						thisBasicDBObject=existTransactionsList.get(0);
					}
				}
				Object[] thisActions = actions.toArray();
				for (Object thisAction : thisActions) {
					BasicDBObject action = (BasicDBObject) thisAction;
					String actionName = action.getString("name");
					if (!actionName.equalsIgnoreCase("sellram") && !actionName.equalsIgnoreCase("buyram")) {
						continue;
					}

					BasicDBObject data = (BasicDBObject) action.get("data");

					Date createdAt = thisBasicDBObject.getDate("createdAt");
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

					Long times = 0l;
					BigDecimal price = BigDecimal.ZERO;
					try {
						times = sdf.parse(sdf.format(createdAt)).getTime();
					} catch (ParseException e) {
						e.printStackTrace();
					}
					price = this.getRamPriceByTimes(times);

					RamTradeLog ramTradeLog = new RamTradeLog();
					ramTradeLog.set_id(thisBasicDBObject.getString("_id"));
					ramTradeLog.setTrx_id(trx_id);
					ramTradeLog.setBlock_id(thisBasicDBObject.getString("block_id"));
					ramTradeLog.setBlock_num(thisBasicDBObject.getString("block_num"));

					ramTradeLog.setRecord_date(sdf2.format(createdAt));
					ramTradeLog.setAction_name(actionName);
					ramTradeLog.setPrice(price);
					if (actionName.equalsIgnoreCase("buyram")) {
						Double quant = Double.parseDouble(data.getString("quant").replace("EOS", "").trim());

						BigDecimal ram_qty = BigDecimal.ZERO;
						if (price.compareTo(BigDecimal.ZERO) != 0) {
							ram_qty = BigDecimal.valueOf(quant).divide(price, 2, BigDecimal.ROUND_HALF_UP);
						}

						ramTradeLog.setPayer(data.getString("payer"));
						ramTradeLog.setReceiver(data.getString("receiver"));
						ramTradeLog.setEos_qty(data.getString("quant"));
						ramTradeLog.setRam_qty(ram_qty + " KB");
					} else if (actionName.equalsIgnoreCase("sellram")) {
						Long bytes = data.getLong("bytes");
						BigDecimal bytesK = BigDecimal.valueOf(bytes).divide(BigDecimal.valueOf(1024l), 2,
								BigDecimal.ROUND_HALF_UP);
						BigDecimal eos_qty = bytesK.multiply(price);
						eos_qty = eos_qty.setScale(4, BigDecimal.ROUND_HALF_UP);

						ramTradeLog.setPayer(data.getString("account"));
						ramTradeLog.setReceiver(data.getString("receiver"));
						ramTradeLog.setEos_qty(eos_qty + " EOS");
						ramTradeLog.setRam_qty(bytesK + " KB");
					}
					result.add(ramTradeLog);
					countN++;
					if(countN == pageSize) {
						existMap.clear();
						return result;
					}
				}
				existMap.put(trx_id, trx_id);
			}
		} while (haveList);
		existMap.clear();
		return result;
	}
	
	@Override
	@Deprecated
	public List<RamTradeLog> getNewTradeOrdersByAccountName(String accountName, int page, int pageSize) throws MLException {
		Object[] actionsNames = new Object[] { "buyram", "sellram" };
		
//		Criteria[] actorCriterias = new Criteria[3];
//		actorCriterias[0] = Criteria.where("actions.authorization.actor").is(accountName);
//		actorCriterias[1] = Criteria.where("actions.data.receiver").is(accountName);
//		actorCriterias[2] = Criteria.where("actions.data.to").is(accountName);
//		
//		Criteria actorCriteria = new Criteria();
//		actorCriteria.orOperator(actorCriterias);
		
		Criteria actorCriteria = Criteria.where("actions.authorization.actor").is(accountName);
		
		Criteria actionsNameCriteria = Criteria.where("actions.name").in(actionsNames);
		Query query = new Query(actorCriteria);
		query = query.addCriteria(actionsNameCriteria);
		query = query.addCriteria(Criteria.where("block_id").exists(true));
		query = query.addCriteria(Criteria.where("createdAt").exists(true));
		
		query = query.with(new Sort(new Order(Direction.DESC, "createdAt")));
		query = query.limit(pageSize);
		query = query.skip((page - 1) * pageSize);

		List<Transactions> transactionsList = mongoTemplate.find(query, Transactions.class);
		
		List<RamTradeLog> result = new ArrayList<RamTradeLog>();
		for (BasicDBObject thisBasicDBObject : transactionsList) {
			BasicDBList actions = (BasicDBList) thisBasicDBObject.get("actions");
			String trx_id = thisBasicDBObject.getString("trx_id");

			Object[] thisActions = actions.toArray();
			for (Object thisAction : thisActions) {
				BasicDBObject action = (BasicDBObject) thisAction;
				String actionName = action.getString("name");
				if (!actionName.equalsIgnoreCase("sellram") && !actionName.equalsIgnoreCase("buyram")) {
					continue;
				}

				BasicDBObject data = (BasicDBObject) action.get("data");

				Date createdAt = thisBasicDBObject.getDate("createdAt");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

				Long times = 0l;
				BigDecimal price = BigDecimal.ZERO;
				try {
					times = sdf.parse(sdf.format(createdAt)).getTime();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				price = this.getRamPriceByTimes(times);

				
				RamTradeLog ramTradeLog = new RamTradeLog();
				ramTradeLog.set_id(thisBasicDBObject.getString("_id"));
				ramTradeLog.setTrx_id(trx_id);
				ramTradeLog.setBlock_id(thisBasicDBObject.getString("block_id"));
				ramTradeLog.setBlock_num(thisBasicDBObject.getString("block_num"));
				
				ramTradeLog.setRecord_date(sdf2.format(createdAt));
				ramTradeLog.setAction_name(actionName);
				ramTradeLog.setPrice(price);
				if (actionName.equalsIgnoreCase("buyram")) {
					Double quant = Double.parseDouble(data.getString("quant").replace("EOS", "").trim());

					BigDecimal ram_qty = BigDecimal.ZERO;
					if(price.compareTo(BigDecimal.ZERO)!=0) {
						ram_qty = BigDecimal.valueOf(quant).divide(price, 2, BigDecimal.ROUND_HALF_UP);
					}

					ramTradeLog.setPayer(data.getString("payer"));
					ramTradeLog.setReceiver(data.getString("receiver"));
					ramTradeLog.setEos_qty(data.getString("quant"));
					ramTradeLog.setRam_qty(ram_qty + " KB");
				} else if (actionName.equalsIgnoreCase("sellram")) {
					Long bytes = data.getLong("bytes");
					BigDecimal bytesK = BigDecimal.valueOf(bytes).divide(BigDecimal.valueOf(1024l), 2,
							BigDecimal.ROUND_HALF_UP);
					BigDecimal eos_qty = bytesK.multiply(price);
					eos_qty = eos_qty.setScale(4, BigDecimal.ROUND_HALF_UP);

					ramTradeLog.setPayer(data.getString("account"));
					ramTradeLog.setReceiver(data.getString("receiver"));
					ramTradeLog.setEos_qty(eos_qty + " EOS");
					ramTradeLog.setRam_qty(bytesK + " KB");
				}
				result.add(ramTradeLog);
			}
		}
		return result;
	}
	
	public Map<String, Object> calculateAmountStatistics() {
		

		Map<String, Object> result = new HashMap<String, Object>();
		
		
		int pageSize = 2000;
		
		BigDecimal buy_big_amount = BigDecimal.ZERO;
		BigDecimal buy_mid_amount = BigDecimal.ZERO;
		BigDecimal buy_small_amount = BigDecimal.ZERO;
		BigDecimal sell_big_amount = BigDecimal.ZERO;
		BigDecimal sell_mid_amount = BigDecimal.ZERO;
		BigDecimal sell_small_amount = BigDecimal.ZERO;
		
		Object[] actionsNames = new Object[] { "buyram", "sellram" };
		
		Criteria actionsNameCriteria = Criteria.where("actions.name").in(actionsNames).and("block_id").exists(true);
		long utcTimes = DateUtils.getUtcTimes();
		long calculateTimes = utcTimes-24*60*60*1000;
		Date date = new Date(calculateTimes);
		Criteria todayCriteria = Criteria.where("createdAt").gte(date);
		
		Query query = new Query(todayCriteria);
		query = query.addCriteria(actionsNameCriteria);
		
//		Criteria myCriteria = todayCriteria;
//		myCriteria = myCriteria.andOperator(actionsNameCriteria);
		
//		Aggregation aggregation = Aggregation.newAggregation(Aggregation.unwind("actions"),
//				Aggregation.project("eos").andExpression("{ $toDouble: { $substrBytes: [ \"$actions.data.quant\", 0, 4 ] } }").as("eos"),
//				Aggregation.match(myCriteria),
//				Aggregation.group("null").sum("eos").as("myEOS"));
////		String xyz = aggregation.toString();
//		AggregationResults<BasicDBObject> outputTypeCount1 = mongoTemplate.aggregate(aggregation, "pt", BasicDBObject.class);
		
		long total = mongoTemplate.count(query, Transactions.class);
		long xcount = 1;
		BigDecimal totalPage = BigDecimal.valueOf(total).divide(BigDecimal.valueOf(pageSize), 0, BigDecimal.ROUND_CEILING);
		for(int i = 1; i <= totalPage.intValue(); i++) {
			query = query.limit(pageSize);
			query = query.skip((i - 1) * pageSize);
			
			List<Transactions> transactionsList = mongoTemplate.find(query, Transactions.class);
			for (BasicDBObject thisBasicDBObject : transactionsList) {
				BasicDBList actions = (BasicDBList) thisBasicDBObject.get("actions");
				Object[] thisActions = actions.toArray();
				for (Object thisAction : thisActions) {
					BasicDBObject action = (BasicDBObject) thisAction;
					String actionName = action.getString("name");
					if (!actionName.equalsIgnoreCase("sellram") && !actionName.equalsIgnoreCase("buyram")) {
						continue;
					}

					Date createdAt = thisBasicDBObject.getDate("createdAt");
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

				
					
					BasicDBObject data = (BasicDBObject) action.get("data");
					if(actionName.equalsIgnoreCase("buyram")) {
						Double quant = Double.parseDouble(data.getString("quant").replace("EOS", "").trim());
						BigDecimal eos_qty = BigDecimal.valueOf(quant);
						if(eos_qty.compareTo(BigDecimal.valueOf(BIG_BILLS_AMMOUNT))>=0) {
							buy_big_amount = buy_big_amount.add(eos_qty);
						}else if(eos_qty.compareTo(BigDecimal.valueOf(MID_BILLS_AMMOUNT))<0) {
							buy_small_amount = buy_small_amount.add(eos_qty);
						}else {
							buy_mid_amount = buy_mid_amount.add(eos_qty);
						}
					}else if(actionName.equalsIgnoreCase("sellram")) {
						Long times = 0l;
						BigDecimal price = BigDecimal.ZERO;
						try {
							times = sdf.parse(sdf.format(createdAt)).getTime();
						} catch (ParseException e) {
							e.printStackTrace();
						}
						price = this.getRamPriceByTimes(times);
						
						Long bytes = data.getLong("bytes");
						BigDecimal bytesK = BigDecimal.valueOf(bytes).divide(BigDecimal.valueOf(1024l), 2,
								BigDecimal.ROUND_HALF_UP);
						BigDecimal eos_qty = bytesK.multiply(price);
						eos_qty = eos_qty.setScale(4, BigDecimal.ROUND_HALF_UP);
						
						if(eos_qty.compareTo(BigDecimal.valueOf(BIG_BILLS_AMMOUNT))>=0) {
							sell_big_amount = sell_big_amount.add(eos_qty);
						}else if(eos_qty.compareTo(BigDecimal.valueOf(MID_BILLS_AMMOUNT))<0) {
							sell_small_amount = sell_small_amount.add(eos_qty);
						}else {
							sell_mid_amount = sell_mid_amount.add(eos_qty);
						}
					}
					System.out.println(xcount+" 处理中。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。"+xcount);
					xcount++;
				}
			}
		}
		
		BigDecimal buy_all_amount = buy_big_amount.add(buy_mid_amount).add(buy_small_amount);
		BigDecimal sell_all_amount = sell_big_amount.add(sell_mid_amount).add(sell_small_amount);
		BigDecimal all_amount = buy_all_amount.add(sell_all_amount);
		
		BigDecimal buy_big_amount_percent = buy_big_amount.divide(all_amount, 2, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));
		BigDecimal buy_mid_amount_percent = buy_mid_amount.divide(all_amount, 2, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));
		BigDecimal buy_small_amount_percent = buy_small_amount.divide(all_amount, 2, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));
		
		BigDecimal sell_big_amount_percent = sell_big_amount.divide(all_amount, 2, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));
		BigDecimal sell_mid_amount_percent = sell_mid_amount.divide(all_amount, 2, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));
		BigDecimal sell_small_amount_percent = BigDecimal.valueOf(100).subtract(buy_big_amount_percent).subtract(buy_mid_amount_percent)
				.subtract(buy_small_amount_percent).subtract(sell_big_amount_percent).subtract(sell_mid_amount_percent);
		
		result.put("buy_big_amount", buy_big_amount.divide(BigDecimal.valueOf(1000), 2, BigDecimal.ROUND_HALF_UP)+" k");
		result.put("buy_mid_amount", buy_mid_amount.divide(BigDecimal.valueOf(1000), 2, BigDecimal.ROUND_HALF_UP)+" k");
		result.put("buy_small_amount", buy_small_amount.divide(BigDecimal.valueOf(1000), 2, BigDecimal.ROUND_HALF_UP)+" k");
		result.put("buy_all_amount", buy_all_amount);
		
		result.put("buy_big_amount_percent", buy_big_amount_percent+"%");
		result.put("buy_mid_amount_percent", buy_mid_amount_percent+"%");
		result.put("buy_small_amount_percent", buy_small_amount_percent+"%");
		
		
		result.put("sell_big_amount", sell_big_amount.divide(BigDecimal.valueOf(1000), 2, BigDecimal.ROUND_HALF_UP)+" k");
		result.put("sell_mid_amount", sell_mid_amount.divide(BigDecimal.valueOf(1000), 2, BigDecimal.ROUND_HALF_UP)+" k");
		result.put("sell_small_amount", sell_small_amount.divide(BigDecimal.valueOf(1000), 2, BigDecimal.ROUND_HALF_UP)+" k");
		result.put("sell_all_amount", sell_all_amount);
		
		result.put("sell_big_amount_percent", sell_big_amount_percent+"%");
		result.put("sell_mid_amount_percent", sell_mid_amount_percent+"%");
		result.put("sell_small_amount_percent", sell_small_amount_percent+"%");
		
		result.put("big_amount_desc", "大单  >" + BIG_BILLS_AMMOUNT);
		result.put("mid_amount_desc", "中单 " + MID_BILLS_AMMOUNT + "-" + BIG_BILLS_AMMOUNT);
		result.put("small_amount_desc", "小单<" + MID_BILLS_AMMOUNT);
		
		cacheService.set("calculateAmountStatistics", result);
		
		return result;
	}
	
	@Override
	public BasicDBObject saveKLineData(String dateType) throws MLException {
		long utcTimes = DateUtils.getUtcTimes();
		
		long times=0l;
		long startTimes = 0;
		long endTimes = 0;
		if(dateType.contains("m")) {
			times = Long.parseLong(dateType.replace("m", "").trim())*60*1000;
			SimpleDateFormat sfm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			
			try {
				endTimes = sfm.parse(sfm.format(new Date(utcTimes))).getTime();
			} catch (Exception e) {

			}
			startTimes = endTimes-times;
		}else if(dateType.contains("h")) {
			times = Long.parseLong(dateType.replace("h", "").trim())*60*60*1000;
			SimpleDateFormat sfh = new SimpleDateFormat("yyyy-MM-dd HH");
			try {
				endTimes = sfh.parse(sfh.format(new Date(utcTimes))).getTime();
			} catch (Exception e) {

			}
			startTimes = endTimes-times;
		}else if(dateType.contains("d")) {
			times = Long.parseLong(dateType.replace("d", "").trim())*24*60*60*1000;
			SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd");
			try {
				endTimes = sfd.parse(sfd.format(new Date(utcTimes))).getTime();
			} catch (Exception e) {

			}
			startTimes = endTimes-times;
		}else if(dateType.contains("w")) {
			times = Long.parseLong(dateType.replace("w", "").trim())*7*24*60*60*1000;
			SimpleDateFormat sfM = new SimpleDateFormat("yyyy-MM-dd");
			try {
				endTimes = sfM.parse(sfM.format(new Date(utcTimes))).getTime();
			} catch (Exception e) {

			}
			startTimes = endTimes-times;
		}else if(dateType.contains("M")) {
			times = Long.parseLong(dateType.replace("M", "").trim())*30*24*60*60*1000;
			SimpleDateFormat sfM = new SimpleDateFormat("yyyy-MM");
			try {
				endTimes = sfM.parse(sfM.format(new Date(utcTimes))).getTime();
			} catch (Exception e) {

			}
			startTimes = endTimes-times;
		}
		
		
		Criteria recordDateCriteria = new Criteria();
		recordDateCriteria.andOperator(
				Criteria.where("record_date").gte(startTimes),
				Criteria.where("record_date").lt(endTimes)
				);
		Aggregation aggregation = Aggregation.newAggregation(
        		Aggregation.match(recordDateCriteria),
        		Aggregation.group("null")
        		.min("record_date").as("record_date")
        		.max("record_date").as("record_date_max")
        		.max("price").as("max")
        		.min("price").as("min")
        		.first("price").as("close")
        		.last("price").as("open")
        		.sum("trading_volum").as("volum")
        		);
		AggregationResults<BasicDBObject> output = mongoTemplate.aggregate(aggregation, "ram_price", BasicDBObject.class);
		
		BasicDBObject result = output.getMappedResults().get(0);
		
		BigDecimal record_date = new BigDecimal(result.getString("record_date"));
		BigDecimal record_date_max = new BigDecimal(result.getString("record_date_max"));
		
		BigDecimal open = this.getRamPriceByTimes(record_date.longValue());
		BigDecimal close = this.getRamPriceByTimes(record_date_max.longValue());
		
		BigDecimal max = new BigDecimal(result.getString("max"));
		BigDecimal min = new BigDecimal(result.getString("min"));
		BigDecimal volum = new BigDecimal(result.getString("volum"));
		
		BigDecimal increase = close.subtract(open).divide(open, 4, BigDecimal.ROUND_HALF_UP) ;
		BigDecimal amplitude = max.subtract(min).divide(open, 4, BigDecimal.ROUND_HALF_UP) ;
		
		result.put("max", max.doubleValue());
		result.put("min", min.doubleValue());
		result.put("close", close.doubleValue());
		result.put("open", open.doubleValue());
		result.put("volum", volum.doubleValue());
		
		result.put("increase", increase.doubleValue());
		result.put("amplitude", amplitude.doubleValue());
		
		Query queryExists = new Query(Criteria.where("record_date").is(startTimes));
		BasicDBObject existsKline = mongoTemplate.findOne(queryExists, BasicDBObject.class, "ram_price_kline_"+dateType);
		if(existsKline == null) {
			mongoTemplate.save(result, "ram_price_kline_"+dateType);
		}
		
		return result;
	}
	
	@Override
	public List<JSONObject> getRamKLines(String dateType, int pageSize) {
		if(dateType.equals("1d")||dateType.equals("7d")||dateType.equals("1w")) {
			dateType="1h";
		}
		if(dateType.equals("1M")){
			dateType="2h";
		}
		Query query = new Query();
		query = query.with(new Sort(new Order(Direction.DESC, "record_date")));
		query = query.limit(pageSize);
		List<JSONObject> result = mongoTemplate.find(query, JSONObject.class, "ram_price_kline_"+dateType);
		Collections.sort(result, new Comparator<Object>(){
            /*
             * 返回负数表示：p1 小于p2，
             * 返回0 表示：p1和p2相等，
             * 返回正数表示：p1大于p2
             */
            public int compare(Object p1, Object p2) {
            	JSONObject jo1 = JSONObject.parseObject(p1.toString());
            	JSONObject jo2 = JSONObject.parseObject(p2.toString());
            	Long xx1 = jo1.getLong("record_date");
            	Long xx2 = jo2.getLong("record_date");
                //按照时间戳进行升序排列
                if(xx1 > xx2){
                    return 1;
                }
                if(xx1 == xx2){
                    return 0;
                }
                return -1;
            }
        });
		//获取不满一个周期的数据
		if(result.size()>0) {
			Long startDateTimes = result.get(result.size()-1).getLong("record_date_max");
			JSONObject latest = this.getLatestInfo(startDateTimes);
			if(latest != null) {
				result.add(latest);
			}
		}		
		return result;
	}

	@Override
	public List<BasicDBObject> getRamHourLines(long start_date, long end_date) {
		Query query = new Query();
		Criteria recordDateCriteria = new Criteria();
		recordDateCriteria.andOperator(Criteria.where("record_date").gte(start_date), Criteria.where("record_date").lt(end_date));
		query = query.addCriteria(recordDateCriteria);
		query = query.with(new Sort(new Order(Direction.DESC, "record_date")));
		List<BasicDBObject> result = mongoTemplate.find(query, BasicDBObject.class, "ram_price");
		return result;
	}
	
	
	@Override
	public JSONObject getLatestInfo(Long startDateTimes) {
		Criteria recordDateCriteria = Criteria.where("record_date").gt(startDateTimes);
		Aggregation aggregation = Aggregation.newAggregation(
				Aggregation.match(recordDateCriteria),
				Aggregation.group("null").sum("trading_volum").as("volum")
				.max("price").as("max")
				.min("price").as("min")
				.min("record_date").as("record_date")
				.max("record_date").as("record_date_max")
				);
		AggregationResults<BasicDBObject> output = mongoTemplate.aggregate(aggregation, "ram_price",
				BasicDBObject.class);
		

		BigDecimal volum = BigDecimal.ZERO;
		BigDecimal max = BigDecimal.ZERO;
		BigDecimal min = BigDecimal.ZERO;
		BigDecimal open = BigDecimal.ZERO;
		BigDecimal close = BigDecimal.ZERO;
		BigDecimal increase = BigDecimal.ZERO;
		BigDecimal amplitude = BigDecimal.ZERO;
		
		if(output.getMappedResults().size()==0) {
			return null;
		}
		BasicDBObject resultBasic = output.getMappedResults().get(0);
		
		BigDecimal record_date = new BigDecimal(resultBasic.getString("record_date"));
		BigDecimal record_date_max = new BigDecimal(resultBasic.getString("record_date_max"));

		open =this.getPriceByCodeAndTimes(record_date.longValue());
		close = this.getPriceByCodeAndTimes(record_date_max.longValue());
		
		max = new BigDecimal(resultBasic.getString("max"));
		min = new BigDecimal(resultBasic.getString("min"));
		volum = new BigDecimal(resultBasic.getString("volum"));
		
		if(open.compareTo(BigDecimal.ZERO)!=0) {
			increase = close.subtract(open).divide(open, 5, BigDecimal.ROUND_HALF_UP);
			amplitude = max.subtract(min).divide(open, 5, BigDecimal.ROUND_HALF_UP);
		}
			
		JSONObject result = new JSONObject();
		result.put("record_date", record_date.longValue());
		result.put("record_date_max", record_date_max.longValue());
		result.put("volum", volum.doubleValue());
		result.put("max", max.doubleValue());
		result.put("min", min.doubleValue());
		result.put("open", open.doubleValue());
		result.put("close", close.doubleValue());
		result.put("increase", increase.doubleValue());
		result.put("amplitude", amplitude.doubleValue());
		return result;
	}
	
	public BigDecimal getPriceByCodeAndTimes(Long times) {
		String collection_name = "ram_price";
		Query query = new Query(Criteria.where("record_date").is(times));
		BasicDBObject result = mongoTemplate.findOne(query, BasicDBObject.class, collection_name);

		BigDecimal price = BigDecimal.ZERO;
		if(result != null) {
			String priceString = result.getString("price");
			price = BigDecimal.valueOf(Double.parseDouble(priceString));
		}	
		return price;
	}
}
	
