package it.etoken.base.model.news.vo;

import java.util.List;

public class BishijieData {
	List<NewsFromBishijie> express;
	int total;
	
	public List<NewsFromBishijie> getExpress() {
		return express;
	}
	public void setExpress(List<NewsFromBishijie> express) {
		this.express = express;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	
}
