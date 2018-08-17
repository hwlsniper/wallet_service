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
import it.etoken.base.model.user.vo.Invite;
import it.etoken.component.user.dao.mapper.UserInviteMapper;
import it.etoken.component.user.service.UserInviteService;

@Component
@Transactional
public class UserInviteServiceImpl implements UserInviteService {

	private final static Logger logger = LoggerFactory.getLogger(UserInviteServiceImpl.class);

	@Autowired
	private UserInviteMapper userInviteMapper;

	public MLPage<Invite> findAll(String code, int page) throws MLException {
		try {
			Page<Invite> result = PageHelper.startPage(page, PageConstant.size);
			userInviteMapper.findInvite();
			return new MLPage<Invite>(result.getResult(), result.getTotal());
		} catch (MLException ex) {
			logger.error(ex.toString());
			throw ex;
		} catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}
}
