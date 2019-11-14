package com.coretex.core.business.services.customer.attribute;

import java.util.List;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.services.common.generic.SalesManagerEntityService;
import com.coretex.items.commerce_core_model.CustomerOptionItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;


public interface CustomerOptionService extends SalesManagerEntityService<CustomerOptionItem> {

	List<CustomerOptionItem> listByStore(MerchantStoreItem store, LanguageItem language)
			throws ServiceException;


	void saveOrUpdate(CustomerOptionItem entity) throws ServiceException;


	CustomerOptionItem getByCode(MerchantStoreItem store, String optionCode);


}
