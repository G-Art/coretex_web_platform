package com.coretex.core.business.services.customer.attribute;

import java.util.List;
import java.util.UUID;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.services.common.generic.SalesManagerEntityService;
import com.coretex.items.commerce_core_model.CustomerItem;
import com.coretex.items.commerce_core_model.CustomerAttributeItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;


public interface CustomerAttributeService extends
		SalesManagerEntityService<CustomerAttributeItem> {

	void saveOrUpdate(CustomerAttributeItem customerAttribute)
			throws ServiceException;

	CustomerAttributeItem getByCustomerOptionId(MerchantStoreItem store,
												UUID customerId, UUID id);

	List<CustomerAttributeItem> getByCustomerOptionValueId(MerchantStoreItem store,
														   UUID id);

	List<CustomerAttributeItem> getByOptionId(MerchantStoreItem store, UUID id);


	List<CustomerAttributeItem> getByCustomer(MerchantStoreItem store, CustomerItem customer);


}
