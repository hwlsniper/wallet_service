package it.etoken.component.news.facede.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;

import it.etoken.base.common.exception.MLException;
import it.etoken.base.common.result.MLResult;
import it.etoken.base.common.result.MLResultList;
import it.etoken.base.common.result.MLResultObject;
import it.etoken.base.model.news.entity.Banner;
import it.etoken.component.news.service.BannerService;
import it.etoken.componet.news.facade.BannerFacadeAPI;

@Service(version = "1.0.0")
public class BannerFacadeAPIImpl implements BannerFacadeAPI {

	private final static Logger logger = LoggerFactory.getLogger(BannerFacadeAPIImpl.class);

	@Autowired
	BannerService bannerService;

	@Override
	public MLResultList<Banner> findAll(int page) {
		try {
			Page<Banner> result = bannerService.findAll(page);
			return new MLResultList<Banner>(result.getResult());
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultList<Banner>(e);
		}
	}

	@Override
	public MLResultObject<Banner> findById(Long id) {
		try {
			Banner b = bannerService.findById(id);
			return new MLResultObject<Banner>(b);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultObject<Banner>(e);
		}
	}

	@Override
	public MLResult saveUpdate(Banner banner) {
		try {
			bannerService.saveUpdate(banner);
			return new MLResult(true);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResult(e);
		}
	}

	@Override
	public MLResult delete(Long id) {
		try {
			bannerService.delete(id);
			return new MLResult(true);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResult(e);
		}
	}

}
