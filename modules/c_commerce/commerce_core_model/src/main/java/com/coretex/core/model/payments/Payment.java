package com.coretex.core.model.payments;

import java.math.BigDecimal;
import java.util.Map;

import com.coretex.enums.commerce_core_model.PaymentTypeEnum;
import com.coretex.enums.commerce_core_model.TransactionTypeEnum;
import com.coretex.items.commerce_core_model.CurrencyItem;

public class Payment {

	private PaymentTypeEnum paymentType;
	private TransactionTypeEnum transactionType = TransactionTypeEnum.AUTHORIZECAPTURE;
	private String moduleName;
	private CurrencyItem currency;
	private BigDecimal amount;
	private Map<String, String> paymentMetaData = null;

	public void setPaymentType(PaymentTypeEnum paymentType) {
		this.paymentType = paymentType;
	}

	public PaymentTypeEnum getPaymentType() {
		return paymentType;
	}

	public void setTransactionType(TransactionTypeEnum transactionType) {
		this.transactionType = transactionType;
	}

	public TransactionTypeEnum getTransactionType() {
		return transactionType;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getModuleName() {
		return moduleName;
	}

	public CurrencyItem getCurrency() {
		return currency;
	}

	public void setCurrency(CurrencyItem currency) {
		this.currency = currency;
	}

	public Map<String, String> getPaymentMetaData() {
		return paymentMetaData;
	}

	public void setPaymentMetaData(Map<String, String> paymentMetaData) {
		this.paymentMetaData = paymentMetaData;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

}
