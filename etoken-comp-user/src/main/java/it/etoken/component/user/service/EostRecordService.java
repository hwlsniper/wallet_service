package it.etoken.component.user.service;


import java.util.List;

import it.etoken.base.common.exception.MLException;
import it.etoken.base.model.user.entity.EostRecord;

public interface EostRecordService {

	public List<EostRecord> findByUid(String uid) throws MLException;

	public void saveEostRecord(EostRecord eostRecord) throws MLException;
}
