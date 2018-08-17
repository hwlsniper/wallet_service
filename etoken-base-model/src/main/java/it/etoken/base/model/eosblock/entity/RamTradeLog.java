package it.etoken.base.model.eosblock.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class RamTradeLog implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2098297619125027448L;
	
	private String _id;
	private String trx_id;
	private String block_id;
	private String block_num;
	private String record_date;
	private String action_name;
	private BigDecimal price;
	private String payer;
	private String receiver;
	private String eos_qty;
	private String ram_qty;
	
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
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public String getPayer() {
		return payer;
	}
	public void setPayer(String payer) {
		this.payer = payer;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getEos_qty() {
		return eos_qty;
	}
	public void setEos_qty(String eos_qty) {
		this.eos_qty = eos_qty;
	}
	public String getRam_qty() {
		return ram_qty;
	}
	public void setRam_qty(String ram_qty) {
		this.ram_qty = ram_qty;
	}
}
