package com.coretex.core.business.services.customer.attribute;

import java.util.List;
import java.util.UUID;

import com.coretex.items.commerce_core_model.CustomerAttributeItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import org.springframework.stereotype.Service;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.repositories.customer.attribute.CustomerAttributeDao;
import com.coretex.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.coretex.items.commerce_core_model.CustomerItem;


@Service("customerAttributeService")
public class CustomerAttributeServiceImpl extends
		SalesManagerEntityServiceImpl<CustomerAttributeItem> implements CustomerAttributeService {

	private CustomerAttributeDao customerAttributeDao;

	public CustomerAttributeServiceImpl(CustomerAttributeDao customerAttributeDao) {
		super(customerAttributeDao);
		this.customerAttributeDao = customerAttributeDao;
	}


	@Override
	public void saveOrUpdate(CustomerAttributeItem customerAttribute)
			throws ServiceException {

		customerAttributeDao.save(customerAttribute);


	}


	@Override
	public CustomerAttributeItem getByCustomerOptionId(MerchantStoreItem store, UUID customerId, UUID id) {
		return customerAttributeDao.findByOptionId(store.getUuid(), customerId, id);
	}


	@Override
	public List<CustomerAttributeItem> getByCustomer(MerchantStoreItem store, CustomerItem customer) {
		return customerAttributeDao.findByCustomerId(store.getUuid(), customer.getUuid());
	}


	@Override
	public List<CustomerAttributeItem> getByCustomerOptionValueId(MerchantStoreItem store,
																  UUID id) {
		return customerAttributeDao.findByOptionValueId(store.getUuid(), id);
	}

	@Override
	public List<CustomerAttributeItem> getByOptionId(MerchantStoreItem store,
													 UUID id) {
		return customerAttributeDao.findByOptionId(store.getUuid(), id);
	}

}
