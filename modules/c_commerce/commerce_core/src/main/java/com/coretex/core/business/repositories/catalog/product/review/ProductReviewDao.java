package com.coretex.core.business.repositories.catalog.product.review;

import java.util.List;
import java.util.UUID;

import com.coretex.items.commerce_core_model.ProductReviewItem;
import com.coretex.core.activeorm.dao.Dao;

public interface ProductReviewDao extends Dao<ProductReviewItem> {


	//	@Query("select p from ProductReviewItem p join fetch p.customer pc join fetch p.product pp join fetch pp.merchantStore ppm left join fetch p.descriptions pd where p.id = ?1")
	ProductReviewItem findOne(UUID id);

	//	@Query("select p from ProductReviewItem p join fetch p.customer pc join fetch p.product pp join fetch pp.merchantStore ppm left join fetch p.descriptions pd where pc.id = ?1")
	List<ProductReviewItem> findByCustomer(UUID customerId);

	//	@Query("select p from ProductReviewItem p left join fetch p.descriptions pd join fetch p.customer pc join fetch pc.merchantStore pcm left join fetch pc.defaultLanguage pcl left join fetch pc.attributes pca left join fetch pca.customerOption pcao left join fetch pca.customerOptionValue pcav left join fetch pcao.descriptions pcaod left join fetch pcav.descriptions pcavd join fetch p.product pp join fetch pp.merchantStore ppm  join fetch p.product pp join fetch pp.merchantStore ppm left join fetch p.descriptions pd where pp.id = ?1")
	List<ProductReviewItem> findByProduct(UUID productId);

	//	@Query("select p from ProductReviewItem p join fetch p.product pp join fetch pp.merchantStore ppm  where pp.id = ?1")
	List<ProductReviewItem> findByProductNoCustomers(UUID productId);

	//	@Query("select p from ProductReviewItem p left join fetch p.descriptions pd join fetch p.customer pc join fetch pc.merchantStore pcm left join fetch pc.defaultLanguage pcl left join fetch pc.attributes pca left join fetch pca.customerOption pcao left join fetch pca.customerOptionValue pcav left join fetch pcao.descriptions pcaod left join fetch pcav.descriptions pcavd join fetch p.product pp join fetch pp.merchantStore ppm  join fetch p.product pp join fetch pp.merchantStore ppm left join fetch p.descriptions pd where pp.id = ?1 and pd.language.id =?2")
	List<ProductReviewItem> findByProduct(UUID productId, UUID languageId);

	//	@Query("select p from ProductReviewItem p left join fetch p.descriptions pd join fetch p.customer pc join fetch pc.merchantStore pcm left join fetch pc.defaultLanguage pcl left join fetch pc.attributes pca left join fetch pca.customerOption pcao left join fetch pca.customerOptionValue pcav left join fetch pcao.descriptions pcaod left join fetch pcav.descriptions pcavd join fetch p.product pp join fetch pp.merchantStore ppm  join fetch p.product pp join fetch pp.merchantStore ppm left join fetch p.descriptions pd where pp.id = ?1 and pc.id = ?2")
	ProductReviewItem findByProductAndCustomer(UUID productId, UUID customerId);


}
