package it.etoken.base.model.admin.entity;

import it.etoken.base.model.BaseEntity;

public class SysConfig  extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String value;
	private String desc;
	
	
	
	public SysConfig() {
		super();
		// TODO Auto-generated constructor stub
	}
	public SysConfig(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
