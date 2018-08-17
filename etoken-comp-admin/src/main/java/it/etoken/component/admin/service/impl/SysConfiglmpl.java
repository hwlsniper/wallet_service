package it.etoken.component.admin.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import it.etoken.base.common.Constant.PageConstant;
import it.etoken.base.common.exception.MLCommonException;
import it.etoken.base.common.exception.MLException;
import it.etoken.base.model.admin.entity.SysConfig;
import it.etoken.base.model.user.entity.User;
import it.etoken.component.admin.dao.mapper.SysConfigMaper;
import it.etoken.component.admin.service.SysConfigService;

@Component
@Transactional
public class SysConfiglmpl implements SysConfigService {

	private final static Logger logger = LoggerFactory.getLogger(SysConfiglmpl.class);
	
	@Autowired
	SysConfigMaper sysConfigMaper;
	
	@Override
	@Cacheable(value = "SysConfig", keyGenerator = "wiselyKeyGenerator")
	public SysConfig findByName(String name) throws MLException {
		try {
			SysConfig sysConfig = sysConfigMaper.findByName(name);
			return sysConfig;
		} catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	
	}

	@Override
	@Cacheable(value = "SysConfig", keyGenerator = "wiselyKeyGenerator")
	public void update(SysConfig upgrade) throws MLException {
		try {
			 sysConfigMaper.update(upgrade);			
		} catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
		
	}

	@Override
	@Cacheable(value = "SysConfig", keyGenerator = "wiselyKeyGenerator")
	public List<SysConfig> findAll() throws MLException {
		try {
			Page<SysConfig> result = PageHelper.startPage(1,50);  
			sysConfigMaper.Findall();
			return result.getResult();
		} catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}		
	}
	

}
