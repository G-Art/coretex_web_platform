package com.coretex.core.business.services.catalog.product.availability;

import org.springframework.stereotype.Service;


import com.coretex.core.business.repositories.catalog.product.availability.ProductAvailabilityDao;
import com.coretex.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.coretex.items.commerce_core_model.ProductAvailabilityItem;

@Service("productAvailabilityService")
public class ProductAvailabilityServiceImpl extends
		SalesManagerEntityServiceImpl<ProductAvailabilityItem> implements
		ProductAvailabilityService {


	private ProductAvailabilityDao productAvailabilityDao;

	public ProductAvailabilityServiceImpl(
			ProductAvailabilityDao productAvailabilityDao) {
		super(productAvailabilityDao);
		this.productAvailabilityDao = productAvailabilityDao;
	}


	@Override
	public void saveOrUpdate(ProductAvailabilityItem availability)  {

		this.save(availability);

	}


}
