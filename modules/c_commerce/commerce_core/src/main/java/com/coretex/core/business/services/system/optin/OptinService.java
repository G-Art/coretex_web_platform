package com.coretex.core.business.services.system.optin;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.services.common.generic.SalesManagerEntityService;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.OptinItem;
import com.coretex.enums.commerce_core_model.OptinTypeEnum;

/**
 * Registers OptinItem events
 *
 * @author carlsamson
 */
public interface OptinService extends SalesManagerEntityService<OptinItem> {


	OptinItem getOptinByMerchantAndType(MerchantStoreItem store, OptinTypeEnum type) throws ServiceException;

	OptinItem getOptinByCode(MerchantStoreItem store, String code) throws ServiceException;

}
