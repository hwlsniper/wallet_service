package it.etoken.componet.user.facade;

import it.etoken.base.common.result.MLResult;
import it.etoken.base.common.result.MLResultList;
import it.etoken.base.model.user.entity.EostRecord;

public interface EostRecordFacadeAPI {
	
	public MLResultList<EostRecord> findByUid(String uid);

	public MLResult saveEostRecord(EostRecord eostRecord);

}
