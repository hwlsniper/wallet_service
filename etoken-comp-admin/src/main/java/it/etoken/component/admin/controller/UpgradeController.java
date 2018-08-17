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

import it.etoken.base.model.admin.entity.Upgrade;
import it.etoken.component.admin.exception.MLAdminException;
import it.etoken.component.admin.service.UpgradeService;

@Controller
@RequestMapping(value = "/admin/upgrade")
public class UpgradeController extends BaseController {

	private final static Logger logger = LoggerFactory.getLogger(BaseController.class);

	@Autowired
	UpgradeService upgradeService;

	@ResponseBody
	@RequestMapping(value = "/get")
	public Object get(@RequestBody Map<String, String> requestMap, HttpServletRequest request) {
		logger.info("/get request map : " + requestMap);
		try {
			String os = requestMap.get("os");
			if (StringUtils.isEmpty(os)) {
				return this.error(MLAdminException.PARAM_ERROR, null);
			}
			return this.success(upgradeService.findByOs(os));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error(MLAdminException.SYS_ERROR, null);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/list")
	public Object list(@RequestBody Map<String, String> requestMap, HttpServletRequest request) {
		logger.info("/get request map : " + requestMap);
		try {
			return this.success(upgradeService.findAll());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error(MLAdminException.SYS_ERROR, null);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/update")
	public Object update(@RequestBody Upgrade upgrade, HttpServletRequest request) {
		logger.info("/update request map : " + upgrade);
		try {
			if (StringUtils.isEmpty(upgrade.getId())) {
				return this.error(MLAdminException.PARAM_ERROR, null);
			}
			upgradeService.update(upgrade);
			return this.success(null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error(MLAdminException.SYS_ERROR, null);
		}
	}
}
