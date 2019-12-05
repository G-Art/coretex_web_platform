package com.coretex.core.business.services.customer;


import java.util.List;
import java.util.UUID;


import com.coretex.core.business.services.common.generic.SalesManagerEntityService;
import com.coretex.core.model.common.Address;
import com.coretex.items.commerce_core_model.CustomerItem;
import com.coretex.core.model.customer.CustomerCriteria;
import com.coretex.core.model.customer.CustomerList;
import com.coretex.items.commerce_core_model.MerchantStoreItem;


public interface CustomerService extends SalesManagerEntityService<CustomerItem> {

	List<CustomerItem> getByName(String firstName);

	CustomerItem getByNick(String nick);

	void saveOrUpdate(CustomerItem customer) ;

	CustomerList getListByStore(MerchantStoreItem store, CustomerCriteria criteria);

	CustomerItem getByEmail(String email);

	CustomerItem getByNick(String nick, UUID storeId);


}
