package com.coretex.core.business.services.catalog.product.price;

import com.coretex.core.business.repositories.catalog.product.price.ProductPriceDao;
import com.coretex.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.coretex.items.commerce_core_model.ProductPriceItem;
import org.springframework.stereotype.Service;


@Service("productPrice")
public class ProductPriceServiceImpl extends SalesManagerEntityServiceImpl<ProductPriceItem>
		implements ProductPriceService {

	public ProductPriceServiceImpl(ProductPriceDao productPriceDao) {
		super(productPriceDao);
	}


}
