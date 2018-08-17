package it.etoken.base.common.result;

import it.etoken.base.common.exception.MLException;

/**
 * 对象结果
 * 
 * @author hule
 *
 * @param <T>
 */
public class MLResultObject<T> extends MLResult {

	private static final long serialVersionUID = 1L;
	
	private T result;

	/**
	 * 正确返回
	 * @param result
	 */
	public MLResultObject(T result) {
		super(true);
		this.result = result;
	}
	
	/**
	 * 错误返回
	 * @param exc
	 */
	public MLResultObject(MLException exc) {
		super(exc);
	}

	public MLResultObject() {
		super(false);
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}
}
