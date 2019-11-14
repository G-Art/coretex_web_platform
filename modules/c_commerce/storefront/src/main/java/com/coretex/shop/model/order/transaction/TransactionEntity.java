package com.coretex.shop.model.order.transaction;

import java.io.Serializable;
import java.util.UUID;

import com.coretex.shop.model.entity.Entity;

/**
 * Readable version of TransactionItem entity object
 *
 * @author c.samson
 */
public class TransactionEntity extends Entity implements Serializable {


	private static final long serialVersionUID = 1L;
	private UUID orderId;
	private String details;
	private String transactionDate;
	private String amount;


	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	public UUID getOrderId() {
		return orderId;
	}

	public void setOrderId(UUID orderId) {
		this.orderId = orderId;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}


}
