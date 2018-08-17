package it.etoken.component.news.service;

import com.github.pagehelper.Page;

import it.etoken.base.common.exception.MLException;
import it.etoken.base.model.news.entity.Banner;

public interface BannerService {

	public void saveUpdate(Banner Banner) throws MLException;

	public void delete(Long id) throws MLException;

	public Page<Banner> findAll(int page) throws MLException;

	public Banner findById(Long id) throws MLException;

}
