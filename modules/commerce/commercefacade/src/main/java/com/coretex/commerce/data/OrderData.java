package com.coretex.commerce.data;

import java.math.BigDecimal;
import java.util.Set;

public class OrderData extends GenericItemData {

	private Set<OrderEntryData> entries;
	private StoreData store;
	private CurrencyData currency;
	private CustomerData customer;
	private LocaleData locale;
	private BigDecimal total;
	private Boolean customerAgreement;
	private Boolean confirmedAddress;
	private String status;
	private String date;

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public StoreData getStore() {
		return store;
	}

	public void setStore(StoreData store) {
		this.store = store;
	}

	public CurrencyData getCurrency() {
		return currency;
	}

	public void setCurrency(CurrencyData currency) {
		this.currency = currency;
	}


	public Boolean getCustomerAgreement() {
		return customerAgreement;
	}

	public void setCustomerAgreement(Boolean customerAgreement) {
		this.customerAgreement = customerAgreement;
	}

	public Boolean getConfirmedAddress() {
		return confirmedAddress;
	}

	public void setConfirmedAddress(Boolean confirmedAddress) {
		this.confirmedAddress = confirmedAddress;
	}

	public Set<OrderEntryData> getEntries() {
		return entries;
	}

	public void setEntries(Set<OrderEntryData> entries) {
		this.entries = entries;
	}

	public LocaleData getLocale() {
		return locale;
	}

	public void setLocale(LocaleData locale) {
		this.locale = locale;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public CustomerData getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerData customer) {
		this.customer = customer;
	}
}
