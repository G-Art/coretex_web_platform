package com.coretex.commerce.data;

public class OrderPlaceResult {
	private boolean paymentValid;
	private boolean deliveryTypeValid;
	private boolean addressValid;
	private boolean orderCreated;

	private String paymentAction;

	public boolean isPaymentValid() {
		return paymentValid;
	}

	public void setPaymentValid(boolean paymentValid) {
		this.paymentValid = paymentValid;
	}

	public boolean isDeliveryTypeValid() {
		return deliveryTypeValid;
	}

	public void setDeliveryTypeValid(boolean deliveryTypeValid) {
		this.deliveryTypeValid = deliveryTypeValid;
	}

	public boolean isAddressValid() {
		return addressValid;
	}

	public void setAddressValid(boolean addressValid) {
		this.addressValid = addressValid;
	}

	public boolean isOrderCreated() {
		return orderCreated;
	}

	public void setOrderCreated(boolean orderCreated) {
		this.orderCreated = orderCreated;
	}

	public String getPaymentAction() {
		return paymentAction;
	}

	public void setPaymentAction(String paymentAction) {
		this.paymentAction = paymentAction;
	}
}
