package com.coretex.commerce.data.minimal;

import com.coretex.commerce.data.GenericItemData;

public class MinimalManufacturerData extends GenericItemData {

	private String code;
	private String name;
	private String store;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}
}
