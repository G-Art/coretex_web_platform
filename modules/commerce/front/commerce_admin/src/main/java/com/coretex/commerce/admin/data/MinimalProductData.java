package com.coretex.commerce.admin.data;

public class MinimalProductData extends GenericItemData {

	private String sku;
	private String name;
	private Boolean available;
	private String merchantStore;

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}

	public String getMerchantStore() {
		return merchantStore;
	}

	public void setMerchantStore(String merchantStore) {
		this.merchantStore = merchantStore;
	}
}
