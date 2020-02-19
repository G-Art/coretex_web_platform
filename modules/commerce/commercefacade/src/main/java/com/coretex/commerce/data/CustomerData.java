package com.coretex.commerce.data;

import java.util.List;

public class CustomerData  extends GenericItemData {

	private String email;
	private String firstName;
	private String lastName;
	private LocaleData language;
	private Boolean active;
	private List<GroupData> groups;
	private StoreData store;

	private Boolean anonymous;

	private AddressData billing;
	private AddressData delivery;


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

	public LocaleData getLanguage() {
		return language;
	}

	public void setLanguage(LocaleData language) {
		this.language = language;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public List<GroupData> getGroups() {
		return groups;
	}

	public void setGroups(List<GroupData> groups) {
		this.groups = groups;
	}

	public StoreData getStore() {
		return store;
	}

	public void setStore(StoreData store) {
		this.store = store;
	}

	public Boolean getAnonymous() {
		return anonymous;
	}

	public void setAnonymous(Boolean anonymous) {
		this.anonymous = anonymous;
	}

	public AddressData getBilling() {
		return billing;
	}

	public void setBilling(AddressData billing) {
		this.billing = billing;
	}

	public AddressData getDelivery() {
		return delivery;
	}

	public void setDelivery(AddressData delivery) {
		this.delivery = delivery;
	}
}
