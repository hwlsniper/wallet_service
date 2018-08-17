package it.etoken.base.model.eosblock.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class ETTradeLog implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8869062089277173819L;
	private String _id;
	private String trx_id;
	private String block_id;
	private String block_num;
	private String record_date;
	private String action_name;
	private String price;
	private String price_rmb;
	private String eos_qty;
	private String token_qty;
	private String account;
	private String token_contract;
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getTrx_id() {
		return trx_id;
	}
	public void setTrx_id(String trx_id) {
		this.trx_id = trx_id;
	}
	public String getBlock_id() {
		return block_id;
	}
	public void setBlock_id(String block_id) {
		this.block_id = block_id;
	}
	public String getBlock_num() {
		return block_num;
	}
	public void setBlock_num(String block_num) {
		this.block_num = block_num;
	}
	public String getRecord_date() {
		return record_date;
	}
	public void setRecord_date(String record_date) {
		this.record_date = record_date;
	}
	public String getAction_name() {
		return action_name;
	}
	public void setAction_name(String action_name) {
		this.action_name = action_name;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getPrice_rmb() {
		return price_rmb;
	}
	public void setPrice_rmb(String price_rmb) {
		this.price_rmb = price_rmb;
	}
	public String getEos_qty() {
		return eos_qty;
	}
	public void setEos_qty(String eos_qty) {
		this.eos_qty = eos_qty;
	}
	public String getToken_qty() {
		return token_qty;
	}
	public void setToken_qty(String token_qty) {
		this.token_qty = token_qty;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getToken_contract() {
		return token_contract;
	}
	public void setToken_contract(String token_contract) {
		this.token_contract = token_contract;
	}
}
