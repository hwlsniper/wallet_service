package it.etoken.component.news.test.facade;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.dubbo.config.annotation.Reference;

import it.etoken.base.common.result.MLResultList;
import it.etoken.base.model.news.entity.News;
import it.etoken.component.news.Application;
import it.etoken.componet.news.facade.NewsFacadeAPI;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=Application.class)
public class NewsFacedeTest {

	@Reference(version="1.0.0",timeout=5000)
	private NewsFacadeAPI newsFacadeAPI;
	
	@Test
    public void testNews(){
		News n = new News();
		n.setContent("内容");
		n.setTitle("标题");
		n.setUrl("http://www.baidu.com");
		n.setTid(1l);
//		newsFacadeAPI.saveUpdate(n);
    }
	
	@Test
    public void testFindNews(){
		News n = new News();
//		MLResultList<News> n= newsFacadeAPI.getNewsByTid(1L,1);
//		System.out.println(n.getList().size());
    }
}
