package it.etoken.base.model.user.vo;

import java.io.Serializable;

public class UpInvite implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long uid;

	private Long iid;

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Long getIid() {
		return iid;
	}

	public void setIid(Long iid) {
		this.iid = iid;
	}

}
