package it.etoken.component.market.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;

import it.etoken.base.cache.utils.MLCacheMarket;
import it.etoken.base.common.exception.MLException;
import it.etoken.base.common.http.HttpClientService;
import it.etoken.base.common.utils.DateUtils;
import it.etoken.base.model.market.entity.Coins;
import it.etoken.base.model.market.vo.CoinTicker;
import it.etoken.cache.service.CacheService;
import it.etoken.component.market.service.CoinsService;
import it.etoken.component.market.service.MarketService;

//@Component
public class OkexServiceImpl implements MarketService {

	private final static Logger logger = LoggerFactory.getLogger(OkexServiceImpl.class);
	
	final String server="https://www.okex.com/api/v1/ticker.do?symbol=";
	
	final String server_rate = "https://www.okex.com/api/v1/exchange_rate.do";
	
	final String server_line = "https://www.okex.com/api/v1/kline.do?";
	
	final String [] lines = new String[]{"5min","1hour","6hour","1day"};
	
	DecimalFormat formatter =new DecimalFormat("#.0000");
	
	DecimalFormat formatter1 =new DecimalFormat("#");
	
	DecimalFormat formatter2 =new DecimalFormat("#.00");
	
	@Autowired
	HttpClientService httpClientService;
	
	@Autowired
	CacheService cacheService;
	
	@Autowired
	CoinsService coinsService;

	@Override
	@Async
	public void ticker(Coins coins) throws MLException {
		try{
			logger.info("==============================");
			logger.info("==============================");
			logger.info("============= "+coins.getCode()+" ticker =================");
			logger.info("==============================");
			logger.info("==============================");
			String result = httpClientService.doGet(server+coins.getCode().toLowerCase()+"_usdt");
			JSONObject jo = JSON.parseObject(result).getJSONObject("ticker");
			cacheService.set("ok_ticker_"+coins.getCode(),jo);
			
			//保存k线
			this.line(coins);
		}catch (Exception e) {
			logger.error("ticker",e);
		}
	}

	@Override
	@Async
	public void exchange() throws MLException {
		try{
			logger.info("==============================");
			logger.info("==============================");
			logger.info("============= exchange =================");
			logger.info("==============================");
			logger.info("==============================");
			logger.info("=============================== "+server_rate);
			String rate = httpClientService.doGet(server_rate);
			JSONObject jo = JSON.parseObject(rate);
			BigDecimal r = jo.getBigDecimal("rate");
			cacheService.set(MLCacheMarket.ok_rate,r);
			cacheService.set("CNY_USDT",r);
			cacheService.set("USDT_USDT",1);
		}catch (Exception e) {
			logger.error("exchange",e);
			cacheService.set(MLCacheMarket.ok_rate,6.5);
			cacheService.set("CNY_USDT",6.5);
			cacheService.set("USDT_USDT",1);
		}
		
		this.ethExchange();
	}
	
	private void ethExchange() throws MLException  {
		try{
			String result = httpClientService.doGet(server+"ETH_USDT");
			JSONObject ro = JSON.parseObject(result);
			JSONObject jo= ro.getJSONObject("ticker");
			BigDecimal price_usd  = jo.getBigDecimal("last");
			
			BigDecimal cnyUsdt= cacheService.get(MLCacheMarket.ok_rate, BigDecimal.class);
			BigDecimal price = price_usd.multiply(cnyUsdt);
			
			cacheService.set("CNY_ETH",price);
		}catch (Exception e) {
			cacheService.set("CNY_ETH",520.00);
			logger.error("exchange",e);
		}
	}

	@Override
	public List<CoinTicker> getTicker() throws MLException {
		try{
			double rate = 6.68;
			BigDecimal br = cacheService.get(MLCacheMarket.ok_rate,BigDecimal.class);
			if(br!=null && br.doubleValue()>0){
				rate = br.doubleValue();
			}
			List<CoinTicker> tikes = new ArrayList<>();
			Page<Coins> coins = coinsService.findAllBy4Market();
			for(Coins c : coins.getResult()){
				CoinTicker t = new CoinTicker();
				t.setId(c.getId());
				t.setCode(c.getCode());
				t.setImg(c.getImg());
				t.setName(c.getName());
				//凌晨价格
				BigDecimal start = cacheService.get("ok_start_"+c.getCode().toLowerCase(),BigDecimal.class);
				if(start!=null){
					t.setStart(Double.parseDouble(formatter.format(start.doubleValue()*rate)));
				}
				else {
					if(c.getCode().equalsIgnoreCase("eos")) {
						double defaultStartPrice = Double.parseDouble(formatter.format(9.13*rate));
						cacheService.set("ok_start_"+c.getCode().toLowerCase(), 9.13);
						t.setStart(defaultStartPrice);
					}
				}
				JSONObject obj = cacheService.get("ok_ticker_"+c.getCode().toLowerCase(),JSONObject.class);
				
				if(obj!=null){
					t.setMax(Double.parseDouble(formatter.format(obj.getDoubleValue("high")*rate)));
					t.setMin(Double.parseDouble(formatter.format(obj.getDoubleValue("low")*rate)));
					t.setPrice(Double.parseDouble(formatter.format(obj.getDoubleValue("last")*rate)));
					t.setUsd(Double.parseDouble(formatter.format(obj.getDoubleValue("last"))));
					t.setTxs(Double.parseDouble(formatter1.format(obj.getDoubleValue("vol"))));
					t.setValue(Double.parseDouble(formatter.format(c.getMarke()*t.getUsd())));
					if(t.getStart()==0) {
						continue;
					}
					t.setIncrease(Double.parseDouble(formatter2.format((t.getPrice()-t.getStart())/t.getStart()*100)));
				}
				tikes.add(t);
			}
			return tikes;
		}catch (Exception e) {
			logger.error("getTicker",e);
		}
		return null;
	}
	
	@Override
	public Map getLine(String code,String type) throws MLException {
		try{
			Map data = cacheService.get("ok_line_"+code.toLowerCase()+"_"+type,Map.class);
			return data;
		}catch (Exception e) {
			logger.error(e.toString());
		}
		return null;
	}


	private void line(Coins coins) throws MLException {
		try{
			for(int i=0;i<lines.length;i++){
				String type = lines[i];
				Map<String,List> data = new HashMap<>();
				List<Double> txs = new ArrayList<>();
				List<Double> ps = new ArrayList<>();
				List<String> x = new ArrayList<>();
				data.put("txs", txs);
				data.put("ps", ps);
				data.put("x", x);
				String result = httpClientService.doGet(server_line+"symbol="+coins.getCode()+"_usdt&type="+type+"&size=100");
				JSONArray js = JSONArray.parseArray(result);
				for(int j=0;j<js.size();j++){
					JSONArray a = js.getJSONArray(j);
					txs.add(Double.parseDouble(formatter.format(Double.parseDouble(String.valueOf(a.get(5))))));
					ps.add(Double.parseDouble(formatter.format(Double.parseDouble(String.valueOf(a.get(2))))));
					if(i==0 || i==1){
						x.add(DateUtils.forSecond((long)a.get(0)));
						
					}else{
						x.add(DateUtils.forDay((long)a.get(0)));
					}
				}
				cacheService.set("ok_line_"+coins.getCode().toLowerCase()+"_"+i,data);
			}
			
		}catch (Exception e) {
			logger.error("line",e);
		}
	}
	
	@Override
	@Async
	public void morningPrice(Coins coins) throws MLException {
//		{"date":"1528956741","ticker":{"high":"10.2870","vol":"19014522.8270","last":"10.1502","low":"9.1300","buy":"10.1502","sell":"10.1562"}}
		try{
			logger.info("==============================");
			logger.info("============= "+coins.getCode()+" morningPrice begin =================");
			logger.info("==============================");
			String result = httpClientService.doGet(server+coins.getCode().toLowerCase()+"_usdt");
			JSONObject jo = JSON.parseObject(result).getJSONObject("ticker");
			
			BigDecimal last = jo.getBigDecimal("last"); //9.13
			
			logger.info("==============================");
			logger.info("============= "+coins.getCode()+" morningPrice:" + last + " =================");
			logger.info("==============================");
			
			cacheService.set("ok_start_"+coins.getCode().toLowerCase(), last);

		}catch (Exception e) {
			logger.error("morningPrice",e);
		}
	}
}
