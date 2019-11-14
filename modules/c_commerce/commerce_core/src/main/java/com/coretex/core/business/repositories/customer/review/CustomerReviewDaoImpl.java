package com.coretex.core.business.repositories.customer.review;

import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.commerce_core_model.CustomerReviewItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class CustomerReviewDaoImpl extends DefaultGenericDao<CustomerReviewItem> implements CustomerReviewDao {


	public CustomerReviewDaoImpl() {
		super(CustomerReviewItem.ITEM_TYPE);
	}

	@Override
	public CustomerReviewItem findOne(UUID id) {
		return find(id);
	}

	@Override
	public List<CustomerReviewItem> findByReviewer(UUID id) {
		return null;
	}

	@Override
	public List<CustomerReviewItem> findByReviewed(UUID id) {
		return null;
	}

	@Override
	public CustomerReviewItem findByRevieweAndReviewed(UUID reviewer, UUID reviewed) {
		return null;
	}
}
