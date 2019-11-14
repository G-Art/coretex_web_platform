package com.coretex.core.model.order;

import com.coretex.core.model.common.Criteria;

import java.util.UUID;

public class OrderCriteria extends Criteria {

	private String customerName;
	private String paymentMethod;
	private UUID customerId;

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerName() {
		return customerName;
	}

	public UUID getCustomerId() {
		return customerId;
	}

	public void setCustomerId(UUID customerId) {
		this.customerId = customerId;
	}


}
