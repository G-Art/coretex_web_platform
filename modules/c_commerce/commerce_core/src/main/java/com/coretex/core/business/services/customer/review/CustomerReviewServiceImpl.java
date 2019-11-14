package com.coretex.core.business.services.customer.review;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import com.coretex.items.commerce_core_model.CustomerItem;
import com.coretex.items.commerce_core_model.CustomerReviewItem;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;

import com.coretex.core.business.repositories.customer.review.CustomerReviewDao;
import com.coretex.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.coretex.core.business.services.customer.CustomerService;

@Service("customerReviewService")
public class CustomerReviewServiceImpl extends
		SalesManagerEntityServiceImpl<CustomerReviewItem> implements CustomerReviewService {

	private CustomerReviewDao customerReviewDao;

	@Resource
	private CustomerService customerService;

	public CustomerReviewServiceImpl(
			CustomerReviewDao customerReviewDao) {
		super(customerReviewDao);
		this.customerReviewDao = customerReviewDao;
	}


	private void saveOrUpdate(CustomerReviewItem review) {


		Validate.notNull(review, "CustomerReviewItem cannot be null");
		Validate.notNull(review.getCustomer(), "CustomerReviewItem.customer cannot be null");
		Validate.notNull(review.getReviewedCustomer(), "CustomerReviewItem.reviewedCustomer cannot be null");


		//refresh customer
		CustomerItem customer = customerService.getById(review.getReviewedCustomer().getUuid());

		//ajust product rating
		Integer count = 0;
		if (customer.getCustomerReviewCount() != null) {
			count = customer.getCustomerReviewCount();
		}


		BigDecimal averageRating = customer.getCustomerReviewAvg();
		if (averageRating == null) {
			averageRating = new BigDecimal(0);
		}
		//get reviews


		BigDecimal totalRating = averageRating.multiply(new BigDecimal(count));
		totalRating = totalRating.add(new BigDecimal(review.getReviewRating()));

		count = count + 1;
		double avg = totalRating.doubleValue() / count.intValue();

		customer.setCustomerReviewAvg(new BigDecimal(avg));
		customer.setCustomerReviewCount(count);
		super.save(review);

		customerService.update(customer);

		review.setReviewedCustomer(customer);


	}

	public void update(CustomerReviewItem review) {
		this.saveOrUpdate(review);
	}

	public void create(CustomerReviewItem review) {
		this.saveOrUpdate(review);
	}


	@Override
	public List<CustomerReviewItem> getByCustomer(CustomerItem customer) {
		Validate.notNull(customer, "CustomerItem cannot be null");
		return customerReviewDao.findByReviewer(customer.getUuid());
	}

	@Override
	public List<CustomerReviewItem> getByReviewedCustomer(CustomerItem customer) {
		Validate.notNull(customer, "CustomerItem cannot be null");
		return customerReviewDao.findByReviewed(customer.getUuid());
	}


	@Override
	public CustomerReviewItem getByReviewerAndReviewed(UUID reviewer, UUID reviewed) {
		Validate.notNull(reviewer, "Reviewer customer cannot be null");
		Validate.notNull(reviewed, "Reviewer customer cannot be null");
		return customerReviewDao.findByRevieweAndReviewed(reviewer, reviewed);
	}

}
