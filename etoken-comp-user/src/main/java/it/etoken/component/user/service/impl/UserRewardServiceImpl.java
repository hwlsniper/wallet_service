package it.etoken.component.user.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import it.etoken.base.common.Constant.PageConstant;
import it.etoken.base.common.exception.MLCommonException;
import it.etoken.base.common.exception.MLException;
import it.etoken.base.common.result.MLPage;
import it.etoken.base.model.user.vo.Reward;
import it.etoken.component.user.dao.mapper.EostRecordMapper;
import it.etoken.component.user.service.UserRewardService;

@Component
@Transactional
public class UserRewardServiceImpl implements UserRewardService {

	private final static Logger logger = LoggerFactory.getLogger(UserRewardServiceImpl.class);

	@Autowired
	private EostRecordMapper eostRecordMapper;

	public MLPage<Reward> findAll(String nickname, int page) throws MLException {
		try {
			Page<Reward> result = PageHelper.startPage(page, PageConstant.size);
			eostRecordMapper.findRecord(nickname);
			return new MLPage<Reward>(result.getResult(), result.getTotal());
		} catch (MLException ex) {
			logger.error(ex.toString());
			throw ex;
		} catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}
}
