package com.coretex.shop.model.order.transaction;

import com.coretex.enums.commerce_core_model.PaymentTypeEnum;
import com.coretex.shop.utils.Enum;

public class PersistablePayment extends PaymentEntity {


	private static final long serialVersionUID = 1L;
	@Enum(enumClass = PaymentTypeEnum.class, ignoreCase = true)
	private String paymentType;

	private String transactionType;

	private String paymentToken;//any token after doing init

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getPaymentToken() {
		return paymentToken;
	}

	public void setPaymentToken(String paymentToken) {
		this.paymentToken = paymentToken;
	}


}
