package com.coretex.core.business.repositories.catalog.product.file;

import com.coretex.core.activeorm.dao.Dao;
import com.coretex.items.commerce_core_model.DigitalProductItem;

import java.util.UUID;

public interface DigitalProductDao extends Dao<DigitalProductItem> {

	//	@Query("select p from DigitalProductItem p inner join fetch p.product pp inner join fetch pp.merchantStore ppm where ppm.id =?1 and pp.id = ?2")
	DigitalProductItem findByProduct(UUID storeId, UUID productId);

	//	@Query("select p from DigitalProductItem p inner join fetch p.product pp inner join fetch pp.merchantStore ppm where p.id = ?1")
	DigitalProductItem findOne(UUID id);


}
