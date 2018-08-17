package it.etoken.component.news.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.etoken.base.common.exception.MLCommonException;
import it.etoken.base.common.exception.MLException;
import it.etoken.base.common.result.MLResult;
import it.etoken.base.model.news.entity.JobManager;
import it.etoken.base.model.news.entity.NewsType;
import it.etoken.component.news.dao.mapper.JobManagerMapper;
import it.etoken.component.news.dao.mapper.NewsMapper;
import it.etoken.component.news.service.JobManagerService;

@Component
@Transactional
public class JobManagerServiceImpl implements JobManagerService{
	private final static Logger logger = LoggerFactory.getLogger(JobManagerServiceImpl.class);
	@Autowired
	private JobManagerMapper jobManagerMapper;
	

	@Override
	public MLResult Update(JobManager JobManager) throws MLException {
		try {
			jobManagerMapper.updateByPrimaryKey(JobManager);
			return new MLResult(true);
		} catch (MLException ex) {
			logger.error(ex.toString());
			throw new MLException(MLCommonException.system_err);
		} catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}	
	}


	@Override
	public JobManager SelectByJobManager(String jobname) throws MLException {
		try {
			JobManager jobManager=jobManagerMapper.selectByJobname(jobname);
			return jobManager;
		} catch (MLException ex) {
			logger.error(ex.toString());
			throw new MLException(MLCommonException.system_err);
		} catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}	
		
	}

}
