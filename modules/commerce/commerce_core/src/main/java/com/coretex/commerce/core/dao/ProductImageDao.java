package com.coretex.commerce.core.dao;

import com.coretex.core.activeorm.dao.Dao;
import com.coretex.items.cx_core.ProductImageItem;

import java.util.UUID;

public interface ProductImageDao extends Dao<ProductImageItem> {


	ProductImageItem findOne(UUID id);


}