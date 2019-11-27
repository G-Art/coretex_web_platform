package com.coretex.core.business.services.catalog.product.manufacturer;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.services.common.generic.SalesManagerEntityService;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.commerce_core_model.ManufacturerItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;

import java.util.List;
import java.util.UUID;

public interface ManufacturerService extends SalesManagerEntityService<ManufacturerItem> {

	List<ManufacturerItem> listByStore(MerchantStoreItem store, LocaleItem language)
			throws ServiceException;

	List<ManufacturerItem> listByStore(MerchantStoreItem store) throws ServiceException;

	void saveOrUpdate(ManufacturerItem manufacturer) throws ServiceException;

	Long getCountManufAttachedProducts(ManufacturerItem manufacturer) throws ServiceException;


	ManufacturerItem getByCode(MerchantStoreItem store, String code);

	/**
	 * List manufacturers by products from a given list of categories
	 *
	 * @param store
	 * @param ids
	 * @param language
	 * @return
	 * @throws ServiceException
	 */
	List<ManufacturerItem> listByProductsByCategoriesId(MerchantStoreItem store,
														List<UUID> ids, LocaleItem language) throws ServiceException;

}
