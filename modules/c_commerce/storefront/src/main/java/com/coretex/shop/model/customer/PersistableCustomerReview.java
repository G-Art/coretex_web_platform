package com.coretex.shop.model.customer;

import java.util.UUID;

public class PersistableCustomerReview extends CustomerReviewEntity {


	private static final long serialVersionUID = 1L;
	private UUID reviewedCustomer;

	public UUID getReviewedCustomer() {
		return reviewedCustomer;
	}

	public void setReviewedCustomer(UUID reviewedCustomer) {
		this.reviewedCustomer = reviewedCustomer;
	}

}
