package it.etoken.base.cache.utils;

import it.etoken.cache.service.MLCacheEnum;

/**
 * @author hule
 *
 */
public enum MLCacheUserPoint implements MLCacheEnum {

	/**
	 * 每次奖励配置
	 */
	SIGNIN_REWARD("signin_reward", "签到每次奖励"),
	UP_REWARD("up_reward", "点赞每次奖励"),
	DOWN_REWARD("down_reward", "点踩每次奖励"),
	SHARE_REWARD("share_reward", "分享每次奖励"),
	
	/**
	 * 每日奖励上限
	 */
	SIGNIN_REWARD_PER_DAY("signin_reward_per_day", "每日签到奖励上限"),
	UP_REWARD_PER_DAY("up_reward_per_day", "每日点赞奖励上限"),
	DOWN_REWARD_PER_DAY("down_reward_per_day", "每日点踩奖励上限"),
	SHARE_REWARD_PER_DAY("share_reward_per_day", "每日分享奖励上限"),
	TOTAL_REWARD_PER_DAY("total_reward_per_day", "每日累计奖励上限");
	
	private MLCacheUserPoint(String key, String desc) {
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