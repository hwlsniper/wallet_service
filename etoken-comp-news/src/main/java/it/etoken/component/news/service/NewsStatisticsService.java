package it.etoken.component.news.service;

import it.etoken.base.common.exception.MLException;
import it.etoken.base.model.news.entity.NewsStatistics;

public interface NewsStatisticsService {

	public void addNewsStatistics(NewsStatistics ns) throws MLException;

	public NewsStatistics findStatistics(Long tid) throws MLException;

	public void updateUp(Long tid) throws MLException;
	
	public void updateDown(Long tid) throws MLException;
	
	public void updateView(Long tid) throws MLException;
	
	public void updateShare(Long tid) throws MLException;
}
