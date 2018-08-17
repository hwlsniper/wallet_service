package it.etoken.component.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ComponentScan(basePackages={"it.etoken.component.admin","it.etoken.base.cache","it.etoken.base.common.http","it.etoken.base.common.dao"})
@PropertySource({"classpath:config_database.properties","classpath:config_redis.properties","classpath:config_http.properties"}) 
@ServletComponentScan
@EnableTransactionManagement
@EnableScheduling
public class Application {

	private final static Logger logger = LoggerFactory.getLogger(Application.class);
	
	public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        logger.info("=========== start user component over ===========");
    }
	
	@Bean
	public TaskScheduler taskScheduler() {
		ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
		taskScheduler.setPoolSize(20);
		taskScheduler.setThreadNamePrefix("springboot-task");
		
		return taskScheduler;
	}
}
