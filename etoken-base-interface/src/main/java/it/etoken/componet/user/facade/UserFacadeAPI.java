package it.etoken.componet.user.facade;

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
import it.etoken.componet.user.point.UserPointType;

public interface UserFacadeAPI {

	public MLResultObject<LoginUser> login(String username, String password);

	public MLResultObject<LoginUser> loginUserInfo(Long id);

	public MLResult chagePwd(String phone, String password);

	public MLResultObject<LoginUser> register(String phone, String invite, String password);

	public MLResultObject<MLPage<User>> findAll(String nickname, int page);

	public MLResult changeStatus(Long id, Integer status);

	public MLResultObject<MLPage<Invite>> findInvite(String code, int page);

	public MLResultObject<MLPage<Reward>> findReward(String code, int page);

	public MLResultObject<InviteInfo> inviteInfo(Long id);

	public MLResultObject<String> getBindCode(Long id);

	public MLResult bindCode(Long id, String code);

	public MLResultObject<UserPointVO> getPoint(String uid);

	public MLResultObject<MLPage<UserPointRecord>> getPointRecord(String nickname, int page);

	public MLResultObject<UserPointRecord> updatePoint(UserPointType type, String uid);
	
	public MLResultObject<Boolean> isSigned(String uid) throws MLException;
	
	public MLResultObject<Boolean> createEosAccount(UserExt userExt) throws MLException;
	
	public MLResultObject<Boolean> canCreateEosAccountByUid(Long uid) throws MLException;
	
	public MLResultObject<Long> getCreateEosAccountNeedPoint() throws MLException;
	
	public MLResultObject<Boolean> addPointByUsername(String username, int point);
	
	public MLResultObject<Boolean> is_registered_user(String phone);
	
	public MLResultObject<EosAccountOrder> getOnePaidOderByUid(Long uid) throws MLException;
	
	public MLResultObject<Boolean> createEosAccountByOrder(Long orderId) throws MLException;
}
