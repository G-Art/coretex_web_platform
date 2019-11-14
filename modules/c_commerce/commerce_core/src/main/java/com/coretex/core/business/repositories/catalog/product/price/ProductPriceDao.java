package com.coretex.core.business.repositories.catalog.product.price;

import com.coretex.items.commerce_core_model.ProductPriceItem;
import com.coretex.core.activeorm.dao.Dao;

import java.util.UUID;

public interface ProductPriceDao extends Dao<ProductPriceItem> {


	//	@Query("select p from ProductPriceItem p left join fetch p.descriptions pd inner join fetch p.productAvailability pa inner join fetch pa.product pap inner join fetch pap.merchantStore papm where p.id = ?1")
	ProductPriceItem findOne(UUID id);


}
