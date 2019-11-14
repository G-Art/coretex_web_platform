package com.coretex.core.business.repositories.catalog.product.attribute;

import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.commerce_core_model.ProductOptionItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class ProductOptionDaoImpl extends DefaultGenericDao<ProductOptionItem> implements ProductOptionDao {

	public ProductOptionDaoImpl() {
		super(ProductOptionItem.ITEM_TYPE);
	}

	@Override
	public ProductOptionItem findOne(UUID id) {
		return null;
	}

	@Override
	public ProductOptionItem findOne(UUID storeId, UUID id) {
		return null;
	}

	@Override
	public List<ProductOptionItem> findByStoreId(UUID storeId, UUID languageId) {
		return null;
	}

	@Override
	public List<ProductOptionItem> findByName(UUID storeId, String name, UUID languageId) {
		return null;
	}

	@Override
	public ProductOptionItem findByCode(UUID storeId, String optionCode) {
		return null;
	}

	@Override
	public List<ProductOptionItem> findByReadOnly(UUID storeId, UUID languageId, boolean readOnly) {
		return null;
	}
}
