package it.etoken.base.model.user.entity;

import it.etoken.base.model.BaseEntity;

public class EostRecord extends BaseEntity {
	private Long uid;
	private Long bid;
	private String type;
	private Double eost;
	private String eosAccount;

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Long getBid() {
		return bid;
	}

	public void setBid(Long bid) {
		this.bid = bid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Double getEost() {
		return eost;
	}

	public void setEost(Double eost) {
		this.eost = eost;
	}
	public String getEosAccount() {
		return eosAccount;
	}

	public void setEosAccount(String eosAccount) {
		this.eosAccount = eosAccount;
	}

}
