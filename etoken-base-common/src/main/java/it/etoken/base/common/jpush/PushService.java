package it.etoken.base.common.jpush;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import cn.jiguang.common.ClientConfig;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import it.etoken.base.common.exception.MLException;

@Component
public class PushService {

	private final static Logger logger = LoggerFactory.getLogger(PushService.class);
	
	JPushClient jpushClient;

	@Value("${jpush.master}")
	String master;

	@Value("${jpush.appid}")
	String appid;

	@PostConstruct
	public void init() {
		jpushClient = new JPushClient(master, appid, null, ClientConfig.getInstance());
	}

	public void pushAll(String content, Map<String, String> extras) throws MLException {
		try {
			PushPayload p =  PushPayload.newBuilder().setPlatform(Platform.all()).setMessage(Message.content(content))
					 .setAudience(Audience.all())
				.setNotification(Notification.android(content, null, extras)).build();
			jpushClient.sendPush(p);
		}catch(Exception e) {
			logger.error("",e);
		}
	}

	public void pushByTag(String tag, String content, Map<String, String> extras) throws MLException {
		try {
			PushPayload p = PushPayload.newBuilder().setPlatform(Platform.all()).setAudience(Audience.tag(tag))
				.setMessage(Message.content(content)).setNotification(Notification.android(content, null, extras)).build();
			jpushClient.sendPush(p);
		}catch(Exception e) {
			logger.error("",e);
		}
	}

	public void pushByTag(String[] tags, String content, Map<String, String> extras) throws MLException {
		try {
			PushPayload p = PushPayload.newBuilder().setPlatform(Platform.all()).setAudience(Audience.tag(tags))
					.setMessage(Message.content(content)).setNotification(Notification.android(content, null, extras)).build();
			jpushClient.sendPush(p);
		}catch (Exception e) {
			logger.error("",e);
		}
	}
}
