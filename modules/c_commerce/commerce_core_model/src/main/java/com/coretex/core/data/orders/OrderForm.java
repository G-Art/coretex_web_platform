package com.coretex.core.data.orders;

import com.coretex.enums.commerce_core_model.OrderStatusEnum;
import com.coretex.items.commerce_core_model.CreditCardItem;
import com.coretex.items.commerce_core_model.CurrencyItem;
import com.coretex.items.commerce_core_model.OrderProductItem;
import com.coretex.items.commerce_core_model.OrderStatusHistoryItem;
import com.coretex.items.commerce_core_model.OrderTotalItem;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Set;


public class OrderForm implements Serializable {

	private static final long serialVersionUID = 1L;

	private String uuid;
	private List<OrderStatusEnum> orderStatusList = Arrays.asList(OrderStatusEnum.values());
	private String datePurchased = "";
	private Billing billing;
	private Delivery delivery;
	private String customerEmailAddress;
	private String orderHistoryComment = "";
	private OrderStatusEnum status;
	private Boolean customerAgreement;
	private String shippingModuleCode;
	private String paymentType;
	private String paymentModuleCode;
	private BigDecimal total;

	private transient CurrencyItem currency;
	private transient CreditCardItem creditCard;
	private transient Set<OrderProductItem> orderProducts;
	private transient Set<OrderTotalItem> orderTotal;
	private transient Set<OrderStatusHistoryItem> orderHistory;

	private String customerId;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public CurrencyItem getCurrency() {
		return currency;
	}

	public void setCurrency(CurrencyItem currency) {
		this.currency = currency;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public Set<OrderTotalItem> getOrderTotal() {
		return orderTotal;
	}

	public void setOrderTotal(Set<OrderTotalItem> orderTotal) {
		this.orderTotal = orderTotal;
	}

	public Set<OrderStatusHistoryItem> getOrderHistory() {
		return orderHistory;
	}

	public void setOrderHistory(Set<OrderStatusHistoryItem> orderHistory) {
		this.orderHistory = orderHistory;
	}

	public String getPaymentModuleCode() {
		return paymentModuleCode;
	}

	public Set<OrderProductItem> getOrderProducts() {
		return orderProducts;
	}

	public void setOrderProducts(Set<OrderProductItem> orderProducts) {
		this.orderProducts = orderProducts;
	}

	public void setPaymentModuleCode(String paymentModuleCode) {
		this.paymentModuleCode = paymentModuleCode;
	}

	public CreditCardItem getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(CreditCardItem creditCard) {
		this.creditCard = creditCard;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public Boolean getCustomerAgreement() {
		return customerAgreement;
	}

	public void setCustomerAgreement(Boolean customerAgreement) {
		this.customerAgreement = customerAgreement;
	}

	public List<OrderStatusEnum> getOrderStatusList() {
		return orderStatusList;
	}

	public void setOrderStatusList(List<OrderStatusEnum> orderStatusList) {
		this.orderStatusList = orderStatusList;
	}

	public OrderStatusEnum getStatus() {
		return status;
	}

	public void setStatus(OrderStatusEnum status) {
		this.status = status;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getOrderHistoryComment() {
		return orderHistoryComment;
	}

	public void setOrderHistoryComment(String orderHistoryComment) {
		this.orderHistoryComment = orderHistoryComment;
	}

	public Delivery getDelivery() {
		return delivery;
	}

	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;
	}

	public String getCustomerEmailAddress() {
		return customerEmailAddress;
	}

	public void setCustomerEmailAddress(String customerEmailAddress) {
		this.customerEmailAddress = customerEmailAddress;
	}

	public String getDatePurchased() {
		return datePurchased;
	}

	public void setDatePurchased(String datePurchased) {
		this.datePurchased = datePurchased;
	}

	public Billing getBilling() {
		return billing;
	}

	public void setBilling(Billing billing) {
		this.billing = billing;
	}

	public String getShippingModuleCode() {
		return shippingModuleCode;
	}

	public void setShippingModuleCode(String shippingModuleCode) {
		this.shippingModuleCode = shippingModuleCode;
	}
}