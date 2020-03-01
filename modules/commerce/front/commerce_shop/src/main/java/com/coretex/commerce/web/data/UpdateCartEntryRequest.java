package com.coretex.commerce.web.data;

import java.util.UUID;

public class UpdateCartEntryRequest {
	private UUID entry;
	private Integer quantity;

	public UUID getEntry() {
		return entry;
	}

	public void setEntry(UUID entry) {
		this.entry = entry;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
}
