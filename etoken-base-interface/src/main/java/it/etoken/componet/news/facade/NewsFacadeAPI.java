package it.etoken.componet.news.facade;

import it.etoken.base.common.exception.MLException;
import it.etoken.base.common.result.MLPage;
import it.etoken.base.common.result.MLResult;
import it.etoken.base.common.result.MLResultList;
import it.etoken.base.common.result.MLResultObject;
import it.etoken.base.model.news.entity.News;
import it.etoken.base.model.news.vo.NewsVO;

public interface NewsFacadeAPI {
	
	/**
	 * 获得新闻
	 * @return
	 */
	public MLResultList<NewsVO> getNewsByTid(Long tid,int page);
	
	/**
	 * 获得新闻
	 * @return
	 */
	public MLResultObject<News> getNewsById(Long id);
	
	/**
	 * 获得新闻
	 * @return
	 */
	public MLResultObject<MLPage<News>> findAll(int page,String title);
	
	/**
	 * 获得新闻
	 * @return
	 */
	public MLResult saveUpdate(News news);
	
	/**
	 * 删除新闻
	 * @return
	 */
	public MLResult delete(Long id);
	
	public MLResult push(Long id);
	
	/**
	 * 根据地三方ID和来源获取新闻
	 * @param otherid 第三方ID
	 * @param source 来源
	 * @return
	 * @throws MLException
	 */
	public MLResultObject<News> findByOtherIdAndSource(String otherid, String source) throws MLException;
	
	/**
	 * 根据咨询标题和来源获取新闻
	 * @param title 资讯标题
	 * @param source 来源
	 * @return
	 * @throws MLException
	 */
	public MLResultObject<News> findByTitleAndSource(String title, String source) throws MLException;

	/**
	 * 获取最新的100条新闻
	 * @return
	 */
	public MLResultList<News> findLatestNews();
	
	/**
	 * 查询所有新闻
	 * @return
	 */
	public MLResultList<News> findAllNews();

	MLResultObject<News> findByContent(String content) throws MLException;

	MLResultList<News> findByTitle(String title) throws MLException;

}
