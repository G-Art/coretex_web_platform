package com.coretex.core.business.services.system;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.services.common.generic.SalesManagerEntityService;
import com.coretex.items.commerce_core_model.SystemConfigurationItem;

public interface SystemConfigurationService extends
		SalesManagerEntityService<SystemConfigurationItem> {

	SystemConfigurationItem getByKey(String key) throws ServiceException;

}
