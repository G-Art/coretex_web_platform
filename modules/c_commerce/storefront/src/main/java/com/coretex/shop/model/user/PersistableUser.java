package com.coretex.shop.model.user;

import java.util.ArrayList;
import java.util.List;

import com.coretex.shop.model.security.PersistableGroup;

public class PersistableUser extends UserEntity {

	private String password;


	private static final long serialVersionUID = 1L;

	private List<PersistableGroup> groups = new ArrayList<PersistableGroup>();

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<PersistableGroup> getGroups() {
		return groups;
	}

	public void setGroups(List<PersistableGroup> groups) {
		this.groups = groups;
	}

}
