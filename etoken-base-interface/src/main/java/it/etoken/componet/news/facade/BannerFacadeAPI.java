package it.etoken.componet.news.facade;

import it.etoken.base.common.result.MLResult;
import it.etoken.base.common.result.MLResultList;
import it.etoken.base.common.result.MLResultObject;
import it.etoken.base.model.news.entity.Banner;

public interface BannerFacadeAPI {

	public MLResultList<Banner> findAll(int page);

	public MLResultObject<Banner> findById(Long id);

	public MLResult saveUpdate(Banner banner);

	public MLResult delete(Long id);
}
