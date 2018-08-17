package it.etoken.component.admin.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import it.etoken.base.common.exception.MLException;
import it.etoken.base.model.admin.vo.LoginAdmin;
import it.etoken.cache.service.CacheService;
import it.etoken.component.admin.exception.MLAdminException;
import it.etoken.component.admin.service.AdminService;

@Controller
@RequestMapping(value = "/")
public class CommonController extends BaseController {

	private final static Logger logger = LoggerFactory.getLogger(BaseController.class);

	@Autowired
	AdminService adminService;

	@Autowired
	CacheService cacheService;

	@ResponseBody
	@RequestMapping(value = "/login")
	public Object login(@RequestBody Map<String, String> requestMap, HttpServletRequest request) {
		logger.info("/login request map : " + requestMap);
		try {
			String username = requestMap.get("userName");
			String password = requestMap.get("password");
			if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
				return this.error(MLAdminException.PARAM_ERROR, null);
			}
			LoginAdmin result = adminService.login(username, password);
			return this.success(result);
		} catch (MLException ex) {
			logger.error(ex.getMessage(), ex);
			return this.error(ex.getCode(), ex.getMsg(), null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error(MLAdminException.SYS_ERROR, null);
		}
	}
}
