package com.coretex.shop.model.order;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.coretex.enums.commerce_core_model.OrderStatusEnum;
import com.coretex.enums.commerce_core_model.PaymentTypeEnum;
import com.coretex.shop.model.order.total.OrderTotal;

public class OrderEntity extends Order implements Serializable {


	private static final long serialVersionUID = 1L;
	private List<OrderTotal> totals;
	private List<OrderAttribute> attributes = new ArrayList<OrderAttribute>();

	private PaymentTypeEnum paymentType;
	private String paymentModule;
	private String shippingModule;
	private List<OrderStatusEnum> previousOrderStatus;
	private OrderStatusEnum orderStatus;
	private Date datePurchased;
	private String currency;
	private boolean customerAgreed;
	private boolean confirmedAddress;
	private String comments;

	public void setTotals(List<OrderTotal> totals) {
		this.totals = totals;
	}

	public List<OrderTotal> getTotals() {
		return totals;
	}

	public PaymentTypeEnum getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(PaymentTypeEnum paymentType) {
		this.paymentType = paymentType;
	}

	public String getPaymentModule() {
		return paymentModule;
	}

	public void setPaymentModule(String paymentModule) {
		this.paymentModule = paymentModule;
	}

	public String getShippingModule() {
		return shippingModule;
	}

	public void setShippingModule(String shippingModule) {
		this.shippingModule = shippingModule;
	}

	public Date getDatePurchased() {
		return datePurchased;
	}

	public void setDatePurchased(Date datePurchased) {
		this.datePurchased = datePurchased;
	}

	public void setPreviousOrderStatus(List<OrderStatusEnum> previousOrderStatus) {
		this.previousOrderStatus = previousOrderStatus;
	}

	public List<OrderStatusEnum> getPreviousOrderStatus() {
		return previousOrderStatus;
	}

	public void setOrderStatus(OrderStatusEnum orderStatus) {
		this.orderStatus = orderStatus;
	}

	public OrderStatusEnum getOrderStatus() {
		return orderStatus;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public boolean isCustomerAgreed() {
		return customerAgreed;
	}

	public void setCustomerAgreed(boolean customerAgreed) {
		this.customerAgreed = customerAgreed;
	}

	public boolean isConfirmedAddress() {
		return confirmedAddress;
	}

	public void setConfirmedAddress(boolean confirmedAddress) {
		this.confirmedAddress = confirmedAddress;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public List<OrderAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<OrderAttribute> attributes) {
		this.attributes = attributes;
	}


}
