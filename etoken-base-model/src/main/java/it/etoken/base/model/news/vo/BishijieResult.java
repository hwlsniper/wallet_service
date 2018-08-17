package it.etoken.base.model.news.vo;

public class BishijieResult {
	private int error;
	private String message;
	private BishijieData data;
	
	public int getError() {
		return error;
	}
	public void setError(int error) {
		this.error = error;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public BishijieData getData() {
		return data;
	}
	public void setData(BishijieData data) {
		this.data = data;
	}
	
}


