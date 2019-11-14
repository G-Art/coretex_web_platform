package com.coretex.core.business.services.catalog.product.attribute;

import java.util.List;
import java.util.UUID;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.services.common.generic.SalesManagerEntityService;
import com.coretex.items.commerce_core_model.ProductOptionItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;

public interface ProductOptionService extends SalesManagerEntityService<ProductOptionItem> {

	List<ProductOptionItem> listByStore(MerchantStoreItem store, LanguageItem language)
			throws ServiceException;


	List<ProductOptionItem> getByName(MerchantStoreItem store, String name,
									  LanguageItem language) throws ServiceException;

	void saveOrUpdate(ProductOptionItem entity) throws ServiceException;


	List<ProductOptionItem> listReadOnly(MerchantStoreItem store, LanguageItem language)
			throws ServiceException;


	ProductOptionItem getByCode(MerchantStoreItem store, String optionCode);

	ProductOptionItem getById(MerchantStoreItem store, UUID optionId);


}
