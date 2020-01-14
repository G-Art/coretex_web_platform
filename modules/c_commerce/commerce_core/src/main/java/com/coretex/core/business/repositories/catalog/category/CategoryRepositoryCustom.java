package com.coretex.core.business.repositories.catalog.category;

import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.cx_core.CategoryItem;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface CategoryRepositoryCustom {

	List<Map<String, Object>> countProductsByCategories(MerchantStoreItem store,
														List<UUID> categoryIds);

	List<CategoryItem> listByStoreAndParent(MerchantStoreItem store, CategoryItem category);

}
