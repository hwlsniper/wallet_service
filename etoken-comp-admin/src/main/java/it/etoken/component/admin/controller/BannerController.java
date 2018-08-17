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

import it.etoken.base.common.exception.MLException;
import it.etoken.base.common.result.MLResult;
import it.etoken.base.common.result.MLResultList;
import it.etoken.base.common.utils.MathUtil;
import it.etoken.base.model.news.entity.Banner;
import it.etoken.base.model.news.entity.News;
import it.etoken.component.admin.exception.MLAdminException;
import it.etoken.componet.news.facade.BannerFacadeAPI;

@Controller
@RequestMapping(value = "/admin/banner")
public class BannerController extends BaseController {

	private final static Logger logger = LoggerFactory.getLogger(BaseController.class);

	@Reference(version = "1.0.0")
	BannerFacadeAPI bannerFacadeAPI;

	@ResponseBody
	@RequestMapping(value = "/list")
	public Object login(@RequestBody Map<String, String> requestMap, HttpServletRequest request) {
		logger.info("/list request map : " + requestMap);
		try {
			String page = requestMap.get("page");
			if (StringUtils.isEmpty(page) || !MathUtil.isInteger(page)) {
				return this.error(MLAdminException.PARAM_ERROR, null);
			}
			MLResultList<Banner> result = bannerFacadeAPI.findAll(Integer.parseInt(page));
			if(result.isSuccess()) {
				return this.success(result.getList());
			}else {
				return this.error(result.getErrorCode(),result.getErrorHint(), null);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error(MLAdminException.SYS_ERROR, null);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(@RequestBody Banner params, HttpServletRequest request) {
		logger.info("/add request map : " + params);
		try {
			if (StringUtils.isEmpty(params.getTitle()) || StringUtils.isEmpty(params.getUrl())
					|| StringUtils.isEmpty(params.getImg())) {
				return this.error(MLAdminException.PARAM_ERROR, null);
			}
			MLResult r = bannerFacadeAPI.saveUpdate(params);
			if(r.isSuccess()) {
				return this.success(true);
			}else {
				return this.error(r.getErrorCode(),r.getErrorHint(), null);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error(MLAdminException.SYS_ERROR, null);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public Object update(@RequestBody Banner params, HttpServletRequest request) {
		logger.info("/update request map : " + params);
		try {
			if (StringUtils.isEmpty(params.getTitle()) || StringUtils.isEmpty(params.getUrl())
					|| StringUtils.isEmpty(params.getImg())) {
				return this.error(MLAdminException.PARAM_ERROR, null);
			}
			MLResult r = bannerFacadeAPI.saveUpdate(params);
			if(r.isSuccess()) {
				return this.success(null);
			}else {
				return this.error(r.getErrorCode(),r.getErrorHint(),null);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error(MLAdminException.SYS_ERROR, null);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public Object delete(@RequestBody News params, HttpServletRequest request) {
		logger.info("/update request map : " + params);
		try {
			if (params.getId() == null) {
				return this.error(MLAdminException.PARAM_ERROR, null);
			}
			MLResult r =bannerFacadeAPI.delete(params.getId());
			if(r.isSuccess()) {
				return this.success(null);
			}else {
				return this.error(r.getErrorCode(),r.getErrorHint(),null);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error(MLAdminException.SYS_ERROR, null);
		}
	}
}
