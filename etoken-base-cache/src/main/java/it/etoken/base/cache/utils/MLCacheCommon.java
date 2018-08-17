package it.etoken.base.cache.utils;

import it.etoken.cache.service.MLCacheEnum;

/**
 * @author hule
 *
 */
public enum MLCacheCommon implements MLCacheEnum {

	system_err("session", "未知异常");
	
	private MLCacheCommon(String key, String desc) {
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