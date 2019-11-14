package com.coretex.core.business.repositories.catalog.product.file;

import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.commerce_core_model.DigitalProductItem;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DigitalProductDaoImpl extends DefaultGenericDao<DigitalProductItem> implements DigitalProductDao {
	public DigitalProductDaoImpl() {
		super(DigitalProductItem.ITEM_TYPE);
	}

	@Override
	public DigitalProductItem findByProduct(UUID storeId, UUID productId) {
		return null;
	}

	@Override
	public DigitalProductItem findOne(UUID id) {
		return null;
	}
}
