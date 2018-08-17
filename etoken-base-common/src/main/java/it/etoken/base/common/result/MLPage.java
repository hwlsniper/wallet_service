package it.etoken.base.common.result;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果
 * 
 * @author hule
 *
 * @param <T>
 */
public class MLPage<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private long total;

	private List<T> list;

	public MLPage() {

	}

	public MLPage(List<T> list, long total) {
		this.list = list;
		this.total = total;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

}
