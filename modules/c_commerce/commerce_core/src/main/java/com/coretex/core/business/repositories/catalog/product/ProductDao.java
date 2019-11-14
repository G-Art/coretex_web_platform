package com.coretex.core.business.repositories.catalog.product;

import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.core.activeorm.dao.Dao;


public interface ProductDao extends Dao<ProductItem>, ProductRepositoryCustom {

}
