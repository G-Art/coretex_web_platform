package com.coretex.core.business.repositories.customer;

import com.coretex.core.model.customer.CustomerCriteria;
import com.coretex.core.model.customer.CustomerList;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;


public interface CustomerRepositoryCustom {

	CustomerList listByStore(MerchantStoreItem store, CustomerCriteria criteria);


}
