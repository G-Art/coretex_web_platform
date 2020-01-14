package com.coretex.commerce.core.dao.impl;

import com.coretex.commerce.core.dao.ProductDao;
import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.cx_core.ProductItem;
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
}
