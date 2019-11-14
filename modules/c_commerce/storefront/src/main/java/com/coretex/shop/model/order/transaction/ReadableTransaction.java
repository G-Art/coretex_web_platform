package com.coretex.shop.model.order.transaction;

import java.io.Serializable;

import com.coretex.enums.commerce_core_model.TransactionTypeEnum;
import com.coretex.enums.commerce_core_model.PaymentTypeEnum;

public class ReadableTransaction extends TransactionEntity implements Serializable {


	private static final long serialVersionUID = 1L;

	private PaymentTypeEnum paymentType;
	private TransactionTypeEnum transactionType;

	public PaymentTypeEnum getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(PaymentTypeEnum paymentType) {
		this.paymentType = paymentType;
	}

	public TransactionTypeEnum getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionTypeEnum transactionType) {
		this.transactionType = transactionType;
	}


}
