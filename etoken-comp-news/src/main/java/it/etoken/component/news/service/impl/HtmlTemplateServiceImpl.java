package it.etoken.component.news.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.etoken.base.common.exception.MLCommonException;
import it.etoken.base.common.exception.MLException;
import it.etoken.base.common.result.MLResultList;
import it.etoken.base.model.news.entity.HtmlTemplate;
import it.etoken.base.model.news.entity.JobManager;
import it.etoken.component.news.dao.mapper.HtmlTemplateMapper;
import it.etoken.component.news.dao.mapper.JobManagerMapper;
import it.etoken.component.news.service.HtmlTemplateService;
@Component
@Transactional
public class HtmlTemplateServiceImpl implements HtmlTemplateService {
	private final static Logger logger = LoggerFactory.getLogger(HtmlTemplateServiceImpl.class);
	@Autowired
	private  HtmlTemplateMapper htmlTemplateMapper;
	@Override
	public HtmlTemplate findTemplate() {
		try {
			HtmlTemplate htmlTemplate=htmlTemplateMapper.selectByPrimaryKey(1L);
			return htmlTemplate;
		} catch (MLException ex) {
			logger.error(ex.toString());
			throw new MLException(MLCommonException.system_err);
		} catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}	
	}


}
