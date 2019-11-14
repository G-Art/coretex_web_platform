package com.coretex.core.business.services.customer.optin;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.services.common.generic.SalesManagerEntityService;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.CustomerOptinItem;

/**
 * Used for optin in customers
 * An implementation example is for signin in users
 *
 * @author carlsamson
 */
public interface CustomerOptinService extends SalesManagerEntityService<CustomerOptinItem> {

	/**
	 * OptinItem a given customer. This has no reference to a specific CustomerItem object but contains
	 * only email, first name and lastname
	 *
	 * @param optin
	 * @throws ServiceException
	 */
	void optinCumtomer(CustomerOptinItem optin) throws ServiceException;


	/**
	 * Removes a specific CustomerOptinItem
	 *
	 * @param optin
	 * @throws ServiceException
	 */
	void optoutCumtomer(CustomerOptinItem optin) throws ServiceException;

	/**
	 * Find an existing CustomerOptinItem
	 *
	 * @param store
	 * @param emailAddress
	 * @param code
	 * @return
	 * @throws ServiceException
	 */
	CustomerOptinItem findByEmailAddress(MerchantStoreItem store, String emailAddress, String code) throws ServiceException;


}
