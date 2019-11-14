package com.coretex.core.business.repositories.catalog.product.type;

import com.coretex.items.commerce_core_model.ProductTypeItem;
import com.coretex.core.activeorm.dao.Dao;

public interface ProductTypeDao extends Dao<ProductTypeItem> {

	ProductTypeItem findByCode(String code);
}
