package it.etoken.component.api.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import it.etoken.base.common.result.MLResultList;
import it.etoken.base.model.market.entity.EosElector;
import it.etoken.base.model.market.vo.EosElectorVo;
import it.etoken.cache.service.CacheService;
import it.etoken.component.api.eosrpc.EosResult;
import it.etoken.component.api.eosrpc.ListProducers;
import it.etoken.component.api.exception.MLApiException;
import it.etoken.componet.coins.facade.EosElectorFacadeAPI;


@Controller
@RequestMapping(value = "/eoselector")
public class EosElectorController extends BaseController{

	private final static Logger logger = LoggerFactory.getLogger(BaseController.class);
	
	@Reference(version="1.0.0")
	EosElectorFacadeAPI eosElectorFacadeAPI;
	
	@Autowired
	CacheService cacheService;
	
	@Value("${nodeos.path.chain}")
	String URL_CHAIN;
	
	@Value("${nodeos.path.chain.backup}")
	String URL_CHAIN_BACKUP;
	/**
	 * 获取候选人列表
	 * @param requestMap
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/list")
	public Object list(@RequestParam Map<String, String> requestMap, HttpServletRequest request)
	{
		try {
			MLResultList<EosElector> result = eosElectorFacadeAPI.findAll(1);
			
			if(result.isSuccess()){
				List<EosElector> myList = result.getList();
				
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("json", true);
				jsonObject.put("lower_bound", "");
				jsonObject.put("limit", 130);
				EosResult resp = new ListProducers().run(URL_CHAIN, URL_CHAIN_BACKUP, jsonObject.toString());
				
				if (resp.isSuccess()) {
					JSONObject jso = JSONObject.parseObject(resp.getData());
					BigDecimal total_producer_vote_weight = jso.getBigDecimal("total_producer_vote_weight");
					JSONArray rows = jso.getJSONArray("rows");
					List<EosElectorVo> myResultList = new ArrayList<EosElectorVo>();
					for(int i=0;i<rows.size();i++){
						JSONObject row = rows.getJSONObject(i);
						String owner = row.getString("owner");
						BigDecimal total_votes = row.getBigDecimal("total_votes");
						String url = row.getString("url");
						for(EosElector eosElector : myList) {
							if(owner.equalsIgnoreCase(eosElector.getAccount())) {
								EosElectorVo eosElectorVo = new EosElectorVo();
								BeanUtils.copyProperties(eosElector, eosElectorVo);
								eosElectorVo.setRanking(i+1);
								eosElectorVo.setTotal_votes(total_votes);
								eosElectorVo.setUrl(url);
								BigDecimal total_votes_percent = total_votes.divide(total_producer_vote_weight,2);
								total_votes_percent.setScale(2, BigDecimal.ROUND_HALF_UP);
								eosElectorVo.setTotal_votes_percent(total_votes_percent);
								myResultList.add(eosElectorVo);
								break;
							}
						}


						}
					return this.success(myResultList);
				}else {
					return this.error(resp.getStatus(), resp.getData());
				}
			}else{
				return this.error(result.getErrorCode(),result.getErrorHint(),result.getList());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return this.error(MLApiException.SYS_ERROR,null);
		}
	}
}
