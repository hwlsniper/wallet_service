package it.etoken.component.user.service;

import it.etoken.base.common.exception.MLException;
import it.etoken.base.common.result.MLPage;
import it.etoken.base.model.user.vo.Invite;

public interface UserInviteService {
	
	public MLPage<Invite> findAll(String code,int page) throws MLException;
	
}
