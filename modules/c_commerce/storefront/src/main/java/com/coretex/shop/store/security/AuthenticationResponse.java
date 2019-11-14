package com.coretex.shop.store.security;

import java.io.Serializable;
import java.util.UUID;

import com.coretex.shop.model.entity.Entity;

public class AuthenticationResponse extends Entity implements Serializable {
	public AuthenticationResponse() {
	}


	private static final long serialVersionUID = 1L;
	private String token;

	public AuthenticationResponse(UUID userId, String token) {
		this.token = token;
		super.setUuid(userId);
	}

	public String getToken() {
		return token;
	}

}
