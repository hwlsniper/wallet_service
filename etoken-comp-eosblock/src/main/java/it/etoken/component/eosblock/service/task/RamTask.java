package it.etoken.component.eosblock.service.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;

import it.etoken.base.common.exception.MLCommonException;
import it.etoken.base.common.exception.MLException;
import it.etoken.component.eosblock.service.RamPriceService;

@Component
public class RamTask {

	@Autowired
	RamPriceService ramPriceService;

	// 获取当前内存价格表的数据
	@Scheduled(cron = "0 */1 * * * ?")
	public void getRamPrice() {
		try {
			System.out.println("开始获取最新内存价格数据...");
			JSONObject ramInfo = ramPriceService.getRamInfo();
			System.out.println(ramInfo);
			System.out.println("获取最新内存价格数据完成");
		} catch (Exception e) {
			e.printStackTrace();
			throw new MLException(MLCommonException.system_err);
		}

	}
	
	//构建内存价格曲线数据
	@Scheduled(cron = "0 */1 * * * ?")
	public void buildLineData() {
		try {
			System.out.println("开始构建内存价格曲线数据...");
			ramPriceService.buildLineData();
			System.out.println("构建内存价格曲线数据完成");
		} catch (Exception e) {
			e.printStackTrace();
			throw new MLException(MLCommonException.system_err);
		}

	}
	
	//获取最新交易记录
	@Scheduled(cron = "*/2 * * * * ?")
	public void getNewTradeOrders() {
		try {
			System.out.println("开始获取内存最新交易记录...");
			ramPriceService.getNewTradeOrders();
			System.out.println("获取最新交易记录完成完成");
		} catch (Exception e) {
			e.printStackTrace();
			throw new MLException(MLCommonException.system_err);
		}

	}
	
	//获取大单交易记录
	@Scheduled(cron = "*/10 * * * * ?")
	public void getBigTradeOrders() {
		try {
			System.out.println("开始获取内存大单交易记录...");
			ramPriceService.getBigTradeOrders();
			System.out.println("获取大单易记录完成完成");
		} catch (Exception e) {
			e.printStackTrace();
			throw new MLException(MLCommonException.system_err);
		}

	}
	
//	// 计算24小时内资金分布
//	@Scheduled(cron = "*/5 * * * * ?")
//	public void calculateAmountStatistics() {
//		try {
//			System.out.println("开始计算24小时资金分布...");
//			ramPriceService.calculateAmountStatistics();
//			System.out.println("计算24小时资金分布完成");
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new MLException(MLCommonException.system_err);
//		}
//
//	}
	
	@Scheduled(cron = "0 */5 * * * ?")
	public void buildKLine5M() {
		try {
			System.out.println("开始保存5分钟K线数据...");
			BasicDBObject kLineData = ramPriceService.saveKLineData("5m");
			System.out.println(kLineData);
			System.out.println("保存5分钟K线数据完成");
		} catch (Exception e) {
			e.printStackTrace();
			throw new MLException(MLCommonException.system_err);
		}
	}
	
	@Scheduled(cron = "0 */15 * * * ?")
	public void buildKLine10M() {
		try {
			System.out.println("开始保存15分钟K线数据...");
			BasicDBObject kLineData = ramPriceService.saveKLineData("15m");
			System.out.println(kLineData);
			System.out.println("保存15分钟K线数据完成");
		} catch (Exception e) {
			e.printStackTrace();
			throw new MLException(MLCommonException.system_err);
		}
	}
	
	@Scheduled(cron = "0 */30 * * * ?")
	public void buildKLine30M() {
		try {
			System.out.println("开始保存30分钟K线数据...");
			BasicDBObject kLineData = ramPriceService.saveKLineData("30m");
			System.out.println(kLineData);
			System.out.println("保存30分钟K线数据完成");
		} catch (Exception e) {
			e.printStackTrace();
			throw new MLException(MLCommonException.system_err);
		}
	}
	
	@Scheduled(cron = "0 0 */1 * * ?")
	public void buildKLine1h() {
		try {
			System.out.println("开始保存1小时K线数据...");
			BasicDBObject kLineData = ramPriceService.saveKLineData("1h");
			System.out.println(kLineData);
			System.out.println("保存1小时钟K线数据完成");
		} catch (Exception e) {
			e.printStackTrace();
			throw new MLException(MLCommonException.system_err);
		}
	}
	
	@Scheduled(cron = "0 0 */2 * * ?")
	public void buildKLine2h() {
		try {
			System.out.println("开始保存2小时K线数据...");
			BasicDBObject kLineData = ramPriceService.saveKLineData("2h");
			System.out.println(kLineData);
			System.out.println("保存2小时钟K线数据完成");
		} catch (Exception e) {
			e.printStackTrace();
			throw new MLException(MLCommonException.system_err);
		}
	}
	
	@Scheduled(cron = "0 0 */4 * * ?")
	public void buildKLine4h() {
		try {
			System.out.println("开始保存4小时K线数据...");
			BasicDBObject kLineData = ramPriceService.saveKLineData("4h");
			System.out.println(kLineData);
			System.out.println("保存4小时钟K线数据完成");
		} catch (Exception e) {
			e.printStackTrace();
			throw new MLException(MLCommonException.system_err);
		}
	}
	
	@Scheduled(cron = "0 0 */6 * * ?")
	public void buildKLine6h() {
		try {
			System.out.println("开始保存6小时K线数据...");
			BasicDBObject kLineData = ramPriceService.saveKLineData("6h");
			System.out.println(kLineData);
			System.out.println("保存6小时钟K线数据完成");
		} catch (Exception e) {
			e.printStackTrace();
			throw new MLException(MLCommonException.system_err);
		}
	}
	
	@Scheduled(cron = "0 0 */12 * * ?")
	public void buildKLine12h() {
		try {
			System.out.println("开始保存12小时K线数据...");
			BasicDBObject kLineData = ramPriceService.saveKLineData("12h");
			System.out.println(kLineData);
			System.out.println("保存12小时钟K线数据完成");
		} catch (Exception e) {
			e.printStackTrace();
			throw new MLException(MLCommonException.system_err);
		}
	}
	
	@Scheduled(cron = "0 0 0 */1 * ?")
	public void buildKLine1d() {
		try {
			System.out.println("开始保存1天K线数据...");
			BasicDBObject kLineData = ramPriceService.saveKLineData("1d");
			System.out.println(kLineData);
			System.out.println("保存1天K线数据完成");
		} catch (Exception e) {
			e.printStackTrace();
			throw new MLException(MLCommonException.system_err);
		}
	}
	
	@Scheduled(cron = "0 0 0 ? * MON")
	public void buildKLine1w() {
		try {
			System.out.println("开始保存1周K线数据...");
			BasicDBObject kLineData = ramPriceService.saveKLineData("1w");
			System.out.println(kLineData);
			System.out.println("保存1周钟K线数据完成");
		} catch (Exception e) {
			e.printStackTrace();
			throw new MLException(MLCommonException.system_err);
		}
	}
}
