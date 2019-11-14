package com.coretex.core.business.repositories.catalog.product.image;

import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.commerce_core_model.ProductImageItem;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProductImageDaoImpl extends DefaultGenericDao<ProductImageItem> implements ProductImageDao {
	public ProductImageDaoImpl() {
		super(ProductImageItem.ITEM_TYPE);
	}

	@Override
	public ProductImageItem findOne(UUID id) {
		return find(id);
	}
}
