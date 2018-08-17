package it.etoken.component.api.eosrpc;

public enum EosErrcode {

	EOSRPC_ACCOUNT_ALREADY_EXIST(3050000, "账户已存在");

	private EosErrcode(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	private int code;
	private String msg;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
