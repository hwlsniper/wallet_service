package it.etoken.component.user.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.util.StringUtil;

import it.etoken.base.common.Constant.PageConstant;
import it.etoken.base.common.exception.MLCommonException;
import it.etoken.base.common.exception.MLException;
import it.etoken.base.common.result.MLPage;
import it.etoken.base.common.utils.MathUtil;
import it.etoken.base.model.admin.entity.SysConfig;
import it.etoken.base.model.user.entity.EosAccountOrder;
import it.etoken.base.model.user.entity.EosAccountOrderExample;
import it.etoken.base.model.user.entity.EostRecord;
import it.etoken.base.model.user.entity.User;
import it.etoken.base.model.user.entity.UserExt;
import it.etoken.base.model.user.entity.UserExtExample;
import it.etoken.base.model.user.entity.UserInvite;
import it.etoken.base.model.user.entity.UserInviteRecord;
import it.etoken.base.model.user.entity.UserPoint;
import it.etoken.base.model.user.entity.UserPointRecord;
import it.etoken.base.model.user.entity.UserSigninLog;
import it.etoken.base.model.user.entity.UserSigninLogExample;
import it.etoken.base.model.user.entity.UserSigninLogExample.Criteria;
import it.etoken.base.model.user.vo.InviteInfo;
import it.etoken.base.model.user.vo.LoginUser;
import it.etoken.base.model.user.vo.UpInvite;
import it.etoken.base.model.user.vo.UserPointVO;
import it.etoken.cache.service.CacheService;
import it.etoken.component.user.dao.mapper.EosAccountOrderMapper;
import it.etoken.component.user.dao.mapper.EostRecordMapper;
import it.etoken.component.user.dao.mapper.SysConfigMaper;
import it.etoken.component.user.dao.mapper.UserExtMapper;
import it.etoken.component.user.dao.mapper.UserInviteMapper;
import it.etoken.component.user.dao.mapper.UserInviteRecordMapper;
import it.etoken.component.user.dao.mapper.UserMapper;
import it.etoken.component.user.dao.mapper.UserPointMapper;
import it.etoken.component.user.dao.mapper.UserSigninLogMapper;
import it.etoken.component.user.exception.MLUserException;
import it.etoken.component.user.service.UserService;
import it.etoken.componet.user.point.UserPointType;

@Component
@Transactional
public class UserServiceImpl implements UserService {

	private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	/**
	 * 邀请是否奖励eos
	 */
	private boolean isInviteRewardEos = false;
	/**
	 * 注册是否奖励eos
	 */
	private boolean isRegRewardEos = false;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private UserInviteMapper userInviteMapper;

	@Autowired
	private UserInviteRecordMapper userInviteRecordMapper;

	@Autowired
	private EostRecordMapper eostRecordMapper;

	@Autowired
	private UserPointMapper userPointMapper;

	@Autowired
	CacheService cacheService;
	
	@Autowired
	private UserSigninLogMapper  userSigninLogMapper;
	
	@Autowired
	private UserExtMapper userExtMapper;
	
	@Autowired
	private SysConfigMaper sysConfigMaper;
	
	@Autowired
	private EosAccountOrderMapper eosAccountOrderMapper;
	
	public final Long CREATE_EOS_ACCOUNT_NEED_POINT = 10L;

	@Override
	public LoginUser register(String phone, String invite, String password) throws MLException {
		try {
			User user = userMapper.login(phone);
			// 用户名已存在
			if (user != null) {
				throw new MLException(MLUserException.REG_EXIST);
			}
			UserInvite userInvite = null;
			// 输入了邀请码
			if (!StringUtil.isEmpty(invite)) {
				userInvite = userInviteMapper.findByCode(invite);
				// 邀请嘛不存在
				if (userInvite == null) {
					throw new MLException(MLUserException.REG_INVCODE);
				}
			}

			// 保存用户
			User regUser = new User();
			regUser.setPassword(MathUtil.MD5(password));
			regUser.setUsername(phone);
			regUser.setNickname(MathUtil.phoneRep(phone));
			userMapper.insert(regUser);

			// 生成邀请码
			UserInvite i = new UserInvite();
			i.setUid(regUser.getId());
			i.setCode(MathUtil.inviteCode(regUser.getId()));
			i.setUsed(0l);
			i.setMax_use(-1l);
			userInviteMapper.insert(i);

			// 邀请奖励
			if (userInvite != null) {
				// 添加邀请，更新邀请数量
				addInvite(regUser.getId(), userInvite.getId());
				// 不限制数量
				inviteReward(regUser.getId(), userInvite.getUid(), userInvite.getId());
				// 注册送币
				regReward(regUser.getId());
			}

			return parserLogin(regUser);

		} catch (MLException ex) {
			logger.error("", ex);
			throw ex;
		} catch (Exception e) {
			logger.error("", e);
			throw new MLException(MLCommonException.system_err);
		}
	}

	private void inviteReward(Long regUid, Long iUid, Long iid) {
		if (!isInviteRewardEos) {
			return;
		}
		Double ireward1 = cacheService.getLevel1Reward();
		Double ireward2 = cacheService.getLevel2Reward();
		// 邀请者一级
		userMapper.addEost(iUid, ireward1);
		// 保存奖励记录
		EostRecord r1 = new EostRecord();
		r1.setUid(iUid);
		r1.setBid(iid);
		r1.setType("invite_l1");
		r1.setEost(ireward1);
		eostRecordMapper.insert(r1);
		// 注册者奖励
		userMapper.addEost(regUid, ireward1);
		// 保存奖励记录
		EostRecord r2 = new EostRecord();
		r2.setUid(regUid);
		r2.setBid(iid);
		r2.setType("invite_l1");
		r2.setEost(ireward1);
		eostRecordMapper.insert(r2);

		// 二级奖励
		UpInvite up = userInviteRecordMapper.getUpInvite(iUid);
		if (up != null) {
			// 注册者奖励
			userMapper.addEost(up.getUid(), ireward2);
			// 保存奖励记录
			EostRecord r3 = new EostRecord();
			r3.setUid(up.getUid());
			r3.setBid(up.getIid());
			r3.setType("invite_l2");
			r3.setEost(ireward2);
			eostRecordMapper.insert(r3);
		}
	}

	/**
	 * 添加邀请，并更新邀请数量
	 * 
	 * @param uid
	 * @param iid
	 */
	private void addInvite(Long uid, Long iid) {
		UserInviteRecord record1 = new UserInviteRecord();
		record1.setUid(uid);
		record1.setIid(iid);
		userInviteRecordMapper.insert(record1);
		userInviteMapper.updateUsed(iid);
	}

	/**
	 * 注册奖励
	 * 
	 * @param uid
	 */
	private void regReward(Long uid) {
		if (!isRegRewardEos) {
			return;
		}
		Double rward = cacheService.getRegReward();
		userMapper.addEost(uid, rward);
		EostRecord r1 = new EostRecord();
		r1.setUid(uid);
		r1.setBid(uid);
		r1.setType("reg");
		r1.setEost(rward);
		eostRecordMapper.insert(r1);
	}

	/**
	 * 登陆信息
	 * 
	 * @param user
	 * @return
	 */
	private LoginUser parserLogin(User user) {
		LoginUser loginUser = new LoginUser();
		loginUser.setNickname(user.getNickname());
		loginUser.setPhoto(user.getPhoto());
		loginUser.setToken(MathUtil.getUUID());
		loginUser.setUid(user.getId());
		loginUser.setEost(user.getEost());
		loginUser.setReward(String.valueOf(cacheService.getLevel1Reward()));
		UserInvite userInvite = userInviteMapper.findByUid(loginUser.getUid());
		if (userInvite != null) {
			loginUser.setInvite(userInvite.getCode());
		}
		// 保存token
		cacheService.setUserToken(loginUser.getUid(), loginUser.getToken());
		return loginUser;
	}

	/**
	 * 登陆
	 */
	@Override
	public LoginUser login(String phone, String password) throws MLException {
		try {
			User user = userMapper.login(phone);
			// 用户名已存在
			if (user == null || !MathUtil.MD5(password).equals(user.getPassword())) {
				throw new MLException(MLUserException.LOGIN_ERR);
			}
			if (user.getStatus() != 0) {
				throw new MLException(MLUserException.LOGIN_FORBID);
			}
			return parserLogin(user);
		} catch (MLException ex) {
			logger.error(ex.toString());
			throw ex;
		} catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}

	@Override
	public MLPage<User> findAll(String nickname, int page) throws MLException {
		try {
			Page<User> result = PageHelper.startPage(page, PageConstant.size);
			userMapper.search(nickname);
			return new MLPage<User>(result.getResult(), result.getTotal());
		} catch (MLException ex) {
			logger.error(ex.toString());
			throw ex;
		} catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}

	@Override
	public void changeStatus(Long id, Integer status) throws MLException {
		try {
			userMapper.updateStatus(id, status);
		} catch (MLException ex) {
			logger.error(ex.toString());
			throw ex;
		} catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}

	@Override
	public void chagePwd(String phone, String pwd) throws MLException {
		try {
			if (userMapper.exist(phone) > 0) {
				userMapper.updatePwd(phone, MathUtil.MD5(pwd));
			} else {
				throw new MLException(MLUserException.USER_EXIST);
			}

		} catch (MLException ex) {
			logger.error(ex.toString());
			throw ex;
		} catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}

	@Override
	public LoginUser loginUserInfo(Long id) throws MLException {
		try {
			User user = userMapper.findById(id);
			LoginUser loginUser = new LoginUser();
			loginUser.setNickname(user.getNickname());
			loginUser.setPhoto(user.getPhoto());
			loginUser.setUid(user.getId());
			loginUser.setEost(user.getEost());
			return loginUser;
		} catch (MLException ex) {
			logger.error(ex.toString());
			throw ex;
		} catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}

	@Override
	public InviteInfo inviteInfo(Long id) throws MLException {
		try {
			User user = userMapper.findById(id);
			UserInvite i = userInviteMapper.findByUid(id);
			Long c = userInviteRecordMapper.leve2Count(id);
			InviteInfo iv = new InviteInfo();
			iv.setCode(i.getCode());
			iv.setInviteL1Count(String.valueOf(i.getUsed()));
			if (c == null) {
				iv.setInviteL2Count(String.valueOf(0));
			} else {
				iv.setInviteL2Count(String.valueOf(c));
			}
			iv.setReward(String.valueOf(user.getEost()));
			iv.setL1Reward(String.valueOf(cacheService.getLevel1Reward()));
			iv.setL2Reward(String.valueOf(cacheService.getLevel2Reward()));
			iv.setRegReward(String.valueOf(cacheService.getRegReward()));
			iv.setInviteUrl(cacheService.getInviteUrl());
			return iv;
		} catch (MLException ex) {
			logger.error(ex.toString());
			throw ex;
		} catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}

	@Override
	public String getBindCode(Long id) throws MLException {
		try {
			String code = userInviteRecordMapper.getBindCode(id);
			return code;
		} catch (MLException ex) {
			logger.error(ex.toString());
			throw ex;
		} catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}

	@Override
	public void bindCode(Long uid, String code) throws MLException {
		try {
			UserInvite userInvite = userInviteMapper.findByCode(code);
			// 邀请嘛不存在
			if (userInvite == null) {
				throw new MLException(MLUserException.REG_INVCODE);
			}

			if (uid.equals(userInvite.getUid())) {
				throw new MLException(MLUserException.BIND_SELF);
			}
			Long c = userInviteRecordMapper.isBind(uid);
			logger.error("==============================" + uid + "=======" + userInvite.getId() + "======" + c);
			if (c > 0) {
				throw new MLException(MLUserException.ISBIND);
			}
			// 添加邀请
			addInvite(uid, userInvite.getId());
			// 不限制数量
			inviteReward(uid, userInvite.getUid(), userInvite.getId());
			// 注册送币
			regReward(uid);

		} catch (MLException ex) {
			logger.error(ex.toString());
			throw ex;
		} catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}

	@Override
	public MLPage<UserPointRecord> getPointRecord(String nickname, int page) throws MLException {
		try {
			Page<UserPointRecord> result = PageHelper.startPage(page, PageConstant.size);
			userPointMapper.findAllRecord(nickname);
			return new MLPage<UserPointRecord>(result.getResult(), result.getTotal());
		} catch (MLException ex) {
			logger.error(ex.toString());
			throw ex;
		} catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}

	@Override
	public UserPointVO getPoint(String uid) throws MLException {
		try {
			UserPoint points = userPointMapper.findByUid(uid);
			if (points == null) {
				points = new UserPoint();
				points.setUid(Long.parseLong(uid));
				points.setSignin((long) 0);
				points.setShare((long) 0);
				points.setInteract((long) 0);
				points.setStore((long) 0);
				points.setTurnin((long) 0);
				points.setTurnout((long) 0);
				points.setCreatedate(new Date());
				points.setModifydate(new Date());
			}
			UserPointVO vo = new UserPointVO();
			BeanUtils.copyProperties(points, vo);
			return vo;
		} catch (MLException ex) {
			logger.error(ex.toString());
			throw ex;
		} catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}

	@Override
	public UserPointRecord updatePoint(UserPointType type, String uid) throws MLException {

		UserPoint points = new UserPoint();
		points.setUid(Long.parseLong(uid));
		points.setSignin((long) 0);
		points.setShare((long) 0);
		points.setInteract((long) 0);
		points.setStore((long) 0);
		points.setTurnin((long) 0);
		points.setTurnout((long) 0);
		try {
			long reward = 0;
			User user = userMapper.findById(Long.parseLong(uid));
			if (type == UserPointType.SIGN_IN) { // 签到积分，日上限一次， 连续三日+1，连续五日+2, 连续七日+3
				Long hasLogin = cacheService.get("point_has_signin_" + uid, Long.class);
				if (hasLogin != null) {
					// 当天已经签到过
					throw new MLException(MLUserException.SIGNIN_ERR);
				}
				cacheService.set("point_has_signin_" + uid, uid);
				cacheService.expire("point_has_signin_" + uid, getLeftTimeOfToday("Asia/Shanghai"));

				long recordCnt = userPointMapper.countSigninRecordByUid(uid);
				if (recordCnt >= 1) {
					// 当天已经签到过
					logger.error("signin exception --- uid=" + uid);
					throw new MLException(MLUserException.SIGNIN_ERR);
				}
				
				//添加签到记录 ，防止刷签到。add by abill
				UserSigninLog userSigninLog = new UserSigninLog();
				userSigninLog.setUid(user.getId());
				userSigninLog.setNickname(user.getNickname());
				userSigninLog.setSigndate(new Date());
				userSigninLogMapper.insert(userSigninLog);
				
				reward = getSignPointReward(uid);
				points.setSignin(reward);
			} else if (type == UserPointType.UP) { // 点赞
				int max = cacheService.getUpRewardPerDay();
				int unit = cacheService.getUpReward();
				reward = getPointReward(type.name(), uid, max, unit);
				points.setInteract(reward);
			} else if (type == UserPointType.DOWN) { // 点踩
				int max = cacheService.getDownRewardPerDay();
				int unit = cacheService.getDownReward();
				reward = getPointReward(type.name(), uid, max, unit);
				points.setInteract(reward);
			} else if (type == UserPointType.SHARE) { // 分享
				int max = cacheService.getShareRewardPerDay();
				int unit = cacheService.getShareReward();
				reward = getPointReward(type.name(), uid, max, unit);
				points.setShare(reward);
			}
			if (reward <= 0) {
				throw new MLException(MLUserException.NO_REWARD);
			}

			userMapper.addPoint(Long.parseLong(uid), reward); // 增加总积分

			

			UserPointRecord record = new UserPointRecord(); // 积分记录
			record.setUid(Long.parseLong(uid));
			record.setNickname(user.getNickname());
			record.setType(type.name());
			record.setPoint(reward);
			userPointMapper.insertRecord(record); // 记录积分记录

			UserPoint pointsInSql = userPointMapper.findByUid(uid);
			if (pointsInSql == null) {
				points.setNickname(user.getNickname());
				userPointMapper.insert(points); // 没有时插入各项积分累计
				return record;
			}

			if (type == UserPointType.SIGN_IN) {
				pointsInSql.setSignin(pointsInSql.getSignin() + points.getSignin());
			} else if ((type == UserPointType.UP) || (type == UserPointType.DOWN)) {
				pointsInSql.setInteract(pointsInSql.getInteract() + points.getInteract());
			} else if (type == UserPointType.SHARE) {
				pointsInSql.setShare(pointsInSql.getShare() + points.getShare());
			}
			userPointMapper.update(pointsInSql); // 存在时更新各项积分累计

			return record;
		} catch (MLException ex) {
			logger.error(ex.toString());
			throw ex;
		} catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}

	/**
	 * 获取到当天结束还有多少秒
	 * 
	 * @return
	 */
	public static Long getLeftTimeOfToday(String zone) {
		if (zone == null || "".equals(zone)) {
			zone = "Asia/Shanghai";
		}

		ZoneId zoneId = ZoneId.of(zone);
		LocalDateTime midnight = LocalDateTime.now(zoneId).plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
		long seconds = ChronoUnit.SECONDS.between(LocalDateTime.now(zoneId), midnight);
		System.out.println("midnight111111111111111:"+midnight);
		return seconds;
	}

	/**
	 * 获取签到对应所得的积分:连续三日+1，连续五日+2, 连续七日+3
	 * 
	 * @param uid
	 * @return
	 */
	private int getSignPointReward(String uid) {
		int rewardUnit = cacheService.getSigninReward();
		int[] pointParams = { rewardUnit, rewardUnit, rewardUnit + 1, rewardUnit, rewardUnit + 2, rewardUnit,
				rewardUnit + 3 };
		String contkey = "point_signin_" + uid;
		String firstkey = "firstkey" + uid;
		String lasttkey = "lasttkey" + uid;
		Long cnt1 = cacheService.incr(contkey);
		Long lasttime = cacheService.get(lasttkey, Long.class);
		int rettime = 0;
		Long leftTimeOfToday = getLeftTimeOfToday("Asia/Shanghai");
		if (cnt1 == 1 || lasttime == null) {
			cacheService.expire(contkey, 6 * 24 * 60 * 60 + leftTimeOfToday);
			cacheService.set(firstkey, System.currentTimeMillis());
			rettime = 0;
		} else {
			Long first = cacheService.get(firstkey, Long.class);
			rettime = (int) (((System.currentTimeMillis() - first) / (24 * 60 * 60 * 1000)));

		}
		if (rettime < 0) {
			rettime = 0;
		}

		cacheService.set(lasttkey, System.currentTimeMillis());
		cacheService.expire(lasttkey, 24 * 60 * 60 + leftTimeOfToday);
		return pointParams[rettime];
	}

	/**
	 * 
	 * @param type
	 *            积分奖励类型
	 * @param max
	 *            最大奖励数目
	 * @param unit
	 *            每次奖励数
	 * @return
	 */
	private int getPointReward(String type, String uid, int max, int unit) {
		try {
			List<UserPointRecord> tupr = userPointMapper.findTodayRecord(uid, type);
			int tsum = 0; // 今天点赞积分总和
			for (UserPointRecord upr : tupr) {
				tsum += upr.getPoint();
			}
			if (tsum >= max) {
				return 0;
			}
		} catch (MLException ex) {
			logger.error(ex.toString());
			return 0;
		} catch (Exception e) {
			logger.error(e.toString());
			return 0;
		}

		Long cnt = new Long(0);
		for (int i = 0; i < unit; i++) {
			cnt = cacheService.incr(type + uid);
		}

		if (cnt == unit) {
			cacheService.expire(type + uid, getLeftTimeOfToday("Asia/Shanghai")); // 每天只能奖励一次控制
		} else {
			if (cnt.intValue() > max) {
				return 0;
			}
		}

		return unit;
	}
	
	@Override
	public Boolean isSigned(String uid) throws MLException {
		Long hasLogin = cacheService.get("point_has_signin_" + uid, Long.class);
		if (hasLogin != null) {
			// 当天已经签到过
			return true;
		}
		UserSigninLogExample usle = new UserSigninLogExample();
		Criteria criteria = usle.createCriteria();
		criteria.andUidEqualTo(Long.valueOf(uid));
		
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
		Date nowDate = new Date();
		Date signDate = null;
		try {
			signDate = sdf.parse(sdf.format(nowDate.getTime()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		criteria.andSigndateEqualTo(signDate);
	        
		long signCount = userSigninLogMapper.countByExample(usle);
		if(signCount>=1) {
			return true;
		}
		return false;
	}
	
	@Override
	public Boolean createEosAccount(UserExt userExt) throws MLException {
		User user = userMapper.findById(userExt.getUid());
		if(null == user) {
			throw new MLException(MLUserException.USER_EXIST); //用户不存在
		}
		int point = Integer.parseInt(user.getPoint());
		if(point < CREATE_EOS_ACCOUNT_NEED_POINT) {
			Boolean createByOderResult = this.createEosAccountByOrder(user.getId());
			if(!createByOderResult) {
				throw new MLException(MLUserException.POINT_TOO_LOW);
			}else {
				return true;
			}
		}
		
		UserExtExample example  = new UserExtExample();
		it.etoken.base.model.user.entity.UserExtExample.Criteria criteria = example.createCriteria();
		criteria.andUidEqualTo(userExt.getUid());
		
		List<UserExt> uel = userExtMapper.selectByExample(example);
		if(!uel.isEmpty()) {
			UserExt ue = uel.get(0);
			if(null != ue && !ue.getEosAccount().isEmpty()) {
				throw new MLException(MLUserException.EOS_ACCOUNT_EXIST); //用户已经创建过EOS账号
			}else {
				//已存在扩展表记录，但没有eos账号，修改
				ue.setEosAccount(userExt.getEosAccount());
				int updateResult = userExtMapper.updateByPrimaryKey(userExt);
				if(updateResult==0) {
					throw new MLException(MLUserException.SYS_ERROR);
				}
			}
		}else {
			//创建扩展表记录
			int createResult = userExtMapper.insert(userExt);
			if (createResult == 0) {
				throw new MLException(MLUserException.SYS_ERROR);
			}
		}
		return true;
	}
	
	@Override
	public Boolean createEosAccountByOrder(Long orderId) throws MLException {
		EosAccountOrder paidOrder = eosAccountOrderMapper.selectByPrimaryKey(orderId);
		if(null == paidOrder || !paidOrder.getStatus().equalsIgnoreCase("paid")) {
			throw new MLException(MLUserException.NO_PAY_ORDER_ERR);
		}
		EosAccountOrder updatePaidOrder = new EosAccountOrder();
		updatePaidOrder.setId(paidOrder.getId());
		updatePaidOrder.setStatus("completed");
		updatePaidOrder.setUpdatedate(new Date());
		int result = eosAccountOrderMapper.updateByPrimaryKeySelective(updatePaidOrder);
		if(result == 0) {
			throw new MLException(MLUserException.UPDATE_ORDER_STATE_ERR);
		}
		return true;
	}
	
	@Override
	public EosAccountOrder getOnePaidOderByUid(Long uid) throws MLException {
		EosAccountOrderExample example  = new EosAccountOrderExample();
		it.etoken.base.model.user.entity.EosAccountOrderExample.Criteria criteria = example.createCriteria();
		criteria.andUidEqualTo(uid);
		criteria.andStatusEqualTo("paid");
		example.setOrderByClause("id asc");
		List<EosAccountOrder> paidOrderList = eosAccountOrderMapper.selectByExample(example);
		if(null == paidOrderList || paidOrderList.isEmpty()) {
			throw new MLException(MLUserException.NO_PAY_ORDER_ERR);
		}
		return paidOrderList.get(0);
	}
	
	@Override
	public Boolean canCreateEosAccountByUid(Long uid) throws MLException {
		User user = userMapper.findById(uid);
		if(null == user) {
			throw new MLException(MLUserException.USER_EXIST); //用户不存在
		}
		int point = Integer.parseInt(user.getPoint());
		
		SysConfig sysConfig = sysConfigMaper.findByName("create_eos_account_need_point");
		Long needPoint = CREATE_EOS_ACCOUNT_NEED_POINT;
		if(null != sysConfig) {
			needPoint = Long.valueOf(sysConfig.getValue());
		}
		if(point < needPoint) {
			throw new MLException(MLUserException.POINT_TOO_LOW);
		}
		
		UserExtExample example  = new UserExtExample();
		it.etoken.base.model.user.entity.UserExtExample.Criteria criteria = example.createCriteria();
		criteria.andUidEqualTo(uid);
		
		List<UserExt> uel = userExtMapper.selectByExample(example);
		if(!uel.isEmpty()) {
			UserExt ue = uel.get(0);
			if(null != ue && !ue.getEosAccount().isEmpty()) {
				throw new MLException(MLUserException.EOS_ACCOUNT_EXIST); //用户已经创建过EOS账号
			}
		}
		return true;
	}
	
	@Override
	public Long getCreateEosAccountNeedPoint() throws MLException {
		SysConfig sysConfig = sysConfigMaper.findByName("create_eos_account_need_point");
		Long needPoint = CREATE_EOS_ACCOUNT_NEED_POINT;
		if(null != sysConfig) {
			needPoint = Long.valueOf(sysConfig.getValue());
		}
		return needPoint;
	}
	
	@Override
	public Boolean addPointByUsername(String username, int point){
		try {
			//查询用户是否注册没注册返回用户不存在，已注册，用户表加需要加的积分
			User user = userMapper.findByUsername(username);
			if(null == user) {
				throw new MLException(MLUserException.USER_NOT_EXIST);
			}
			//用户表添加积分
			userMapper.addPoint(Long.valueOf(user.getId()), Long.valueOf(point));
			//查询用户是否今日已签到 如果用户没有签到，插入签到信息，积分为需要加的积分 如果用户已签到，则修改今日签到积分为：需要加的积分+今日签到积分
			UserSigninLogExample example  = new UserSigninLogExample();
			it.etoken.base.model.user.entity.UserSigninLogExample.Criteria  criteria = example.createCriteria();
			criteria.andUidEqualTo(user.getId());
			Date signdate = new Date();
			try {
				SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
				signdate = sdf.parse(sdf.format(signdate));
				criteria.andSigndateEqualTo(signdate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			long countTodaySign = userSigninLogMapper.countByExample(example);
			if(countTodaySign == 0l) {
				UserPointRecord record = new UserPointRecord();
				record.setUid(user.getId());
				record.setNickname(user.getNickname());
				record.setType(UserPointType.SIGN_IN.name());
				record.setPoint(Long.valueOf(point));
				
				userPointMapper.insertRecord(record);
			}else {
				UserPointRecord record=userPointMapper.findUserPointRecordByUid(user.getId());
				Long point_new=record.getPoint()+point;
				record.setPoint(Long.valueOf(point_new));
				userPointMapper.updateRecord(record);
			}
			//查询用户是否存在积分统计记录,如果用户没有积分统计记录，则插入 如果已经存在，则修改 
			UserPoint userPoint=userPointMapper.findByUid(user.getId().toString());
			if(userPoint==null) {
				UserPoint u=new UserPoint();
				u.setUid(user.getId());
				u.setShare(0L);
				u.setInteract(0L);
				u.setStore(0L);
				u.setTurnin(0L);
				u.setTurnout(0L);
				u.setNickname(user.getNickname());
				u.setSignin(Long.valueOf(point));
				userPointMapper.insert(u);
			}else {
				Long singIn=userPoint.getSignin()+point;
				userPoint.setSignin(singIn);
				userPointMapper.update(userPoint);
			}
			return true;
		} catch (MLException ex) {
			logger.error(ex.toString());
			throw ex;
		} catch (Exception e) {
			logger.error(e.toString());
			throw new MLException(MLCommonException.system_err);
		}
	}

	//查询是否是已经注册的用户
	@Override
	public Boolean is_registered_user(String phone) throws MLException {
		try {
			User user = userMapper.login(phone);
			// 用户名已存在
			if (user != null) {
				return true;
			}else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
}
