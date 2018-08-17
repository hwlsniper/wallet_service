package it.etoken.base.cache.service.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;

import it.etoken.base.cache.utils.MLCacheUser;
import it.etoken.base.cache.utils.MLCacheUserPoint;
import it.etoken.cache.service.CacheService;
import it.etoken.cache.service.MLCacheEnum;

/**
 * 
 * @author hule
 *
 */
@Service
public class CacheServiceImpl implements CacheService {

	private final static Logger logger = LoggerFactory.getLogger(CacheServiceImpl.class);

	@Autowired
	private RedisTemplate<String, ?> redisTemplate;

	@Override
	public boolean set(final MLCacheEnum key, Object value) {
		return this.set(key.getKey(), value);
	}

	@Override
	public boolean set(final String key, Object value) {
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				connection.set(serializer.serialize(key), serializer.serialize(JSON.toJSONString(value)));
				return true;
			}
		});
		return result;
	}

	public <T> T get(String key, Class<T> clz) {
		String json = redisTemplate.execute(new RedisCallback<String>() {
			@Override
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				byte[] value = connection.get(serializer.serialize(key));
				return serializer.deserialize(value);
			}
		});
		if (json != null) {
			return JSON.parseObject(json, clz);
		}
		return null;
	}

	public <T> T get(MLCacheEnum key, Class<T> clz) {
		return this.get(key.getKey(), clz);
	}

	@Override
	public boolean expire(final MLCacheEnum key, long expire) {
		return redisTemplate.expire(key.getKey(), expire, TimeUnit.SECONDS);
	}

	@Override
	public boolean expire(String key, long expire) {
		return redisTemplate.expire(key, expire, TimeUnit.SECONDS);
	}

	@Override
	public <T> boolean setList(MLCacheEnum key, List<T> list) {
		return set(key, list);
	}

	@Override
	public <T> List<T> getList(MLCacheEnum key, Class<T> clz) {
		String json = redisTemplate.execute(new RedisCallback<String>() {
			@Override
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				byte[] value = connection.get(serializer.serialize(key.getKey()));
				return serializer.deserialize(value);
			}
		});
		if (json != null) {
			List<T> list = JSON.parseArray(json, clz);
			return list;
		}
		return null;
	}

	@Override
	public long lpush(final MLCacheEnum key, Object obj) {
		final String value = JSON.toJSONString(obj);
		long result = redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				long count = connection.lPush(serializer.serialize(key.getKey()), serializer.serialize(value));
				return count;
			}
		});
		return result;
	}

	@Override
	public long rpush(final MLCacheEnum key, Object obj) {
		final String value = JSON.toJSONString(obj);
		long result = redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				long count = connection.rPush(serializer.serialize(key.getKey()), serializer.serialize(value));
				return count;
			}
		});
		return result;
	}

	@Override
	public String lpop(final MLCacheEnum key) {
		String result = redisTemplate.execute(new RedisCallback<String>() {
			@Override
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				byte[] res = connection.lPop(serializer.serialize(key.getKey()));
				return serializer.deserialize(res);
			}
		});
		return result;
	}

	public Long incr(String key) {
		return redisTemplate.boundValueOps(key).increment(1);
	}

	@Override
	public Double getRegReward() {
		try {
			Double reward = get(MLCacheUser.REG_REWARD, Double.class);
			if (reward == null) {
				reward = 1.0;
			}
			return reward;
		} catch (Exception e) {
			return 1.0;
		}
	}

	@Override
	public void setUserToken(Long id, String token) {
		set("user_token" + id, token);
	}

	@Override
	public String getUserToken(Long id) {
		return get("user_token" + id, String.class);
	}

	@Override
	public void setAdminToken(Long id, String token) {
		set("admin_token" + id, token);
	}

	@Override
	public String getAdminToken(Long id) {
		return get("admin_token" + id, String.class);
	}

	@Override
	public void setRegReward(Double value) {
		set(MLCacheUser.REG_REWARD, value);
	}

	@Override
	public Double getLevel1Reward() {
		try {
			Double reward = get(MLCacheUser.LEV1_REWARD, Double.class);
			if (reward == null) {
				reward = 0.05;
			}
			return reward;
		} catch (Exception e) {
			return 0.05;
		}
	}

	@Override
	public Double getLevel2Reward() {
		try {
			Double reward = get(MLCacheUser.LEV2_REWARD, Double.class);
			if (reward == null) {
				reward = 0.01;
			}
			return reward;
		} catch (Exception e) {
			return 0.01;
		}
	}

	@Override
	public void setLevel1Reward(Double value) {
		set(MLCacheUser.LEV1_REWARD, value);
	}

	@Override
	public void setLevel2Reward(Double value) {
		set(MLCacheUser.LEV2_REWARD, value);
	}

	@Override
	public Long getSmsIpMax() {
		try {
			Long reward = get(MLCacheUser.SMS_IP, Long.class);
			if (reward == null) {
				reward = new Long(10);
			}
			return reward;
		} catch (Exception e) {
			return new Long(10);
		}
	}

	@Override
	public Long getSmsPhoneMax() {
		try {
			Long reward = get(MLCacheUser.SMS_PHONE, Long.class);
			if (reward == null) {
				reward = new Long(5);
			}
			return reward;
		} catch (Exception e) {
			return new Long(5);
		}
	}

	@Override
	public void setSmsIpMax(Long count) {
		set(MLCacheUser.SMS_IP, count);
	}

	@Override
	public void setSmsPhoneMax(Long count) {
		set(MLCacheUser.SMS_PHONE, count);
	}

	@Override
	public String getInviteUrl() {
		try {
			String reward = get(MLCacheUser.INVITE, String.class);
			if (StringUtils.isEmpty(reward)) {
				return "http://static.eostoken.im/invite/index.html";
			}
			return reward;
		} catch (Exception e) {
			return "http://static.eostoken.im/invite/index.html";
		}
	}

	@Override
	public void setInviteUrl(String url) {
		set(MLCacheUser.INVITE, url);
	}

	@Override
	public Integer getSigninReward() {
		try {
			Integer reward = get(MLCacheUserPoint.SIGNIN_REWARD, Integer.class);
			if (reward == null) {
				reward = 1;
			}
			return reward;
		} catch (Exception e) {
			return 1;
		}
	}

	@Override
	public void setSigninReward(int value) {
		set(MLCacheUserPoint.SIGNIN_REWARD, value);
	}

	@Override
	public Integer getUpReward() {
		try {
			Integer reward = get(MLCacheUserPoint.UP_REWARD, Integer.class);
			if (reward == null) {
				reward = 1;
			}
			return reward;
		} catch (Exception e) {
			return 1;
		}
	}

	@Override
	public void setUpReward(int value) {
		set(MLCacheUserPoint.UP_REWARD, value);
	}

	@Override
	public Integer getDownReward() {
		try {
			Integer reward = get(MLCacheUserPoint.DOWN_REWARD, Integer.class);
			if (reward == null) {
				reward = 1;
			}
			return reward;
		} catch (Exception e) {
			return 1;
		}
	}

	@Override
	public void setDownReward(int value) {
		set(MLCacheUserPoint.DOWN_REWARD, value);
	}

	@Override
	public Integer getShareReward() {
		try {
			Integer reward = get(MLCacheUserPoint.SHARE_REWARD, Integer.class);
			if (reward == null) {
				reward = 1;
			}
			return reward;
		} catch (Exception e) {
			return 1;
		}
	}

	@Override
	public void setShareReward(int value) {
		set(MLCacheUserPoint.SHARE_REWARD, value);
	}

	@Override
	public Integer getSigninRewardPerDay() {
		try {
			Integer reward = getSigninReward();
			Integer rewardPerDay = get(MLCacheUserPoint.SIGNIN_REWARD_PER_DAY, Integer.class);
			if (rewardPerDay == null || rewardPerDay < reward) {
				rewardPerDay = reward;
			}
			return rewardPerDay;
		} catch (Exception e) {
			return 1;
		}
	}

	@Override
	public void setSigninRewardPerDay(int value) {
		Integer reward = getSigninReward();
		if (value < reward) {
			return;
		}
		set(MLCacheUserPoint.SIGNIN_REWARD_PER_DAY, value);
	}

	@Override
	public Integer getUpRewardPerDay() {
		try {
			Integer reward = getUpReward();
			Integer rewardPerDay = get(MLCacheUserPoint.UP_REWARD_PER_DAY, Integer.class);
			if (rewardPerDay == null || rewardPerDay < reward) {
				rewardPerDay = reward;
			}
			return rewardPerDay;
		} catch (Exception e) {
			return 1;
		}
	}

	@Override
	public void setUpRewardPerDay(int value) {
		Integer reward = getUpReward();
		if (value < reward) {
			return;
		}
		set(MLCacheUserPoint.UP_REWARD_PER_DAY, value);
	}

	@Override
	public Integer getDownRewardPerDay() {
		try {
			Integer reward = getDownReward();
			Integer rewardPerDay = get(MLCacheUserPoint.DOWN_REWARD_PER_DAY, Integer.class);
			if (rewardPerDay == null || rewardPerDay < reward) {
				rewardPerDay = reward;
			}
			return rewardPerDay;
		} catch (Exception e) {
			return 1;
		}
	}

	@Override
	public void setDownRewardPerDay(int value) {
		Integer reward = getDownReward();
		if (value < reward) {
			return;
		}
		set(MLCacheUserPoint.DOWN_REWARD_PER_DAY, value);
	}

	@Override
	public Integer getShareRewardPerDay() {
		try {
			Integer reward = getShareReward();
			Integer rewardPerDay = get(MLCacheUserPoint.SHARE_REWARD_PER_DAY, Integer.class);
			if (rewardPerDay == null || rewardPerDay < reward) {
				rewardPerDay = reward;
			}
			return rewardPerDay;
		} catch (Exception e) {
			return 1;
		}
	}

	@Override
	public void setShareRewardPerDay(int value) {
		Integer reward = getShareReward();
		if (value < reward) {
			return;
		}
		set(MLCacheUserPoint.SHARE_REWARD_PER_DAY, value);
	}

	@Override
	public Integer getTotalRewardPerDay() {
		try {
			Integer reward = get(MLCacheUserPoint.TOTAL_REWARD_PER_DAY, Integer.class);
			if (reward == null) {
				reward = getSigninRewardPerDay() + getUpRewardPerDay() + getDownRewardPerDay() + getShareRewardPerDay();
			}
			return reward;
		} catch (Exception e) {
			return 1;
		}
	}

	@Override
	public void setTotalRewardPerDay(int value) {
		Integer reward = getSigninRewardPerDay() + getUpRewardPerDay() + getDownRewardPerDay() + getShareRewardPerDay();
		if (value < reward) {
			return;
		}
		set(MLCacheUserPoint.TOTAL_REWARD_PER_DAY, value);
	}
}
