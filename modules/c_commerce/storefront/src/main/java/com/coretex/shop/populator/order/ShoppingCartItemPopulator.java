package com.coretex.shop.populator.order;

import com.coretex.items.commerce_core_model.ShoppingCartEntryItem;
import org.apache.commons.lang3.Validate;

import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.commerce_core_model.ProductAttributeItem;
import com.coretex.core.business.services.catalog.product.ProductService;
import com.coretex.core.business.services.catalog.product.attribute.ProductAttributeService;
import com.coretex.core.business.exception.ConversionException;

import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.core.business.services.shoppingcart.ShoppingCartService;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.shop.model.order.PersistableOrderProduct;

public class ShoppingCartItemPopulator extends
		AbstractDataPopulator<PersistableOrderProduct, ShoppingCartEntryItem> {


	private ProductService productService;
	private ProductAttributeService productAttributeService;
	private ShoppingCartService shoppingCartService;

	@Override
	public ShoppingCartEntryItem populate(PersistableOrderProduct source,
										  ShoppingCartEntryItem target, MerchantStoreItem store, LocaleItem language)
			throws ConversionException {
		Validate.notNull(productService, "Requires to set productService");
		Validate.notNull(productAttributeService, "Requires to set productAttributeService");
		Validate.notNull(shoppingCartService, "Requires to set shoppingCartService");

		ProductItem product = productService.getByUUID(source.getProduct().getUuid());
		if (source.getAttributes() != null) {

			for (com.coretex.shop.model.catalog.product.attribute.ProductAttribute attr : source.getAttributes()) {
				ProductAttributeItem attribute = productAttributeService.getByUUID(attr.getUuid());
				if (attribute == null) {
					throw new ConversionException("ProductAttributeItem with id " + attr.getUuid() + " is null");
				}
				if (!attribute.getProduct().getUuid().equals(source.getProduct().getUuid())) {
					throw new ConversionException("ProductAttributeItem with id " + attr.getUuid() + " is not assigned to ProductItem id " + source.getProduct().getUuid());
				}
				product.getAttributes().add(attribute);
			}
		}

		return shoppingCartService.populateShoppingCartItem(product);

	}

	@Override
	protected ShoppingCartEntryItem createTarget() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setProductAttributeService(ProductAttributeService productAttributeService) {
		this.productAttributeService = productAttributeService;
	}

	public ProductAttributeService getProductAttributeService() {
		return productAttributeService;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	public ProductService getProductService() {
		return productService;
	}

	public void setShoppingCartService(ShoppingCartService shoppingCartService) {
		this.shoppingCartService = shoppingCartService;
	}

	public ShoppingCartService getShoppingCartService() {
		return shoppingCartService;
	}

}
