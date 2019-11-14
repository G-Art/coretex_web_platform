package com.coretex.core.business.repositories.catalog.product.availability;

import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.commerce_core_model.ProductAvailabilityItem;
import org.springframework.stereotype.Component;

@Component
public class ProductAvailabilityDaoImpl extends DefaultGenericDao<ProductAvailabilityItem> implements ProductAvailabilityDao {
	public ProductAvailabilityDaoImpl() {
		super(ProductAvailabilityItem.ITEM_TYPE);
	}
}
