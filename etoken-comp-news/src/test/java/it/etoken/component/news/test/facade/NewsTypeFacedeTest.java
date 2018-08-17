package it.etoken.component.news.test.facade;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import it.etoken.base.model.news.entity.News;
import it.etoken.component.news.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=Application.class)
public class NewsTypeFacedeTest {

	@Test
	 public void testFindNews(){
			News n = new News();
//			MLResultList<News> n= newsFacadeAPI.getNewsByTid(1L,1);
//			System.out.println(n.getList().size());
	    }
}
