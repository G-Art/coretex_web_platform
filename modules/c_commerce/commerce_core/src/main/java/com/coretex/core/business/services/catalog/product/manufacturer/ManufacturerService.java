package com.coretex.core.business.services.catalog.product.manufacturer;


import com.coretex.core.business.services.common.generic.SalesManagerEntityService;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.commerce_core_model.ManufacturerItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;

import java.util.List;
import java.util.UUID;

public interface ManufacturerService extends SalesManagerEntityService<ManufacturerItem> {

	List<ManufacturerItem> listByStore(MerchantStoreItem store, LocaleItem language)
			;

	List<ManufacturerItem> listByStore(MerchantStoreItem store) ;

	void saveOrUpdate(ManufacturerItem manufacturer) ;

	Long getCountManufAttachedProducts(ManufacturerItem manufacturer) ;


	ManufacturerItem getByCode(MerchantStoreItem store, String code);

	/**
	 * List manufacturers by products from a given list of categories
	 *
	 * @param store
	 * @param ids
	 * @param language
	 * @return
	 * @
	 */
	List<ManufacturerItem> listByProductsByCategoriesId(MerchantStoreItem store,
														List<UUID> ids, LocaleItem language) ;

}
