package it.etoken.component.news.service;

import java.util.List;

import it.etoken.base.common.exception.MLException;
import it.etoken.base.common.result.MLPage;
import it.etoken.base.model.news.entity.News;
import it.etoken.base.model.news.vo.NewsVO;

public interface NewsService {

	/**
	 * 添加新闻
	 * @param userId
	 * @return
	 */
	public void saveUpdate(News news)throws MLException;
	
	/**
	 * 删除新闻
	 * @param userId
	 * @return
	 */
	public void delete(Long id)throws MLException;
	
	/**
	 * 查詢新闻
	 * @param userId
	 * @return
	 */
	public List<NewsVO> findByTid(Long tid,int page)throws MLException;
	
	/**
	 * 查詢新闻
	 * @param userId
	 * @return
	 */
	public MLPage<News> findAll(int page,String title)throws MLException;
	
	/**
	 * 查詢新闻
	 * @param userId
	 * @return
	 */
	public News findById(Long id)throws MLException;
	
	public void push(Long id)throws MLException;
	
	/**
	 * 根据地三方id和来源，获取新闻
	 * @param otherid 第三方ID
	 * @param source 来源
	 * @return
	 * @throws MLException
	 */
	public News findByOtherIdAndSource(String otherid, String source) throws MLException;
	
	/**
	 * 根据标题和来源，获取新闻
	 * @param title
	 * @param source
	 * @return
	 * @throws MLException
	 */
	public News findByTitleAndSource(String title, String source) throws MLException;
	
	
	/**
	 * 查詢最新的100条新闻
	 */
	public List<News> findLatestNews() throws MLException;

	public News findByContent(String content);
	/**
	 * 查詢所有的新闻
	 */
	public List<News> findAllNews() throws MLException;

	List<News> findByTitle(String title) throws MLException;
	
}
