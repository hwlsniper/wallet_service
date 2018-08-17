package it.etoken.component.user.service;

import it.etoken.base.common.exception.MLException;
import it.etoken.base.common.result.MLPage;
import it.etoken.base.model.user.entity.EosAccountOrder;
import it.etoken.base.model.user.entity.User;
import it.etoken.base.model.user.entity.UserExt;
import it.etoken.base.model.user.entity.UserPointRecord;
import it.etoken.base.model.user.vo.InviteInfo;
import it.etoken.base.model.user.vo.LoginUser;
import it.etoken.base.model.user.vo.UserPointVO;
import it.etoken.componet.user.point.UserPointType;

public interface UserService {

	public LoginUser register(String phone, String invite, String password) throws MLException;

	public LoginUser login(String phone, String password) throws MLException;

	public MLPage<User> findAll(String nickname, int page) throws MLException;

	public void changeStatus(Long id, Integer status) throws MLException;

	public void chagePwd(String phone, String pwd) throws MLException;

	public LoginUser loginUserInfo(Long id) throws MLException;

	public InviteInfo inviteInfo(Long id) throws MLException;

	public String getBindCode(Long id) throws MLException;

	public void bindCode(Long uid, String code) throws MLException;

	public UserPointVO getPoint(String uid) throws MLException;

	public MLPage<UserPointRecord> getPointRecord(String nickname, int page) throws MLException;

	public UserPointRecord updatePoint(UserPointType type, String uid) throws MLException;
	
	/**
	 * 当日是否已签到
	 * @param uid
	 * @return
	 * @throws MLException
	 */
	public Boolean isSigned(String uid) throws MLException;
	
	/**
	 * 创建eos账号
	 * @param userExt
	 * @return
	 * @throws MLException
	 */
	public Boolean createEosAccount(UserExt userExt) throws MLException;
	
	/**
	 * 判断用户是否可以创建EOS
	 * @param uid
	 * @return
	 * @throws MLException
	 */
	public Boolean canCreateEosAccountByUid(Long uid) throws MLException;
	
	/**
	 * 获取创建eos的积分
	 * @return
	 * @throws MLException
	 */
	public Long getCreateEosAccountNeedPoint() throws MLException;
	
	/**
	 * 根据用户添加积分
	 * @param username
	 * @param point
	 * @return
	 * @throws MLException
	 */
	public Boolean addPointByUsername(String username, int point) throws MLException;
	/**
	 * 查询是否是已经注册的用户
	 * @param phone
	 * @return
	 * @throws MLException
	 */
	public Boolean is_registered_user(String phone) throws MLException;
	
	/**
	 * 通过购买创建账号
	 * @param eosAccountOrder
	 * @return
	 * @throws MLException
	 */
	public Boolean createEosAccountByOrder(Long oderId) throws MLException;
	
	/**
	 * 根据uid查找最先创建的已付款订单
	 * @param uid
	 * @return
	 * @throws MLException
	 */
	public EosAccountOrder getOnePaidOderByUid(Long uid) throws MLException;
}
