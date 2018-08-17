package it.etoken.componet.news.facade;

import it.etoken.base.common.exception.MLException;
import it.etoken.base.common.result.MLResult;
import it.etoken.base.common.result.MLResultObject;
import it.etoken.base.model.news.entity.JobManager;

public interface JobManagerFacadeAPI {
	/**
	 * 修改状态
	 * @param userId
	 * @return
	 */
	public MLResult Update(JobManager JobManager)throws MLException;
	
	/**
	 * 查询状态根据jobname
	 * @param userId
	 * @return
	 */
	public  MLResultObject<JobManager> selectByJobname(String jobname)throws MLException;
}
