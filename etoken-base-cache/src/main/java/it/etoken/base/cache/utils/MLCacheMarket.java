package it.etoken.base.cache.utils;

import it.etoken.cache.service.MLCacheEnum;

/**
 * @author hule
 *
 */
public enum MLCacheMarket implements MLCacheEnum {

	ok_rate("ok_rate", "美元汇率");
	
	private MLCacheMarket(String key, String desc) {
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