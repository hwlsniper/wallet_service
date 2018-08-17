package it.etoken.component.admin.service;

import it.etoken.base.common.exception.MLException;
import it.etoken.base.model.admin.vo.LoginAdmin;

public interface AdminService {
	
	public LoginAdmin login(String username,String password) throws MLException;
	
	public void changePwd(String username,String oldpwd,String newpwd)throws MLException;
	
}
