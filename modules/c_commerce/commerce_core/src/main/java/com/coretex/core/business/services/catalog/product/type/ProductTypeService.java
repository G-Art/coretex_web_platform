package com.coretex.core.business.services.catalog.product.type;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.services.common.generic.SalesManagerEntityService;
import com.coretex.items.commerce_core_model.ProductTypeItem;

public interface ProductTypeService extends SalesManagerEntityService<ProductTypeItem> {

	ProductTypeItem getProductType(String productTypeCode);

}
