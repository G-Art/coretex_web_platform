package com.coretex.shop.model.customer.optin;

import javax.validation.constraints.NotNull;

import com.coretex.items.commerce_core_model.CustomerOptinItem;

public class CustomerOptinEntity extends CustomerOptinItem {


	private static final long serialVersionUID = 1L;

	@NotNull
	private String firstName;
	private String lastName;
	@NotNull
	private String email;

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
