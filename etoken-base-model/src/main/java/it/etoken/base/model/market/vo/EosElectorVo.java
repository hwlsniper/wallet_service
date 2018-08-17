package it.etoken.base.model.market.vo;

import java.math.BigDecimal;

import it.etoken.base.model.market.entity.EosElector;

public class EosElectorVo extends EosElector{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6810032895015950402L;
	private Integer ranking;
	private BigDecimal total_votes;
	private BigDecimal total_votes_percent;
	
	public Integer getRanking() {
		return ranking;
	}
	public void setRanking(Integer ranking) {
		this.ranking = ranking;
	}
	public BigDecimal getTotal_votes() {
		return total_votes;
	}
	public void setTotal_votes(BigDecimal total_votes) {
		this.total_votes = total_votes;
	}
	public BigDecimal getTotal_votes_percent() {
		return total_votes_percent;
	}
	public void setTotal_votes_percent(BigDecimal total_votes_percent) {
		this.total_votes_percent = total_votes_percent;
	}
	
	
}