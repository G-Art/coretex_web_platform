package com.coretex.commerce.core.dao.impl;

import com.coretex.commerce.core.dao.ProductImageDao;
import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.cx_core.ProductImageItem;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DefaultProductImageDao extends DefaultGenericDao<ProductImageItem> implements ProductImageDao {

	public DefaultProductImageDao() {
		super(ProductImageItem.ITEM_TYPE);
	}

	@Override
	public ProductImageItem findOne(UUID id) {
		return find(id);
	}
}
