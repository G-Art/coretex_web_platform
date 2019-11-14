package com.coretex.core.business.services.catalog.product.attribute;

import java.util.List;
import java.util.UUID;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.services.common.generic.SalesManagerEntityService;
import com.coretex.items.commerce_core_model.ProductOptionValueItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;

public interface ProductOptionValueService extends SalesManagerEntityService<ProductOptionValueItem> {

	void saveOrUpdate(ProductOptionValueItem entity) throws ServiceException;

	List<ProductOptionValueItem> getByName(MerchantStoreItem store, String name,
										   LanguageItem language) throws ServiceException;


	List<ProductOptionValueItem> listByStore(MerchantStoreItem store, LanguageItem language)
			throws ServiceException;

	List<ProductOptionValueItem> listByStoreNoReadOnly(MerchantStoreItem store,
													   LanguageItem language) throws ServiceException;

	ProductOptionValueItem getByCode(MerchantStoreItem store, String optionValueCode);

	ProductOptionValueItem getById(MerchantStoreItem store, UUID optionValueId);

}
