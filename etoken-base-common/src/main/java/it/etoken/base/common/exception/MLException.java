package it.etoken.base.common.exception;

/**
 * 自定义异常
 * 
 * @author hule
 *
 */
public class MLException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public MLException(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public MLException(MLExceptionEnum exc) {
		this.code = exc.getCode();
		this.msg = exc.getMsg();
	}

	public MLException(MLExceptionEnum exc,String msg) {
		this.code = exc.getCode();
		this.msg = exc.getMsg()+","+msg;
	}
	
	/**
	 * 异常码
	 */
	private String code;
	/**
	 * 异常信息
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
