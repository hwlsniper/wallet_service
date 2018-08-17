package it.etoken.base.model.user.vo;

import java.io.Serializable;
import java.util.Date;

public class Reward implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Date createdate;

	private Date modifydate;

	private String nickname;

	private String reward;

	private String action;

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public Date getModifydate() {
		return modifydate;
	}

	public void setModifydate(Date modifydate) {
		this.modifydate = modifydate;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

}
