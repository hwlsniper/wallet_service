package it.etoken.component.news.service;


import it.etoken.base.common.exception.MLException;
import it.etoken.base.common.result.MLResult;
import it.etoken.base.model.news.entity.JobManager;
import it.etoken.base.model.news.entity.NewsType;

public interface JobManagerService {
	/**
	 * 修改状态
	 * @param userId
	 * @return
	 */
	public MLResult Update(JobManager JobManager)throws MLException;
	
	/**
	 * 查询状态根据工作名
	 * @param userId
	 * @return
	 */
	public JobManager SelectByJobManager(String jobname)throws MLException;

}
