package com.coretex.core.business.services.merchant;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.services.common.generic.SalesManagerEntityService;
import com.coretex.core.model.common.GenericEntityList;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.core.model.merchant.MerchantStoreCriteria;
import com.coretex.items.commerce_core_model.MerchantStoreItem;

public interface MerchantStoreService extends SalesManagerEntityService<MerchantStoreItem> {

	MerchantStoreItem getByCode(String code) throws ServiceException;

	boolean existByCode(String code);

	void saveOrUpdate(MerchantStoreItem store) throws ServiceException;

	GenericEntityList<MerchantStoreItem> getByCriteria(MerchantStoreCriteria criteria) throws ServiceException;

}
