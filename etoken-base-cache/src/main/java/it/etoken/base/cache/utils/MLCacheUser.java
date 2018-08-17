package it.etoken.base.cache.utils;

import it.etoken.cache.service.MLCacheEnum;

/**
 * @author hule
 *
 */
public enum MLCacheUser implements MLCacheEnum {

	LEV1_REWARD("lev1_reward", "一级奖励"),
	LEV2_REWARD("lev2_reward", "二级奖励"),
	REG_REWARD("reg_reward", "注册奖励数量"),
	SMS_IP("sms_ip", "短信ip上限"),
	INVITE("invite_url","邀请链接"),
	SMS_PHONE("sms_phone", "短信手机上限");
	
	private MLCacheUser(String key, String desc) {
		this.key = key;
		this.desc = desc;
	}
	
	private String key;

	private String desc;

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public String getDesc() {
		return desc;
	}
}