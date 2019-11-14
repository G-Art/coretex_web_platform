package com.coretex.core.business.services.customer.optin;


import com.coretex.items.commerce_core_model.MerchantStoreItem;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.repositories.customer.optin.CustomerOptinDao;
import com.coretex.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.coretex.items.commerce_core_model.CustomerOptinItem;


@Service
public class CustomerOptinServiceImpl extends SalesManagerEntityServiceImpl<CustomerOptinItem> implements CustomerOptinService {


	private CustomerOptinDao customerOptinDao;

	public CustomerOptinServiceImpl(CustomerOptinDao customerOptinDao) {
		super(customerOptinDao);
		this.customerOptinDao = customerOptinDao;
	}

	@Override
	public void optinCumtomer(CustomerOptinItem optin) throws ServiceException {
		Validate.notNull(optin, "CustomerOptinItem must not be null");

		customerOptinDao.save(optin);


	}

	@Override
	public void optoutCumtomer(CustomerOptinItem optin) throws ServiceException {
		Validate.notNull(optin, "CustomerOptinItem must not be null");

		customerOptinDao.delete(optin);

	}

	@Override
	public CustomerOptinItem findByEmailAddress(MerchantStoreItem store, String emailAddress, String code) {
		return customerOptinDao.findByMerchantAndCodeAndEmail(store.getUuid(), code, emailAddress);
	}

}
