package com.coretex.commerce.admin.data;

import java.math.BigDecimal;
import java.util.Set;

public class OrderData extends GenericItemData {

	private BigDecimal total;
	private String name;
	private String status;
	private String date;
	private MerchantStoreData merchant;
	private CurrencyData currency;
	private AddressData delivery;
	private AddressData billing;
	private String phone;
	private String email;
	private BigDecimal currencyValue;
	private String orderDateFinished;
	private String ipAddress;
	private Boolean customerAgreement;
	private Boolean confirmedAddress;
	private String paymentType;
	private Set<OrderProductData> orderProducts;
	private Set<OrderTotalData> orderTotal;

//	orderHistory: Set<OrderStatusHistoryItem>
//	orderAttributes: Set<OrderAttributeItem>


	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public MerchantStoreData getMerchant() {
		return merchant;
	}

	public void setMerchant(MerchantStoreData merchant) {
		this.merchant = merchant;
	}

	public CurrencyData getCurrency() {
		return currency;
	}

	public void setCurrency(CurrencyData currency) {
		this.currency = currency;
	}

	public AddressData getDelivery() {
		return delivery;
	}

	public void setDelivery(AddressData delivery) {
		this.delivery = delivery;
	}

	public AddressData getBilling() {
		return billing;
	}

	public void setBilling(AddressData billing) {
		this.billing = billing;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public BigDecimal getCurrencyValue() {
		return currencyValue;
	}

	public void setCurrencyValue(BigDecimal currencyValue) {
		this.currencyValue = currencyValue;
	}

	public String getOrderDateFinished() {
		return orderDateFinished;
	}

	public void setOrderDateFinished(String orderDateFinished) {
		this.orderDateFinished = orderDateFinished;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
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

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public Set<OrderProductData> getOrderProducts() {
		return orderProducts;
	}

	public void setOrderProducts(Set<OrderProductData> orderProducts) {
		this.orderProducts = orderProducts;
	}

	public Set<OrderTotalData> getOrderTotal() {
		return orderTotal;
	}

	public void setOrderTotal(Set<OrderTotalData> orderTotal) {
		this.orderTotal = orderTotal;
	}
}
