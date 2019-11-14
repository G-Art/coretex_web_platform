package com.coretex.core.business.repositories.catalog.category;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.coretex.items.commerce_core_model.CategoryItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;

public interface CategoryRepositoryCustom {

	List<Map<String, Object>> countProductsByCategories(MerchantStoreItem store,
														List<UUID> categoryIds);

	List<CategoryItem> listByStoreAndParent(MerchantStoreItem store, CategoryItem category);

}
