package com.coretex.commerce.core.dao.impl;

import com.coretex.commerce.core.dao.ProductDao;
import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.core.activeorm.services.PageableSearchResult;
import com.coretex.items.cx_core.CategoryItem;
import com.coretex.items.cx_core.ProductItem;
import com.coretex.relations.cx_core.CategoryProductRelation;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DefaultProductDao extends DefaultGenericDao<ProductItem> implements ProductDao {

	public DefaultProductDao() {
		super(ProductItem.ITEM_TYPE);
	}

	@Override
	public ProductItem getByCode(String code) {
		return findSingle(Map.of(ProductItem.CODE, code), true);
	}

	@Override
	public PageableSearchResult<ProductItem> getCategoryPage(String code, long count, long page) {
		return getSearchService().searchPageable(
				"SELECT p.* FROM #" + ProductItem.ITEM_TYPE + " AS p " +
						"JOIN " + CategoryProductRelation.ITEM_TYPE + " AS cpr ON cpr.target = p.uuid " +
						"JOIN " + CategoryItem.ITEM_TYPE + " AS c ON cpr.source = c.uuid " +
						"WHERE c.code = :code ORDER BY p." + ProductItem.CREATE_DATE,
				Map.of("code", code),
				count, page);
	}
}
