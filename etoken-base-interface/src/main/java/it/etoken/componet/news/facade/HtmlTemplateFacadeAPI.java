package it.etoken.componet.news.facade;

import it.etoken.base.common.result.MLResultList;
import it.etoken.base.common.result.MLResultObject;
import it.etoken.base.model.news.entity.HtmlTemplate;
import it.etoken.base.model.news.entity.News;

public interface HtmlTemplateFacadeAPI {
	/**
	 * 查询模板
	 * @return
	 */
	public MLResultObject<HtmlTemplate> findTemplate();
}
