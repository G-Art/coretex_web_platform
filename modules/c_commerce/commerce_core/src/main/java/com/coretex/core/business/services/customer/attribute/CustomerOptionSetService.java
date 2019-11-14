package com.coretex.core.business.services.customer.attribute;

import java.util.List;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.services.common.generic.SalesManagerEntityService;
import com.coretex.items.commerce_core_model.CustomerOptionItem;
import com.coretex.items.commerce_core_model.CustomerOptionSetItem;
import com.coretex.items.commerce_core_model.CustomerOptionValueItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;


public interface CustomerOptionSetService extends SalesManagerEntityService<CustomerOptionSetItem> {


	void saveOrUpdate(CustomerOptionSetItem entity) throws ServiceException;


	List<CustomerOptionSetItem> listByStore(MerchantStoreItem store,
											LanguageItem language) throws ServiceException;


	List<CustomerOptionSetItem> listByOption(CustomerOptionItem option,
											 MerchantStoreItem store);


	List<CustomerOptionSetItem> listByOptionValue(CustomerOptionValueItem optionValue,
												  MerchantStoreItem store);

}
