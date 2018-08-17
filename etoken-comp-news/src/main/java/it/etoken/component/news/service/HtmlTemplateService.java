package it.etoken.component.news.service;


import it.etoken.base.common.exception.MLException;
import it.etoken.base.common.result.MLResult;
import it.etoken.base.common.result.MLResultList;
import it.etoken.base.model.news.entity.HtmlTemplate;
import it.etoken.base.model.news.entity.JobManager;
import it.etoken.base.model.news.entity.NewsType;

public interface HtmlTemplateService {
	/**
	 * 查询模板
	 * @return
	 */
	public HtmlTemplate findTemplate();
}
