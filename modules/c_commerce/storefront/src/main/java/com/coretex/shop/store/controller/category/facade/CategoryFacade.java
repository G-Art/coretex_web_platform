package com.coretex.shop.store.controller.category.facade;

import com.coretex.items.commerce_core_model.CategoryItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.shop.model.catalog.category.PersistableCategory;
import com.coretex.shop.model.catalog.category.ReadableCategory;

import java.util.List;
import java.util.UUID;

public interface CategoryFacade {

	/**
	 * Returns a list of ReadableCategory ordered and built according to a given depth
	 *
	 * @param store
	 * @param depth
	 * @param language
	 * @return
	 * @throws Exception
	 */
	List<ReadableCategory> getCategoryHierarchy(MerchantStoreItem store, int depth, LanguageItem language, String filter);

	PersistableCategory saveCategory(MerchantStoreItem store, PersistableCategory category);

	ReadableCategory getById(MerchantStoreItem store, UUID id, LanguageItem language);

	ReadableCategory getByCode(MerchantStoreItem store, String code, LanguageItem language) throws Exception;

	void deleteCategory(UUID categoryId);

	void deleteCategory(CategoryItem category);

}
