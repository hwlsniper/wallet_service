package it.etoken.component.user.facede.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

import it.etoken.base.common.exception.MLException;
import it.etoken.base.common.result.MLResult;
import it.etoken.base.common.result.MLResultList;
import it.etoken.base.common.result.MLResultObject;
import it.etoken.base.model.user.entity.EostRecord;
import it.etoken.component.user.service.EostRecordService;
import it.etoken.componet.user.facade.EostRecordFacadeAPI;

@Service(version = "1.0.0")
public class EostRecordFacadeAPIImpl implements EostRecordFacadeAPI{

	private final static Logger logger = LoggerFactory.getLogger(EostRecordFacadeAPIImpl.class);
	
	@Autowired
	EostRecordService eostRecordService;
	
	@Override
	public MLResultList<EostRecord> findByUid(String uid) {
		try {
			return new MLResultList<EostRecord>(eostRecordService.findByUid(uid));
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultList<EostRecord>(e);
		}
	}

	@Override
	public MLResult saveEostRecord(EostRecord eostRecord) {
		try {
			eostRecordService.saveEostRecord(eostRecord);
			return new MLResult(true);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResult(e);
		}
	}

	
}
