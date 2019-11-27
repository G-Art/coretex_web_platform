package com.coretex.shop.populator.catalog;

import com.coretex.items.commerce_core_model.ProductImageItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.core.LocaleItem;
import org.apache.commons.lang3.Validate;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.shop.model.catalog.product.PersistableImage;

public class PersistableProductImagePopulator extends AbstractDataPopulator<PersistableImage, ProductImageItem> {


	private ProductItem product;

	@Override
	public ProductImageItem populate(PersistableImage source, ProductImageItem target, MerchantStoreItem store, LocaleItem language)
			throws ConversionException {

		Validate.notNull(product, "Must set a product setProduct(ProductItem)");
		Validate.notNull(product.getUuid(), "ProductItem must have an id not null");
		Validate.notNull(source.getContentType(), "ContentItem type must be set on persistable image");


		target.setDefaultImage(source.isDefaultImage());
		target.setImageType(source.getImageType());
		target.setProductImage(source.getName());
		if (source.getImageUrl() != null) {
			target.setProductImageUrl(source.getImageUrl());
		}
		target.setProduct(product);

		return target;
	}

	@Override
	protected ProductImageItem createTarget() {
		// TODO Auto-generated method stub
		return null;
	}

	public ProductItem getProduct() {
		return product;
	}

	public void setProduct(ProductItem product) {
		this.product = product;
	}

}
