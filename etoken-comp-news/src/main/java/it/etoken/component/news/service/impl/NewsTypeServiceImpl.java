package it.etoken.component.news.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import it.etoken.base.common.exception.MLCommonException;
import it.etoken.base.common.exception.MLException;
import it.etoken.base.model.news.entity.NewsType;
import it.etoken.component.news.dao.mapper.NewsTypeMapper;
import it.etoken.component.news.service.NewsTypeService;

@Component
@Transactional
public class NewsTypeServiceImpl implements NewsTypeService {
	
	private final static Logger logger = LoggerFactory.getLogger(NewsTypeServiceImpl.class);
	
	@Autowired
	private NewsTypeMapper newsTypeMapper;

	@Override
	@CacheEvict(value="newsTypeCache",allEntries=true)
	public void saveUpdate(NewsType newsType) throws MLException {
		try{
			if(newsType.getId()==null){
				newsTypeMapper.insert(newsType);
			}else{
				newsTypeMapper.update(newsType);
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
	@CacheEvict(value="newsTypeCache",allEntries=true)
	public void delete(Long id) throws MLException {
		try{
			newsTypeMapper.delete(id);
		}catch (MLException ex) {
			logger.error(ex.toString());
			throw ex;
		}catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}

	@Override
	@Cacheable(value="newsTypeCache",keyGenerator="wiselyKeyGenerator") 
	public Page<NewsType> findAll(int page) throws MLException {
		try{
			Page<NewsType> result = PageHelper.startPage(page,50);  
			PageHelper.orderBy("modifydate desc");
			newsTypeMapper.findAll();
			return result;
		}catch (MLException ex) {
			logger.error(ex.toString());
			throw ex;
		}catch (Exception e) {
			logger.error(e.toString());
			e.printStackTrace();
			throw new MLException(MLCommonException.system_err);
		}
	}
}
