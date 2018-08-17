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

import it.etoken.base.common.exception.MLException;
import it.etoken.base.common.http.HttpClientService;
import it.etoken.base.common.utils.DateUtils;
import it.etoken.base.model.market.entity.Coins;
import it.etoken.base.model.market.vo.CoinTicker;
import it.etoken.cache.service.CacheService;
import it.etoken.component.market.service.CoinsService;
import it.etoken.component.market.service.MarketService;

//@Component
public class BigOneServiceImpl implements MarketService {

	private final static Logger logger = LoggerFactory.getLogger(BigOneServiceImpl.class);
	
	final String maket = "https://api.big.one/markets/";
	
	final String usdt_rate = "https://www.okex.com/api/v1/exchange_rate.do";
	
	final String [] lines = new String[]{"0000005","0000060","0000360","0001440"};
	
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
			String result = httpClientService.doGet(maket+coins.getSymble());
			
			JSONObject ro = JSON.parseObject(result).getJSONObject("data");
			JSONObject jo= ro.getJSONObject("ticker");
			cacheService.set("big_ticker_"+coins.getCode().toLowerCase(),jo);
			//保存汇率
			try {
				String sys [] = coins.getSymble().split("-");
				BigDecimal big = cacheService.get("CNY_"+sys[1].toUpperCase(),BigDecimal.class);
				if(big!=null) {
					BigDecimal rs = new BigDecimal(big.doubleValue()*jo.getDoubleValue("price"));
					cacheService.set("CNY_"+sys[0].toUpperCase(),rs);
				}
			}catch (Exception e) {
				logger.error("rate",e);
			}
			//保存k线
			for(int i=0;i<lines.length;i++){
				String type = lines[i];
				JSONArray jar = ro.getJSONObject("metrics").getJSONArray(type);
				cacheService.set("big_line_"+coins.getCode().toLowerCase()+"_"+i,gemLine(i,jar));
			}
		}catch (Exception e) {
			logger.error("ticker",e);
		}
	}
	
	/**
	 * 生存k线数据
	 * @param i
	 * @param js
	 * @return
	 */
	private Map<String,List> gemLine(int i,JSONArray js){
		Map<String,List> data = new HashMap<>();
		List<Double> txs = new ArrayList<>();
		List<Double> ps = new ArrayList<>();
		List<String> x = new ArrayList<>();
		data.put("txs", txs);
		data.put("ps", ps);
		data.put("x", x);
		for(int j=js.size()-1;j>=0;j--){
			
			JSONArray a = js.getJSONArray(j);
			
			if(Double.parseDouble(String.valueOf(a.get(3)))>0) {
				
				txs.add(Double.parseDouble(formatter.format(Double.parseDouble(String.valueOf(a.get(5))))));
				ps.add(Double.parseDouble(formatter.format(Double.parseDouble(String.valueOf(a.get(3))))));
				if(i==0 || i==1){
					x.add(DateUtils.forSecond((long)a.get(0)));
					
				}else{
					x.add(DateUtils.forDay((long)a.get(0)));
				}
			}
			
		}
		return data;
	}

	@Override
	@Async
	public void exchange() throws MLException {
		try{
			String rate = httpClientService.doGet(usdt_rate);
			JSONObject jo = JSON.parseObject(rate);
			BigDecimal r = jo.getBigDecimal("rate");
			cacheService.set("CNY_USDT",r);
		}catch (Exception e) {
			cacheService.set("CNY_USDT",6.63);
			logger.error("exchange",e);
		}
		
		this.ethExchange();
	}
	
	private void ethExchange() throws MLException  {
		try{
			String result = httpClientService.doGet(maket+"ETH-USDT");
			JSONObject ro = JSON.parseObject(result).getJSONObject("data");
			JSONObject jo= ro.getJSONObject("ticker");
			BigDecimal price_usd  = jo.getBigDecimal("price");
			
			BigDecimal cnyUsdt= cacheService.get("CNY_USDT", BigDecimal.class);
			BigDecimal price = price_usd.multiply(cnyUsdt);
			
			cacheService.set("CNY_ETH",price);
		}catch (Exception e) {
			cacheService.set("CNY_ETH",588.26);
			logger.error("exchange",e);
		}
	}

	@Override
	public List<CoinTicker> getTicker() throws MLException {
		try{
			List<CoinTicker> tikes = new ArrayList<>();
			Page<Coins> coins = coinsService.findAllBy4Market();
			for(Coins c : coins.getResult()){
				BigDecimal rate = new BigDecimal(0);
				try {
					rate = cacheService.get("CNY_"+c.getSymble().split("-")[1],BigDecimal.class);
				}catch (Exception e) {
					logger.error("getTicker-getrate",e);
				}
				CoinTicker t = new CoinTicker();
				t.setId(c.getId());
				t.setCode(c.getCode());
				t.setImg(c.getImg());
				t.setName(c.getName());
				t.setId(c.getId());
				JSONObject obj = cacheService.get("big_ticker_"+c.getCode().toLowerCase(),JSONObject.class);
				if(obj!=null){
					t.setStart(Double.parseDouble(formatter.format(obj.getDoubleValue("open")*rate.doubleValue())));
					t.setMax(Double.parseDouble(formatter.format(obj.getDoubleValue("high")*rate.doubleValue())));
					t.setMin(Double.parseDouble(formatter.format(obj.getDoubleValue("low")*rate.doubleValue())));
					t.setPrice(Double.parseDouble(formatter.format(obj.getDoubleValue("price")*rate.doubleValue())));
					t.setUsd(Double.parseDouble(formatter.format(obj.getDoubleValue("price"))));
					t.setTxs(Double.parseDouble(formatter1.format(obj.getDoubleValue("volume"))));
					t.setValue(Double.parseDouble(formatter.format(c.getMarke()*t.getUsd())));
					if(t.getStart()==0) {
						t.setIncrease(Double.parseDouble(formatter2.format(t.getPrice()*100)));
					}else {
						t.setIncrease(Double.parseDouble(formatter2.format((t.getPrice()-t.getStart())/t.getStart()*100)));
					}
					
				}
				else {
					logger.error("获取缓存数据失败: big_ticker_"+c.getCode().toLowerCase());
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
			Map data = cacheService.get("big_line_"+code.toLowerCase()+"_"+type,Map.class);
			return data;
		}catch (Exception e) {
			logger.error(e.toString());
		}
		return null;
	}
	
	@Override
	public void morningPrice(Coins coins)  throws MLException{
		//TODO:暂时不需要实现
	}
}
