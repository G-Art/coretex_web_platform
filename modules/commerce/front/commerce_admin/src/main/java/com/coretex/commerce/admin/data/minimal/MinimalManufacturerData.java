package com.coretex.commerce.admin.data.minimal;

import com.coretex.commerce.admin.data.GenericItemData;

public class MinimalManufacturerData extends GenericItemData {

	private String code;
	private String name;
	private String merchantStore;

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

	public String getMerchantStore() {
		return merchantStore;
	}

	public void setMerchantStore(String merchantStore) {
		this.merchantStore = merchantStore;
	}
}
