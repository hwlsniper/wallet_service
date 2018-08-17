package it.etoken.component.news.service;

import com.github.pagehelper.Page;

import it.etoken.base.common.exception.MLException;
import it.etoken.base.model.news.entity.NewsType;

public interface NewsTypeService {

	/**
	 * 添加新闻类型
	 * @param userId
	 * @return
	 */
	public void saveUpdate(NewsType newsType)throws MLException;
	
	/**
	 * 删除新闻类型
	 * @param userId
	 * @return
	 */
	public void delete(Long id)throws MLException;
		
	/**
	 * 查詢新闻类型
	 * @param userId
	 * @return
	 */
	public Page<NewsType> findAll(int page)throws MLException;
}
