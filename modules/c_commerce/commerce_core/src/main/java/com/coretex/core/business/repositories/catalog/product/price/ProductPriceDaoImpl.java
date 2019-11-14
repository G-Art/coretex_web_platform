package com.coretex.core.business.repositories.catalog.product.price;

import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.commerce_core_model.ProductPriceItem;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProductPriceDaoImpl extends DefaultGenericDao<ProductPriceItem> implements ProductPriceDao {
	public ProductPriceDaoImpl() {
		super(ProductPriceItem.ITEM_TYPE);
	}

	@Override
	public ProductPriceItem findOne(UUID id) {
		return find(id);
	}
}
