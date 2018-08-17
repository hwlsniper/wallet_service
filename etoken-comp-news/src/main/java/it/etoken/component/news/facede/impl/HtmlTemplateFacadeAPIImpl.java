package it.etoken.component.news.facede.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

import it.etoken.base.common.exception.MLException;
import it.etoken.base.common.result.MLPage;
import it.etoken.base.common.result.MLResult;
import it.etoken.base.common.result.MLResultList;
import it.etoken.base.common.result.MLResultObject;
import it.etoken.base.model.news.entity.HtmlTemplate;
import it.etoken.base.model.news.entity.JobManager;
import it.etoken.base.model.news.entity.News;
import it.etoken.base.model.news.vo.NewsVO;
import it.etoken.component.news.service.HtmlTemplateService;
import it.etoken.component.news.service.JobManagerService;
import it.etoken.component.news.service.NewsService;
import it.etoken.componet.news.facade.HtmlTemplateFacadeAPI;
import it.etoken.componet.news.facade.JobManagerFacadeAPI;
import it.etoken.componet.news.facade.NewsFacadeAPI;


@Service(version = "1.0.0")
public class HtmlTemplateFacadeAPIImpl implements HtmlTemplateFacadeAPI {

	private final static Logger logger = LoggerFactory.getLogger(HtmlTemplateFacadeAPIImpl.class);

	@Autowired
	HtmlTemplateService htmlTemplateService;

	@Override
	public MLResultObject<HtmlTemplate> findTemplate() {
		try {
			HtmlTemplate result = htmlTemplateService.findTemplate();
			return new MLResultObject<HtmlTemplate>(result);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultObject<HtmlTemplate>(e);
		}
	}

}
