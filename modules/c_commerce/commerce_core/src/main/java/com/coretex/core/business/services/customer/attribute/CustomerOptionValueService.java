package com.coretex.core.business.services.customer.attribute;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.services.common.generic.SalesManagerEntityService;
import com.coretex.items.commerce_core_model.CustomerOptionValueItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;

import java.util.List;


public interface CustomerOptionValueService extends SalesManagerEntityService<CustomerOptionValueItem> {


	List<CustomerOptionValueItem> listByStore(MerchantStoreItem store, LanguageItem language)
			throws ServiceException;

	void saveOrUpdate(CustomerOptionValueItem entity) throws ServiceException;

	CustomerOptionValueItem getByCode(MerchantStoreItem store, String optionValueCode);


}
