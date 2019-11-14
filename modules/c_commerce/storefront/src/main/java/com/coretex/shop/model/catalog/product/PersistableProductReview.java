package com.coretex.shop.model.catalog.product;

import java.io.Serializable;
import java.util.UUID;

import javax.validation.constraints.NotNull;

public class PersistableProductReview extends ProductReviewEntity implements
		Serializable {


	private static final long serialVersionUID = 1L;
	@NotNull
	private UUID customerId;

	public UUID getCustomerId() {
		return customerId;
	}

	public void setCustomerId(UUID customerId) {
		this.customerId = customerId;
	}


}
