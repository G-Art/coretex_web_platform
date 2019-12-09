package com.coretex.shop.populator.order;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.business.services.catalog.product.ProductService;
import com.coretex.core.business.services.catalog.product.attribute.ProductAttributeService;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.commerce_core_model.OrderProductPriceItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.OrderProductItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.shop.model.order.PersistableOrderProduct;
import org.apache.commons.lang3.Validate;

import java.util.HashSet;
import java.util.Set;

public class PersistableOrderProductPopulator extends
		AbstractDataPopulator<PersistableOrderProduct, OrderProductItem> {

	private ProductService productService;
	private ProductAttributeService productAttributeService;


	public ProductAttributeService getProductAttributeService() {
		return productAttributeService;
	}

	public void setProductAttributeService(
			ProductAttributeService productAttributeService) {
		this.productAttributeService = productAttributeService;
	}

	/**
	 * Converts a ShoppingCartEntryItem carried in the ShoppingCartItem to an OrderProductItem
	 * that will be saved in the system
	 */
	@Override
	public OrderProductItem populate(PersistableOrderProduct source, OrderProductItem target,
									 MerchantStoreItem store, LocaleItem language) throws ConversionException {

		Validate.notNull(productService, "productService must be set");
		Validate.notNull(productAttributeService, "productAttributeService must be set");


		try {
			ProductItem modelProduct = productService.getByUUID(source.getProduct().getUuid());
			if (modelProduct == null) {
				throw new ConversionException("Cannot get product with id (productId) " + source.getProduct().getUuid());
			}

			if (!modelProduct.getMerchantStore().getUuid().equals(store.getUuid())) {
				throw new ConversionException("Invalid product id " + source.getProduct().getUuid());
			}


			target.setOneTimeCharge(source.getPrice());
			target.setProductName(source.getProduct().getDescription().getName());
			target.setProductQuantity(source.getOrderedQuantity());
			target.setSku(source.getProduct().getSku());

			OrderProductPriceItem orderProductPrice = new OrderProductPriceItem();
			orderProductPrice.setDefaultPrice(true);
			orderProductPrice.setProductPrice(source.getPrice());
			orderProductPrice.setOrderProduct(target);


			Set<OrderProductPriceItem> prices = new HashSet<OrderProductPriceItem>();
			prices.add(orderProductPrice);

			target.setPrices(prices);

		} catch (Exception e) {
			throw new ConversionException(e);
		}


		return target;
	}

	@Override
	protected OrderProductItem createTarget() {
		return null;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	public ProductService getProductService() {
		return productService;
	}


}
