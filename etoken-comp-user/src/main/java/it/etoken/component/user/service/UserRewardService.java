package it.etoken.component.user.service;

import it.etoken.base.common.exception.MLException;
import it.etoken.base.common.result.MLPage;
import it.etoken.base.model.user.vo.Reward;

public interface UserRewardService {
	
	public MLPage<Reward> findAll(String nickname,int page) throws MLException;
	
}
