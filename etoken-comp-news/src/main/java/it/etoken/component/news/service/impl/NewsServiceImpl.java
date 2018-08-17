package it.etoken.component.news.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import it.etoken.base.common.Constant.PageConstant;
import it.etoken.base.common.exception.MLCommonException;
import it.etoken.base.common.exception.MLException;
import it.etoken.base.common.jpush.PushService;
import it.etoken.base.common.result.MLPage;
import it.etoken.base.common.utils.MathUtil;
import it.etoken.base.model.news.entity.News;
import it.etoken.base.model.news.entity.NewsStatistics;
import it.etoken.base.model.news.vo.NewsVO;
import it.etoken.component.news.dao.mapper.NewsMapper;
import it.etoken.component.news.service.NewsService;
import it.etoken.component.news.service.NewsStatisticsService;

@Component
@Transactional
public class NewsServiceImpl implements NewsService {
	
	private final static Logger logger = LoggerFactory.getLogger(NewsTypeServiceImpl.class);
	
	@Autowired
	private NewsMapper newsMapper;
	
	@Autowired
	private NewsStatisticsService newsStatisticsService;
	
	@Autowired
	private PushService pushService;

	@Override
	@CacheEvict(value="newsCache",allEntries=true)
	public void saveUpdate(News news) throws MLException {
		try{
			if(news.getId()==null){
				newsMapper.insert(news);
				
				NewsStatistics ns = new NewsStatistics();
				ns.setNid(news.getId());
				ns.setDown(new Long(MathUtil.random(10)));
				ns.setUp(new Long(MathUtil.random(100)));
				ns.setView(0l);
				ns.setShare(0l);
				newsStatisticsService.addNewsStatistics(ns);
			}else{
				newsMapper.update(news);
			}
		}catch (MLException ex) {
			logger.error(ex.toString());
			throw ex;
		}catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}

	@Override
	@CacheEvict(value="newsCache",allEntries=true)
	public void delete(Long id) throws MLException {
		try{
			newsMapper.delete(id);
		}catch (MLException ex) {
			logger.error(ex.toString());
			throw ex;
		}catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}

	@Override
	@Cacheable(value="newsCache",keyGenerator="wiselyKeyGenerator") 
	public List<NewsVO> findByTid(Long tid, int page) throws MLException {
		try{
			List<NewsVO> vos = new ArrayList<>();
			
			Page<News> result = PageHelper.startPage(page,PageConstant.size);
			
			PageHelper.orderBy("createdate desc");
			
			newsMapper.findByTid(tid);
			
			for(News n : result) {
				NewsStatistics ns = newsStatisticsService.findStatistics(n.getId());
				NewsVO vo = new NewsVO();
				BeanUtils.copyProperties(n, vo);
				if(ns!=null) {
					vo.setShare(ns.getShare());
					vo.setDown(ns.getDown());
					vo.setUp(ns.getUp());
					vo.setView(ns.getView());
				}
				vos.add(vo);
			}
			return vos;
		}catch (MLException ex) {
			logger.error(ex.toString());
			throw ex;
		}catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}

	@Override
	@Cacheable(value="newsCache",keyGenerator="wiselyKeyGenerator") 
	public MLPage<News> findAll(int page,String title) throws MLException {
		try{
			Page<News> result = PageHelper.startPage(page,PageConstant.size);
			PageHelper.orderBy("n.createdate desc");
			newsMapper.findAll(title);
			return new MLPage<News>(result.getResult(), result.getTotal());
		}catch (MLException ex) {
			logger.error(ex.toString());
			throw ex;
		}catch (Exception e) {
			logger.error(e.toString());
			e.printStackTrace();
			throw new MLException(MLCommonException.system_err);
		}
	}
	
	@Override
	public List<News> findLatestNews() throws MLException {
		try{
			List<News> list=newsMapper.findLatestNews();
			return list;
		}catch (MLException ex) {
			logger.error(ex.toString());
			throw ex;
		}catch (Exception e) {
			logger.error(e.toString());
			e.printStackTrace();
			throw new MLException(MLCommonException.system_err);
		}
	}
	
	@Override
	public List<News> findAllNews() throws MLException {
		try{
			List<News> list=newsMapper.findAllNews();
			return list;
		}catch (MLException ex) {
			logger.error(ex.toString());
			throw ex;
		}catch (Exception e) {
			logger.error(e.toString());
			e.printStackTrace();
			throw new MLException(MLCommonException.system_err);
		}
	}
	
	@Override
	@Cacheable(value="newsCache",keyGenerator="wiselyKeyGenerator") 
	public News findById(Long id) throws MLException {
		try{
			return newsMapper.findById(id);
		}catch (MLException ex) {
			logger.error(ex.toString());
			throw ex;
		}catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}

	@Override
	public void push(Long id) throws MLException {
		try{
			News n =  newsMapper.findById(id);
			if(n==null) {
				throw new MLException(MLCommonException.system_err);
			}
			Map<String,String> extr = new HashMap<>();
			extr.put("url",n.getUrl());
//			pushService.pushAll(n.getTitle(),extr);
			pushService.pushByTag("newsmorningbook", n.getTitle(),extr);
		}catch (MLException ex) {
			logger.error(ex.toString());
			throw ex;
		}catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}
	
	@Override
	public News findByOtherIdAndSource(String otherid, String source) throws MLException {
		try{
			return newsMapper.findByOtherIdAndSource(otherid, source);
		}catch (MLException ex) {
			logger.error(ex.toString());
			throw ex;
		}catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}
	
	@Override
	public News findByTitleAndSource(String title, String source) throws MLException{
		try{
			return newsMapper.findByTitleAndSource(title, source);
		}catch (MLException ex) {
			logger.error(ex.toString());
			throw ex;
		}catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}
	
	@Override
	public News findByContent(String content) throws MLException{
		try{
			return newsMapper.findByContent(content);
		}catch (MLException ex) {
			logger.error(ex.toString());
			throw ex;
		}catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}
	
	@Override
	public List<News> findByTitle(String title) throws MLException{
		try{
			return newsMapper.findByTitle(title);
		}catch (MLException ex) {
			logger.error(ex.toString());
			throw ex;
		}catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}
}
