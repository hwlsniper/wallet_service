package it.etoken.component.news.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.etoken.base.common.exception.MLCommonException;
import it.etoken.base.common.exception.MLException;
import it.etoken.base.model.news.entity.NewsStatistics;
import it.etoken.component.news.dao.mapper.NewsStatisticsMapper;
import it.etoken.component.news.service.NewsStatisticsService;

@Component
@Transactional
public class NewsStatisticsServiceImpl implements NewsStatisticsService {

	private final static Logger logger = LoggerFactory.getLogger(NewsTypeServiceImpl.class);

	@Autowired
	private NewsStatisticsMapper newsStatisticsMapper;

	@Override
	public void addNewsStatistics(NewsStatistics ns) throws MLException {
		try{
			newsStatisticsMapper.insert(ns);
		}catch (MLException ex) {
			logger.error(ex.toString());
			throw ex;
		}catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}

	@Override
	@Cacheable(value = "newsStatistics", key = "T(String).valueOf(#tid)")
	public NewsStatistics findStatistics(Long tid) throws MLException {
		try{
			NewsStatistics ns =  newsStatisticsMapper.findById(tid);
			return ns;
		}catch (MLException ex) {
			logger.error(ex.toString());
			throw ex;
		}catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}

	@Override
	@CacheEvict(value={"newsCache","newsStatistics"},allEntries=true)
	public void updateUp(Long tid) throws MLException {
		try{
			newsStatisticsMapper.updateUp(tid);
		}catch (MLException ex) {
			logger.error(ex.toString());
			throw ex;
		}catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}

	@Override
	@CacheEvict(value={"newsCache","newsStatistics"},allEntries=true)
	public void updateDown(Long tid) throws MLException {
		try{
			newsStatisticsMapper.updateDown(tid);
		}catch (MLException ex) {
			logger.error(ex.toString());
			throw ex;
		}catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}

	@Override
	@CacheEvict(value={"newsCache","newsStatistics"},allEntries=true)
	public void updateView(Long tid) throws MLException {
		try{
			newsStatisticsMapper.updateView(tid);
		}catch (MLException ex) {
			logger.error(ex.toString());
			throw ex;
		}catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}

	@Override
	@CacheEvict(value={"newsCache","newsStatistics"},allEntries=true)
	public void updateShare(Long tid) throws MLException {
		try{
			newsStatisticsMapper.updateShare(tid);
		}catch (MLException ex) {
			logger.error(ex.toString());
			throw ex;
		}catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}
}
