package it.etoken.component.admin.task;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import it.etoken.base.common.exception.MLCommonException;
import it.etoken.base.common.exception.MLException;
import it.etoken.base.common.result.MLResult;
import it.etoken.base.common.result.MLResultList;
import it.etoken.base.common.result.MLResultObject;
import it.etoken.base.common.utils.HtmlUtils;
import it.etoken.base.common.utils.HttpClientUtils;
import it.etoken.base.common.utils.LevenshteinDistanceUtil;
import it.etoken.base.model.news.entity.HtmlTemplate;
import it.etoken.base.model.news.entity.JobManager;
import it.etoken.base.model.news.entity.News;
import it.etoken.base.model.news.vo.BishijieData;
import it.etoken.base.model.news.vo.BishijieResult;
import it.etoken.base.model.news.vo.ImeosData;
import it.etoken.base.model.news.vo.ImeosResult;
import it.etoken.base.model.news.vo.NewsFromBishijie;
import it.etoken.cache.service.CacheService;
import it.etoken.componet.news.facade.HtmlTemplateFacadeAPI;
import it.etoken.componet.news.facade.JobManagerFacadeAPI;
import it.etoken.componet.news.facade.NewsFacadeAPI;

@Component
public class NewsFlashTask {

	@Reference(version = "1.0.0", timeout = 30000)
	NewsFacadeAPI newsFacadeAPI;
	
	@Reference(version = "1.0.0", timeout = 30000, retries=0)
	NewsFacadeAPI newsFacadeAPI2;
	
	@Reference(version = "1.0.0", timeout = 30000, retries=0)
	JobManagerFacadeAPI jobManagerFacadeAPI;
	
	@Reference(version = "1.0.0", timeout = 30000, retries=0)
	HtmlTemplateFacadeAPI htmlTemplateFacadeAPI;
	
	@Value("${html.server}")
	String HtmlServer;

	@Value("${html.save}")
	String HtmlSave;
	
	@Autowired
	CacheService cacheService;
	
	
	
	
	//获取到的所有新闻资讯的集合
	private List<News> listNews=new ArrayList<News>();
	

//	/**
//	 * 每5分钟获取币世界短讯
//	 */
//	@Scheduled(cron = "0 */1 * * * ?")
//	public void getNewsFlashFromBishijieEos() {
//		 this.getNewsFlashFromBishijieKeyword("eos");
//	}
	
	 /**
	 * 每5分钟获取币世界短讯
	 */
	 @Scheduled(cron = "0 */5 * * * ?")
	 public void collectNewsFlashFromImeos() {
		 this.getNewsFlashFromImeos();
		 this.getNewsFlashFromBishijieKeyword("eos");
		 this.getNewsFlashFromJinsecaijingKeyword("eos");
		 this.handleResultList();
	 }
	 

	private void getNewsFlashFromImeos() {
		String url = "https://api.bitzhidao.com/newsflashes";
		try {
			String result = HttpClientUtils.doGet(url);

			Gson gson = new Gson();
			ImeosResult imeosResult = gson.fromJson(result, ImeosResult.class);
			System.out.println(Thread.currentThread().getName() + "，IMEOS快讯采集线程开始采集......");
			if (null != imeosResult) {
				if (imeosResult.getTotal() != 0) {
					List<ImeosData> data = imeosResult.getData();
					for (ImeosData idata : data) {
						String content = idata.getContent();
						News news = new News();
						String content_new=manage(content);
						news.setTitle(idata.getTitle());
						news.setContent(content_new);
						news.setHtml(content_new);
						news.setSource(idata.getSource());
						news.setOtherid(idata.get_id());

						long time = idata.getIssueTime();
//						time -= 8 * 60 * 60 * 1000;
						Date updateDate = new Date(time);
						news.setCreatedate(updateDate);
						news.setModifydate(updateDate);

						//this.processOne(news);
						listNews.add(news);//将获取到的新闻添加到所有新闻资讯的集合中
					}
				}
			}
			System.out.println(Thread.currentThread().getName() + "，IMEOS快讯采集线程采集结束。");

		} catch (Exception e) {
			e.printStackTrace();
			throw new MLException(MLCommonException.system_err);
		}
	}

	private void getNewsFlashFromBishijieKeyword(String keyword) {
		String url = "http://www.bishijie.com/api/newsv17/search?key=" + keyword + "&size=100";
		try {
			String result = HttpClientUtils.doGet(url);

			Gson gson = new Gson();
			BishijieResult bishijieResult = gson.fromJson(result, BishijieResult.class);

			int error = bishijieResult.getError();
			System.out.println(Thread.currentThread().getName() + "，Bishijie快讯采集线程。开始采集关键字：" + keyword + "......");
			// 获取成功数据
			if (error == 0) {
				BishijieData data = bishijieResult.getData();
				List<NewsFromBishijie> express = data.getExpress();
				for (NewsFromBishijie nfb : express) {
					// 内容存在问题，则跳过
					if (StringUtils.isEmpty(nfb.getTitle()) || StringUtils.isEmpty(nfb.getContent())
							|| StringUtils.isEmpty(nfb.getSource())) {
						continue;
					}
					synchronized (this) {
						News news = new News();
						
						String content = nfb.getContent();
						String content_new=manage(content);
						news.setTitle(nfb.getTitle());
						news.setContent(content_new);
						news.setHtml(content_new);
						news.setSource(nfb.getSource());
						news.setOtherid(String.valueOf(nfb.getNewsflash_id()));

						SimpleDateFormat datefmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Date updateTime = datefmt.parse(nfb.getUpdate_time());
						long time = updateTime.getTime();
						time -= 8 * 60 * 60 * 1000;
						Date updateDate = new Date(time);
						news.setCreatedate(updateDate);
						news.setModifydate(updateDate);
						
						listNews.add(news);
						//this.processOne(news);
						
					}
				}
			}
			System.out.println(Thread.currentThread().getName() + "，快讯采集线程，采集关键字：" + keyword + "结束。");

		} catch (Exception e) {
			e.printStackTrace();
			throw new MLException(MLCommonException.system_err);
		}
	}
	
	
	private void getNewsFlashFromJinsecaijingKeyword(String keyword) {
		String url = "https://api.jinse.com/v3/live/list?limit=100&id=&flag=down&keyword=" + keyword + "&sort=&grade=&version=9.9.9";
		//List<News> result = new ArrayList<News>();
		try {
			String requestResult = HttpClientUtils.doGet(url);
			JSONObject jo = JSONObject.parseObject(requestResult);
			JSONArray ja = jo.getJSONArray("list");
			Iterator<Object> it = ja.iterator();
			while(it.hasNext()) {
				JSONObject jjo = (JSONObject)it.next();
				JSONArray lives = jjo.getJSONArray("lives");
				Iterator<Object> livesIt = lives.iterator();
				while(livesIt.hasNext()) {
					JSONObject thisNews = (JSONObject)livesIt.next();
					
					News news = new News();
					String id = thisNews.getString("id");
					String content = thisNews.getString("content");
					String content_new=manage(content);
					String created_at = thisNews.getString("created_at")+"000";
					
					String[] contentArray = content_new.split("】");
					news.setTitle(contentArray[0].replaceAll("【", "").trim());
					news.setContent(contentArray[1]);
					news.setOtherid(id);
					news.setSource("金色财经");
					
//					SimpleDateFormat datefmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//					String str=datefmt.format(created_ats);  
//					Date updateTime = datefmt.parse(str);
//					long time = updateTime.getTime();
					long time=Long.parseLong(created_at);
					time -= 8 * 60 * 60 * 1000;
					Date updateDate = new Date(time);
					news.setCreatedate(updateDate);
					news.setModifydate(updateDate);
					
					//result.add(news);
					listNews.add(news);//将获取到的新闻添加到所有新闻资讯的集合中
				}
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			throw new MLException(MLCommonException.system_err);
		}
		//return result;
	}
	
	
	/**
	 *处理收集到的信息并保存到数据库中
	 * */
    private void handleResultList() {
    	//从数据库里面removeRepeatJob的状态为wait可以执行不为wait跳过
    	MLResultObject<JobManager> jobmanager=jobManagerFacadeAPI.selectByJobname("removeRepeat");
		JobManager j=jobmanager.getResult();
    	try {
    		String status=jobmanager.getResult().getStatus();
    		if(status.equals("doing")) {
    			return;
    		}else {
    			j.setStatus("doing");
            	Date date=new Date();
            	j.setCreatedate(date);
            	MLResult r=jobManagerFacadeAPI.Update(j);
            	if(r.isSuccess()) {
        			System.out.println("修改doing状态成功");
        		}
    		}
        	//从数据库里面查询出来的100条资料的集合
        	MLResultList<News> list=newsFacadeAPI.findLatestNews();
        	//MLResultList<JobManager> JobManager=newsFacadeAPI.findLatestNews();
        	//等待存储的筛选出来的没用重复的list集合。
        	List<News> listWait=new ArrayList<News>();
        	//去重复（数据相似度高的项）
        	listWait=deduplicated(listNews,list);
        	//将数据添加到数据库中
        	for (News news : listWait) {
        		try {
        			if (news.getTitle().contains("推广")) {
        				continue;
					}
        			this.processOne(news);;
				} catch (Exception e) {
					e.printStackTrace();
				}	
    		}
		} catch (Exception e) {
			e.printStackTrace();
			throw new MLException(MLCommonException.system_err);
		}finally {
			//修改数据库里面removeRepeatJob的状态为wait
        	j.setStatus("wait");
        	Date date=new Date();
        	j.setModifydate(date);
        	MLResult r=jobManagerFacadeAPI.Update(j);
    		if(r.isSuccess()) {
    			System.out.println("修改状态为wait成功");
    		}
		}
    	
    }
    //去除listNews中与list中数据相似度高的项
    private List<News> deduplicated(List<News> listNews, MLResultList<News> list) {
    	List<News> listWait=new ArrayList<News>();
    	for (News news : listNews) {
    		if(StringUtils.isEmpty(news.getTitle()) || StringUtils.isEmpty(news.getContent())
    		          || StringUtils.isEmpty(news.getSource())) {
    		          continue;
    		        }
    		boolean isSame = false;
    		//不同记录条数
    		for (News news1 : list.getList()) {
    			String title=news.getTitle().trim();
    			String title1=news1.getTitle().trim();
    			if(title.equals(title1)) {
    				isSame = true;
    				break;
    			}
    			double contentSimilarity= LevenshteinDistanceUtil.similarity(news.getContent(),news1.getContent());
    			if(contentSimilarity>0.8) {
    				isSame = true;
    				break;
    			}
			}
    	    if(!isSame) {
//	    			System.out.println(news.getTitle()+"哈哈哈哈哈哈"+news.getContent());
	    			if(unexists(listWait,news)) {
	    				listWait.add(news);
    			}
    		}
		}
		return listWait;
	}


	//检测list是否有相似度高的新闻
	private boolean unexists(List<News> listWait, News news) {
		  for (News thisNews : listWait) {
			  if(StringUtils.isEmpty(thisNews.getTitle()) || StringUtils.isEmpty(thisNews.getContent())
    		          || StringUtils.isEmpty(thisNews.getSource())) {
    		          continue;
    		        }
      			double contentSimilarity= LevenshteinDistanceUtil.similarity(news.getContent(),thisNews.getContent());
      			if(contentSimilarity>0.8) {
      				//找到一条相似度高则返回false
      				return false;
      			}
  		}
		return true;
	}

	//将收集资讯进行去空格去图片处理
	private String manage(String content) {
		content = content.replace("点击“查看详情”", "");
		content = content.replace("点击查看详情", "");
		content = content.replace("点击“显示详情”", "");
		
		content = content.replaceAll("<a href=\"(.*?)\".*?>(.*?)</a>", "");
		content = content.replaceAll("<img src=\"(.*?)\".*?>(.*?)</img>", "");
		
		content = content.replaceAll("\\!\\[\\]\\((.*?)\\)", "");
		content = content.trim();
		return content;
		
	}

	//获取当前eos价格
	public Double findEosPrice() {
		   double eosrate = 0;
		   double peos = 0;
		   double peosD = 0;
			try {
				   BigDecimal eosrateB = cacheService.get("CNY_USDT",BigDecimal.class);
				      if(null != eosrateB) {
				        eosrate = eosrateB.doubleValue();
				      }
				      DecimalFormat formatter2 =new DecimalFormat("#.00");
				      JSONObject geteEos = cacheService.get("gateio_ticker_eos",JSONObject.class);
				      if(null != geteEos) {
				        peosD = geteEos.getDoubleValue("last");
				      }else{
				        JSONObject okEos = cacheService.get("ok_ticker_eos",JSONObject.class);
				        if(null != okEos) {
				          peosD = okEos.getDoubleValue("last");
				        }
				      }
				      peos = Double.parseDouble(formatter2.format(peosD*eosrate));
				      System.out.println("peos值"+peos);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return peos;
	}
	private void processOne(News newsFlash) throws Exception {
		if (StringUtils.isEmpty(newsFlash.getTitle()) || StringUtils.isEmpty(newsFlash.getContent())
				|| StringUtils.isEmpty(newsFlash.getSource())) {
			throw new Exception("内容不正确");
		}
		synchronized (this) {
			// 根据咨询内容判断是否已存在，存在则跳过
			MLResultObject<News> existNewsResultByContent = newsFacadeAPI.findByContent(newsFlash.getContent());
			if (existNewsResultByContent.isSuccess()) {
				if (null != existNewsResultByContent.getResult()) {
					return;
				}
			}
			String content = newsFlash.getContent();
			// 生成html
			if (!StringUtils.isEmpty(newsFlash.getContent())) {
				content = content.replace("点击“查看详情”", "");
				content = content.replace("点击查看详情", "");
				content = content.replace("点击“显示详情”", "");
				
				content = content.replaceAll("<a href=\"(.*?)\".*?>(.*?)</a>", "");
				content = content.replaceAll("<img src=\"(.*?)\".*?>(.*?)</img>", "");
				
				content = content.replaceAll("\\!\\[\\]\\((.*?)\\)", "");
				content = content.trim();
				
				if(content.length()<10) {
					return;
				}
				
				MLResultObject<HtmlTemplate> ml=htmlTemplateFacadeAPI.findTemplate();
				String template=ml.getResult().getTemplate();
				Double eosprice=findEosPrice();
				String tempurl="";
				for (int i=0; i<3 ;i++) {
					tempurl= HtmlUtils.gemHtmlforAlerts(content,newsFlash.getTitle(),eosprice.toString(), HtmlSave,template);
					File file=new File(tempurl);    
					if(!file.exists())    
					{  
						continue;
					}   
				} 
				newsFlash.setUrl(HtmlServer + tempurl);
			}
			newsFlash.setContent(content);
			newsFlash.setHtml(content);

			newsFlash.setTid(12l);
			newsFlash.setVisable(1l);

			MLResult r = newsFacadeAPI2.saveUpdate(newsFlash);

			MLResult rs = null;
			MLResultObject<News> haveNewsResult = newsFacadeAPI.findByOtherIdAndSource(newsFlash.getOtherid(),
					newsFlash.getSource());
			if (haveNewsResult.isSuccess()) {
				if (null != haveNewsResult.getResult()) {
					// 修改创建时间和修改时间为币世界修改时间
					News updateNews = haveNewsResult.getResult();

					Long updateNewsID= updateNews.getId();
					if(null !=updateNewsID && updateNewsID!=0l)
					{
						newsFlash.setId(updateNewsID);
	
						rs = newsFacadeAPI.saveUpdate(newsFlash);
					}
					// 推送快讯
					//MLResult pushResult = newsFacadeAPI2.push(updateNews.getId());
//					if (pushResult.isSuccess()) {
//						System.out.println("推送快讯成功，快讯标题: " + newsFlash.getTitle());
//					} else {
//						System.out.println("推送快讯失败，快讯标题: " + newsFlash.getTitle());
//					}
				}
			}

			if (r.isSuccess() && null != rs && rs.isSuccess()) {
				System.out.println("保存快讯成功，快讯标题: " + newsFlash.getTitle());
			} else {
				System.out.println("保存快讯失败，快讯标题: " + newsFlash.getTitle());
			}
		}
	}
}
