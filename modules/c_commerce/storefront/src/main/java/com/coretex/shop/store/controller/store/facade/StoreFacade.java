package com.coretex.shop.store.controller.store.facade;

import javax.servlet.http.HttpServletRequest;

import com.coretex.core.model.content.InputContentFile;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.core.model.merchant.MerchantStoreCriteria;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.shop.model.shop.PersistableBrand;
import com.coretex.shop.model.shop.PersistableMerchantStore;
import com.coretex.shop.model.shop.ReadableBrand;
import com.coretex.shop.model.shop.ReadableMerchantStore;
import com.coretex.shop.model.shop.ReadableMerchantStoreList;

/**
 * Layer between shop controllers, services and API with sm-core
 *
 * @author carlsamson
 */
public interface StoreFacade {

	/**
	 * Find MerchantStoreItem model from store code
	 *
	 * @param code
	 * @return
	 * @throws Exception
	 */
	MerchantStoreItem getByCode(HttpServletRequest request);

	MerchantStoreItem get(String code);

	MerchantStoreItem getByCode(String code);

	ReadableMerchantStore getByCode(String code, String lang);

	ReadableMerchantStore getByCode(String code, LanguageItem lang);

	boolean existByCode(String code);

	/**
	 * List MerchantStoreItem using various criterias
	 *
	 * @param criteria
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	ReadableMerchantStoreList getByCriteria(MerchantStoreCriteria criteria, String drawParam, LanguageItem lang);

	/**
	 * Creates a brand new MerchantStoreItem
	 *
	 * @param store
	 * @throws Exception
	 */
	ReadableMerchantStore create(PersistableMerchantStore store);

	/**
	 * Updates an existing store
	 *
	 * @param store
	 * @throws Exception
	 */
	ReadableMerchantStore update(PersistableMerchantStore store);

	/**
	 * Deletes a MerchantStoreItem based on store code
	 *
	 * @param code
	 */
	void delete(String code);

	/**
	 * Get Logo, social networks and other brand configurations
	 *
	 * @param code
	 * @return
	 */
	ReadableBrand getBrand(String code);

	/**
	 * Create store brand
	 *
	 * @param merchantStoreCode
	 * @param brand
	 */
	void createBrand(String merchantStoreCode, PersistableBrand brand);

	/**
	 * Delete store logo
	 */
	void deleteLogo(String code);

	/**
	 * Add MerchantStoreItem logo
	 *
	 * @param code
	 * @param cmsContentImage
	 */
	void addStoreLogo(String code, InputContentFile cmsContentImage);

}
