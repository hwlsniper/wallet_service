package it.etoken.component.api.eosrpc;

import it.etoken.component.api.exception.MLApiException;

public class EosResult {

	public EosResult(MLApiException status, String data) {
		this.status = status;
		this.data = data;
	}

	private MLApiException status;

	private String data;

	public MLApiException getStatus() {
		return status;
	}

	public void setStatus(MLApiException status) {
		this.status = status;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public boolean isSuccess() {
		return this.status == MLApiException.SUCCESS;
	}
}
