package it.etoken.component.api.exception;

import it.etoken.base.common.exception.MLExceptionEnum;

/**
 * 异常枚举
 * 
 * @author hule
 *
 */
public enum MLApiException implements MLExceptionEnum {

	SUCCESS("0", "success"),
	PARAM_ERROR("400","参数错误"),
	SYS_ERROR("500", "系统错误"),
	CAPTURE_ERR("505","验证码错误"),
	KCAPTURE_ERR("505","图形验证码错误"),
	CODE_EXP("506","输入错误过多，请更换验证码"),
	PHONE_ERR("506","不支持该手机号码"),
	EOSRPC_COMM_ERR("507", "EOS节点通讯错误"),
	ACCOUNT_CREATE_ERR("508", "账户创建失败"),
	GET_BALANCE_ERR("509", "获取余额失败"),
	GET_PRODUCERS_ERR("510", "获取竞选人列表失败"),
	GET_ACCOUNT_ERR("511", "获取账户信息失败"),
	ACCOUNT_NAME_ERR("512", "账户名称必须为12位"), 
	GET_KEY_ACOUNT_ERR("513", "根据公钥获取账户信息失败"),
	GET_UNDELEGATEBW_ERR("514", "获取赎回信息失败"),
	ACCOUNT_NAME_EXIST("515", "账号已存在"),
	CONTRACT_ACCOUNT_ERROR("516", "合约账号错误"),
	CONTRACT_NAME_ERROR("517", "合约名称错误"),
	CONTRACT_EXIST_ERROR("518", "合约已存在"),
	CONTRACT_NOT_EXIST_ERROR("519","抱歉, 没有搜索到您要找的Token"),
	POINT_TOO_LOW("520","积分过低，请继续努力或联系客服购买帐号"),
	UNKNOWN_ERROR("501", "未知错误"),
	ACCOUNT_NAME_NOT_EXIST("521", "账号不存在"),
	ACTIVITY_IS_OVER("522", "100积分免费创建活动已结束，更多活动敬请期待"),
	EOSRPC_FAIL("600", "失败"),
	DELEGATEBWED("601","您已经免费抵押过，把机会留给别人吧"),
	SUFFICIENT_RESOURCES("602","您仍有不少资源，把机会留给别人吧"),
	AUDIT("603","您提取的eos正在审核中，请耐心等候"),
	RECEIVE("604","您提取的eos已经发放成功"),
	POINTNOTENOUGH("605","您的积分暂时没有达到领取标准，多多签到可以新增积分哦");

	private MLApiException(String code, String msg) {
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