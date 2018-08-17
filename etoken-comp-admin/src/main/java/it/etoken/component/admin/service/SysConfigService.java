package it.etoken.component.admin.service;

import java.util.List;

import it.etoken.base.common.exception.MLException;
import it.etoken.base.model.admin.entity.SysConfig;
import it.etoken.base.model.admin.entity.Upgrade;
import it.etoken.base.model.admin.vo.LoginAdmin;

public interface SysConfigService {

	public SysConfig findByName(String name) throws MLException;

	public void update(SysConfig upgrade) throws MLException;

	public List<SysConfig> findAll() throws MLException;

}
