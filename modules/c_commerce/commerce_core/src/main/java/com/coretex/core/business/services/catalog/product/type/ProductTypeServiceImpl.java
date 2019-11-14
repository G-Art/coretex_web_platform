package com.coretex.core.business.services.catalog.product.type;

import com.coretex.core.business.repositories.catalog.product.type.ProductTypeDao;
import com.coretex.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.coretex.items.commerce_core_model.ProductTypeItem;
import org.springframework.stereotype.Service;

@Service("productTypeService")
public class ProductTypeServiceImpl extends SalesManagerEntityServiceImpl<ProductTypeItem>
		implements ProductTypeService {

	private ProductTypeDao productTypeDao;

	public ProductTypeServiceImpl(
			ProductTypeDao productTypeDao) {
		super(productTypeDao);
		this.productTypeDao = productTypeDao;
	}

	@Override
	public ProductTypeItem getProductType(String productTypeCode) {

		return productTypeDao.findByCode(productTypeCode);

	}


}
