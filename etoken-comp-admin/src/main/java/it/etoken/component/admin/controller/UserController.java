package it.etoken.component.admin.controller;

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

import it.etoken.base.common.result.MLPage;
import it.etoken.base.common.result.MLResult;
import it.etoken.base.common.result.MLResultObject;
import it.etoken.base.common.utils.MathUtil;
import it.etoken.base.model.user.entity.User;
import it.etoken.base.model.user.vo.Invite;
import it.etoken.base.model.user.vo.Reward;
import it.etoken.component.admin.exception.MLAdminException;
import it.etoken.componet.user.facade.UserFacadeAPI;

@Controller
@RequestMapping(value = "/admin/user")
public class UserController extends BaseController {

	private final static Logger logger = LoggerFactory.getLogger(BaseController.class);

	@Reference(version = "1.0.0")
	UserFacadeAPI userFacadeAPI;

	@ResponseBody
	@RequestMapping(value = "/list")
	public Object login(@RequestBody Map<String, String> requestMap, HttpServletRequest request) {
		logger.info("/list request map : " + requestMap);
		try {
			String page = requestMap.get("page");
			String search = requestMap.get("search");
			if (StringUtils.isEmpty(page) || !MathUtil.isInteger(page)) {
				return this.error(MLAdminException.PARAM_ERROR, null);
			}
			MLResultObject<MLPage<User>> result = userFacadeAPI.findAll(search, Integer.parseInt(page));
			if (result.isSuccess()) {
				return this.success(result.getResult());
			} else {
				return this.error(result.getErrorCode(), result.getErrorHint(), null);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error(MLAdminException.SYS_ERROR, null);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/invite/list")
	public Object invite(@RequestBody Map<String, String> requestMap, HttpServletRequest request) {
		logger.info("/invite/list request map : " + requestMap);
		try {
			String page = requestMap.get("page");
			String search = requestMap.get("search");
			if (StringUtils.isEmpty(page) || !MathUtil.isInteger(page)) {
				return this.error(MLAdminException.PARAM_ERROR, null);
			}
			MLResultObject<MLPage<Invite>> result = userFacadeAPI.findInvite(search, Integer.parseInt(page));
			if (result.isSuccess()) {
				return this.success(result.getResult());
			} else {
				return this.error(result.getErrorCode(), result.getErrorHint(), null);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error(MLAdminException.SYS_ERROR, null);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/reward/list")
	public Object reward(@RequestBody Map<String, String> requestMap, HttpServletRequest request) {
		logger.info("/reward/list request map : " + requestMap);
		try {
			String page = requestMap.get("page");
			String search = requestMap.get("search");
			if (StringUtils.isEmpty(page) || !MathUtil.isInteger(page)) {
				return this.error(MLAdminException.PARAM_ERROR, null);
			}
			MLResultObject<MLPage<Reward>> result = userFacadeAPI.findReward(search, Integer.parseInt(page));
			if (result.isSuccess()) {
				return this.success(result.getResult());
			} else {
				return this.error(result.getErrorCode(), result.getErrorHint(), null);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error(MLAdminException.SYS_ERROR, null);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/changeStatus")
	public Object changeStatus(@RequestBody User user, HttpServletRequest request) {
		logger.info("/changeStatus request map : " + user);
		try {

			if (StringUtils.isEmpty(user.getId()) || StringUtils.isEmpty(user.getStatus())) {
				return this.error(MLAdminException.PARAM_ERROR, null);
			}
			MLResult result = userFacadeAPI.changeStatus(user.getId(), user.getStatus());
			if (result.isSuccess()) {
				return this.success(true);
			} else {
				return this.error(result.getErrorCode(), result.getErrorHint(), null);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error(MLAdminException.SYS_ERROR, null);
		}
	}
}