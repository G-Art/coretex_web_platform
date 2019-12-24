package com.coretex.commerce.data;

import com.coretex.commerce.data.minimal.MinimalStoreData;

public class StoreData extends MinimalStoreData {

	private AddressData address;
	private String dateBusinessSince;
	private LocaleData defaultLanguage;
	private ZoneData zone;
	private CurrencyData currency;

	public AddressData getAddress() {
		return address;
	}

	public void setAddress(AddressData address) {
		this.address = address;
	}

	public String getDateBusinessSince() {
		return dateBusinessSince;
	}

	public void setDateBusinessSince(String dateBusinessSince) {
		this.dateBusinessSince = dateBusinessSince;
	}

	public LocaleData getDefaultLanguage() {
		return defaultLanguage;
	}

	public void setDefaultLanguage(LocaleData defaultLanguage) {
		this.defaultLanguage = defaultLanguage;
	}

	public ZoneData getZone() {
		return zone;
	}

	public void setZone(ZoneData zone) {
		this.zone = zone;
	}

	public CurrencyData getCurrency() {
		return currency;
	}

	public void setCurrency(CurrencyData currency) {
		this.currency = currency;
	}
}
