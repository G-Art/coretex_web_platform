package com.coretex.commerce.data;

import java.math.BigDecimal;
import java.util.LinkedHashSet;

public class AbstractOrderData extends GenericItemData {

	private LinkedHashSet<OrderEntryData> entries;
	private StoreData store;
	private CurrencyData currency;
	private CustomerData customer;
	private LocaleData locale;
	private BigDecimal total;
	private DeliveryTypeData deliveryType;
	private AddressData address;
	private String date;
	private PaymentTypeData paymentMode;

	public LinkedHashSet<OrderEntryData> getEntries() {
		return entries;
	}

	public void setEntries(LinkedHashSet<OrderEntryData> entries) {
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

	public DeliveryTypeData getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(DeliveryTypeData deliveryType) {
		this.deliveryType = deliveryType;
	}

	public AddressData getAddress() {
		return address;
	}

	public void setAddress(AddressData address) {
		this.address = address;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public PaymentTypeData getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(PaymentTypeData paymentMode) {
		this.paymentMode = paymentMode;
	}
}
