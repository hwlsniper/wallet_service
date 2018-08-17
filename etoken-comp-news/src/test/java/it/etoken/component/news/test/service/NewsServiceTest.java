package it.etoken.component.news.test.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.gson.Gson;

import it.etoken.base.common.exception.MLCommonException;
import it.etoken.base.common.exception.MLException;
import it.etoken.base.common.jpush.PushService;
import it.etoken.base.common.result.MLPage;
import it.etoken.base.common.utils.HttpClientUtils;
import it.etoken.base.model.news.entity.News;
import it.etoken.base.model.news.vo.BishijieData;
import it.etoken.base.model.news.vo.BishijieResult;
import it.etoken.base.model.news.vo.ImeosData;
import it.etoken.base.model.news.vo.ImeosResult;
import it.etoken.base.model.news.vo.NewsFromBishijie;
import it.etoken.component.news.Application;
import it.etoken.component.news.service.NewsService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=Application.class)
public class NewsServiceTest {

	@Autowired
	private NewsService newsService;
	
	@Autowired
	private PushService pushService;
	
	@Test
    public void testNews(){
		News n = new News();
//		n.setContent("内容");
//		n.setTitle("标题");
//		n.setUrl("http://www.baidu.com");
//		n.setTid(1l);
//		newsService.saveUpdate(n);
    }
	
	@Test
    public void testFindNews(){
//		MLPage<News> n= newsService.findAll(1,"");
//		System.out.println(n.getList().size());
    }
	

	@Test
    public void testPush(){
		Map<String,String> extr = new HashMap<>();
		extr.put("url","http://static.eostoken.im/html/20180403/1522720720974.html");
//		pushService.pushAll("如何理解EOS的性能设计 - 主链部分", extr);
    }
	
	
//	@Test
//	public void testGetNewsFlashFromBishijie() {
//		BishijieResult bishijieResult = newsService.getNewsFlashFromBishijie();
//		System.out.println("error: " + bishijieResult.getError());
//		System.out.println("message: " + bishijieResult.getMessage());
//		BishijieData data = bishijieResult.getData();
//		
//		System.out.println("total: " + data.getTotal());
//		List<NewsFromBishijie> express = data.getExpress();
//		for(NewsFromBishijie nfb: express) {
//			System.out.println("newsflash_id: "+ nfb.getNewsflash_id()+" title: " + nfb.getTitle());
//			
//		}
//	}
	
	@Test
	public void findByOtherIdAndSource() {
		Long otherid = 32184l;
//		String source = "币世界";
//		News news = newsService.findByOtherIdAndSource(otherid, source);
//		System.out.println(news.getTitle());
	}
	
//	@Test
//	public void testImeos() {
//		String url = "https://api.bitzhidao.com/newsflashes";
//		try {
//			String result = HttpClientUtils.doGet(url);
//	
//			Gson gson = new Gson();
//			ImeosResult imeosResult = gson.fromJson(result, ImeosResult.class);
//			System.out.println(Thread.currentThread().getName() + "，IMEOS快讯采集线程......");
//			if(null!=imeosResult) {
//				if(imeosResult.getTotal()!=0) {
//					int total = imeosResult.getTotal();
//					int limit = imeosResult.getLimit();
//					int skip = imeosResult.getSkip();
//					List<ImeosData> data = imeosResult.getData();
//					for(ImeosData idata : data) {
//						System.out.println("content:" + idata.getContent());
//						System.out.println("source: " + idata.getSource());
//					}
//				}
//			}
//			
//		}catch(Exception e) {
//			e.printStackTrace();
//			throw new MLException(MLCommonException.system_err);
//		}
//	}
	
	@Test
	public void testChongfu() {
		String title="ETH价格下跌或因EOS抛售引起";
		String source = "币世界";
//		News news = newsService.findByTitleAndSource(title, source);
		
//		System.out.println("!!!!!!!!!!!!!!!!!!!!!: " + news.getTitle());
	}
	
}
