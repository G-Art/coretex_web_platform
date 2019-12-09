package com.coretex.shop.populator.order;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.business.services.catalog.product.PricingService;
import com.coretex.core.business.services.catalog.product.ProductService;
import com.coretex.core.business.services.catalog.product.attribute.ProductAttributeService;
import com.coretex.core.model.catalog.product.price.FinalPrice;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.OrderProductItem;
import com.coretex.items.commerce_core_model.OrderProductPriceItem;
import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.commerce_core_model.ProductPriceItem;
import com.coretex.items.commerce_core_model.ShoppingCartEntryItem;
import com.coretex.items.core.LocaleItem;
import org.apache.commons.lang3.Validate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OrderProductPopulator extends
		AbstractDataPopulator<ShoppingCartEntryItem, OrderProductItem> {

	private ProductService productService;
	private ProductAttributeService productAttributeService;
	private PricingService pricingService;


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
	public OrderProductItem populate(ShoppingCartEntryItem source, OrderProductItem target,
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

			target.setOneTimeCharge(source.getItemPrice());
			target.setProductName(source.getProduct().getName());
			target.setProductQuantity(source.getQuantity());
			target.setSku(source.getProduct().getSku());

			FinalPrice finalPrice = pricingService.calculateProductPrice(modelProduct);
			if (finalPrice == null) {
				throw new ConversionException("Object final price not populated in shoppingCartItem (source)");
			}
			//Default price
			OrderProductPriceItem orderProductPrice = orderProductPrice(finalPrice);
			orderProductPrice.setOrderProduct(target);

			Set<OrderProductPriceItem> prices = new HashSet<OrderProductPriceItem>();
			prices.add(orderProductPrice);

			//Other prices
			List<FinalPrice> otherPrices = finalPrice.getAdditionalPrices();
			if (otherPrices != null) {
				for (FinalPrice otherPrice : otherPrices) {
					OrderProductPriceItem other = orderProductPrice(otherPrice);
					other.setOrderProduct(target);
					prices.add(other);
				}
			}

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

	private OrderProductPriceItem orderProductPrice(FinalPrice price) {

		OrderProductPriceItem orderProductPrice = new OrderProductPriceItem();

		ProductPriceItem productPrice = price.getProductPrice();

		orderProductPrice.setDefaultPrice(productPrice.getDefaultPrice());

		orderProductPrice.setProductPrice(price.getFinalPrice());
		orderProductPrice.setProductPriceCode(productPrice.getCode());
		if (productPrice != null) {
			orderProductPrice.setProductPriceName(productPrice.getName());
		}
		if (price.isDiscounted()) {
			orderProductPrice.setProductPriceSpecial(productPrice.getProductPriceSpecialAmount());
			orderProductPrice.setProductPriceSpecialStartDate(productPrice.getProductPriceSpecialStartDate());
			orderProductPrice.setProductPriceSpecialEndDate(productPrice.getProductPriceSpecialEndDate());
		}

		return orderProductPrice;
	}

	public PricingService getPricingService() {
		return pricingService;
	}

	public void setPricingService(PricingService pricingService) {
		this.pricingService = pricingService;
	}
}
