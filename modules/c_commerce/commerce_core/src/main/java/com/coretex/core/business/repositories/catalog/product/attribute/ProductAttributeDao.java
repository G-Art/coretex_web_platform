package com.coretex.core.business.repositories.catalog.product.attribute;

import java.util.List;
import java.util.UUID;

import com.coretex.core.activeorm.dao.Dao;
import com.coretex.items.commerce_core_model.ProductAttributeItem;

public interface ProductAttributeDao extends Dao<ProductAttributeItem> {

	//	@Query("select p from ProductAttributeItem p join fetch p.product pr left join fetch p.productOption po left join fetch p.productOptionValue pov left join fetch po.descriptions pod left join fetch pov.descriptions povd left join fetch po.merchantStore where p.id = ?1")
	ProductAttributeItem findOne(UUID id);

	//	@Query("select p from ProductAttributeItem p join fetch p.product pr left join fetch p.productOption po left join fetch p.productOptionValue pov left join fetch po.descriptions pod left join fetch pov.descriptions povd left join fetch po.merchantStore pom where pom.id = ?1 and po.id = ?2")
	List<ProductAttributeItem> findByOptionId(UUID storeId, UUID id);

	//	@Query("select distinct p from ProductAttributeItem p join fetch p.product pr left join fetch p.productOption po left join fetch p.productOptionValue pov left join fetch po.descriptions pod left join fetch pov.descriptions povd left join fetch po.merchantStore pom where pom.id = ?1 and po.id = ?2")
	List<ProductAttributeItem> findByOptionValueId(UUID storeId, UUID id);

	//	@Query("select distinct p from ProductAttributeItem p join fetch p.product pr left join fetch p.productOption po left join fetch p.productOptionValue pov left join fetch po.descriptions pod left join fetch pov.descriptions povd left join fetch pov.merchantStore povm where povm.id = ?1 and pr.id = ?2 and p.id in ?3")
	List<ProductAttributeItem> findByAttributeIds(UUID storeId, UUID productId, List<UUID> ids);

	//	@Query("select distinct p from ProductAttributeItem p join fetch p.product pr left join fetch p.productOption po left join fetch p.productOptionValue pov left join fetch po.descriptions pod left join fetch pov.descriptions povd left join fetch po.merchantStore pom where pom.id = ?1 and pr.id = ?2 and povd.language.id = ?3")
	List<ProductAttributeItem> findByProductId(UUID storeId, UUID productId, UUID languageId);
}
