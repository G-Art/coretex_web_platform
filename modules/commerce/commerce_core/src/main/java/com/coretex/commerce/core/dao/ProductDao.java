package com.coretex.commerce.core.dao;

import com.coretex.core.activeorm.dao.Dao;
import com.coretex.items.cx_core.ProductItem;

public interface ProductDao extends Dao<ProductItem> {
	ProductItem getByCode(String code);
}
