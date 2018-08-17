package it.etoken.base.model.user.vo;

import java.io.Serializable;

public class InviteInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String inviteL2Count;

	private String inviteL1Count;

	private String l1Reward;

	private String l2Reward;

	private String code;

	private String reward;

	private String regReward;

	private String inviteUrl;

	public String getInviteL2Count() {
		return inviteL2Count;
	}

	public void setInviteL2Count(String inviteL2Count) {
		this.inviteL2Count = inviteL2Count;
	}

	public String getInviteL1Count() {
		return inviteL1Count;
	}

	public void setInviteL1Count(String inviteL1Count) {
		this.inviteL1Count = inviteL1Count;
	}

	public String getL1Reward() {
		return l1Reward;
	}

	public void setL1Reward(String l1Reward) {
		this.l1Reward = l1Reward;
	}

	public String getL2Reward() {
		return l2Reward;
	}

	public void setL2Reward(String l2Reward) {
		this.l2Reward = l2Reward;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}

	public String getRegReward() {
		return regReward;
	}

	public void setRegReward(String regReward) {
		this.regReward = regReward;
	}

	public String getInviteUrl() {
		return inviteUrl;
	}

	public void setInviteUrl(String inviteUrl) {
		this.inviteUrl = inviteUrl;
	}

}
