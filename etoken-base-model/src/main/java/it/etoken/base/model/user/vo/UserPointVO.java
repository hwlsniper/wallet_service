package it.etoken.base.model.user.vo;

import java.io.Serializable;
import java.util.Date;

public class UserPointVO implements Serializable {

	/**
	 * 用户积分
	 */
	private static final long serialVersionUID = 1L;
	private Long uid;
	private String nickname; // 昵称
	private Long signin; // 签到
	private Long share; // 分享资讯
	private Long interact; // 资讯互动
	private Long store; // 资产存储
	private Long turnin; // 转入累计
	private Long turnout; // 转出累计
	private Date createdate;
	private Date modifydate;
	
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

	public Long getSignin() {
		return signin;
	}

	public void setSignin(Long signin) {
		this.signin = signin;
	}

	public Long getShare() {
		return share;
	}

	public void setShare(Long share) {
		this.share = share;
	}

	public Long getInteract() {
		return interact;
	}

	public void setInteract(Long interact) {
		this.interact = interact;
	}

	public Long getStore() {
		return store;
	}

	public void setStore(Long store) {
		this.store = store;
	}

	public Long getTurnin() {
		return turnin;
	}

	public void setTurnin(Long turnin) {
		this.turnin = turnin;
	}

	public Long getTurnout() {
		return turnout;
	}

	public void setTurnout(Long turnout) {
		this.turnout = turnout;
	}
	
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
