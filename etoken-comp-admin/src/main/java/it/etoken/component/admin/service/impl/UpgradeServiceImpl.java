package it.etoken.component.admin.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.etoken.base.common.exception.MLCommonException;
import it.etoken.base.common.exception.MLException;
import it.etoken.base.model.admin.entity.Upgrade;
import it.etoken.component.admin.dao.mapper.UpgradeMapper;
import it.etoken.component.admin.service.UpgradeService;

@Component
@Transactional
public class UpgradeServiceImpl implements UpgradeService {

	private final static Logger logger = LoggerFactory.getLogger(UpgradeServiceImpl.class);

	@Autowired
	UpgradeMapper upgradeMapper;

	@Override
	@Cacheable(value = "upgradeCache", keyGenerator = "wiselyKeyGenerator")
	public Upgrade findByOs(String os) throws MLException {
		try {
			Upgrade upgrade = upgradeMapper.findByOs(os);
			return upgrade;
		} catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}
	
	@Override
	@Cacheable(value = "upgradeCache", keyGenerator = "wiselyKeyGenerator")
	public List<Upgrade> findAll() throws MLException {
		try {
			return upgradeMapper.findAll();
		} catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}

	@Override
	@CacheEvict(value = "upgradeCache", allEntries = true)
	public void update(Upgrade upgrade) throws MLException {
		try {
			upgradeMapper.update(upgrade);
		} catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}

}
