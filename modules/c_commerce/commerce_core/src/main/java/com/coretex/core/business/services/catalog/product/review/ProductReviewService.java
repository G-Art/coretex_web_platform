package com.coretex.core.business.services.catalog.product.review;

import java.util.List;
import java.util.UUID;

import com.coretex.core.business.services.common.generic.SalesManagerEntityService;
import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.commerce_core_model.ProductReviewItem;
import com.coretex.items.commerce_core_model.CustomerItem;
import com.coretex.items.commerce_core_model.LanguageItem;

public interface ProductReviewService extends
		SalesManagerEntityService<ProductReviewItem> {


	List<ProductReviewItem> getByCustomer(CustomerItem customer);

	List<ProductReviewItem> getByProduct(ProductItem product);

	List<ProductReviewItem> getByProduct(ProductItem product, LanguageItem language);

	ProductReviewItem getByProductAndCustomer(UUID productId, UUID customerId);

	/**
	 * @param product
	 * @return
	 */
	List<ProductReviewItem> getByProductNoCustomers(ProductItem product);


}
