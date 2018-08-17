package it.etoken.component.admin.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.etoken.base.common.exception.MLCommonException;
import it.etoken.base.common.exception.MLException;
import it.etoken.base.common.utils.MathUtil;
import it.etoken.base.model.admin.entity.Admin;
import it.etoken.base.model.admin.vo.LoginAdmin;
import it.etoken.cache.service.CacheService;
import it.etoken.component.admin.dao.mapper.AdminMapper;
import it.etoken.component.admin.exception.MLAdminException;
import it.etoken.component.admin.service.AdminService;

@Component
@Transactional
public class AdminServiceImpl implements AdminService {

	private final static Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);
	
	@Autowired
	AdminMapper adminMapper;
	
	@Autowired
	CacheService cacheService;

	@Override
	@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
	public LoginAdmin login(String username, String password) throws MLException {
		try {
			Admin admin = adminMapper.findByUsername(username);
			// 用户名已存在
			if (admin == null) {
				throw new MLException(MLAdminException.LOGIN_ERROR);
			}
			if(admin.getLogin_error()<=0) {
				throw new MLException(MLAdminException.LOGIN_ERROR,"账户被锁定请联系管理人员");
			}
			if(!MathUtil.MD5(password).equals(admin.getPassword())) {
				adminMapper.loginError(admin.getId());
				throw new MLException(MLAdminException.LOGIN_ERROR,"错误"+(admin.getLogin_error()-1)+"次后，账户将被锁定");
			}
			adminMapper.resetLoginError(admin.getId());
			return parserLogin(admin);
		} catch (MLException ex) {
			logger.error(ex.toString());
			throw ex;
		} catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}
	
	/**
	 * 登陆信息
	 * @param user
	 * @return
	 */
	private LoginAdmin parserLogin(Admin admin) {
		LoginAdmin loginAdmin = new LoginAdmin();
		loginAdmin.setToken(MathUtil.getUUID());
		loginAdmin.setId(admin.getId());
		loginAdmin.setUsername(admin.getUsername());
		cacheService.setAdminToken(admin.getId(), loginAdmin.getToken());
		return loginAdmin;
	}

	@Override
	public void changePwd(String username, String oldpwd, String newpwd) throws MLException {
		try {
			Admin admin = adminMapper.findByUsername(username);
			if(admin==null || !MathUtil.MD5(oldpwd).equals(admin.getPassword())) {
				throw new MLException(MLAdminException.OLD_ERROR);
			}
			adminMapper.changePwd(admin.getId(), MathUtil.MD5(newpwd));
		} catch (MLException ex) {
			logger.error(ex.toString());
			throw ex;
		} catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}

}
