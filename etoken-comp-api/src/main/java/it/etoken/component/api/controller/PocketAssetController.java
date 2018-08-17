package it.etoken.component.api.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;

import it.etoken.base.common.result.MLResult;
import it.etoken.base.common.result.MLResultList;
import it.etoken.base.common.result.MLResultObject;
import it.etoken.base.model.market.entity.PocketAsset;
import it.etoken.component.api.exception.MLApiException;
import it.etoken.componet.coins.facade.PocketAssetFacadeAPI;
import it.etoken.componet.user.facade.UserFacadeAPI;

@Controller
@RequestMapping(value = "/pocketAsset")
public class PocketAssetController extends BaseController {

	private final static Logger logger = LoggerFactory.getLogger(BaseController.class);

	@Reference(version = "1.0.0", timeout = 10000)
	PocketAssetFacadeAPI pocketAssetFacadeAPI;
	
	@Reference(version = "1.0.0", timeout = 30000, retries = 0)
	PocketAssetFacadeAPI pocketAssetFacadeAPI2;
	
	@Reference(version = "1.0.0", timeout = 10000)
	UserFacadeAPI userFacadeAPI;

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
			try {
				pageN = Integer.parseInt(page);
			} catch (Exception ee) {
			}

			MLResultList<PocketAsset> result = pocketAssetFacadeAPI.findAll(pageN);

			if (result.isSuccess()) {
				List<PocketAsset> myList = result.getList();

				return this.success(myList);
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

			if (StringUtils.isEmpty(name)) {
				return this.error(MLApiException.PARAM_ERROR, "资产名称不能为空");
			}
			if (StringUtils.isEmpty(contract_account)) {
				return this.error(MLApiException.PARAM_ERROR, "合约帐号不能为空");
			}
			
			PocketAsset pocketAsset = new PocketAsset();
			pocketAsset.setName(name);
			pocketAsset.setContractAccount(contract_account);
			
			MLResult result = pocketAssetFacadeAPI.saveUpdate(pocketAsset);

			if (result.isSuccess()) {
				return this.success(true);
			} else {
				return this.error(result.getErrorCode(), result.getErrorHint(), "");
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
	@RequestMapping(value = "/getCreateEosAccountNeedPoint")
	public Object getCreateEosAccountNeedPoint(@RequestBody Map<String, String> requestMap, HttpServletRequest request) {
		try {
			
			MLResultObject<Long> needPointResult = userFacadeAPI.getCreateEosAccountNeedPoint();

			if (needPointResult.isSuccess()) {
				return this.success(needPointResult.getResult());
			} else {
				return this.error(needPointResult.getErrorCode(), needPointResult.getErrorHint(), "");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error(MLApiException.SYS_ERROR, null);
		}
	}
}
