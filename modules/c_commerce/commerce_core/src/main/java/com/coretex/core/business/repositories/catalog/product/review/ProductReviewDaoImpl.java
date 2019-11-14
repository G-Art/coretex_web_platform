package com.coretex.core.business.repositories.catalog.product.review;

import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.commerce_core_model.ProductReviewItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class ProductReviewDaoImpl extends DefaultGenericDao<ProductReviewItem> implements ProductReviewDao {

	public ProductReviewDaoImpl() {
		super(ProductReviewItem.ITEM_TYPE);
	}

	@Override
	public ProductReviewItem findOne(UUID id) {
		return find(id);
	}

	@Override
	public List<ProductReviewItem> findByCustomer(UUID customerId) {
		return null;
	}

	@Override
	public List<ProductReviewItem> findByProduct(UUID productId) {
		return null;
	}

	@Override
	public List<ProductReviewItem> findByProductNoCustomers(UUID productId) {
		return null;
	}

	@Override
	public List<ProductReviewItem> findByProduct(UUID productId, UUID languageId) {
		return null;
	}

	@Override
	public ProductReviewItem findByProductAndCustomer(UUID productId, UUID customerId) {
		return null;
	}
}
