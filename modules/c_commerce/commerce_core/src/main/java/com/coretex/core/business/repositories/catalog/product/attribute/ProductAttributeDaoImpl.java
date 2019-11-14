package com.coretex.core.business.repositories.catalog.product.attribute;

import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.commerce_core_model.ProductAttributeItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class ProductAttributeDaoImpl extends DefaultGenericDao<ProductAttributeItem> implements ProductAttributeDao {

	public ProductAttributeDaoImpl() {
		super(ProductAttributeItem.ITEM_TYPE);
	}

	@Override
	public ProductAttributeItem findOne(UUID id) {
		return null;
	}

	@Override
	public List<ProductAttributeItem> findByOptionId(UUID storeId, UUID id) {
		return null;
	}

	@Override
	public List<ProductAttributeItem> findByOptionValueId(UUID storeId, UUID id) {
		return null;
	}

	@Override
	public List<ProductAttributeItem> findByAttributeIds(UUID storeId, UUID productId, List<UUID> ids) {
		return null;
	}

	@Override
	public List<ProductAttributeItem> findByProductId(UUID storeId, UUID productId, UUID languageId) {
		return null;
	}
}
