package it.etoken.base.model.admin.entity;

import it.etoken.base.model.BaseEntity;

public class Admin extends BaseEntity {

	private String username;
	private String password;
	private Long login_error;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getLogin_error() {
		return login_error;
	}

	public void setLogin_error(Long login_error) {
		this.login_error = login_error;
	}

}
