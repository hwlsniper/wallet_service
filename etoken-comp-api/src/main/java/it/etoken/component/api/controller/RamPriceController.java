package it.etoken.component.api.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;

import it.etoken.base.common.result.MLResultList;
import it.etoken.base.common.utils.DateUtils;
import it.etoken.base.model.eosblock.entity.RamTradeLog;
import it.etoken.cache.service.CacheService;
import it.etoken.component.api.exception.MLApiException;
import it.etoken.component.eosblock.mongo.model.RamLargeRank;
import it.etoken.component.eosblock.mongo.model.RamPriceInfo;
import it.etoken.componet.eosblock.facade.RamLargeRankFacadeAPI;
import it.etoken.componet.eosblock.facade.RamPriceFacadeAPI;

@Controller
@RequestMapping(value = "/ramprice")
public class RamPriceController extends BaseController {

	private final static Logger logger = LoggerFactory.getLogger(BaseController.class);
	@Autowired
	CacheService cacheService;

	@Reference(version = "1.0.0", timeout = 30000, retries = 3)
	RamPriceFacadeAPI ramPriceFacadeAPI;
	
	@Reference(version = "1.0.0", timeout = 30000, retries = 3)
	RamLargeRankFacadeAPI ramLargeRankFacadeAPI;

	@ResponseBody
	@RequestMapping(value = "/ramPriceInfo")
	public Object findRamPriceInfo(HttpServletRequest request) {
		try {
			RamPriceInfo ram_price_info = cacheService.get("ram_price_info",RamPriceInfo.class);
			 return this.success(ram_price_info);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error(MLApiException.SYS_ERROR, null);
		}
	}
	

	@SuppressWarnings("unchecked")

	@ResponseBody
	@RequestMapping(value = "/line/{type}")
	public Object findRamPriceTwoHour(@PathVariable Integer type,@RequestParam Map<String, String> requestMap, HttpServletRequest request) {
		logger.info("/line/"+type+" request map : " + requestMap);
		try {
				if(type==null) {
					type=2;
				}
				Map<String,List> data = new HashMap<>();
				List<Double> txs = new ArrayList<>();
				List<Double> ps = new ArrayList<>();
				List<String> x = new ArrayList<>();
				data.put("txs", txs);//交易量
				data.put("ps", ps);//价格
				data.put("x", x);//时间
				List list = cacheService.get("ram_price_hours_"+type,List.class);
				Collections.sort(list, new Comparator<Object>(){
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
				for (Object object : list) {
					JSONObject jo = JSONObject.parseObject(object.toString());
					ps.add(jo.getDouble("price"));
					if(jo.getDouble("trading_volum")==null) {
						txs.add(0.0);
					}else {
						txs.add(jo.getDouble("trading_volum"));
					}
					Long xx = jo.getLong("record_date")+8*60*60*1000;
					if(type==2 || type==6|| type==24){
						SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
						x.add(sf.format(xx));
					}else{
						SimpleDateFormat df = new SimpleDateFormat("MM-dd");
						x.add(df.format(xx));
					}
				}
				return this.success(data);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error(MLApiException.SYS_ERROR, null);
		}
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/getNewTradeOrders")
	public Object getNewTradeOrders(HttpServletRequest request) {
		logger.info("getNewTradeOrders");
		try {
			@SuppressWarnings("unchecked")
			List<RamTradeLog> list = cacheService.get("getNewTradeOrders", List.class);
			if(null == list || list.isEmpty()) {
				MLResultList<RamTradeLog> result = ramPriceFacadeAPI.getNewTradeOrders();
				if(result.isSuccess()) {
					list = result.getList();
				}else {
					return this.error(result.getErrorCode(),result.getErrorHint(), null);
				}
			}
			if(null != list && !list.isEmpty()) {
				return this.success(list);
			}else {
				return this.error(MLApiException.SYS_ERROR, null);
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error(MLApiException.SYS_ERROR, null);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/getBigTradeOrders")
	public Object getBigTradeOrders(HttpServletRequest request) {
		logger.info("getBigTradeOrders");
		try {
			@SuppressWarnings("unchecked")
			List<RamTradeLog> list = cacheService.get("getBigTradeOrders", List.class);
			if(null == list || list.isEmpty()) {
				MLResultList<RamTradeLog> result = ramPriceFacadeAPI.getBigTradeOrders();
				if(result.isSuccess()) {
					list = result.getList();
				}else {
					return this.error(result.getErrorCode(),result.getErrorHint(), null);
				}
			}
			if(null != list && !list.isEmpty()) {
				return this.success(list);
			}else {
				return this.error(MLApiException.SYS_ERROR, null);
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error(MLApiException.SYS_ERROR, null);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/getNewTradeOrdersByAccountName")
	public Object getNewTradeOrdersByAccountName(@RequestBody Map<String, String> requestMap, HttpServletRequest request) {
		logger.info("getBigTradeOrders");
		String account_name = requestMap.get("account_name");
		if(null == account_name || account_name.isEmpty()) {
			return this.error(MLApiException.PARAM_ERROR, null);
		}
		
		int page = 1;
		if(null != requestMap.get("page") && !requestMap.get("page").isEmpty()) {
			page = Integer.parseInt(requestMap.get("page"));
		}
		
		int pageCount = 100;
		if(null != requestMap.get("pageCount") && !requestMap.get("pageCount").isEmpty()) {
			pageCount = Integer.parseInt(requestMap.get("pageCount"));
		}
		
		String last_id = "";
		if(null != requestMap.get("last_id") && !requestMap.get("last_id").isEmpty()) {
			last_id = requestMap.get("last_id");
		}
		
		try {
			MLResultList<RamTradeLog> result = null;
			if(last_id.equalsIgnoreCase("")) {
				result = ramPriceFacadeAPI.getNewTradeOrdersByAccountName(account_name, page, pageCount);
			}else {
				if(last_id.equalsIgnoreCase("-1")) {
					last_id = "";
				}
				result = ramPriceFacadeAPI.getNewTradeOrdersByAccountNameNew(account_name, pageCount, last_id);
			}
			if(result.isSuccess()) {
				List<RamTradeLog> list = result.getList();
				return this.success(list);
			}else {
				return this.error(result.getErrorCode(),result.getErrorHint(), null);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error(MLApiException.SYS_ERROR, null);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/getNewTradeOrdersByAccountName2")
	public Object getNewTradeOrdersByAccountName2(@RequestParam Map<String, String> requestMap, HttpServletRequest request) {
		logger.info("getBigTradeOrders");
		String account_name = requestMap.get("account_name");
		if(null == account_name || account_name.isEmpty()) {
			return this.error(MLApiException.PARAM_ERROR, null);
		}
		
		int page = 1;
		if(null != requestMap.get("page") && !requestMap.get("page").isEmpty()) {
			page = Integer.parseInt(requestMap.get("page"));
		}
		
		int pageCount = 100;
		if(null != requestMap.get("pageCount") && !requestMap.get("pageCount").isEmpty()) {
			pageCount = Integer.parseInt(requestMap.get("pageCount"));
		}
		
		String last_id = "";
		if(null != requestMap.get("last_id") && !requestMap.get("last_id").isEmpty()) {
			last_id = requestMap.get("last_id");
		}
		
		try {
			MLResultList<RamTradeLog> result = null;
			if(last_id.equalsIgnoreCase("")) {
				result = ramPriceFacadeAPI.getNewTradeOrdersByAccountName(account_name, page, pageCount);
			}else {
				if(last_id.equalsIgnoreCase("-1")) {
					last_id = "";
				}
				result = ramPriceFacadeAPI.getNewTradeOrdersByAccountNameNew(account_name, pageCount, last_id);
			}
			if(result.isSuccess()) {
				List<RamTradeLog> list = result.getList();
				return this.success(list);
			}else {
				return this.error(result.getErrorCode(),result.getErrorHint(), null);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error(MLApiException.SYS_ERROR, null);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/getLargeRamRank")
	public Object getLargeRamRank(HttpServletRequest request) {
		try {
			RamPriceInfo ram_price_info = cacheService.get("ram_price_info",RamPriceInfo.class);
			Object a=ram_price_info.get("total_ram");
			Double total=new Double(a.toString());
			Double total_new=total*1024*1024*1024;
			BigDecimal total_ram=new BigDecimal(total_new);
			MLResultList<RamLargeRank> result =ramLargeRankFacadeAPI.getNewestRank();
			List<JSONObject> list=new ArrayList<>();
			int i=1;
			for (BasicDBObject thisBasicDBObject : result.getList()) {
				JSONObject jsonObject=new JSONObject();
				jsonObject.put("num",i);
				i=i+1;
				jsonObject.put("account", thisBasicDBObject.get("account"));
				jsonObject.put("historyAverageCost", thisBasicDBObject.get("historyAverageCost"));
				String profit=thisBasicDBObject.getString("profit");
				Double profit_value =new BigDecimal(profit).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
				if(profit_value>0) {
					jsonObject.put("profit", "+"+profit_value.toString());
				}else {
					jsonObject.put("profit", profit_value.toString());
				}
				Double ram=thisBasicDBObject.getDouble("ramQuota")/1024/1024/1024;
				if(ram>1) {
					Double value =new BigDecimal(ram).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
					jsonObject.put("ramQuota", value.toString()+"GB");
				}else {
					Double ram_new=thisBasicDBObject.getDouble("ramQuota")/1024/1024;
					Double value =new BigDecimal(ram_new).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
					jsonObject.put("ramQuota", value.toString()+"MB");
				}
				BigDecimal ramQuota = new BigDecimal(thisBasicDBObject.getDouble("ramQuota"));
				Double per= ramQuota.divide(total_ram,4,BigDecimal.
					    ROUND_HALF_UP).doubleValue()*100;
				Double per_value =new BigDecimal(per).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
				jsonObject.put("per",per_value.toString()+"%");
				list.add(jsonObject);
			}
			return this.success(list);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error(MLApiException.SYS_ERROR, null);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/getRamHourLines")
	public Object getRamHourLines(@RequestBody Map<String, String> requestMap, HttpServletRequest request) {
		logger.info("/RamLine" + requestMap);
		try {
				String start_date = requestMap.get("start_date");
				String end_date = requestMap.get("end_date");
				if(null == start_date || start_date.isEmpty()||null == end_date || end_date.isEmpty()) {
					return this.error(MLApiException.PARAM_ERROR, null);
				}
				Map<String,List> data = new HashMap<>();
				List<Double> txs = new ArrayList<>();
				List<Double> ps = new ArrayList<>();
				List<String> x = new ArrayList<>();
				data.put("txs", txs);//交易量
				data.put("ps", ps);//价格
				data.put("x", x);//时间
				MLResultList<BasicDBObject> list=ramPriceFacadeAPI.getRamHourLines(Long.parseLong(start_date), Long.parseLong(end_date));
				Collections.sort(list.getList(), new Comparator<Object>(){
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
				for (Object object : list.getList()) {
					JSONObject jo = JSONObject.parseObject(object.toString());
					ps.add(jo.getDouble("price"));
					if(jo.getDouble("trading_volum")==null) {
						txs.add(0.0);
					}else {
						txs.add(jo.getDouble("trading_volum"));
					}
					Long xx = jo.getLong("record_date")+8*60*60*1000;
					SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
					x.add(sf.format(xx));
					
				}
				System.out.println(this.success(data));
				return this.success(data);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error(MLApiException.SYS_ERROR, null);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/getRamHourLines2")
	public Object getRamHourLines2(@RequestParam Map<String, String> requestMap, HttpServletRequest request) {
		logger.info("/RamLine" + requestMap);
		try {
				String start_date = requestMap.get("start_date");
				String end_date = requestMap.get("end_date");
				if(null == start_date || start_date.isEmpty()||null == end_date || end_date.isEmpty()) {
					return this.error(MLApiException.PARAM_ERROR, null);
				}
				Map<String,List> data = new HashMap<>();
				List<Double> txs = new ArrayList<>();
				List<Double> ps = new ArrayList<>();
				List<String> x = new ArrayList<>();
				data.put("txs", txs);//交易量
				data.put("ps", ps);//价格
				data.put("x", x);//时间
				MLResultList<BasicDBObject> list=ramPriceFacadeAPI.getRamHourLines(Long.parseLong(start_date), Long.parseLong(end_date));
				Collections.sort(list.getList(), new Comparator<Object>(){
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
				for (Object object : list.getList()) {
					JSONObject jo = JSONObject.parseObject(object.toString());
					ps.add(jo.getDouble("price"));
					if(jo.getDouble("trading_volum")==null) {
						txs.add(0.0);
					}else {
						txs.add(jo.getDouble("trading_volum"));
					}
					Long xx = jo.getLong("record_date")+8*60*60*1000;
					SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
					x.add(sf.format(xx));
					
				}
				System.out.println(this.success(data));
				return this.success(data);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error(MLApiException.SYS_ERROR, null);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/getRamKLines")
	public Object getRamKLines(@RequestBody Map<String, String> requestMap, HttpServletRequest request) {
		logger.info("/getRamKLine" + requestMap);
		try {
			    String dateType=requestMap.get("dateType");
				String pageSize=requestMap.get("pageSize");
				if(dateType==null || dateType.isEmpty()) {
					return this.error(MLApiException.PARAM_ERROR, null);
				}
				int count = 180;
				if(pageSize!=null && !pageSize.isEmpty()) {
					count = Integer.parseInt(pageSize);
				}
				MLResultList<JSONObject> result=ramPriceFacadeAPI.getRamKLines(dateType,count);
				if(result.isSuccess()) {
					return this.success(result.getList());
				}else {
					return this.error(result.getErrorCode(),result.getErrorHint(), null);
				}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error(MLApiException.SYS_ERROR, null);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/getRamKLines2")
	public Object getRamKLines2(@RequestParam Map<String, String> requestMap, HttpServletRequest request) {
		logger.info("/getRamKLine");
		try {
			    String dateType=requestMap.get("dateType");
				String pageSize=requestMap.get("pageSize");
				if(dateType==null || dateType.isEmpty()) {
					return this.error(MLApiException.PARAM_ERROR, null);
				}
				int count = 180;
				if(pageSize!=null && !pageSize.isEmpty()) {
					count = Integer.parseInt(pageSize);
				}
				MLResultList<JSONObject> result=ramPriceFacadeAPI.getRamKLines(dateType,count);
				if(result.isSuccess()) {
					return this.success(result.getList());
				}else {
					return this.error(result.getErrorCode(),result.getErrorHint(), null);
				}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error(MLApiException.SYS_ERROR, null);
		}
	}
}