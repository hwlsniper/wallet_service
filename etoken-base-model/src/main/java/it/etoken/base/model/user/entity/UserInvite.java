package it.etoken.base.model.user.entity;

import it.etoken.base.model.BaseEntity;

public class UserInvite extends BaseEntity{

	private Long uid;
	private String code;
	private Long max_use;
	private Long used;

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getMax_use() {
		return max_use;
	}

	public void setMax_use(Long max_use) {
		this.max_use = max_use;
	}

	public Long getUsed() {
		return used;
	}

	public void setUsed(Long used) {
		this.used = used;
	}

}
