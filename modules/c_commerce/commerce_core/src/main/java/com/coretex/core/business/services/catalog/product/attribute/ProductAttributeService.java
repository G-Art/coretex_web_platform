package com.coretex.core.business.services.catalog.product.attribute;

import java.util.List;
import java.util.UUID;


import com.coretex.core.business.services.common.generic.SalesManagerEntityService;
import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.commerce_core_model.ProductAttributeItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.core.LocaleItem;

public interface ProductAttributeService extends
		SalesManagerEntityService<ProductAttributeItem> {

	void saveOrUpdate(ProductAttributeItem productAttribute)
			;

	List<ProductAttributeItem> getByOptionId(MerchantStoreItem store,
											 UUID id);

	List<ProductAttributeItem> getByOptionValueId(MerchantStoreItem store,
												  UUID id);

	List<ProductAttributeItem> getByProductId(MerchantStoreItem store, ProductItem product, LocaleItem language);

	List<ProductAttributeItem> getByAttributeIds(MerchantStoreItem store, ProductItem product, List<UUID> ids);
}
