package it.etoken.component.market.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.github.pagehelper.Page;

import it.etoken.base.common.jpush.PushService;
import it.etoken.base.model.market.entity.Coins;
import it.etoken.base.model.market.vo.CoinTicker;
import it.etoken.component.market.service.CoinsService;
import it.etoken.component.market.service.MarketService;

@Component
public class DeepJob {
	
	@Autowired
	CoinsService coinsService;
	
	@Autowired
	MarketService bigOneService;
	
	@Autowired
	PushService pushService;
	
	@PostConstruct
    public void  init(){
		exchange();
	}

	/**
	 * 60分钟更新一次汇率
	 */
	@Scheduled(fixedDelay=30*60*1000)
    public void exchange(){
		bigOneService.exchange();
	}
	
	/**
	 * 1秒更新一次价格信息
	 */
//	@Scheduled(fixedDelay=60*1000)
	@Scheduled(fixedDelay=10*1000)
    public void ticker(){
		Page<Coins> coins = coinsService.findAllBy4Market();
		for(Coins c : coins.getResult()){
			bigOneService.ticker(c);
		}
	}
	
	/**
	 * 1秒更新一次价格信息
	 */
	@Scheduled(cron="0 15 10 * * ?")
    public void push(){
		List<CoinTicker> ts = bigOneService.getTicker();
		for(CoinTicker t : ts) {
			if(t.getName().toLowerCase()=="eos") {
				Map<String,String> extr = new HashMap<>();
				extr.put("url","alert://");
				pushService.pushByTag("1","EOS当前价格："+t.getPrice()+"，今日涨跌："+t.getIncrease()+"%", extr);
			}
		}
	}
	
	/**
	 * 获取零时价格
	 */
	@Scheduled(cron="0 0 8 * * ?")
    public void getMorningPrice(){
		Page<Coins> coins = coinsService.findAllBy4Market();
		for(Coins c : coins.getResult()){
			bigOneService.morningPrice(c);
		}
	}
}
