package com.coretex.core.business.repositories.catalog.product.image;

import com.coretex.items.commerce_core_model.ProductImageItem;
import com.coretex.core.activeorm.dao.Dao;

import java.util.UUID;

public interface ProductImageDao extends Dao<ProductImageItem> {


	//	@Query("select p from ProductImageItem p left join fetch p.descriptions pd inner join fetch p.product pp inner join fetch pp.merchantStore ppm where p.id = ?1")
	ProductImageItem findOne(UUID id);


}
