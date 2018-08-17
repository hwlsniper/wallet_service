package it.etoken.base.common.result;

import java.io.Serializable;

import it.etoken.base.common.exception.MLException;

/**
 * 
 * @author hule
 *
 */
public class MLResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 是否成功
	 */

	private boolean isSuccess;

	/**
	 * 错误代码
	 */
	private String errorCode;

	/**
	 * 错误信息
	 */
	private String errorHint;
	
	public MLResult(boolean isSuccess){
		this.isSuccess = isSuccess;
	}

	public MLResult(MLException exc) {
		this.isSuccess = false;
		if(exc!=null){
			this.errorCode = exc.getCode();
			this.errorHint = exc.getMsg();
		}
	}

	public String getErrorHint() {
		return errorHint;
	}

	public void setErrorHint(String errorHint) {
		this.errorHint = errorHint;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	@Override
	public String toString() {
		return errorCode + "：" + errorHint;
	}

}
