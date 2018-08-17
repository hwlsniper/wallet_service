package it.etoken.component.api.controller;

import java.math.BigDecimal;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import it.etoken.base.common.jpush.PushService;
import it.etoken.base.common.result.MLResultList;
import it.etoken.base.common.result.MLResultObject;
import it.etoken.base.common.utils.HttpClientUtils;
import it.etoken.base.model.eosblock.entity.AccountsKeys;
import it.etoken.base.model.eosblock.entity.Actions;
import it.etoken.base.model.eosblock.entity.Delegatebw;
import it.etoken.base.model.eosblock.entity.Tokens;
import it.etoken.component.api.eosrpc.CreateAccount;
import it.etoken.component.api.eosrpc.EosResult;
import it.etoken.component.api.eosrpc.GetAccountDelbandInfo;
import it.etoken.component.api.eosrpc.GetAccountInfo;
import it.etoken.component.api.eosrpc.GetBalance;
import it.etoken.component.api.eosrpc.GetDelegatebw;
import it.etoken.component.api.eosrpc.GetGlobalInfo;
import it.etoken.component.api.eosrpc.GetInfo;
import it.etoken.component.api.eosrpc.GetKeyAccounts;
import it.etoken.component.api.eosrpc.GetUndelegatebwInfo;
import it.etoken.component.api.eosrpc.GetVotingInfo;
import it.etoken.component.api.eosrpc.ListProducers;
import it.etoken.component.api.eosrpc.PushTransaction;
import it.etoken.component.api.exception.MLApiException;
import it.etoken.componet.eosblock.facade.AccountsKeysFacadeAPI;
import it.etoken.componet.eosblock.facade.ActionsFacadeAPI;
import it.etoken.componet.eosblock.facade.DelegatebwFacadeAPI;
import it.etoken.componet.eosblock.facade.TokensFacadeAPI;
import it.etoken.componet.eosblock.facade.TransactionsFacadeAPI;

@Controller
@RequestMapping(value = "/eosrpc")
public class EosRpcController extends BaseController {

	private final static Logger logger = LoggerFactory.getLogger(EosRpcController.class);

	@Autowired
	PushService pushService;
	
	@Reference(version = "1.0.0")
	TokensFacadeAPI tokensFacadeAPI;
	
	@Reference(version = "1.0.0")
	AccountsKeysFacadeAPI accountsKeysFacadeAPI;
	
	@Reference(version = "1.0.0")
	ActionsFacadeAPI actionsFacadeAPI;
	

	@Reference(version = "1.0.0", timeout = 120000, retries = 3)
	TransactionsFacadeAPI transactionsFacadeAPI;
	
	@Reference(version = "1.0.0")
	DelegatebwFacadeAPI delegatebwFacadeAPI;


	@Value("${nodeos.path.chain}")
	String URL_CHAIN;
	@Value("${nodeos.path.chain.backup}")
	String URL_CHAIN_BACKUP;
	@Value("${eos.server.api}")
	String EOS_SERVER_API;
	@Value("${nodeos.path.history}")
	String URL_HISTORY;
	@Value("${nodeos.path.history.backup}")
	String URL_HISTORY_BACKUP;
	@ResponseBody
	@RequestMapping(value = "/getInfo")
	public Object getInfo(HttpServletRequest request) {
		EosResult resp = null;
		try {
			resp = new GetInfo().run(URL_CHAIN, URL_CHAIN_BACKUP, "");
			if (resp.isSuccess()) {
				return this.success(resp.getData());
			} else {
				return this.error(resp.getStatus(), resp.getData());
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return this.error(MLApiException.EOSRPC_FAIL, null);
	}

	@ResponseBody
	@RequestMapping(value = "/createAccount")
	public Object createAccount(@org.springframework.web.bind.annotation.RequestBody Map<String, String> requestMap,
			HttpServletRequest request) {
		logger.info("/createAccount request map : " + requestMap);
		JSONObject jsonObject = new JSONObject();
		try {
			String name = requestMap.get("username");
			if (StringUtils.isEmpty(name) || name.length() != 12) {
				return this.error(MLApiException.ACCOUNT_NAME_ERR, "账户名称必须为12位");
			}
			jsonObject.put("username", requestMap.get("username"));
			jsonObject.put("owner", requestMap.get("owner"));
			jsonObject.put("active", requestMap.get("active"));
		} catch (JSONException e2) {
			e2.printStackTrace();
			return this.error(MLApiException.PARAM_ERROR, null);
		}

		EosResult resp = null;
		try {
			resp = new CreateAccount().run(EOS_SERVER_API, jsonObject.toString()); // http://localhost:7001/account/create,需要在本地启动eos-server-api服务
			if (resp.isSuccess()) {
				return this.success(resp.getData());
			} else {
				return this.error(resp.getStatus(), resp.getData());
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return this.error(MLApiException.EOSRPC_FAIL, null);
	}

	@ResponseBody
	@RequestMapping(value = "/pushTransaction")
	public Object pushTransaction(@org.springframework.web.bind.annotation.RequestBody Map<String, String> requestMap,
			HttpServletRequest request) {
		logger.info("/pushTransaction request map : " + requestMap);

		String from = requestMap.get("from");
		String to = requestMap.get("to");
		String amount = requestMap.get("amount");
		String transactionData = requestMap.get("data");
		String memo = requestMap.get("memo");
		
		memo = memo==null ? "" : memo;
		
		EosResult resp = null;
		try {
			resp = new PushTransaction().run(URL_CHAIN, URL_CHAIN_BACKUP, transactionData);
			if (resp.isSuccess()) {
				Map<String, String> extr = new HashMap<>();
				extr.put("url", "transfer://");
				pushService.pushByTag(to, to + "收到一笔  " + amount  + " 转账." + "来自：" + from + ". 备注：" + memo, extr);
				return this.success(resp.getData());
			} else {
				return this.error(resp.getStatus(), resp.getData());
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return this.error(MLApiException.EOSRPC_FAIL, null);
	}

	@ResponseBody
	@RequestMapping(value = "/getCurrencyBalance")
	public Object getCurrencyBalance(
			@org.springframework.web.bind.annotation.RequestBody Map<String, String> requestMap,
			HttpServletRequest request) {
		logger.info("/getCurrencyBalance request map : " + requestMap);

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("code", requestMap.get("contract"));
			jsonObject.put("account", requestMap.get("account"));
			jsonObject.put("symbol", requestMap.get("symbol"));
		} catch (JSONException e2) {
			e2.printStackTrace();
			return this.error(MLApiException.PARAM_ERROR, null);
		}
		EosResult resp = null;
		try {
			resp = new GetBalance().run(URL_CHAIN, URL_CHAIN_BACKUP, jsonObject.toString());
			if (resp.isSuccess()) {
				return this.success(resp.getData());
			}else {
				System.out.println(this.error(resp.getStatus(), resp.getData()).toString());
				return this.error(resp.getStatus(), resp.getData());
			}
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
		return this.error(MLApiException.EOSRPC_FAIL, null);
		
	}

	@ResponseBody
	@RequestMapping(value = "/listProducers")
	public Object listProducers(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("json", true);
			jsonObject.put("lower_bound", "");
			jsonObject.put("limit", 130);
		} catch (JSONException e2) {
			e2.printStackTrace();
			return this.error(MLApiException.PARAM_ERROR, null);
		}
		EosResult resp = null;
		try {
			resp = new ListProducers().run(URL_CHAIN, URL_CHAIN_BACKUP, jsonObject.toString());
			if (resp.isSuccess()) {
				return this.success(JSONObject.parseObject(resp.getData()));
			}else {
				return this.error(resp.getStatus(), resp.getData());
			}
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
		return this.error(MLApiException.EOSRPC_FAIL, null);
	}

	@ResponseBody
	@RequestMapping(value = "/voteProducers")
	public Object voteProducers(@org.springframework.web.bind.annotation.RequestBody Map<String, String> requestMap,
			HttpServletRequest request) {
		logger.info("/voteProducers request map : " + requestMap);

		String transactionData = requestMap.get("data");
		
		EosResult resp = null;
		try {
			resp = new PushTransaction().run(URL_CHAIN, URL_CHAIN_BACKUP, transactionData);
			if (resp.isSuccess()) {
				return this.success(resp.getData());
			}else {
				return this.error(resp.getStatus(), resp.getData());
			}
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
		return this.error(MLApiException.EOSRPC_FAIL, null);
	}

	@ResponseBody
	@RequestMapping(value = "/getAccountInfo")
	public Object getAccountInfo(
			@org.springframework.web.bind.annotation.RequestBody Map<String, String> requestMap,
			HttpServletRequest request) {

		logger.info("/getAccountInfo request map : " + requestMap);
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("account_name", requestMap.get("username"));
//			jsonObject.put("account_name", "morning12345");
		} catch (JSONException e2) {
			e2.printStackTrace();
			return this.error(MLApiException.PARAM_ERROR, null);
		}
		EosResult resp = null;
		try {
			resp = new GetAccountInfo().run(URL_CHAIN, URL_CHAIN_BACKUP, jsonObject.toString());
			if (resp.isSuccess()) {
				return this.success(JSONObject.parseObject(resp.getData()));
			}else{
				return this.error(resp.getStatus(), resp.getData());
			}
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
		return this.error(MLApiException.EOSRPC_FAIL, null);
	}

	@ResponseBody
	@RequestMapping(value = "/getVotingInfo")
	public Object getVotingInfo(@org.springframework.web.bind.annotation.RequestBody Map<String, String> requestMap,
			HttpServletRequest request) {

		logger.info("/getVotingInfo request map : " + requestMap);
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("json", true);
			jsonObject.put("code", "eosio");
			jsonObject.put("scope", "eosio");
			jsonObject.put("table", "voters");
			jsonObject.put("table_key", "owner");
			jsonObject.put("lower_bound", requestMap.get("username"));
			// jsonObject.put("lower_bound", "useraaaaaaab");
			jsonObject.put("limit", 1);
		} catch (JSONException e2) {
			e2.printStackTrace();
			return this.error(MLApiException.PARAM_ERROR, null);
		}

		EosResult resp = null;
		try {
			resp = new GetVotingInfo().run(URL_CHAIN, URL_CHAIN_BACKUP, jsonObject.toString());
			if (resp.isSuccess()) {
				return this.success(JSONObject.parseObject(resp.getData()));
			}else{
				return this.error(resp.getStatus(), resp.getData());
			}
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
		return this.error(MLApiException.EOSRPC_FAIL, null);
	}
	
	@ResponseBody
	@RequestMapping(value = "/getUndelegatebwInfo")
	public Object getUndelegatebwInfo(
			@org.springframework.web.bind.annotation.RequestBody Map<String, String> requestMap,
			HttpServletRequest request) {

		logger.info("/getUndelegatebwInfo request map : " + requestMap);
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("json", true);
			jsonObject.put("code", "eosio");
			jsonObject.put("scope", requestMap.get("username"));
//			jsonObject.put("scope", "wrtggteweghg");
			jsonObject.put("table", "refunds");
			jsonObject.put("table_key", "owner");
//			jsonObject.put("lower_bound", requestMap.get("account_name"));
//			 jsonObject.put("lower_bound", "useraaaaaaaa");
			jsonObject.put("lower_bound", "");
			jsonObject.put("upper_bound", "");
			jsonObject.put("limit", 1);
		} catch (JSONException e2) {
			e2.printStackTrace();
			return this.error(MLApiException.PARAM_ERROR, null);
		}

		EosResult resp = null;
		try {
		resp = new GetUndelegatebwInfo().run(URL_CHAIN, URL_CHAIN_BACKUP, jsonObject.toString());
			if (resp.isSuccess()) {
				return this.success(JSONObject.parseObject(resp.getData()));
			}else {
				return this.error(resp.getStatus(), resp.getData());
			}
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
		return this.error(MLApiException.EOSRPC_FAIL, null);
	}

	@ResponseBody
	@RequestMapping(value = "/getKeyAccounts")
	public Object getKeyAccounts(@RequestBody Map<String, String> requestMap,
			HttpServletRequest request) {
		logger.info("/getKeyAccounts request map : " + requestMap);
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("public_key", requestMap.get("public_key"));
		} catch (JSONException e2) {
			e2.printStackTrace();
			return this.error(MLApiException.PARAM_ERROR, null);
		}
		EosResult resp = null;
		try {
			resp = new GetKeyAccounts().run(URL_HISTORY, URL_HISTORY_BACKUP, jsonObject.toString());
			if (resp.isSuccess()) {
				return this.success(JSONObject.parseObject(resp.getData()));
			}else {
				return this.error(resp.getStatus(), resp.getData());
			}
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
		return this.error(MLApiException.EOSRPC_FAIL, null);
	}
	
	
	
	@ResponseBody
	@RequestMapping(value = "/getActions")
	public Object getActions(@RequestBody Map<String, Object> requestMap,
			HttpServletRequest request) {
		logger.info("/getActions request map : " + requestMap);
		try {
			String code=requestMap.get("code").toString();//代币符号
			String account=requestMap.get("contract_account").toString();//合约账号
			String actor=requestMap.get("account_name").toString();//用户账户名
			//String page=requestMap.get("page").toString();
			int page=1;
			if (null != requestMap.get("page") && !requestMap.get("page").toString().isEmpty()) {
				page = Integer.parseInt(requestMap.get("page").toString());
			}
			int pageSize = 10;
			if (null != requestMap.get("countPerPage") && !requestMap.get("countPerPage").toString().isEmpty()) {
				pageSize = Integer.parseInt(requestMap.get("countPerPage").toString());
			}
			if (null == actor || actor.isEmpty()) {
				return this.error(MLApiException.PARAM_ERROR, requestMap);
			}
			String last_id="";//这一页最后一条的id
			if(null != requestMap.get("last_id") && !requestMap.get("last_id").toString().isEmpty()) {
				last_id = requestMap.get("last_id").toString();
			}
			
			MLResultList<JSONObject> result = null;
			if(last_id.equalsIgnoreCase("")) {
				result=transactionsFacadeAPI.findByAccountAndActor(page, pageSize, account, actor,code);
				if (!result.isSuccess()) {
					return this.error(result.getErrorCode(),result.getErrorHint(), null);
				}
				return this.success(result.getList());
			}else {
				if(last_id.equalsIgnoreCase("-1")) {
					last_id = "";
				}
				result=transactionsFacadeAPI.findByAccountAndActorNew(last_id, pageSize, account, actor,code);
				if (!result.isSuccess()) {
					return this.error(result.getErrorCode(),result.getErrorHint(), null);
				}
				return this.success(result.getList());
			}	
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return this.error(MLApiException.EOSRPC_FAIL, null);
		}
	}
	
	
//	@ResponseBody
//	@RequestMapping(value = "/getActions")
//	public Object getActions(@RequestBody Map<String, Object> requestMap,
//			HttpServletRequest request) {
//		logger.info("/getActions request map : " + requestMap);
//		JSONObject jsonObject = new JSONObject(requestMap);
//		EosResult resp = null;
//		try {
//			resp = new GetActions().run(URL_HISTORY, URL_HISTORY_BACKUP, jsonObject.toString());
//			if (resp.isSuccess()) {
//				return this.success(JSONObject.parseObject(resp.getData()));
//			}else {
//				return this.error(resp.getStatus(), resp.getData());
//			}
//		}catch (Exception e) {
//			logger.error(e.getMessage());
//		}
//		return this.error(MLApiException.EOSRPC_FAIL, null);
//	}
	
//	@ResponseBody
//	@RequestMapping(value = "/getActions")
//	public Object getActions(@RequestBody Map<String, Object> requestMap, HttpServletRequest request) {
//		logger.info("/getActions request map : " + requestMap);
//		JSONObject jsonObject = new JSONObject(requestMap);
//		EosResult resp = null;
//		try {
//
//			if (null == requestMap.get("account_name") || requestMap.get("account_name").toString().isEmpty()) {
//				return this.error(MLApiException.PARAM_ERROR, requestMap);
//			}
//			if (null == requestMap.get("page") || requestMap.get("page").toString().isEmpty()) {
//				// 兼容之前的接口
//				resp = new GetActions().run(URL_HISTORY, URL_HISTORY_BACKUP, jsonObject.toString());
//				if (resp.isSuccess()) {
//					System.out.println(this.success(JSONObject.parseObject(resp.getData())));
//					return this.success(JSONObject.parseObject(resp.getData()));
//				}else {
//					return this.error(resp.getStatus(), resp.getData());
//				}
//			} else {
//				// 分页新方式
//				int start_account_action_seq = Integer.parseInt(requestMap.get("start_account_action_seq").toString());
//				int countPerPage = 10;
//				if (null != requestMap.get("countPerPage") && !requestMap.get("countPerPage").toString().isEmpty()) {
//					countPerPage = Integer.parseInt(requestMap.get("countPerPage").toString());
//				}
//
//				JSONArray tempJsonArray = new JSONArray();
//				JSONArray JsonArrayResult = new JSONArray();
//				int pos = -1;
//				int offset = countPerPage;
//				do {
//					if(start_account_action_seq != -1) {
//						pos = start_account_action_seq - countPerPage+1;
//						if (pos < 0) {
//							pos = 0;
//							offset = start_account_action_seq+1;
//						}
//					}
//
//					JSONObject jo = new JSONObject();
//					jo.put("pos", pos);
//					jo.put("offset ", offset);
//					jo.put("account_name", requestMap.get("account_name").toString());
//
//					resp = new GetActions4Page().run(URL_HISTORY, URL_HISTORY_BACKUP, jo.toString());
//					if (resp.isSuccess()) {
//						JSONObject tempResult = JSONObject.parseObject(resp.getData());
//						JSONArray actionsArray = tempResult.getJSONArray("actions");
//
//						if (actionsArray.size() == 0) {
//							break;
//						}
//						tempJsonArray.addAll(actionsArray);
//						tempJsonArray = SortJsonArray.sortJsonArray(tempJsonArray, "account_action_seq");
//						if (tempJsonArray.size() >= countPerPage || actionsArray.size() == 0) {
//							for (int i = 0; i < tempJsonArray.size(); i++) {
//								if (i > countPerPage - 1) {
//									break;
//								}
//								JsonArrayResult.add(tempJsonArray.getJSONObject(i));
//							}
//							JSONObject res = new JSONObject();
//							res.put("actions", SortJsonArray.sortJsonArray(JsonArrayResult, "account_action_seq"));
////							EosResult er = new EosResult(MLApiException.SUCCESS, res.toString());
//							System.out.println(this.success(res));
//							return this.success(res);
//						}
//
//						if (tempJsonArray.size() > 0) {
//							JSONObject lastjo = tempJsonArray.getJSONObject(tempJsonArray.size() - 1);
//							start_account_action_seq = lastjo.getIntValue("account_action_seq")-1;
//						}
//
//					} else {
//						return this.error(resp.getStatus(), resp.getData());
//					}
//				} while (tempJsonArray.size() >= countPerPage);
//			}
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//		}
//		return this.error(MLApiException.EOSRPC_FAIL, null);
//	}
	
	@ResponseBody
	@RequestMapping(value = "/getGlobalInfo")
	public Object getGlobalInfo(@RequestBody Map<String, String> requestMap, HttpServletRequest request) {
		logger.info("/getGlobalInfo request map : " + requestMap);
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("json", true);
			jsonObject.put("code", "eosio");
			jsonObject.put("scope", "eosio");
			jsonObject.put("table", "global");
		} catch (JSONException e2) {
			e2.printStackTrace();
			return this.error(MLApiException.PARAM_ERROR, null);
		}

		EosResult resp = null;
		try {
		resp = new GetGlobalInfo().run(URL_CHAIN, URL_CHAIN_BACKUP, jsonObject.toString());
			if (resp.isSuccess()) {
				return this.success(JSONObject.parseObject(resp.getData()));
			}else {
				return this.error(resp.getStatus(), resp.getData());
			}
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
		return this.error(MLApiException.EOSRPC_FAIL, null);
	}
	
	@ResponseBody
	@RequestMapping(value = "/queryRamPrice")
	public Object queryRamPrice(@RequestBody Map<String, String> requestMap, HttpServletRequest request) {
		logger.info("/queryRamPrice request map : " + requestMap);
		try {
			String url = "https://tbeospre.mytokenpocket.vip/v1/ram_price";
			String result = HttpClientUtils.doGet(url);
			GsonBuilder gb = new GsonBuilder();
		    Gson g = gb.create();
		    Map<String, String> map = g.fromJson(result, new TypeToken<Map<String, String>>() {}.getType());
		    Double data= Double.parseDouble(map.get("data"));
		    Double realPrice = 1 * 1024 / data;
		    BigDecimal b = new BigDecimal(realPrice);
		    realPrice = b.setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue(); 
		    return this.success(realPrice);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return this.error(MLApiException.EOSRPC_FAIL, null);
	}
	
	@ResponseBody
	@RequestMapping(value = "/queryAcountBalance")
	public Object queryAcountBalance(@RequestBody Map<String, String> requestMap, HttpServletRequest request) {
		logger.info("/queryAcountBalance request map : " + requestMap);
		try {
			String account=requestMap.get("account");
			String symbol=requestMap.get("symbol");
			MLResultList<Tokens> list=tokensFacadeAPI.findByAccountAndSymbol(account, symbol);
			if(list.getList()==null|| list.getList().size()<=0) {
				  return this.error(MLApiException.CONTRACT_NOT_EXIST_ERROR, null);
			}
			//有个问题token表同一账户有多条数据取那一条？
			Double amount=list.getList().get(0).getAmount();
		    return this.success(amount);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return this.error(MLApiException.EOSRPC_FAIL, null);
	}
	
	@ResponseBody
	@RequestMapping(value = "/queryKeyAccounts")
	public Object queryKeyAccounts(@RequestBody Map<String, String> requestMap, HttpServletRequest request) {
		logger.info("/queryKeyAccounts request map : " + requestMap);
		try {
			String publicKey=requestMap.get("public_key");
			MLResultList<AccountsKeys> result=accountsKeysFacadeAPI.findByPublicKey(publicKey);
			if(!result.isSuccess()) {
				return this.error(MLApiException.EOSRPC_FAIL, null);
			}
			List<AccountsKeys> list=result.getList();
			if(list==null|| list.size()<=0) {
				return this.error(MLApiException.CONTRACT_NOT_EXIST_ERROR, null);
			}
			ArrayList<String>  accountNames = new ArrayList<String> ();
			for (AccountsKeys accountsKeys : list) {
				accountNames.add(accountsKeys.getAccount());
			}
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("account_names", accountNames);
			return this.success(jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return this.error(MLApiException.EOSRPC_FAIL, null);
	}
	

	@ResponseBody
	@RequestMapping(value = "/queryActions")
	public Object queryActions(@RequestBody Map<String, String> requestMap, HttpServletRequest request) {
		logger.info("/queryActions request map : " + requestMap);
		try {
			String accountName=requestMap.get("account_name");
			if (null == accountName || accountName.toString().isEmpty()) {
				return this.error(MLApiException.PARAM_ERROR, requestMap);
			}
			MLResultList<Actions> result=actionsFacadeAPI.findByAccountAndSymbol(accountName);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("actions", result.getList());
			return this.success(jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return this.error(MLApiException.EOSRPC_FAIL, null);
	}
	
	@ResponseBody
	@RequestMapping(value = "/isExistAccountName")
	public Object isExistAccountName(@RequestBody Map<String, String> requestMap, HttpServletRequest request) {
		logger.info("/queryKeyAccounts request map : " + requestMap);
		try {
			String account_name=requestMap.get("account_name");
			JSONObject getAccountJsonObject = new JSONObject();
			getAccountJsonObject.put("account_name", account_name);
			
			EosResult getAccountResp = new GetAccountInfo().run(URL_CHAIN, URL_CHAIN_BACKUP,
					getAccountJsonObject.toString());
			if (getAccountResp.isSuccess()) {
				return this.success(true);
			}else {
				return this.error(MLApiException.ACCOUNT_NAME_NOT_EXIST, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return this.error(MLApiException.EOSRPC_FAIL, null);
		}	
	}
	
	@ResponseBody
	@RequestMapping(value = "/isExistAccountNameAndPublicKey")
	public Object isExistAccountNameAndPublicKey(@RequestBody Map<String, String> requestMap, HttpServletRequest request) {
		logger.info("/queryKeyAccounts request map : " + requestMap);
		try {
			String account_name=requestMap.get("account_name");
			String owner = requestMap.get("owner");
			String active = requestMap.get("active");
			JSONObject getAccountJsonObject = new JSONObject();
			getAccountJsonObject.put("account_name", account_name);
			
			EosResult getAccountResp = new GetAccountInfo().run(URL_CHAIN, URL_CHAIN_BACKUP,
					getAccountJsonObject.toString());
			if (getAccountResp.isSuccess()) {
				JSONObject jo = JSONObject.parseObject(getAccountResp.getData());
				String account_name_j = jo.getString("account_name");
				if(null!=account_name_j && account_name_j.equalsIgnoreCase(account_name)) {
					JSONArray permissions = jo.getJSONArray("permissions");
					String ownerPublicKey = "";
					String activePublicKey = "";
					for(int i=0;i<permissions.size();i++) {
						JSONObject permission = permissions.getJSONObject(i);
						String key = permission.getJSONObject("required_auth").getJSONArray("keys").getJSONObject(0).getString("key");
						String perm_name = permission.getString("perm_name");
						
						if(perm_name.equalsIgnoreCase("owner")) {
							ownerPublicKey = key;
						}else if(perm_name.equalsIgnoreCase("active")) {
							activePublicKey = key;
						}
					}
					if(null != active && activePublicKey.equalsIgnoreCase(active) 
							&& (null == owner || owner.isEmpty() || (ownerPublicKey.equalsIgnoreCase(owner)))) {
						return this.success(true);
					}else{
						return this.error(MLApiException.ACCOUNT_NAME_EXIST, null);
					}
				}
				return this.error(MLApiException.ACCOUNT_NAME_NOT_EXIST, null);
			}else {
				return this.error(MLApiException.ACCOUNT_NAME_NOT_EXIST, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return this.error(MLApiException.EOSRPC_FAIL, null);
		}	
	}
	
	@ResponseBody
	@RequestMapping(value = "/getAccountDelbandInfo")
	public Object getAccountDelbandInfo(@RequestBody Map<String, String> requestMap,
			HttpServletRequest request) {

		logger.info("/getAccountInfo request map : " + requestMap);
		JSONObject jsonObject = new JSONObject();
		try {
			  jsonObject.put("json", true);
		      jsonObject.put("code", "eosio");
		      jsonObject.put("scope", requestMap.get("account_name"));
		      jsonObject.put("table", "delband");
		} catch (JSONException e2) {
			e2.printStackTrace();
			return this.error(MLApiException.PARAM_ERROR, null);
		}
		EosResult resp = null;
		try {
			resp = new GetAccountDelbandInfo().run(URL_CHAIN, URL_CHAIN_BACKUP, jsonObject.toString());
			if (resp.isSuccess()) {
				return this.success(JSONObject.parseObject(resp.getData()));
			}else{
				return this.error(resp.getStatus(), resp.getData());
			}
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
		return this.error(MLApiException.EOSRPC_FAIL, null);
	}
	
	@ResponseBody
	@RequestMapping(value = "/delegatebw")
	public Object delegatebw(@RequestBody Map<String, String> requestMap,HttpServletRequest request) {
		logger.info("/delegatebw request map : " + requestMap);
		    JSONObject jsonObject = new JSONObject();
			String username = requestMap.get("username");
			if (StringUtils.isEmpty(username) || username.length() != 12) {
				return this.error(MLApiException.ACCOUNT_NAME_ERR, "账户名称必须为12位");
			}
			jsonObject.put("account_name", requestMap.get("username"));
			MLResultObject<Delegatebw> resultObject=delegatebwFacadeAPI.findByAccountName(requestMap.get("username"));
			if(resultObject.getResult()!=null) {
				return this.error(MLApiException.DELEGATEBWED, "您已经免费抵押过，把机会留给别人吧");	
			}
			EosResult resp = null;
			try {
				resp = new GetAccountInfo().run(URL_CHAIN, URL_CHAIN_BACKUP, jsonObject.toString());
				if (resp.isSuccess()) {
					String  data=resp.getData();
					JSONObject json=JSONObject.parseObject(data);
					JSONObject cpu_limit=json.getJSONObject("cpu_limit");
					JSONObject net_limit=json.getJSONObject("net_limit");
					Integer cpuQuantity=cpu_limit.getInteger("available")/1000;
					Integer netQuantity=net_limit.getInteger("available")/1024;
	            	if(netQuantity>10&&cpuQuantity>5) {
	            		return this.error(MLApiException.SUFFICIENT_RESOURCES, "您仍有不少资源，把机会留给别人吧");
	            	}else {
	            		JSONObject jsonObject1 = new JSONObject();
	            		jsonObject1.put("username", username);
	        			Delegatebw delegatebw=new Delegatebw();
	        			delegatebw.setAccountName(username);
	        			delegatebw.setNet("0.5 EOS");
	        			delegatebw.setCpu("2.5 EOS");
	        			delegatebw.setStatus(0L);
	        			delegatebwFacadeAPI.save(delegatebw);
	        			EosResult resp1 = null;
	        			try {
	        				resp1 = new GetDelegatebw().run(EOS_SERVER_API, jsonObject1.toString()); // http://localhost:7001/resource/delegate,需要在本地启动eos-server-api服务
	        				if (resp1.isSuccess()) {
	        					return this.success("恭喜您，抵押成功！");
	        				} else {
	        					return this.error(MLApiException.SYS_ERROR, null);	
	        				}
	        			} catch (Exception e) {
	        				logger.error(e.getMessage());
	        			}
	        			return this.error(MLApiException.SYS_ERROR, null);
	            	}
				}else{
					return this.error(MLApiException.SYS_ERROR, null);
				}
			}catch (Exception e) {
				logger.error(e.getMessage());
			}
			return this.error(MLApiException.SYS_ERROR, null);
	}
	
}
