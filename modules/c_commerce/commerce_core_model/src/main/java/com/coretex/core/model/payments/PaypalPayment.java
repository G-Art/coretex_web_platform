package com.coretex.core.model.payments;

import com.coretex.enums.commerce_core_model.PaymentTypeEnum;

/**
 * When the user performs a payment using paypal
 *
 * @author Carl Samson
 */
public class PaypalPayment extends Payment {

	//express checkout
	private String payerId;
	private String paymentToken;

	public PaypalPayment() {
		super.setPaymentType(PaymentTypeEnum.PAYPAL);
	}

	public void setPayerId(String payerId) {
		this.payerId = payerId;
	}

	public String getPayerId() {
		return payerId;
	}

	public void setPaymentToken(String paymentToken) {
		this.paymentToken = paymentToken;
	}

	public String getPaymentToken() {
		return paymentToken;
	}

}
