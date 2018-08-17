package it.etoken.component.admin.service;

import java.util.List;

import it.etoken.base.common.exception.MLException;
import it.etoken.base.model.admin.entity.Upgrade;

public interface UpgradeService {
	
	public Upgrade findByOs(String os) throws MLException;
	
	public void update(Upgrade upgrade) throws MLException;
	
	public List<Upgrade> findAll() throws MLException;
}
