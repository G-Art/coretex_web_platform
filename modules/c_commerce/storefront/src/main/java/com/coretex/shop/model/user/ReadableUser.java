package com.coretex.shop.model.user;

import java.util.ArrayList;
import java.util.List;

import com.coretex.shop.model.security.ReadableGroup;
import com.coretex.shop.model.security.ReadablePermission;

public class ReadableUser extends UserEntity {


	private static final long serialVersionUID = 1L;
	private String lastAccess;
	private String loginTime;
	private String merchant;

	private List<ReadablePermission> permissions = new ArrayList<ReadablePermission>();
	private List<ReadableGroup> groups = new ArrayList<ReadableGroup>();


	public List<ReadableGroup> getGroups() {
		return groups;
	}

	public void setGroups(List<ReadableGroup> groups) {
		this.groups = groups;
	}


	public String getLastAccess() {
		return lastAccess;
	}

	public void setLastAccess(String lastAccess) {
		this.lastAccess = lastAccess;
	}

	public String getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}

	public String getMerchant() {
		return merchant;
	}

	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}

	public List<ReadablePermission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<ReadablePermission> permissions) {
		this.permissions = permissions;
	}


}
