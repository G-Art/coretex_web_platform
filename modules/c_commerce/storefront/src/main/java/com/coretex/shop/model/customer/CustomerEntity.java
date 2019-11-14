package com.coretex.shop.model.customer;

import java.io.Serializable;

import javax.validation.Valid;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import com.coretex.shop.model.customer.address.Address;

public class CustomerEntity extends Customer implements Serializable {


	private static final long serialVersionUID = 1L;


	@Email(message = "{messages.invalid.email}")
	@NotEmpty(message = "{NotEmpty.customer.emailAddress}")
	private String emailAddress;
	@Valid
	private Address billing;
	private Address delivery;
	private String gender;

	private String language;
	private String firstName;
	private String lastName;

	private String provider;//online, facebook ...


	private String storeCode;

	@NotEmpty(message = "{NotEmpty.customer.userName}")
	//can be email or anything else
	private String userName;

	private Double rating = 0D;
	private int ratingCount;

	public void setUserName(final String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}


	public void setStoreCode(final String storeCode) {
		this.storeCode = storeCode;
	}


	public String getStoreCode() {
		return storeCode;
	}


	public void setEmailAddress(final String emailAddress) {
		this.emailAddress = emailAddress;
	}


	public String getEmailAddress() {
		return emailAddress;
	}


	public void setLanguage(final String language) {
		this.language = language;
	}

	public String getLanguage() {
		return language;
	}


	public Address getBilling() {
		return billing;
	}

	public void setBilling(final Address billing) {
		this.billing = billing;
	}

	public Address getDelivery() {
		return delivery;
	}

	public void setDelivery(final Address delivery) {
		this.delivery = delivery;
	}

	public void setGender(final String gender) {
		this.gender = gender;
	}

	public String getGender() {
		return gender;
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


	public int getRatingCount() {
		return ratingCount;
	}

	public void setRatingCount(int ratingCount) {
		this.ratingCount = ratingCount;
	}

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}


}
