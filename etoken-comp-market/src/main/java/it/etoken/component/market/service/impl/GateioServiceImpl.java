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

@Component
public class GateioServiceImpl implements MarketService {

	private final static Logger logger = LoggerFactory.getLogger(GateioServiceImpl.class);
	
	final String server="https://data.gateio.io/api2/1/ticker/";
	
	final String server_rate = "https://www.okex.com/api/v1/exchange_rate.do";
	
	final String server_line = "https://data.gateio.io/api2/1/candlestick2/";
	
	final String [] lines = new String[]{"300","3600","21600","86400"}; //second
	final String [] lines_hour = new String[]{"16","200","1200","4800"};
	
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
			String symble = coins.getSymble().toLowerCase();
			String[] symbleArray = symble.split("-");
			String result = httpClientService.doGet(server+symbleArray[0]+"_"+symbleArray[1]);
			
			JSONObject jo = JSON.parseObject(result);
			cacheService.set("gateio_ticker_"+coins.getCode(),jo);
			
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
			cacheService.set(MLCacheMarket.ok_rate,6.5);
			cacheService.set("CNY_USDT",6.5);
			cacheService.set("USDT_USDT",1);
//			logger.error("exchange",e);
		}
		
		this.ethExchange();
	}
	
	private void ethExchange() throws MLException  {
		try{
			String result = httpClientService.doGet(server+"ETH_USDT");
			JSONObject ro = JSON.parseObject(result);
			BigDecimal price_usd  = ro.getBigDecimal("last");
			
			BigDecimal cnyUsdt= cacheService.get("CNY_USDT", BigDecimal.class);
			BigDecimal price = price_usd.multiply(cnyUsdt);
			
			cacheService.set("USDT_ETH",price_usd);
			cacheService.set("CNY_ETH",price);
		}catch (Exception e) {
			cacheService.set("USDT_ETH",541.40);
			cacheService.set("CNY_ETH",3520.00);
			logger.error("exchange",e);
		}
	}
	
	private void btcExchange() throws MLException  {
		try{
			String result = httpClientService.doGet(server+"BTC_USDT");
			JSONObject ro = JSON.parseObject(result);
			BigDecimal price_usd  = ro.getBigDecimal("last");
			
			BigDecimal cnyUsdt= cacheService.get("CNY_USDT", BigDecimal.class);
			BigDecimal price = price_usd.multiply(cnyUsdt);
			
			cacheService.set("USDT_BTC",price_usd);
			cacheService.set("CNY_BTC",price);
		}catch (Exception e) {
			cacheService.set("USDT_BTC",6730.00);
			cacheService.set("CNY_BTC",43930.00);
			logger.error("exchange",e);
		}
	}

	@Override
	public List<CoinTicker> getTicker() throws MLException {
		List<CoinTicker> tikes = new ArrayList<CoinTicker>();
		try {
			Page<Coins> coins = coinsService.findAllBy4Market();
			for (Coins c : coins.getResult()) {
				String symble = c.getSymble().toLowerCase();
				String[] symbleArray = symble.split("-");

				BigDecimal bdRate = this.getRMBRateByCode(symbleArray[1]);
				double rate = bdRate.doubleValue();
				
				BigDecimal bdRateUSD = this.getUSDRateByCode(symbleArray[1]);
				double rateUSD = bdRateUSD.doubleValue();

				CoinTicker t = new CoinTicker();
				t.setId(c.getId());
				t.setCode(c.getCode());
				t.setImg(c.getImg());
				t.setName(c.getName());
				
				//凌晨价格
				BigDecimal start = cacheService.get("gateio_start_"+c.getCode().toLowerCase(),BigDecimal.class);
				if(start!=null){
					t.setStart(Double.parseDouble(formatter.format(start.doubleValue()*rate)));
				}
				else {
					t.setStart(0);
				}

				JSONObject obj = cacheService.get("gateio_ticker_" + c.getCode().toLowerCase(), JSONObject.class);
				if (obj != null) {
					t.setMax(Double.parseDouble(formatter.format(obj.getDoubleValue("high24hr") * rate)));
					t.setMin(Double.parseDouble(formatter.format(obj.getDoubleValue("low24hr") * rate)));
					t.setPrice(Double.parseDouble(formatter.format(obj.getDoubleValue("last") * rate)));
					t.setUsd(Double.parseDouble(formatter.format(obj.getDoubleValue("last")*rateUSD)));
					t.setTxs(Double.parseDouble(formatter1.format(obj.getDoubleValue("baseVolume"))));
					t.setValue(Double.parseDouble(formatter.format(c.getMarke() * t.getUsd())));
					t.setIncrease(Double.parseDouble(formatter2.format(obj.getDoubleValue("percentChange"))));
				}
				tikes.add(t);
			}
			return tikes;
		} catch (Exception e) {
			logger.error("getTicker", e);
		}
		return tikes;
	}
	
	/**
	 * 获取各种币对人民币汇率
	 * @param code
	 * @return
	 */
	private BigDecimal getRMBRateByCode(String code) {
		code = code.toUpperCase();
		BigDecimal bdRate = cacheService.get("CNY_" + code, BigDecimal.class);
		if (null != bdRate && bdRate.compareTo(BigDecimal.ZERO) != 0) {
			return bdRate;
		}

		if (code.equalsIgnoreCase("ETH")) {
			return new BigDecimal(3520.00);
		}else if(code.equalsIgnoreCase("BTC")) {
			return new BigDecimal(43930.00);
		}
		return new BigDecimal(6.68); //默认USDT汇率
	}
	
	/**
	 * 获取各种币对美元汇率
	 * @param code
	 * @return
	 */
	private BigDecimal getUSDRateByCode(String code) {
		code = code.toUpperCase();
		BigDecimal bdRate = cacheService.get("USDT_" + code, BigDecimal.class);
		if (null != bdRate && bdRate.compareTo(BigDecimal.ZERO) != 0) {
			return bdRate;
		}

		if (code.equalsIgnoreCase("ETH")) {
			return new BigDecimal(541.40);
		}else if(code.equalsIgnoreCase("BTC")) {
			return new BigDecimal(6730.00);
		}
		return new BigDecimal(1); //默认USDT汇率
	}
	
	@Override
	public Map getLine(String code,String type) throws MLException {
		try{
			Map data = cacheService.get("gateio_line_"+code.toLowerCase()+"_"+type,Map.class);
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
				String a_lines_hour = lines_hour[i];
				Map<String,List> data = new HashMap<>();
				List<Double> txs = new ArrayList<>();
				List<Double> ps = new ArrayList<>();
				List<String> x = new ArrayList<>();
				data.put("txs", txs);
				data.put("ps", ps);
				data.put("x", x);
				
				String symble = coins.getSymble().toLowerCase();
				String[] symbleArray = symble.split("-");
				
				String result = httpClientService.doGet(server_line + symbleArray[0] + "_"+symbleArray[1]+"?group_sec=" + type + "&range_hour=" + a_lines_hour);
				JSONObject r = JSONObject.parseObject(result);
				
				String rst = r.getString("result");
				if(!rst.equalsIgnoreCase("true")) {
					continue;
				}
				
				JSONArray js = r.getJSONArray("data");
				for(int j=0;j<js.size();j++){
					JSONArray a = js.getJSONArray(j);
					txs.add(Double.parseDouble(formatter.format(Double.parseDouble(String.valueOf(a.get(1))))));
					ps.add(Double.parseDouble(formatter.format(Double.parseDouble(String.valueOf(a.get(2))))));
					
					double xx = a.getDouble(0);
					long temp = (long) xx/1000;
					if(i==0 || i==1){
						x.add(DateUtils.forSecond(temp));
					}else{
						x.add(DateUtils.forDay(temp));
					}
				}
				cacheService.set("gateio_line_"+coins.getCode().toLowerCase()+"_"+i, data);
			}
			
		}catch (Exception e) {
			logger.error("line",e);
		}
	}
	
	@Override
	@Async
	public void morningPrice(Coins coins) throws MLException {
		try{
			logger.info("==============================");
			logger.info("============= "+coins.getCode()+" morningPrice begin =================");
			logger.info("==============================");
			
			String symble = coins.getSymble().toLowerCase();
			String[] symbleArray = symble.split("-");
			String result = httpClientService.doGet(server+symbleArray[0]+"_"+symbleArray[1]);
			
			JSONObject jo = JSON.parseObject(result);
			
			BigDecimal last = jo.getBigDecimal("last"); 
			
			logger.info("==============================");
			logger.info("============= "+coins.getCode()+" morningPrice:" + last + " =================");
			logger.info("==============================");
			
			cacheService.set("gateio_start_"+coins.getCode().toLowerCase(), last);

		}catch (Exception e) {
			logger.error("morningPrice",e);
		}
	}
}
