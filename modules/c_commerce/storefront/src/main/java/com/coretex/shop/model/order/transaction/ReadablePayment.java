package com.coretex.shop.model.order.transaction;

import com.coretex.enums.commerce_core_model.PaymentTypeEnum;

public class ReadablePayment extends PaymentEntity {

	private static final long serialVersionUID = 1L;
	private PaymentTypeEnum paymentType;


	public PaymentTypeEnum getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(PaymentTypeEnum paymentType) {
		this.paymentType = paymentType;
	}

}
