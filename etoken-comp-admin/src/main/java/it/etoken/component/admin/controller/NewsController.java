package it.etoken.component.admin.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
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
import com.alibaba.fastjson.JSONObject;

import it.etoken.base.common.result.MLPage;
import it.etoken.base.common.result.MLResult;
import it.etoken.base.common.result.MLResultList;
import it.etoken.base.common.result.MLResultObject;
import it.etoken.base.common.utils.HtmlUtils;
import it.etoken.base.common.utils.MathUtil;
import it.etoken.base.model.news.entity.HtmlTemplate;
import it.etoken.base.model.news.entity.News;
import it.etoken.cache.service.CacheService;
import it.etoken.component.admin.exception.MLAdminException;
import it.etoken.componet.news.facade.HtmlTemplateFacadeAPI;
import it.etoken.componet.news.facade.NewsFacadeAPI;

@Controller
@RequestMapping(value = "/admin/news")
public class NewsController extends BaseController {

	private final static Logger logger = LoggerFactory.getLogger(BaseController.class);

	@Reference(version = "1.0.0",timeout=10000)
	NewsFacadeAPI newsFacadeAPI;
	
	@Reference(version = "1.0.0",timeout=30000,retries=0)
	NewsFacadeAPI newsFacadeAPI2;
	
	@Reference(version = "1.0.0", timeout = 30000, retries=0)
	HtmlTemplateFacadeAPI htmlTemplateFacadeAPI;

	@Autowired
	CacheService cacheService;
	
	@Value("${html.server}")
	String HtmlServer;

	@Value("${html.save}")
	String HtmlSave;

	@ResponseBody
	@RequestMapping(value = "/list")
	public Object login(@RequestBody Map<String, String> requestMap, HttpServletRequest request) {
		logger.info("/list request map : " + requestMap);
		try {
			String page = requestMap.get("page");
			String title = requestMap.get("title");
			if (StringUtils.isEmpty(page) || !MathUtil.isInteger(page)) {
				return this.error(MLAdminException.PARAM_ERROR, null);
			}
			MLResultObject<MLPage<News>> result = newsFacadeAPI.findAll(Integer.parseInt(page),title);
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
	@RequestMapping(value = "/add")
	public Object add(@RequestBody News params, HttpServletRequest request) {
		logger.info("/add request map : " + params);
		try {
			if (StringUtils.isEmpty(params.getTitle()) || StringUtils.isEmpty(params.getTid())
					|| StringUtils.isEmpty(params.getContent()) || StringUtils.isEmpty(params.getSource())) {
				return this.error(MLAdminException.PARAM_ERROR, null);
			}
			//生成html
			if(params.getHtml()!=null) {
				String url = HtmlUtils.gemHtml(params.getHtml(), HtmlSave);
			    params.setUrl(HtmlServer+url);
			}
			MLResult r = newsFacadeAPI2.saveUpdate(params);
			if (r.isSuccess()) {
				return this.success(null);
			} else {
				return this.error(r.getErrorCode(), r.getErrorHint(), null);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error(MLAdminException.SYS_ERROR, null);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public Object update(@RequestBody News params, HttpServletRequest request) {
		logger.info("/update request map : " + params);
		try {
			if (StringUtils.isEmpty(params.getTitle()) || StringUtils.isEmpty(params.getTid())
					|| StringUtils.isEmpty(params.getContent()) || StringUtils.isEmpty(params.getSource())) {
				return this.error(MLAdminException.PARAM_ERROR, null);
			}
			//生成html
			if(params.getHtml()!=null) {
				String url = HtmlUtils.gemHtml(params.getHtml(), HtmlSave);
			    params.setUrl(HtmlServer+url);
			}
			MLResult r = newsFacadeAPI.saveUpdate(params);
			if (r.isSuccess()) {
				return this.success(null);
			} else {
				return this.error(r.getErrorCode(), r.getErrorHint(), null);
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
			MLResult r = newsFacadeAPI.delete(params.getId());
			if (r.isSuccess()) {
				return this.success(null);
			} else {
				return this.error(r.getErrorCode(), r.getErrorHint(), null);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error(MLAdminException.SYS_ERROR, null);
		}
	}


	@ResponseBody
	@RequestMapping(value = "/push")
	public Object push(@RequestBody News params, HttpServletRequest request) {
		logger.info("/update request map : " + params);
		try {
			if (params.getId() == null) {
				return this.error(MLAdminException.PARAM_ERROR, null);
			}
			MLResult r = newsFacadeAPI2.push(params.getId());
			if (r.isSuccess()) {
				return this.success(null);
			} else {
				return this.error(r.getErrorCode(), r.getErrorHint(), null);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error(MLAdminException.SYS_ERROR, null);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/template")
	//查询所有资讯并且根据内容重新生成静态文件
	public String findAllNewsGenerateTemplate(@RequestBody(required=false) Map<String, String> requestMap, HttpServletRequest request) {
			try {
			MLResultList<News> m=newsFacadeAPI.findAllNews();
			List<News> list=m.getList();
			for (News news : list) {
				String content=news.getContent();
				String title=news.getTitle();
//				Date date=news.getCreatedate();
				String url=news.getUrl();
				String[] aa= url.split("/");
				String folderName=aa[4];
				String fileName=aa[5];
				String url_new=HtmlSave+"/"+folderName+"/"+fileName;
				MLResultObject<HtmlTemplate> ml=htmlTemplateFacadeAPI.findTemplate();
				String template=ml.getResult().getTemplate();
				Double eosprice=findEosPrice();
				if(news.getTid()!=12 && news.getHtml()!=null && !"".equals(news.getHtml())  && news.getHtml().length()!=0) {
						 HtmlUtils.regemHtmlForInformation(news.getHtml(), url_new);
				}else {
					HtmlUtils.regemHtml(content, title, eosprice.toString(), url_new,template);
				}
			}
			System.out.println("重新生成html模板成功");
			return "重新生成html模板成功";
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "重新生成html模板失败";
			}
		}

	//获取当前eos价格
	public Double findEosPrice() {
		double eosrate = 0;
		double peos = 0;
	    double peosD = 0;
		try {
			   BigDecimal eosrateB = cacheService.get("CNY_USDT",BigDecimal.class);
			      if(null != eosrateB) {
			        eosrate = eosrateB.doubleValue();
			      }
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		return peos;
	}
}
