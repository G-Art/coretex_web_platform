package com.coretex.commerce.admin.data.minimal;

import com.coretex.commerce.admin.data.GenericItemData;

import java.util.List;

public class MinimalCustomerData extends GenericItemData {

	private String email;
	private String gender;
	private String merchantStore;
	private Boolean active;
	private String firstName;
	private String lastName;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getMerchantStore() {
		return merchantStore;
	}

	public void setMerchantStore(String merchantStore) {
		this.merchantStore = merchantStore;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
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
}
