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

import it.etoken.base.common.Constant.PageConstant;
import it.etoken.base.common.exception.MLCommonException;
import it.etoken.base.common.exception.MLException;
import it.etoken.base.model.news.entity.Banner;
import it.etoken.component.news.dao.mapper.BannerMapper;
import it.etoken.component.news.service.BannerService;

@Component
@Transactional
public class BannerServiceImpl implements BannerService {

	private final static Logger logger = LoggerFactory.getLogger(NewsTypeServiceImpl.class);

	@Autowired
	private BannerMapper bannerMapper;

	@Override
	@CacheEvict(value = "bannerCache", allEntries = true)
	public void saveUpdate(Banner Banner) throws MLException {
		try {
			if (Banner.getId() == null) {
				bannerMapper.insert(Banner);
			} else {
				bannerMapper.update(Banner);
			}
		} catch (MLException ex) {
			logger.error(ex.toString());
			throw ex;
		} catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}

	@Override
	@CacheEvict(value = "bannerCache", allEntries = true)
	public void delete(Long id) throws MLException {
		try {
			bannerMapper.delete(id);
		} catch (MLException ex) {
			logger.error(ex.toString());
			throw ex;
		} catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}

	@Override
	@Cacheable(value = "bannerCache", keyGenerator = "wiselyKeyGenerator")
	public Page<Banner> findAll(int page) throws MLException {
		try {
			Page<Banner> result = PageHelper.startPage(page, PageConstant.size);
			PageHelper.orderBy("modifydate desc");
			bannerMapper.findAll();
			return result;
		} catch (MLException ex) {
			logger.error(ex.toString());
			throw ex;
		} catch (Exception e) {
			logger.error(e.toString());
			e.printStackTrace();
			throw new MLException(MLCommonException.system_err);
		}
	}

	@Override
	@Cacheable(value = "bannerCache", keyGenerator = "wiselyKeyGenerator")
	public Banner findById(Long id) throws MLException {
		try {
			return bannerMapper.findById(id);
		} catch (MLException ex) {
			logger.error(ex.toString());
			throw ex;
		} catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}

}
