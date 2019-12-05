package com.coretex.core.business.services.catalog.product.availability;


import com.coretex.core.business.services.common.generic.SalesManagerEntityService;
import com.coretex.items.commerce_core_model.ProductAvailabilityItem;

public interface ProductAvailabilityService extends
		SalesManagerEntityService<ProductAvailabilityItem> {

	void saveOrUpdate(ProductAvailabilityItem availability) ;

}
