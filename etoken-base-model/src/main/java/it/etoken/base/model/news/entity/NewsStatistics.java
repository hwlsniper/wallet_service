package it.etoken.base.model.news.entity;

import it.etoken.base.model.BaseEntity;

public class NewsStatistics extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private Long nid;
	
	private Long up;
	
	private Long down;
	
	private Long share;
	
	private Long view;

	public Long getNid() {
		return nid;
	}

	public void setNid(Long nid) {
		this.nid = nid;
	}

	public Long getUp() {
		return up;
	}

	public void setUp(Long up) {
		this.up = up;
	}

	public Long getDown() {
		return down;
	}

	public void setDown(Long down) {
		this.down = down;
	}

	public Long getShare() {
		return share;
	}

	public void setShare(Long share) {
		this.share = share;
	}

	public Long getView() {
		return view;
	}

	public void setView(Long view) {
		this.view = view;
	}
	
	

}
