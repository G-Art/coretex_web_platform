package com.coretex.core.business.repositories.catalog.product.type;

import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.commerce_core_model.ProductTypeItem;
import org.springframework.stereotype.Component;

@Component
public class ProductTypeDaoImpl extends DefaultGenericDao<ProductTypeItem> implements ProductTypeDao {

	public ProductTypeDaoImpl() {
		super(ProductTypeItem.ITEM_TYPE);
	}

	@Override
	public ProductTypeItem findByCode(String code) {
		return null;
	}
}
