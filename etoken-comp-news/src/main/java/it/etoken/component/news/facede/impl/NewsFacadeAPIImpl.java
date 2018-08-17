package it.etoken.component.news.facede.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

import it.etoken.base.common.exception.MLException;
import it.etoken.base.common.result.MLPage;
import it.etoken.base.common.result.MLResult;
import it.etoken.base.common.result.MLResultList;
import it.etoken.base.common.result.MLResultObject;
import it.etoken.base.model.news.entity.News;
import it.etoken.base.model.news.vo.NewsVO;
import it.etoken.component.news.service.NewsService;
import it.etoken.componet.news.facade.NewsFacadeAPI;

@Service(version = "1.0.0")
public class NewsFacadeAPIImpl implements NewsFacadeAPI {

	private final static Logger logger = LoggerFactory.getLogger(NewsFacadeAPIImpl.class);

	@Autowired
	NewsService newsService;

	@Override
	public MLResultList<NewsVO> getNewsByTid(Long tid, int page) {
		try {
			List<NewsVO> result = newsService.findByTid(tid, page);
			return new MLResultList<NewsVO>(result);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultList<NewsVO>(e);
		}
	}

	@Override
	public MLResultObject<News> getNewsById(Long id) {
		try {
			News result = newsService.findById(id);
			return new MLResultObject<News>(result);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultObject<News>(e);
		}
	}
	
	@Override
	public MLResult saveUpdate(News news) {
		try {
			newsService.saveUpdate(news);
			return new MLResult(true);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResult(e);
		}
	}

	@Override
	public MLResult delete(Long id) {
		try {
			newsService.delete(id);
			return new MLResult(true);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResult(e);
		}
	}

	@Override
	public MLResultObject<MLPage<News>> findAll(int page,String title) {
		try {
			MLPage<News> result =null;
			if(title.isEmpty()||title==null) {
				 result = newsService.findAll(page,"");
			}else {
				 result = newsService.findAll(page,title);
			}
			return new MLResultObject<MLPage<News>>(result);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultObject<MLPage<News>>(e);
		}
	}
	
	@Override
	public MLResultList<News> findAllNews() {
		try {
			List<News> list = newsService.findAllNews();
			return new MLResultList<News>(list);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultList<News>(e);
		}
	}
	
	@Override
	public MLResultList<News> findLatestNews() {
		try {
			List<News> list = newsService.findLatestNews();
			return new MLResultList<News>(list);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultList<News>(e);
		}
	}

	@Override
	public MLResult push(Long id) {
		try {
			newsService.push(id);
			return new MLResult(true);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResult(e);
		}
	}
	
	@Override
	public MLResultObject<News> findByOtherIdAndSource(String otherid, String source) throws MLException {
		try {
			News result = newsService.findByOtherIdAndSource(otherid, source);
			return new MLResultObject<News>(result);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultObject<News>(e);
		}
	}
	
	@Override
	public MLResultObject<News> findByTitleAndSource(String title, String source) throws MLException {
		try {
			News result = newsService.findByTitleAndSource(title, source);
			return new MLResultObject<News>(result);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultObject<News>(e);
		}
	}
	@Override
	public MLResultObject<News> findByContent(String content) throws MLException {
		try {
			News result = newsService.findByContent(content);
			return new MLResultObject<News>(result);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultObject<News>(e);
		}
	}
	@Override
	public MLResultList<News> findByTitle(String title) throws MLException {
		try {
			List<News> result = newsService.findByTitle(title);
			return new MLResultList<News>(result);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultList<News>(e);
		}
	}
}
