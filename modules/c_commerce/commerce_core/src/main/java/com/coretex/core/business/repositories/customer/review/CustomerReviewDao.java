package com.coretex.core.business.repositories.customer.review;

import java.util.List;
import java.util.UUID;

import com.coretex.items.commerce_core_model.CustomerReviewItem;
import com.coretex.core.activeorm.dao.Dao;

public interface CustomerReviewDao extends Dao<CustomerReviewItem> {

	String customerQuery = ""
			+ "select distinct r from CustomerReviewItem r join fetch "
			+ "r.customer rc "
			//+ "join fetch rc.attributes rca left join "
			//+ "fetch rca.customerOption rcao left join fetch rca.customerOptionValue "
			//+ "rcav left join fetch rcao.descriptions rcaod left join fetch rcav.descriptions "
			+ "join fetch r.reviewedCustomer rr join fetch rc.merchantStore rrm "
			+ "left join fetch r.descriptions rd ";


	//	@Query("select r from CustomerReviewItem r join fetch r.customer rc join fetch r.reviewedCustomer rr join fetch rc.merchantStore rrm left join fetch r.descriptions rd where r.id = ?1")
	CustomerReviewItem findOne(UUID id);

	//	@Query("select distinct r from CustomerReviewItem r join fetch r.customer rc join fetch r.reviewedCustomer rr join fetch rc.merchantStore rrm left join fetch r.descriptions rd where rc.id = ?1")
	List<CustomerReviewItem> findByReviewer(UUID id);

	//	@Query("select distinct r from CustomerReviewItem r join fetch r.customer rc join fetch r.reviewedCustomer rr join fetch rc.merchantStore rrm left join fetch r.descriptions rd where rr.id = ?1")
	List<CustomerReviewItem> findByReviewed(UUID id);

	//	@Query( customerQuery + "where rc.id = ?1 and rr.id = ?2")
	CustomerReviewItem findByRevieweAndReviewed(UUID reviewer, UUID reviewed);


}
