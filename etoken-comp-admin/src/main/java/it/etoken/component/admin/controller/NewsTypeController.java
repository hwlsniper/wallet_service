package it.etoken.component.admin.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;

import it.etoken.base.common.exception.MLException;
import it.etoken.base.common.result.MLResult;
import it.etoken.base.common.result.MLResultList;
import it.etoken.base.model.news.entity.NewsType;
import it.etoken.component.admin.exception.MLAdminException;
import it.etoken.componet.news.facade.NewsTypeFacadeAPI;

@Controller
@RequestMapping(value = "/admin/newsType")
public class NewsTypeController extends BaseController {

	private final static Logger logger = LoggerFactory.getLogger(BaseController.class);

	@Reference(version="1.0.0")
	NewsTypeFacadeAPI newsTypeFacadeAPI;

	@ResponseBody
	@RequestMapping(value = "/list")
	public Object login(@RequestParam Map<String, String> requestMap, HttpServletRequest request) {
		logger.info("/list request map : " + requestMap);
		try {
			MLResultList<NewsType> result = newsTypeFacadeAPI.findAll();
			return this.success(result.getList());
		} catch (MLException ex) {
			logger.error(ex.getMessage(), ex);
			return this.error(ex.getCode(), ex.getMsg(), null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error(MLAdminException.SYS_ERROR, null);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(@RequestBody NewsType params, HttpServletRequest request) {
		logger.info("/add request map : " + params);
		try {
			if (StringUtils.isEmpty(params.getName())) {
				return this.error(MLAdminException.PARAM_ERROR, null);
			}
			MLResult r = newsTypeFacadeAPI.saveUpdate(params);
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
	@RequestMapping(value = "/update")
	public Object update(@RequestBody NewsType params, HttpServletRequest request) {
		logger.info("/update request map : " + params);
		try {
			if (StringUtils.isEmpty(params.getName()) || params.getId()==null) {
				return this.error(MLAdminException.PARAM_ERROR, null);
			}
			MLResult r = newsTypeFacadeAPI.saveUpdate(params);
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
	public Object delete(@RequestBody NewsType params, HttpServletRequest request) {
		logger.info("/update request map : " + params);
		try {
			if (params.getId()==null) {
				return this.error(MLAdminException.PARAM_ERROR, null);
			}
			MLResult r = newsTypeFacadeAPI.delete(params.getId());
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
