package it.etoken.component.news.facede.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

import it.etoken.base.common.exception.MLException;
import it.etoken.base.common.result.MLResult;
import it.etoken.base.common.result.MLResultObject;
import it.etoken.base.model.news.entity.NewsStatistics;
import it.etoken.component.news.service.NewsStatisticsService;
import it.etoken.componet.news.facade.NewsStatisticsFacadeAPI;

@Service(version = "1.0.0")
public class NewsStatisticsFacadeAPIImpl implements NewsStatisticsFacadeAPI {

	private final static Logger logger = LoggerFactory.getLogger(NewsStatisticsFacadeAPIImpl.class);

	@Autowired
	NewsStatisticsService newsStatisticsService;

	@Override
	public MLResultObject<NewsStatistics> findByNid(Long nid) {
		try {
			NewsStatistics result = newsStatisticsService.findStatistics(nid);
			return new MLResultObject<NewsStatistics>(result);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultObject<NewsStatistics>(e);
		}
	}

	@Override
	public MLResult updateUp(Long nid) {
		try {
			newsStatisticsService.updateUp(nid);
			return new MLResult(true);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResult(e);
		}
	}

	@Override
	public MLResult updateDown(Long nid) {
		try {
			newsStatisticsService.updateDown(nid);
			return new MLResult(true);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResult(e);
		}
	}

	@Override
	public MLResult updateShare(Long nid) {
		try {
			newsStatisticsService.updateShare(nid);
			return new MLResult(true);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResult(e);
		}
	}

	@Override
	public MLResult updateView(Long nid) {
		try {
			newsStatisticsService.updateView(nid);
			return new MLResult(true);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResult(e);
		}
	}
}
