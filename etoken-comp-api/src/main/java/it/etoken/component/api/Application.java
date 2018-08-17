package it.etoken.component.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class,HibernateJpaAutoConfiguration.class})
@EnableTransactionManagement
@ComponentScan(basePackages={"it.etoken.component.api","it.etoken.base.cache","it.etoken.base.common.http","it.etoken.base.common.sms","it.etoken.base.common.jpush"})
@PropertySource({"classpath:config_redis.properties","classpath:config_http.properties","classpath:config_jpush.properties"})
@ServletComponentScan
public class Application {
	
	private final static Logger logger = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        logger.info("=========== start api component over ===========");
    }
}
