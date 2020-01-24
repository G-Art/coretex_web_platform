package com.coretex.commerce.core.dao;

import com.coretex.core.activeorm.dao.Dao;
import com.coretex.core.activeorm.services.PageableSearchResult;
import com.coretex.items.cx_core.ProductItem;

public interface ProductDao extends Dao<ProductItem> {
	ProductItem getByCode(String code);

	PageableSearchResult<ProductItem> getCategoryPage(String code, long count, long page);
}
