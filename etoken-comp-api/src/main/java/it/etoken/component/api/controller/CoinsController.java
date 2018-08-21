package it.etoken.component.api.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import it.etoken.base.common.result.MLResult;
import it.etoken.base.common.result.MLResultList;
import it.etoken.base.common.result.MLResultObject;
import it.etoken.base.model.market.entity.Coins;
import it.etoken.base.model.market.vo.CoinTicker;
import it.etoken.cache.service.CacheService;
import it.etoken.component.api.eosrpc.ContractAccountInfo;
import it.etoken.component.api.eosrpc.EosResult;
import it.etoken.component.api.exception.MLApiException;
import it.etoken.component.eosblock.mongo.model.Transactions;
import it.etoken.componet.coins.facade.CoinsFacadeAPI;
import it.etoken.componet.eosblock.facade.TransactionsFacadeAPI;
import it.etoken.componet.market.facede.MarketFacadeAPI;


@Controller
@RequestMapping(value = "/coins")
public class CoinsController extends BaseController{

	private final static Logger logger = LoggerFactory.getLogger(CoinsController.class);
	
	@Reference(version="1.0.0")
	MarketFacadeAPI marketFacadeAPI;
	
	@Reference(version="1.0.0", timeout = 30000, retries = 0)
	CoinsFacadeAPI coinsFacadeAPI4Write;
	
	@Reference(version="1.0.0", timeout = 10000)
	CoinsFacadeAPI coinsFacadeAPI;
	
	@Reference(version="1.0.0", timeout = 3000000, retries = 0)
	TransactionsFacadeAPI transactionsFacadeAPI;
	
	@Autowired
	CacheService cacheService;
	
	@Value("${nodeos.path.chain}")
	String URL_CHAIN;
	@Value("${nodeos.path.chain.backup}")
	String URL_CHAIN_BACKUP;
	
	/**
	 * 账号信息
	 * @param requestMap
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/address")
	public Object address(@RequestParam Map<String, String> requestMap, HttpServletRequest request)
	{
		logger.info("/coins/address request map : " + requestMap);
		try {
			
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
//			double total = peos;
			
			Map<String, Object> data = new HashMap<String,Object>();
			data.put("name","xxx地址");
			data.put("address","1");
			data.put("money",0);
			data.put("increase",0.00);
			data.put("colors",new String[]{"#00ccff","#04f697","#7274d2","#f0e17b","#f68845","#f08484","#f18dd8","#b38af1","#5683f4","#c2f456"});
			
			Map<String, Object> c = new HashMap<String, Object>();
			c.put("name", "EOS");
			c.put("coins", 0);
			c.put("value", peos);
			c.put("rate", 0);
			c.put("img", "http://static.eostoken.im/images/20180309/1520595032475.png");
			
//			Map c1 = new HashMap<>();
//			c1.put("name", "HMC");
//			c1.put("coins", 0);
//			c1.put("value", phmc);
//			c1.put("rate", 0);
//			c1.put("img", "http://static.eostoken.im/images/20180309/1520595195706.png");
//			
//			Map c2 = new HashMap<>();
//			c2.put("name", "MAG");
//			c2.put("coins", 0);
//			c2.put("value", pmag);
//			c2.put("rate", 0);
//			c2.put("img", "http://static.eostoken.im/images/20180309/1520595153800.png");
			
//			data.put("coins",new Map[]{c,c1,c2});
			data.put("coins",new Map[]{c});
			return this.success(data);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return this.error(MLApiException.SYS_ERROR,null);
		}
	}
	
	/**
	 * 深度
	 * @param requestMap
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/deep")
	public Object deep(@RequestParam Map<String, String> requestMap, HttpServletRequest request)
	{
		logger.info("/coins/deep request map : " + requestMap);
		try {
			MLResultList<CoinTicker> result = marketFacadeAPI.getTicker();
			if(result.isSuccess()){
				return this.success(result.getList());
			}else{
				return this.error(result.getErrorCode(),result.getErrorHint(),result.getList());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return this.error(MLApiException.SYS_ERROR,null);
		}
	}
	
	/**
	 * 深度
	 * @param requestMap
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/info/{id}")
	public Object deep(@PathVariable Long id, HttpServletRequest request)
	{
		logger.info("/info/ " + id);
		try {
			MLResultObject<Coins> result = marketFacadeAPI.findById(id);
			if(result.isSuccess()){
				return this.success(result.getResult());
			}else{
				return this.error(result.getErrorCode(),result.getErrorHint(),result.getResult());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return this.error(MLApiException.SYS_ERROR,null);
		}
	}

	/**
	 * 深度
	 * @param requestMap
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/line/{coin}")
	public Object deep(@PathVariable String coin,@RequestParam Map<String, String> requestMap, HttpServletRequest request)
	{
		logger.info("/line/"+coin+" request map : " + requestMap);
		try {
			String type = requestMap.get("type");
			if(!StringUtils.isEmpty(coin) && !StringUtils.isEmpty(type)){
				MLResultObject<Map> result = marketFacadeAPI.getLine(coin, type);
				if(result.isSuccess()){
					return this.success(result.getResult());
				}else{
					return this.error(result.getErrorCode(),result.getErrorHint(),result.getResult());
				}
			}else{
				return this.error(MLApiException.PARAM_ERROR,null);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return this.error(MLApiException.SYS_ERROR,null);
		}
	}
	
	/**
	 * 获取资产列表
	 * 
	 * @param requestMap
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/list")
	public Object list(@RequestBody Map<String, String> requestMap, HttpServletRequest request) {
		try {
			String page = requestMap.get("page");
			
			int pageN = 1;
			String code = "";
			try {
				pageN = Integer.parseInt(page);
			} catch (Exception ee) {
			}
			
			if(null != requestMap.get("code") && !requestMap.get("code").isEmpty()) {
				code = requestMap.get("code").toLowerCase();
			}

			MLResultList<Coins> result = coinsFacadeAPI.findAllByPage(pageN, code);
			
			if (result.isSuccess()) {
				List<Coins> myList = result.getList();
				JSONArray mytResult = new JSONArray();
				for(Coins thisCoins : myList) {
					if(null == thisCoins.getContractAccount() || thisCoins.getContractAccount().isEmpty()) {
						continue;
					}
					JSONObject jo = new JSONObject();
					jo.put("id", thisCoins.getId());
					jo.put("name", thisCoins.getName());
					jo.put("contractAccount", thisCoins.getContractAccount());
					jo.put("icon", thisCoins.getImg());
					jo.put("precisionNumber", thisCoins.getPrecisionNumber());
					jo.put("detail", thisCoins.getIntr());
					jo.put("createDate", thisCoins.getCreatedate());
					jo.put("updateDate", thisCoins.getModifydate());
					
					double eosrate = 0;
					BigDecimal eosrateB = cacheService.get("CNY_USDT",BigDecimal.class);
					if(null != eosrateB) {
						eosrate = eosrateB.doubleValue();
					}
					double peos = 0;
					double peosD = 0;
					DecimalFormat formatter2 =new DecimalFormat("#.00");
					if(null != thisCoins.getCode() && !thisCoins.getCode().isEmpty()) {
						JSONObject geteEos = cacheService.get("gateio_ticker_" + thisCoins.getCode(),JSONObject.class);
						if(null != geteEos) {
							peosD = geteEos.getDoubleValue("last");
						}else{
							JSONObject okEos = cacheService.get("ok_ticker_"+ thisCoins.getCode(),JSONObject.class);
							if(null != okEos) {
								peosD = okEos.getDoubleValue("last");
							}
						}
					}
					peos = Double.parseDouble(formatter2.format(peosD*eosrate));
					jo.put("value", peos);
					
					mytResult.add(jo);
				}
				return this.success(mytResult);
			} else {
				return this.error(result.getErrorCode(), result.getErrorHint(), result.getList());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error(MLApiException.SYS_ERROR, null);
		}
	}
	/**
	 * 获取资产列表
	 * 
	 * @param requestMap
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(@RequestBody Map<String, String> requestMap, HttpServletRequest request) {
		try {
			String name = requestMap.get("name");
			String contract_account = requestMap.get("contract_account");

			if (StringUtils.isEmpty(name) || StringUtils.isEmpty(name.trim())) {
				return this.error(MLApiException.PARAM_ERROR, "资产名称不能为空");
			}
			if (StringUtils.isEmpty(contract_account) || StringUtils.isEmpty(contract_account.trim())) {
				return this.error(MLApiException.PARAM_ERROR, "合约帐号不能为空");
			}
			
			name = name.trim();
			contract_account = contract_account.trim();
			//检测合约账号和合约名是否正确
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("json", "true");
			jsonObject.put("code", contract_account);
			jsonObject.put("scope", name);
			jsonObject.put("table", "stat");
			EosResult checkAccountResp = null;
			checkAccountResp = new ContractAccountInfo().run(URL_CHAIN, URL_CHAIN_BACKUP, jsonObject.toString());
			if (!checkAccountResp.isSuccess()) {
				return this.error(MLApiException.CONTRACT_NOT_EXIST_ERROR, checkAccountResp.getData());
			}
			JSONObject checkJson = JSONObject.parseObject(checkAccountResp.getData());
			JSONArray rows = checkJson.getJSONArray("rows");
			if(null == rows || rows.isEmpty()) {
				JSONObject checkError = checkJson.getJSONObject("error");
				if(null == checkError) {
					return this.error(MLApiException.CONTRACT_NOT_EXIST_ERROR, checkJson);
				}
				if(checkError.getIntValue("code")==3060002) {
					 return this.error(MLApiException.CONTRACT_ACCOUNT_ERROR, checkJson);
				 }else if(checkError.getIntValue("code")==10) {
					 return this.error(MLApiException.CONTRACT_NAME_ERROR, checkJson);
				 }
				return this.error(MLApiException.CONTRACT_NOT_EXIST_ERROR, checkJson);
			}
		
			//确定是否已经存在合约
			MLResultObject<Coins> coinsResultObject = coinsFacadeAPI.findByName(name);
			if(!coinsResultObject.isSuccess()) {
				return this.error(coinsResultObject.getErrorCode(), coinsResultObject.getErrorHint(), coinsResultObject.getResult());
			}
			Coins existCoins = coinsResultObject.getResult();
			if(null != existCoins) {
				return this.error(MLApiException.CONTRACT_EXIST_ERROR, existCoins);
			}
			
			//添加合约
			Coins coins = new Coins();
			coins.setName(name);
			coins.setCode(name.toLowerCase());
			coins.setContractAccount(contract_account);
			
			JSONObject coinInfo = rows.getJSONObject(0);
			
			String supplyString = coinInfo.getString("supply");
			String[] supplyArray = supplyString.split(" ");
			String supply = supplyArray[0].trim();
			
			String max_supply_string = coinInfo.getString("max_supply");
			String[] max_supply_array = max_supply_string.split(" ");
			String max_supply = max_supply_array[0].trim();
			
			int precision_number = 0;
			if(max_supply.indexOf(".") > 0) {
				precision_number = max_supply.length() - max_supply.indexOf(".") - 1;
			}
			
			coins.setMarke((new BigDecimal(supply)).longValue());
			coins.setTotal((new BigDecimal(max_supply)).longValue());
			coins.setPrecisionNumber(precision_number);
			
			MLResult result = coinsFacadeAPI4Write.saveUpdate(coins);

			if (result.isSuccess()) {
				return this.success(true);
			} else {
				return this.error(result.getErrorCode(), result.getErrorHint(), "");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error(MLApiException.CONTRACT_EXIST_ERROR, null);
		}
	}
	
	
	//根据用户查询这个用户拥有的所有的币 account是用户的账号名
	@ResponseBody
	@RequestMapping(value = "/coins/{account}")
	public Object coins(@PathVariable String account,@RequestParam Map<String, String> requestMap, HttpServletRequest request)
	{
		logger.info("/line/"+account+" request map : " + requestMap);
		try {
			if(account.isEmpty()||account==null) {
				return this.error(MLApiException.PARAM_ERROR,null);
			}
			MLResultList<Coins> resultList=coinsFacadeAPI.findAllCoins();
			List<Coins> list=resultList.getList();
			List<String> listCoin=new ArrayList<String>();
			for (Coins coins : list) {
		      String contract=coins.getContractAccount();//合约账号
		      if(contract==null||contract.isEmpty()) {
		    	  continue;
		      }
		      MLResultList<Transactions> result=transactionsFacadeAPI.findAccountCoins(account, contract);
		      if(result.getList().size()>0) {
		    	  listCoin.add(coins.getName());
		      }
			}
			return this.success(listCoin);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return this.error(MLApiException.SYS_ERROR,null);
		}
	}

	/**
	 * 修正数据
	 * 
	 * @param requestMap
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/fixedData")
	public Object fixedData(@RequestParam Map<String, String> requestMap, HttpServletRequest request) {
		try {
			MLResultList<Coins> coinsResultList = coinsFacadeAPI.findAllCoins();
			if(!coinsResultList.isSuccess()) {
				return this.error(coinsResultList.getErrorCode(), coinsResultList.getErrorHint(), coinsResultList.getList());
			}
			List<Coins> coinsList = coinsResultList.getList();
			for(Coins coins : coinsList) {
				if(coins.getContractAccount() == null || coins.getContractAccount().isEmpty()) {
					continue;
				}
				
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("json", "true");
				jsonObject.put("code", coins.getContractAccount());
				jsonObject.put("scope", coins.getName());
				jsonObject.put("table", "stat");
				
				EosResult checkAccountResp = null;
				try {
					checkAccountResp = new ContractAccountInfo().run(URL_CHAIN, URL_CHAIN_BACKUP, jsonObject.toString());
				}catch(Exception ex) {
					continue;
				}
				if (!checkAccountResp.isSuccess()) {
					return this.error(MLApiException.CONTRACT_NOT_EXIST_ERROR, checkAccountResp.getData());
				}
				JSONObject checkJson = JSONObject.parseObject(checkAccountResp.getData());
				JSONArray rows = checkJson.getJSONArray("rows");
				if(null == rows || rows.isEmpty()) {
					JSONObject checkError = checkJson.getJSONObject("error");
					if(null == checkError) {
						return this.error(MLApiException.CONTRACT_NOT_EXIST_ERROR, checkJson);
					}
					if(checkError.getIntValue("code")==3060002) {
						 return this.error(MLApiException.CONTRACT_ACCOUNT_ERROR, checkJson);
					 }else if(checkError.getIntValue("code")==10) {
						 return this.error(MLApiException.CONTRACT_NAME_ERROR, checkJson);
					 }
					return this.error(MLApiException.CONTRACT_NOT_EXIST_ERROR, checkJson);
				}
				
				JSONObject coinInfo = rows.getJSONObject(0);
				
				String supplyString = coinInfo.getString("supply");
				String[] supplyArray = supplyString.split(" ");
				String supply = supplyArray[0].trim();
				
				String max_supply_string = coinInfo.getString("max_supply");
				String[] max_supply_array = max_supply_string.split(" ");
				String max_supply = max_supply_array[0].trim();
				
				int precision_number = 0;
				if(max_supply.indexOf(".") > 0) {
					precision_number = max_supply.length() - max_supply.indexOf(".") - 1;
				}
				
				coins.setMarke((new BigDecimal(supply)).longValue());
				coins.setTotal((new BigDecimal(max_supply)).longValue());
				coins.setPrecisionNumber(precision_number);
				
				MLResult result = coinsFacadeAPI4Write.saveUpdate(coins);

				if (!result.isSuccess()) {
					return this.error(result.getErrorCode(), result.getErrorHint(), "");
				}
			}
			return this.success(true);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error(MLApiException.CONTRACT_EXIST_ERROR, null);
		}
	}

}
