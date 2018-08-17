package it.etoken.component.eosblock.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import it.etoken.cache.service.CacheService;

@Component
public class EOSUtils {
	@Autowired
	CacheService cacheService;
	public BigDecimal getPrice() {
		double eosrate = 0;
		BigDecimal eosrateB = cacheService.get("CNY_USDT",BigDecimal.class);
		if(null != eosrateB) {
			eosrate = eosrateB.doubleValue();
		}
		
		double peos = 0;
		double peosD = 0;
		DecimalFormat formatter2 =new DecimalFormat("#.00");
		JSONObject geteEos = cacheService.get("gateio_ticker_eos",JSONObject.class);
		if(null != geteEos) {
			peosD = geteEos.getDoubleValue("last");
		}else{
			JSONObject okEos = cacheService.get("ok_ticker_eos",JSONObject.class);
			if(null != okEos) {
				peosD = okEos.getDoubleValue("last");
			}
		}
		peos = Double.parseDouble(formatter2.format(peosD*eosrate));
		
		return BigDecimal.valueOf(peos);
	}
}
