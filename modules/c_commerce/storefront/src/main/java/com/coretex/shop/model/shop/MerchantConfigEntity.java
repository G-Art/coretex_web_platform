package com.coretex.shop.model.shop;

import com.coretex.shop.model.entity.Entity;

public class MerchantConfigEntity extends Entity {


	private static final long serialVersionUID = 1L;
	private String key;
	private String value;
	private boolean active;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
