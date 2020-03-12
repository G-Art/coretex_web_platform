package com.coretex.commerce.core.dao;

import com.coretex.core.activeorm.dao.Dao;
import com.coretex.core.activeorm.services.PageableSearchResult;
import com.coretex.items.cx_core.ProductItem;
import com.coretex.items.cx_core.VariantProductItem;

import java.util.UUID;

public interface ProductDao extends Dao<ProductItem> {


	ProductItem getByCode(String code);

	PageableSearchResult<VariantProductItem> getVariants(UUID uuid, long count, long page);

	PageableSearchResult<ProductItem> getCategoryPage(String code, long count, long page);
}
