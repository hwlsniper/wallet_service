package it.etoken.component.api.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;

import it.etoken.base.common.exception.MLCommonException;
import it.etoken.base.common.exception.MLException;
import it.etoken.base.common.jpush.PushService;
import it.etoken.base.common.result.MLResult;
import it.etoken.base.common.result.MLResultList;
import it.etoken.base.common.result.MLResultObject;
import it.etoken.base.model.news.entity.Banner;
import it.etoken.base.model.news.entity.News;
import it.etoken.base.model.news.vo.NewsRoute;
import it.etoken.base.model.news.vo.NewsVO;
import it.etoken.base.model.user.entity.UserPointRecord;
import it.etoken.cache.service.CacheService;
import it.etoken.component.api.exception.MLApiException;
import it.etoken.componet.news.facade.BannerFacadeAPI;
import it.etoken.componet.news.facade.NewsFacadeAPI;
import it.etoken.componet.news.facade.NewsStatisticsFacadeAPI;
import it.etoken.componet.news.facade.NewsTypeFacadeAPI;
import it.etoken.componet.user.facade.UserFacadeAPI;
import it.etoken.componet.user.point.UserPointType;

@Controller
@RequestMapping(value = "/news")
public class NewsController extends BaseController {

	private final static Logger logger = LoggerFactory.getLogger(BaseController.class);

	@Reference(version = "1.0.0")
	NewsFacadeAPI newsFacadeAPI;

	@Reference(version = "1.0.0")
	NewsTypeFacadeAPI newsTypeFacadeAPI;

	@Reference(version = "1.0.0")
	NewsStatisticsFacadeAPI newsStatisticsFacadeAPI;

	@Reference(version = "1.0.0")
	BannerFacadeAPI bannerFacadeAPI;

	@Reference(version = "1.0.0")
	UserFacadeAPI userFacadeAPI;

	@Autowired
	CacheService cacheService;
	
	@Autowired
	private PushService pushService;

	/**
	 * 新闻类型
	 * 
	 * @param requestMap
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/type")
	public Object types(@RequestParam Map<String, String> requestMap, HttpServletRequest request) {
		logger.info("/news/type request map : " + requestMap);
		try {
			MLResultList<NewsRoute> result = newsTypeFacadeAPI.getNewsType(1);
			if (result.isSuccess()) {
				return this.success(result.getList());
			} else {
				return this.error(result.getErrorCode(), result.getErrorHint(), result.getList());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error(MLApiException.SYS_ERROR, null);
		}
	}

	/**
	 * 新闻类型
	 * 
	 * @param requestMap
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/{tid}")
	public Object news(@PathVariable String tid, @RequestParam Map<String, String> requestMap,
			HttpServletRequest request) {
		logger.info("/news/" + tid + " request map : " + requestMap);
		try {
			String page = requestMap.get("page");
			if (!StringUtils.isEmpty(page) && !StringUtils.isEmpty(tid)) {
				MLResultList<NewsVO> result = newsFacadeAPI.getNewsByTid(Long.parseLong(tid), Integer.parseInt(page));
				if (result.isSuccess()) {
					return this.success(result.getList());
				} else {
					return this.error(result.getErrorCode(), result.getErrorHint(), result.getList());
				}
			} else {
				return this.error(MLApiException.PARAM_ERROR, null);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error(MLApiException.SYS_ERROR, null);
		}
	}

	/**
	 * 新闻类型
	 * 
	 * @param requestMap
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/banner")
	public Object banner(@RequestParam Map<String, String> requestMap, HttpServletRequest request) {
		logger.info("/news/banner request map : " + requestMap);
		try {
			MLResultList<Banner> result = bannerFacadeAPI.findAll(1);
			if (result.isSuccess()) {
				return this.success(result.getList());
			} else {
				return this.error(result.getErrorCode(), result.getErrorHint(), result.getList());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error(MLApiException.SYS_ERROR, null);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/up/{id}")
	public Object up(@PathVariable Long id, HttpServletRequest request) {
		logger.info("/up/" + id);
		try {
			MLResult result = newsStatisticsFacadeAPI.updateUp(id);
			if (result.isSuccess()) {
				String uid = request.getHeader("uid");
				String token = request.getHeader("token");
				if (!StringUtils.isEmpty(uid) && !StringUtils.isEmpty(token) 
						&& token.equals(cacheService.getUserToken(Long.parseLong(uid)))) {
					MLResultObject<UserPointRecord> pointResult = userFacadeAPI.updatePoint(UserPointType.UP, uid);
					if (pointResult.isSuccess()) {
						return this.success(pointResult.getResult());
					} 
				}
				
				return this.success(true);
			} else {
				return this.error(result.getErrorCode(), result.getErrorHint(), null);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error(MLApiException.SYS_ERROR, null);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/down/{id}")
	public Object down(@PathVariable Long id, HttpServletRequest request) {
		logger.info("/down/" + id);
		try {
			MLResult result = newsStatisticsFacadeAPI.updateDown(id);
			if (result.isSuccess()) {
				String uid = request.getHeader("uid");
				String token = request.getHeader("token");
				if (!StringUtils.isEmpty(uid) && !StringUtils.isEmpty(token) 
						&& token.equals(cacheService.getUserToken(Long.parseLong(uid)))) {
					MLResultObject<UserPointRecord> pointResult = userFacadeAPI.updatePoint(UserPointType.DOWN, uid);
					if (pointResult.isSuccess()) {
						return this.success(pointResult.getResult());
					}
				}
				
				return this.success(true);
			} else {
				return this.error(result.getErrorCode(), result.getErrorHint(), null);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error(MLApiException.SYS_ERROR, null);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/share/{id}")
	public Object share(@PathVariable Long id, HttpServletRequest request) {
		logger.info("/share/" + id);
		try {
			MLResult result = newsStatisticsFacadeAPI.updateShare(id);
			if (result.isSuccess()) {
				return this.success(true);
			} else {
				return this.error(result.getErrorCode(), result.getErrorHint(), null);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error(MLApiException.SYS_ERROR, null);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/view/{id}")
	public Object view(@PathVariable Long id, HttpServletRequest request) {
		logger.info("/view/" + id);
		try {
			MLResult result = newsStatisticsFacadeAPI.updateView(id);
			if (result.isSuccess()) {
				return this.success(true);
			} else {
				return this.error(result.getErrorCode(), result.getErrorHint(), null);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error(MLApiException.SYS_ERROR, null);
		}
	}

	
	@ResponseBody
	@RequestMapping(value = "/testPush")
	public Object testPush(HttpServletRequest request) {
//		logger.info("/testPush" + id);
		try{
//			MLResultObject<News> result = newsFacadeAPI.getNewsById(Long.parseLong(id));
//			News n=result.getResult();
//			if(n==null) {
//				throw new MLException(MLCommonException.system_err);
//			}
			Map<String,String> extr = new HashMap<>();
//			extr.put("url",n.getUrl());
			pushService.pushByTag("newsTestPush", "test news",extr);
			return this.success(true);
		}catch (MLException ex) {
			logger.error(ex.toString());
			return this.error(MLApiException.SYS_ERROR, null);
		}catch (Exception e) {
			logger.error(e.toString());
			return this.error(MLApiException.SYS_ERROR, null);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/redirect/{uid}/{code}/{id}")
	public void redirect(@PathVariable String uid, @PathVariable String code, @PathVariable String id,
			HttpServletRequest request, HttpServletResponse response) {
		logger.info("/redirect/" + id);
		try {
			if (StringUtils.isEmpty(id)) {
				return;
			}
			MLResultObject<News> result = newsFacadeAPI.getNewsById(Long.parseLong(id));
			if (!result.isSuccess()) {
				return;
			}
			News news = result.getResult();
			String redirectUrl = news.getUrl();
			response.sendRedirect(redirectUrl);
			if (uid == null || code == null || (code.length() < 4) || "ntk".equals(code) || "nuid".equals(uid)) {
				return;
			}

			// 比较token的前四个字符
			String s = cacheService.getUserToken(Long.parseLong(uid)).substring(0, 4);
			if (!code.substring(0, 4).equals(s)) {
				return;
			}
			userFacadeAPI.updatePoint(UserPointType.SHARE, uid);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
