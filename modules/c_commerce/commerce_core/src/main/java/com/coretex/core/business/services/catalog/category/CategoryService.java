package com.coretex.core.business.services.catalog.category;


import com.coretex.core.business.services.common.generic.SalesManagerEntityService;
import com.coretex.items.cx_core.CategoryItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

public interface CategoryService extends SalesManagerEntityService<CategoryItem> {

	List<CategoryItem> getListByLineage(MerchantStoreItem store, String lineage);

	List<CategoryItem> listBySeUrl(MerchantStoreItem store, String seUrl) ;

	void addChild(CategoryItem parent, CategoryItem child) ;

	List<CategoryItem> listByStoreAndParent(MerchantStoreItem store, CategoryItem category) ;


	List<CategoryItem> getByName(MerchantStoreItem store, String name, LocaleItem language) ;

	List<CategoryItem> listByStore(MerchantStoreItem store) ;

	CategoryItem getByCode(MerchantStoreItem store, String code)
			;

	List<CategoryItem> listByStore(MerchantStoreItem store, LocaleItem language)
			;

	void saveOrUpdate(CategoryItem category) ;

	List<CategoryItem> getListByDepth(MerchantStoreItem store, int depth);

	/**
	 * Get root categories by store for a given language
	 *
	 * @param store
	 * @param depth
	 * @param language
	 * @return
	 */
	List<CategoryItem> getListByDepth(MerchantStoreItem store, int depth, LocaleItem language);

	/**
	 * Returns category hierarchy filter by featured
	 *
	 * @param store
	 * @param depth
	 * @param language
	 * @return
	 */
	List<CategoryItem> getListByDepthFilterByFeatured(MerchantStoreItem store, int depth, LocaleItem language);

	List<CategoryItem> getListByLineage(String storeCode, String lineage)
			;

	CategoryItem getByCode(String storeCode, String code) ;

	CategoryItem getBySeUrl(MerchantStoreItem store, String seUrl);

	Stream<CategoryItem> listByParent(CategoryItem category);

	Stream<CategoryItem> listByRoot();

	Stream<CategoryItem> listByParent(UUID categoryUuid);

	CategoryItem getOneByLanguage(UUID categoryId, LocaleItem language);

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
	 * @
	 */
	List<Map<String, Object>> countProductsByCategories(MerchantStoreItem store,
														List<UUID> categoryIds) ;

	/**
	 * Returns a list of CategoryItem by category code for a given language
	 *
	 * @param store
	 * @param codes
	 * @param language
	 * @return
	 */
	List<CategoryItem> listByCodes(MerchantStoreItem store, List<String> codes,
								   LocaleItem language);

	/**
	 * List of CategoryItem by id
	 *
	 * @param store
	 * @param ids
	 * @param language
	 * @return
	 */
	List<CategoryItem> listByIds(MerchantStoreItem store, List<UUID> ids,
								 LocaleItem language);


}
