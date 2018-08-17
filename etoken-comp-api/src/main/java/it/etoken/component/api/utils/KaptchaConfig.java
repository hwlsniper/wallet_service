package it.etoken.component.api.utils;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

@Component  
public class KaptchaConfig {  
    
	@Bean  
    public DefaultKaptcha getDefaultKaptcha(){
        com.google.code.kaptcha.impl.DefaultKaptcha defaultKaptcha = new com.google.code.kaptcha.impl.DefaultKaptcha();  
        Properties properties = new Properties();  
        properties.setProperty("kaptcha.border", "no");  
        properties.setProperty("kaptcha.textproducer.font.color", "0,0,0");  
        properties.setProperty("kaptcha.image.width", "110");  
        properties.setProperty("kaptcha.image.height", "40");  
        properties.setProperty("kaptcha.textproducer.font.size", "30");  
        properties.setProperty("kaptcha.session.key", "code");  
        properties.setProperty("kaptcha.textproducer.char.length", "5");  
        properties.setProperty("kaptcha.textproducer.char.string", "1234567890"); 
        properties.setProperty("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑");  
        properties.setProperty("kaptcha.obscurificator.impl", "com.google.code.kaptcha.impl.FishEyeGimpy");
        properties.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.DefaultNoise");
        //properties.setProperty("kaptcha.textproducer.impl","it.etoken.component.api.utils.ChineseProducer");
        Config config = new Config(properties);  
        defaultKaptcha.setConfig(config);  
        return defaultKaptcha;  
    }
	
}  
