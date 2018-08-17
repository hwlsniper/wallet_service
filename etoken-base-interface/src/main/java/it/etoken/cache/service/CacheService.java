package it.etoken.cache.service;

import java.util.List;

public interface CacheService {

	public boolean set(MLCacheEnum key, Object value);

	public boolean set(String key, Object value);

	public <T> T get(MLCacheEnum key, Class<T> clz);

	public <T> T get(String key, Class<T> clz);

	public boolean expire(MLCacheEnum key, long expire);

	public <T> boolean setList(MLCacheEnum key, List<T> list);

	public <T> List<T> getList(MLCacheEnum key, Class<T> clz);

	public long lpush(MLCacheEnum key, Object obj);

	public long rpush(MLCacheEnum key, Object obj);

	public String lpop(MLCacheEnum key);

	public boolean expire(String key, long expire);

	public Long incr(String key);

	/**
	 * 邀请奖励
	 * 
	 * @return
	 */
	public Double getLevel1Reward();

	/**
	 * 邀请奖励
	 * 
	 * @return
	 */
	public Double getLevel2Reward();

	/**
	 * 邀请奖励
	 * 
	 * @return
	 */
	public void setLevel1Reward(Double value);

	/**
	 * 邀请奖励
	 * 
	 * @return
	 */
	public void setLevel2Reward(Double value);

	/**
	 * 注册奖励
	 * 
	 * @return
	 */
	public Double getRegReward();

	/**
	 * 注册奖励
	 * 
	 * @return
	 */
	public void setRegReward(Double value);

	/**
	 * 设置登陆token
	 * 
	 * @param id
	 */
	public void setUserToken(Long id, String token);

	/**
	 * 获得登陆token
	 * 
	 * @param id
	 * @return
	 */
	public String getUserToken(Long id);

	/**
	 * 设置登陆token
	 * 
	 * @param id
	 */
	public void setAdminToken(Long id, String token);

	/**
	 * 获得登陆token
	 * 
	 * @param id
	 * @return
	 */
	public String getAdminToken(Long id);

	public Long getSmsIpMax();

	public Long getSmsPhoneMax();

	public void setSmsIpMax(Long count);

	public void setSmsPhoneMax(Long count);

	public String getInviteUrl();

	public void setInviteUrl(String url);

	/**
	 * 用户积分奖励
	 */
	/**
	 * 获取签到每次奖励
	 * 
	 * @return
	 */
	public Integer getSigninReward();

	/**
	 * 设置签到每次奖励
	 * 
	 * @return
	 */
	public void setSigninReward(int value);

	/**
	 * 获取点赞每次奖励
	 * 
	 * @return
	 */
	public Integer getUpReward();

	/**
	 * 设置点赞每次奖励
	 * 
	 * @return
	 */
	public void setUpReward(int value);

	/**
	 * 获取点踩每次奖励
	 * 
	 * @return
	 */
	public Integer getDownReward();

	/**
	 * 设置点踩每次奖励
	 * 
	 * @return
	 */
	public void setDownReward(int value);

	/**
	 * 获取咨询分享每次奖励
	 * 
	 * @return
	 */
	public Integer getShareReward();

	/**
	 * 设置咨询分享每次奖励
	 * 
	 * @return
	 */
	public void setShareReward(int value);

	/**
	 * 获取每日签到奖励上限
	 * 
	 * @return
	 */
	public Integer getSigninRewardPerDay();

	/**
	 * 设置每日签到奖励上限
	 * 
	 * @return
	 */
	public void setSigninRewardPerDay(int value);

	/**
	 * 获取每日点赞奖励上限
	 * 
	 * @return
	 */
	public Integer getUpRewardPerDay();

	/**
	 * 设置每日点赞奖励上限
	 * 
	 * @return
	 */
	public void setUpRewardPerDay(int value);

	/**
	 * 获取每日点踩奖励上限
	 * 
	 * @return
	 */
	public Integer getDownRewardPerDay();

	/**
	 * 设置每日点踩奖励上限
	 * 
	 * @return
	 */
	public void setDownRewardPerDay(int value);

	/**
	 * 获取每日咨询分享奖励上限
	 * 
	 * @return
	 */
	public Integer getShareRewardPerDay();

	/**
	 * 设置每日咨询分享奖励上限
	 * 
	 * @return
	 */
	public void setShareRewardPerDay(int value);
	
	/**
	 * 获取每日累计奖励上限
	 * 
	 * @return
	 */
	public Integer getTotalRewardPerDay();

	/**
	 * 设置每日累计奖励上限
	 * 
	 * @return
	 */
	public void setTotalRewardPerDay(int value);
}
