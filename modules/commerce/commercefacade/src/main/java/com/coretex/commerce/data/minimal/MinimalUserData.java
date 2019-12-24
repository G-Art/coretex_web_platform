package com.coretex.commerce.data.minimal;

import com.coretex.commerce.data.GenericItemData;

import java.util.List;

public class MinimalUserData extends GenericItemData {

	private String email;
	private String firstName;
	private String lastName;
	private String adminName;
	private String merchantStore;
	private List<String> groups;
	private Boolean active;

	public String getMerchantStore() {
		return merchantStore;
	}

	public void setMerchantStore(String merchantStore) {
		this.merchantStore = merchantStore;
	}

	public List<String> getGroups() {
		return groups;
	}

	public void setGroups(List<String> groups) {
		this.groups = groups;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}
}
