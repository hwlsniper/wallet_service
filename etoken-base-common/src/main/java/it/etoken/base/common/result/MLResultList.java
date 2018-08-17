package it.etoken.base.common.result;

import java.util.List;

import it.etoken.base.common.exception.MLException;

/**
 * 列表结果
 * 
 * @author hule
 *
 * @param <T>
 */
public class MLResultList<T> extends MLResult {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 数据的结果集
	 */
	private List<T> list;
	
	/**
	 * 分页信息
	 */
	private MLPage<T> page;

	/**
	 * 成功返回普通结果
	 * @param results
	 */
	public MLResultList(List<T> results) {
		super(true);
		this.list = results;
	}
	
	/**
	 * 成功返回分页结果
	 * @param page
	 */
	public MLResultList(MLPage<T> page) {
		super(true);
		this.page = page;
	}
	
	/**
	 * 错误返回
	 * @param exc
	 */
	public MLResultList(MLException exc) {
		super(exc);
	}

	public List<T> getList() {
		return list;
	}

	public void setResults(List<T> results) {
		this.list = results;
		// 返回分页信息
		MLPage<T> pageResult = new MLPage<T>(results,0);
		this.setPageResult(pageResult);
	}

	/**
	 * @return the pageResult
	 */
	public MLPage<?> getPage() {
		return page;
	}

	/**
	 * @param pageResult
	 *            the pageResult to set
	 */
	public void setPageResult(MLPage<T> pageResult) {
		this.page = pageResult;
	}

}
