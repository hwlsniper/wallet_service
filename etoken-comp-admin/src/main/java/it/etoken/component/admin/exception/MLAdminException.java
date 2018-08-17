package it.etoken.component.admin.exception;

import it.etoken.base.common.exception.MLExceptionEnum;

/**
 * 异常枚举
 * 
 * @author hule
 *
 */
public enum MLAdminException implements MLExceptionEnum {

	SUCCESS("0", "success"),
	LOGIN_ERROR("501","用户名密码错误"),
	PARAM_ERROR("400","参数错误"),
	OLD_ERROR("502","原始密码错误"),
	SYS_ERROR("500", "系统异常");

	private MLAdminException(String code, String msg) {
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