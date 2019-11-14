package com.coretex.shop.populator.catalog;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.business.services.catalog.product.ProductService;
import com.coretex.core.business.services.customer.CustomerService;
import com.coretex.core.business.services.reference.language.LanguageService;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.items.commerce_core_model.CustomerItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.commerce_core_model.ProductReviewItem;
import com.coretex.shop.model.catalog.product.PersistableProductReview;
import com.coretex.shop.utils.DateUtil;
import org.apache.commons.lang3.Validate;

import java.util.Date;


public class PersistableProductReviewPopulator extends
		AbstractDataPopulator<PersistableProductReview, ProductReviewItem> {


	private CustomerService customerService;


	private ProductService productService;


	private LanguageService languageService;


	public LanguageService getLanguageService() {
		return languageService;
	}

	public void setLanguageService(LanguageService languageService) {
		this.languageService = languageService;
	}

	@Override
	public ProductReviewItem populate(PersistableProductReview source,
									  ProductReviewItem target, MerchantStoreItem store, LanguageItem language)
			throws ConversionException {


		Validate.notNull(customerService, "customerService cannot be null");
		Validate.notNull(productService, "productService cannot be null");
		Validate.notNull(languageService, "languageService cannot be null");
		Validate.notNull(source.getRating(), "Rating cannot bot be null");

		try {

			if (target == null) {
				target = new ProductReviewItem();
			}

			CustomerItem customer = customerService.getById(source.getCustomerId());

			//check if customer belongs to store
			if (customer == null || !customer.getMerchantStore().getUuid().equals(store.getUuid())) {
				throw new ConversionException("Invalid customer id for the given store");
			}

			if (source.getDate() == null) {
				String date = DateUtil.formatDate(new Date());
				source.setDate(date);
			}
			target.setReviewDate(DateUtil.getDate(source.getDate()));
			target.setCustomer(customer);
			target.setReviewRating(source.getRating());

			ProductItem product = productService.getById(source.getProductId());

			//check if product belongs to store
			if (product == null || !product.getMerchantStore().getUuid().equals(store.getUuid())) {
				throw new ConversionException("Invalid product id for the given store");
			}

			target.setProduct(product);

			LanguageItem lang = languageService.getByCode(language.getCode());
			if (lang == null) {
				throw new ConversionException("Invalid language code, use iso codes (en, fr ...)");
			}

			target.setName("-");
			return target;

		} catch (Exception e) {
			throw new ConversionException("Cannot populate ProductReviewItem", e);
		}

	}

	@Override
	protected ProductReviewItem createTarget() {
		return null;
	}

	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	public ProductService getProductService() {
		return productService;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}


}
