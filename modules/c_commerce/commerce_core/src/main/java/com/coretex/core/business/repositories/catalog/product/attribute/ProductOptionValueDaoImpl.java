package com.coretex.core.business.repositories.catalog.product.attribute;

import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.commerce_core_model.ProductOptionValueItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class ProductOptionValueDaoImpl extends DefaultGenericDao<ProductOptionValueItem> implements ProductOptionValueDao {

	public ProductOptionValueDaoImpl() {
		super(ProductOptionValueItem.ITEM_TYPE);
	}

	@Override
	public ProductOptionValueItem findOne(UUID id) {
		return null;
	}

	@Override
	public ProductOptionValueItem findOne(UUID storeId, UUID id) {
		return null;
	}

	@Override
	public List<ProductOptionValueItem> findByStoreId(UUID storeId, UUID languageId) {
		return null;
	}

	@Override
	public ProductOptionValueItem findByCode(UUID storeId, String optionValueCode) {
		return null;
	}

	@Override
	public List<ProductOptionValueItem> findByName(UUID storeId, String name, UUID languageId) {
		return null;
	}

	@Override
	public List<ProductOptionValueItem> findByReadOnly(UUID storeId, UUID languageId, boolean readOnly) {
		return null;
	}
}
