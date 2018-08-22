package it.etoken.component.user.facede.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

import it.etoken.base.common.exception.MLException;
import it.etoken.base.common.result.MLPage;
import it.etoken.base.common.result.MLResult;
import it.etoken.base.common.result.MLResultObject;
import it.etoken.base.model.user.entity.EosAccountOrder;
import it.etoken.base.model.user.entity.User;
import it.etoken.base.model.user.entity.UserExt;
import it.etoken.base.model.user.entity.UserPointRecord;
import it.etoken.base.model.user.vo.Invite;
import it.etoken.base.model.user.vo.InviteInfo;
import it.etoken.base.model.user.vo.LoginUser;
import it.etoken.base.model.user.vo.Reward;
import it.etoken.base.model.user.vo.UserPointVO;
import it.etoken.component.user.service.UserInviteService;
import it.etoken.component.user.service.UserRewardService;
import it.etoken.component.user.service.UserService;
import it.etoken.componet.user.facade.UserFacadeAPI;
import it.etoken.componet.user.point.UserPointType;

@Service(version = "1.0.0")
public class UserFacadeAPIImpl implements UserFacadeAPI {

	private final static Logger logger = LoggerFactory.getLogger(UserFacadeAPIImpl.class);

	@Autowired
	UserService userService;

	@Autowired
	UserInviteService userInviteService;

	@Autowired
	UserRewardService userRewardService;

	@Override
	public MLResultObject<LoginUser> login(String username, String password) {
		try {
			return new MLResultObject<LoginUser>(userService.login(username, password));
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultObject<LoginUser>(e);
		}
	}

	@Override
	public MLResultObject<LoginUser> register(String phone, String invite, String password) {
		try {
			return new MLResultObject<LoginUser>(userService.register(phone, invite, password));
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultObject<LoginUser>(e);
		}
	}

	@Override
	public MLResultObject<MLPage<User>> findAll(String nickname, int page) {
		try {
			return new MLResultObject<MLPage<User>>(userService.findAll(nickname, page));
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultObject<MLPage<User>>(e);
		}
	}

	@Override
	public MLResult changeStatus(Long id, Integer status) {
		try {
			userService.changeStatus(id, status);
			return new MLResult(true);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResult(e);
		}
	}

	@Override
	public MLResultObject<MLPage<Invite>> findInvite(String code, int page) {
		try {
			return new MLResultObject<MLPage<Invite>>(userInviteService.findAll(code, page));
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultObject<MLPage<Invite>>(e);
		}
	}

	@Override
	public MLResultObject<MLPage<Reward>> findReward(String nickname, int page) {
		try {
			return new MLResultObject<MLPage<Reward>>(userRewardService.findAll(nickname, page));
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultObject<MLPage<Reward>>(e);
		}
	}

	@Override
	public MLResult chagePwd(String phone, String password) {
		try {
			userService.chagePwd(phone, password);
			return new MLResult(true);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResult(e);
		}
	}

	@Override
	public MLResultObject<LoginUser> loginUserInfo(Long id) {
		try {
			return new MLResultObject<LoginUser>(userService.loginUserInfo(id));
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultObject<LoginUser>(e);
		}
	}

	@Override
	public MLResultObject<InviteInfo> inviteInfo(Long id) {
		try {
			return new MLResultObject<InviteInfo>(userService.inviteInfo(id));
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultObject<InviteInfo>(e);
		}
	}

	@Override
	public MLResultObject<String> getBindCode(Long id) {
		try {
			return new MLResultObject<String>(userService.getBindCode(id));
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultObject<String>(e);
		}
	}

	@Override
	public MLResult bindCode(Long id, String code) {
		try {
			userService.bindCode(id, code);
			return new MLResult(true);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResult(e);
		}
	}

	@Override
	public MLResultObject<UserPointRecord> updatePoint(UserPointType type, String uid) {
		try {
			return new MLResultObject<UserPointRecord>(userService.updatePoint(type, uid));
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultObject<UserPointRecord>(e);
		}
	}

	@Override
	public MLResultObject<UserPointVO> getPoint(String uid) {
		try {
			return new MLResultObject<UserPointVO>(userService.getPoint(uid));
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultObject<UserPointVO>(e);
		}
	}

	@Override
	public MLResultObject<MLPage<UserPointRecord>> getPointRecord(String nickname, int page) {
		try {
			return new MLResultObject<MLPage<UserPointRecord>>(userService.getPointRecord(nickname, page));
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultObject<MLPage<UserPointRecord>>(e);
		}
	}
	
	@Override
	public MLResultObject<Boolean> isSigned(String uid) throws MLException {
		try {
			return new MLResultObject<Boolean>(userService.isSigned(uid));
		}
		catch (MLException e){
			logger.error(e.toString());
			return new MLResultObject<Boolean>(e);
		}
	}
	
	@Override
	public MLResultObject<Boolean> createEosAccount(UserExt userExt) throws MLException {
		try {
			return new MLResultObject<Boolean>(userService.createEosAccount(userExt));
		}
		catch (MLException e){
			logger.error(e.toString());
			return new MLResultObject<Boolean>(e);
		}
	}

	@Override
	public MLResultObject<Boolean> canCreateEosAccountByUid(Long uid) throws MLException {
		try {
			return new MLResultObject<Boolean>(userService.canCreateEosAccountByUid(uid));
		}
		catch (MLException e){
			logger.error(e.toString());
			return new MLResultObject<Boolean>(e);
		}
	}
	
	@Override
	public MLResultObject<Long> getCreateEosAccountNeedPoint() throws MLException {
		try {
			return new MLResultObject<Long>(userService.getCreateEosAccountNeedPoint());
		}
		catch (MLException e){
			logger.error(e.toString());
			return new MLResultObject<Long>(e);
		}
	}
	
	@Override
	public MLResultObject<Boolean> addPointByUsername(String username, int point) throws MLException {
		try {
     		return new MLResultObject<Boolean>(userService.addPointByUsername(username, point));
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultObject<Boolean>(e);
		}
	}

	@Override
	public MLResultObject<Boolean> is_registered_user(String phone) {
		try {
     		return new MLResultObject<Boolean>(userService.is_registered_user(phone));
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultObject<Boolean>(e);
		}
	}
	
	@Override
	public MLResultObject<EosAccountOrder> getOnePaidOderByUid(Long uid) throws MLException{
		try {
     		return new MLResultObject<EosAccountOrder>(userService.getOnePaidOderByUid(uid));
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultObject<EosAccountOrder>(e);
		}
	}
	
	@Override
	public MLResultObject<Boolean> createEosAccountByOrder(Long orderId) throws MLException{
		try {
     		return new MLResultObject<Boolean>(userService.createEosAccountByOrder(orderId));
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultObject<Boolean>(e);
		}
	}
	
	@Override
	public MLResultObject<User> findByUid(String uid) {
		try {
     		return new MLResultObject<User>(userService.findByUid(uid));
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResultObject<User>(e);
		}
	}

	@Override
	public MLResult updateEost(String uid) {
		try {
			userService.updateEost(uid);
			return new MLResult(true);
		} catch (MLException e) {
			logger.error(e.toString());
			return new MLResult(e);
		}
	}
}
