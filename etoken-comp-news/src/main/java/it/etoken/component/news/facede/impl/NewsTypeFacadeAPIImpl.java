package it.etoken.component.news.facede.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;

import it.etoken.base.common.exception.MLException;
import it.etoken.base.common.result.MLResult;
import it.etoken.base.common.result.MLResultList;
import it.etoken.base.common.result.MLResultObject;
import it.etoken.base.model.news.entity.NewsType;
import it.etoken.base.model.news.vo.NewsRoute;
import it.etoken.base.model.user.vo.LoginUser;
import it.etoken.component.news.service.NewsTypeService;
import it.etoken.componet.news.facade.NewsTypeFacadeAPI;

@Service(version = "1.0.0")
public class NewsTypeFacadeAPIImpl implements NewsTypeFacadeAPI {

	private final static Logger logger = LoggerFactory.getLogger(NewsTypeFacadeAPIImpl.class);

	@Autowired
	NewsTypeService newsTypeService;

	@Override
	public MLResultList<NewsRoute> getNewsType(int page) {
		try {
			Page<NewsType> result = newsTypeService.findAll(page);
			List<NewsRoute> rs = new ArrayList<>();
			for (NewsType ty : result.getResult()) {
				NewsRoute r = new NewsRoute();
				r.setKey(String.valueOf(ty.getId()));
				r.setTitle(ty.getName());
				r.setType(ty.getType());
				r.setUrl(ty.getUrl());
				rs.add(r);
			}
			return new MLResultList<NewsRoute>(rs);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultList<NewsRoute>(e);
		}
	}

	@Override
	public MLResult saveUpdate(NewsType news) {
		try {
			newsTypeService.saveUpdate(news);
			return new MLResult(true);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultObject<LoginUser>(e);
		}
	}

	@Override
	public MLResult delete(Long id) {
		try {
			newsTypeService.delete(id);
			return new MLResult(true);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResult(e);
		}
	}

	@Override
	public MLResultList<NewsType> findAll() {
		try {
			Page<NewsType> result = newsTypeService.findAll(1);
			return new MLResultList<NewsType>(result);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultList<NewsType>(e);
		}
	}

}
