package it.etoken.component.user.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.etoken.base.common.exception.MLCommonException;
import it.etoken.base.common.exception.MLException;
import it.etoken.base.model.user.entity.EostRecord;
import it.etoken.component.user.dao.mapper.EostRecordMapper;
import it.etoken.component.user.service.EostRecordService;

@Component
@Transactional
public class EostRecordServiceImpl implements EostRecordService{

	private final static Logger logger = LoggerFactory.getLogger(EostRecordServiceImpl.class);
	
	@Autowired
	private EostRecordMapper eostRecordMapper;
	@Override
	public List<EostRecord> findByUid(String uid) {
		try {
			List<EostRecord> list=eostRecordMapper.findEosRecord(Long.parseLong(uid));
			return list;
		} catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}

	@Override
	public void saveEostRecord(EostRecord eostRecord) {
		try {
			eostRecordMapper.insertNew(eostRecord);
		} catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
		
	}
}
