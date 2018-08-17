package it.etoken.component.user.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import it.etoken.base.common.result.MLPage;
import it.etoken.base.model.user.entity.User;
import it.etoken.component.user.Application;
import it.etoken.component.user.dao.mapper.EostRecordMapper;
import it.etoken.component.user.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=Application.class)
public class UserTest {

	@Autowired
	private UserService userService;
	
	@Autowired
	EostRecordMapper eostRecordMapper;
	
	@Test
    public void testNews(){
		Boolean fetch=true;
		int page = 0;
		while(fetch) {
			MLPage<User> uss = userService.findAll(null,page++);
			for(User u : uss.getList()) {
				Long c = eostRecordMapper.regCount(u.getId());
				if(c>2) {
					System.out.println(u.getId()+"-"+c);
				}
			}
			if(uss.getList().isEmpty()) {
				fetch=false;
			}
		}
		
    }
	
}
