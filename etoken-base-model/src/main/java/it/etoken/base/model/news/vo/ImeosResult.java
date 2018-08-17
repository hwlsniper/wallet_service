package it.etoken.base.model.news.vo;

import java.util.List;

public class ImeosResult {
	private int total;
	private int limit;
	private int skip;
	private List<ImeosData> data;
	
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public int getSkip() {
		return skip;
	}
	public void setSkip(int skip) {
		this.skip = skip;
	}
	public List<ImeosData> getData() {
		return data;
	}
	public void setData(List<ImeosData> data) {
		this.data = data;
	}
}
