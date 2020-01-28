package com.coretex.commerce.data.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.beans.Transient;
import java.util.function.Function;

public class RegisterRequest {

	private transient Function<String, String> encoder;

	private String firstName;
	private String lastName;
	private String email;
	private String password;
	@JsonProperty("confirmPassword")
	private String passwordConfirmation;

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordConfirmation() {
		return passwordConfirmation;
	}

	public void setPasswordConfirmation(String passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
	}

	@Transient
	public void setEncoder(Function<String, String> encoder) {
		this.encoder = encoder;
	}

	@Transient
	public String getEncodedPassword() {
		return encoder.apply(getPassword());
	}
}
