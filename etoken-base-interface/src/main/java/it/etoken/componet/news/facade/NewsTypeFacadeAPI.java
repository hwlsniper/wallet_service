package it.etoken.componet.news.facade;

import it.etoken.base.common.result.MLResult;
import it.etoken.base.common.result.MLResultList;
import it.etoken.base.model.news.entity.NewsType;
import it.etoken.base.model.news.vo.NewsRoute;

public interface NewsTypeFacadeAPI {
	
	/**
	 * 获得新闻
	 * @return
	 */
	public MLResultList<NewsRoute> getNewsType(int page);
	
	/**
	 * 
	 * @return
	 */
	public MLResultList<NewsType> findAll();
	
	/**
	 * 获得新闻
	 * @return
	 */
	public MLResult saveUpdate(NewsType news);
	
	/**
	 * 删除新闻
	 * @return
	 */
	public MLResult delete(Long id);
}
