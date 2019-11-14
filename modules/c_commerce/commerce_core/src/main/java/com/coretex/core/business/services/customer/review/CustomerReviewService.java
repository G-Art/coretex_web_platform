package com.coretex.core.business.services.customer.review;

import java.util.List;
import java.util.UUID;

import com.coretex.core.business.services.common.generic.SalesManagerEntityService;
import com.coretex.items.commerce_core_model.CustomerItem;
import com.coretex.items.commerce_core_model.CustomerReviewItem;

public interface CustomerReviewService extends
		SalesManagerEntityService<CustomerReviewItem> {

	/**
	 * All reviews created by a given customer
	 *
	 * @param customer
	 * @return
	 */
	List<CustomerReviewItem> getByCustomer(CustomerItem customer);

	/**
	 * All reviews received by a given customer
	 *
	 * @param customer
	 * @return
	 */
	List<CustomerReviewItem> getByReviewedCustomer(CustomerItem customer);

	/**
	 * Get a review made by a customer to another customer
	 *
	 * @param reviewer
	 * @param reviewed
	 * @return
	 */
	CustomerReviewItem getByReviewerAndReviewed(UUID reviewer, UUID reviewed);

}
