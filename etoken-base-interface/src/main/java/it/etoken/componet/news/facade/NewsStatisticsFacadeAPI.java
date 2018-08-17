package it.etoken.componet.news.facade;

import it.etoken.base.common.result.MLResult;
import it.etoken.base.common.result.MLResultObject;
import it.etoken.base.model.news.entity.NewsStatistics;

public interface NewsStatisticsFacadeAPI {
	
	public MLResultObject<NewsStatistics> findByNid(Long nid);
	
	public MLResult updateUp(Long nid);
	
	public MLResult updateDown(Long nid);
	
	public MLResult updateShare(Long nid);
	
	public MLResult updateView(Long nid);
}
