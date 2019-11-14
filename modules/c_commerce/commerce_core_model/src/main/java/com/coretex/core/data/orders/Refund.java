package com.coretex.core.data.orders;

import java.io.Serializable;
import java.util.UUID;

public class Refund implements Serializable {


	private static final long serialVersionUID = 2392736671094915447L;
	private UUID orderId;
	private String amount;

	public UUID getOrderId() {
		return orderId;
	}

	public void setOrderId(UUID orderId) {
		this.orderId = orderId;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

}
