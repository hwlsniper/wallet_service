package it.etoken.base.cache.utils;

import it.etoken.cache.service.MLCacheEnum;

/**
 * @author hule
 *
 */
public enum MLCacheAdmin implements MLCacheEnum {

	ADMIN_LOGIN("admin_login", "登陆信息");
	
	private MLCacheAdmin(String key, String desc) {
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