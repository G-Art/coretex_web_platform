package com.coretex.commerce.data;

import java.math.BigDecimal;
import java.util.Set;

public class AbstractOrderData extends GenericItemData {

	private Set<OrderEntryData> entries;
	private StoreData store;
	private CurrencyData currency;
	private CustomerData customer;
	private LocaleData locale;
	private BigDecimal total;

	public Set<OrderEntryData> getEntries() {
		return entries;
	}

	public void setEntries(Set<OrderEntryData> entries) {
		this.entries = entries;
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

	public CustomerData getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerData customer) {
		this.customer = customer;
	}

	public LocaleData getLocale() {
		return locale;
	}

	public void setLocale(LocaleData locale) {
		this.locale = locale;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}
}
