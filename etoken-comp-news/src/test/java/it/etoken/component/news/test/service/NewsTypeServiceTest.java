package it.etoken.component.news.test.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import it.etoken.base.model.news.entity.NewsType;
import it.etoken.component.news.Application;
import it.etoken.component.news.service.NewsTypeService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=Application.class)
public class NewsTypeServiceTest {

	@Autowired
	private NewsTypeService newsTypeService;
	
	@Test
    public void testNews(){
		NewsType n = new NewsType();
		n.setName("EOS动态");
//		newsTypeService.saveUpdate(n);
//		System.out.println("==="+n.getId());
//		newsTypeService.delete(n.getId());
    }
}
