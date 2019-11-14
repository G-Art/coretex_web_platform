package com.coretex.core.business.services.catalog.category;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.services.common.generic.SalesManagerEntityService;
import com.coretex.items.commerce_core_model.CategoryItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface CategoryService extends SalesManagerEntityService<CategoryItem> {

	List<CategoryItem> getListByLineage(MerchantStoreItem store, String lineage);

	List<CategoryItem> listBySeUrl(MerchantStoreItem store, String seUrl) throws ServiceException;

	void addChild(CategoryItem parent, CategoryItem child) throws ServiceException;

	List<CategoryItem> listByParent(CategoryItem category) throws ServiceException;

	List<CategoryItem> listByStoreAndParent(MerchantStoreItem store, CategoryItem category) throws ServiceException;


	List<CategoryItem> getByName(MerchantStoreItem store, String name, LanguageItem language) throws ServiceException;

	List<CategoryItem> listByStore(MerchantStoreItem store) throws ServiceException;

	CategoryItem getByCode(MerchantStoreItem store, String code)
			throws ServiceException;

	List<CategoryItem> listByStore(MerchantStoreItem store, LanguageItem language)
			throws ServiceException;

	void saveOrUpdate(CategoryItem category) throws ServiceException;

	List<CategoryItem> getListByDepth(MerchantStoreItem store, int depth);

	/**
	 * Get root categories by store for a given language
	 *
	 * @param store
	 * @param depth
	 * @param language
	 * @return
	 */
	List<CategoryItem> getListByDepth(MerchantStoreItem store, int depth, LanguageItem language);

	/**
	 * Returns category hierarchy filter by featured
	 *
	 * @param store
	 * @param depth
	 * @param language
	 * @return
	 */
	List<CategoryItem> getListByDepthFilterByFeatured(MerchantStoreItem store, int depth, LanguageItem language);

	List<CategoryItem> getListByLineage(String storeCode, String lineage)
			throws ServiceException;

	CategoryItem getByCode(String storeCode, String code) throws ServiceException;

	CategoryItem getBySeUrl(MerchantStoreItem store, String seUrl);

	List<CategoryItem> listByParent(CategoryItem category, LanguageItem language);

	CategoryItem getOneByLanguage(UUID categoryId, LanguageItem language);

	/**
	 * Returns a list by category containing the category code and the number of products
	 * 1->obj[0] = book
	 * obj[1] = 150
	 * 2->obj[0] = novell
	 * obj[1] = 35
	 * ...
	 *
	 * @param store
	 * @param categoryIds
	 * @return
	 * @throws ServiceException
	 */
	List<Map<String, Object>> countProductsByCategories(MerchantStoreItem store,
														List<UUID> categoryIds) throws ServiceException;

	/**
	 * Returns a list of CategoryItem by category code for a given language
	 *
	 * @param store
	 * @param codes
	 * @param language
	 * @return
	 */
	List<CategoryItem> listByCodes(MerchantStoreItem store, List<String> codes,
								   LanguageItem language);

	/**
	 * List of CategoryItem by id
	 *
	 * @param store
	 * @param ids
	 * @param language
	 * @return
	 */
	List<CategoryItem> listByIds(MerchantStoreItem store, List<UUID> ids,
								 LanguageItem language);


}
