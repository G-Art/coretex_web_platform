package com.coretex.shop.model.order.transaction;

import java.io.Serializable;

import com.coretex.enums.commerce_core_model.TransactionTypeEnum;
import com.coretex.enums.commerce_core_model.PaymentTypeEnum;
import com.coretex.shop.utils.Enum;

/**
 * This class is used for writing a transaction in the System
 *
 * @author c.samson
 */
public class PersistableTransaction extends TransactionEntity implements Serializable {


	private static final long serialVersionUID = 1L;

	@Enum(enumClass = PaymentTypeEnum.class, ignoreCase = true)
	private String paymentType;

	@Enum(enumClass = TransactionTypeEnum.class, ignoreCase = true)
	private String transactionType;

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
}
