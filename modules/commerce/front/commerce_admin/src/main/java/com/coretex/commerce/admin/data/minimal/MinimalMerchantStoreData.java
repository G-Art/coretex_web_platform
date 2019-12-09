package com.coretex.commerce.admin.data.minimal;

import com.coretex.commerce.admin.data.GenericItemData;

public class MinimalMerchantStoreData extends GenericItemData {

	private String code;
	private String storeName;
	private String storePhone;
	private String domainName;
	private String storeEmailAddress;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getStorePhone() {
		return storePhone;
	}

	public void setStorePhone(String storePhone) {
		this.storePhone = storePhone;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getStoreEmailAddress() {
		return storeEmailAddress;
	}

	public void setStoreEmailAddress(String storeEmailAddress) {
		this.storeEmailAddress = storeEmailAddress;
	}
}
