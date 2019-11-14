package com.coretex.shop.admin.data;

import java.util.Date;

public class MerchantStoreDto extends GenericDto {

	private String code;
	private Boolean useCache = true;
	private String storeName;
	private String storePhone;
	private String storeAddress;
	private String storeCity;
	private String storePostalCode;
	private String storeStateProvince;
	private String weightUnitCode;
	private String seizeUnitCode;
	private String storeLogo;
	private String storeTemplate;
	private String invoiceTemplate;
	private Date inBusinessSince;
	private String domainName;
	private String continueShoppingUrl;
	private String storeEmailAddress;
	private String dateBusinessSince;
	private Boolean currencyFormatNational = true;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Boolean getUseCache() {
		return useCache;
	}

	public void setUseCache(Boolean useCache) {
		this.useCache = useCache;
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

	public String getStoreAddress() {
		return storeAddress;
	}

	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}

	public String getStoreCity() {
		return storeCity;
	}

	public void setStoreCity(String storeCity) {
		this.storeCity = storeCity;
	}

	public String getStorePostalCode() {
		return storePostalCode;
	}

	public void setStorePostalCode(String storePostalCode) {
		this.storePostalCode = storePostalCode;
	}

	public String getStoreStateProvince() {
		return storeStateProvince;
	}

	public void setStoreStateProvince(String storeStateProvince) {
		this.storeStateProvince = storeStateProvince;
	}

	public String getWeightUnitCode() {
		return weightUnitCode;
	}

	public void setWeightUnitCode(String weightUnitCode) {
		this.weightUnitCode = weightUnitCode;
	}

	public String getSeizeUnitCode() {
		return seizeUnitCode;
	}

	public void setSeizeUnitCode(String seizeUnitCode) {
		this.seizeUnitCode = seizeUnitCode;
	}

	public String getStoreLogo() {
		return storeLogo;
	}

	public void setStoreLogo(String storeLogo) {
		this.storeLogo = storeLogo;
	}

	public String getStoreTemplate() {
		return storeTemplate;
	}

	public void setStoreTemplate(String storeTemplate) {
		this.storeTemplate = storeTemplate;
	}

	public String getInvoiceTemplate() {
		return invoiceTemplate;
	}

	public void setInvoiceTemplate(String invoiceTemplate) {
		this.invoiceTemplate = invoiceTemplate;
	}

	public Date getInBusinessSince() {
		return inBusinessSince;
	}

	public void setInBusinessSince(Date inBusinessSince) {
		this.inBusinessSince = inBusinessSince;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getContinueShoppingUrl() {
		return continueShoppingUrl;
	}

	public void setContinueShoppingUrl(String continueShoppingUrl) {
		this.continueShoppingUrl = continueShoppingUrl;
	}

	public String getStoreEmailAddress() {
		return storeEmailAddress;
	}

	public void setStoreEmailAddress(String storeEmailAddress) {
		this.storeEmailAddress = storeEmailAddress;
	}

	public String getDateBusinessSince() {
		return dateBusinessSince;
	}

	public void setDateBusinessSince(String dateBusinessSince) {
		this.dateBusinessSince = dateBusinessSince;
	}

	public Boolean getCurrencyFormatNational() {
		return currencyFormatNational;
	}

	public void setCurrencyFormatNational(Boolean currencyFormatNational) {
		this.currencyFormatNational = currencyFormatNational;
	}
}
