package com.coretex.shop.admin.model.secutity;

import com.coretex.items.cx_core.UserItem;

import java.io.Serializable;

/**
 * Entity used in the cahange passord page
 *
 * @author csamson777
 */
public class Password implements Serializable {


	private static final long serialVersionUID = 1L;
	private String password;
	private String newPassword;
	private String repeatPassword;

	private UserItem user;


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getRepeatPassword() {
		return repeatPassword;
	}

	public void setRepeatPassword(String repeatPassword) {
		this.repeatPassword = repeatPassword;
	}

	public UserItem getUser() {
		return user;
	}

	public void setUser(UserItem user) {
		this.user = user;
	}


}
