package it.etoken.component.user.exception;

import it.etoken.base.common.exception.MLExceptionEnum;

/**
 * 异常枚举
 * 
 * @author hule
 *
 */
public enum MLUserException implements MLExceptionEnum {

	SUCCESS("0", "success"),
	REG_EXIST("501","用户名已存在"),
	
	REG_INVCODE("502","邀请码不存在"),
	BIND_SELF("505","不能绑定自己"),
	LOGIN_ERR("503","用户名密码错误"),
	LOGIN_FORBID("504","账户被禁用"),
	ISBIND("508","您已经绑定过了"),
	SYS_ERROR("500", "系统异常"),
	USER_EXIST("505", "用户不存在"),
	SIGNIN_ERR("509","今天您已签到"),
	NO_REWARD("510","此次送积分为0"),
	EOS_ACCOUNT_EXIST("511","您已创建过账号, 留点机会给别人吧."),
	POINT_TOO_LOW("512","积分过低，请继续努力或联系客服购买帐号"),
	USER_NOT_EXIST("513","用户不存在"),
	NO_PAY_ORDER_ERR("514","不存在已付款订单."),
	UPDATE_ORDER_STATE_ERR("515","不存在已付款订单."),;

	private MLUserException(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	/**
	 * 错误码
	 */
	private String code;

	/**
	 * 错误信息
	 */
	private String msg;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}