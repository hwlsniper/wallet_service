package it.etoken.component.news;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@ComponentScan(basePackages={"it.etoken.component.news","it.etoken.base.cache","it.etoken.base.common.dao","it.etoken.base.common.jpush"})
@PropertySource({"classpath:config_database.properties","classpath:config_redis.properties","classpath:config_jpush.properties"}) 
public class Application {

	private final static Logger logger = LoggerFactory.getLogger(Application.class);
	
	public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        logger.info("=========== start news component over ===========");
    }
}
