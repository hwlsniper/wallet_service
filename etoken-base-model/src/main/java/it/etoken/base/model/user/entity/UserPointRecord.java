package it.etoken.base.model.user.entity;

import it.etoken.base.model.BaseEntity;

public class UserPointRecord extends BaseEntity {
	private Long uid;
	private String nickname;
	private String type;
	private Long point;

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getPoint() {
		return point;
	}

	public void setPoint(Long point) {
		this.point = point;
	}

}
