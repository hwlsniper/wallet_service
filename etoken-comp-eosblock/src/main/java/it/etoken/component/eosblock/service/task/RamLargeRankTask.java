package it.etoken.component.eosblock.service.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import it.etoken.base.common.exception.MLCommonException;
import it.etoken.base.common.exception.MLException;
import it.etoken.base.common.utils.HttpClientUtils;
import it.etoken.component.eosblock.service.RamLargeRankService;
import it.etoken.component.eosblock.service.RamPriceService;

@Component
public class RamLargeRankTask {

	@Autowired
	RamLargeRankService ramLargeRankService;
	
	

	//得到内存大户排行并保存
	@Scheduled(cron = "0 0 */1 * * ?")
	public void getLargeRank() {
		try {
			System.out.println("获取内存大户排行榜开始");
			ramLargeRankService.getLargeRank();
			System.out.println("获取内存大户排行榜结束");
		} catch (Exception e) {
			e.printStackTrace();
			throw new MLException(MLCommonException.system_err);
		}

	}
}
