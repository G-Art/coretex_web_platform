package com.coretex.shop.populator.order;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.business.services.catalog.product.PricingService;
import com.coretex.core.business.services.catalog.product.ProductService;
import com.coretex.core.business.services.catalog.product.attribute.ProductAttributeService;
import com.coretex.core.business.services.catalog.product.file.DigitalProductService;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.commerce_core_model.ProductAttributeItem;
import com.coretex.items.commerce_core_model.DigitalProductItem;
import com.coretex.core.model.catalog.product.price.FinalPrice;
import com.coretex.items.commerce_core_model.OrderProductPriceItem;
import com.coretex.items.commerce_core_model.ProductPriceItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.OrderProductDownloadItem;
import com.coretex.items.commerce_core_model.OrderProductItem;
import com.coretex.items.commerce_core_model.OrderProductAttributeItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.commerce_core_model.ShoppingCartEntryAttributeItem;
import com.coretex.items.commerce_core_model.ShoppingCartEntryItem;
import com.coretex.shop.constants.ApplicationConstants;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.Validate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OrderProductPopulator extends
		AbstractDataPopulator<ShoppingCartEntryItem, OrderProductItem> {

	private ProductService productService;
	private DigitalProductService digitalProductService;
	private ProductAttributeService productAttributeService;
	private PricingService pricingService;


	public ProductAttributeService getProductAttributeService() {
		return productAttributeService;
	}

	public void setProductAttributeService(
			ProductAttributeService productAttributeService) {
		this.productAttributeService = productAttributeService;
	}

	public DigitalProductService getDigitalProductService() {
		return digitalProductService;
	}

	public void setDigitalProductService(DigitalProductService digitalProductService) {
		this.digitalProductService = digitalProductService;
	}

	/**
	 * Converts a ShoppingCartEntryItem carried in the ShoppingCartItem to an OrderProductItem
	 * that will be saved in the system
	 */
	@Override
	public OrderProductItem populate(ShoppingCartEntryItem source, OrderProductItem target,
									 MerchantStoreItem store, LocaleItem language) throws ConversionException {

		Validate.notNull(productService, "productService must be set");
		Validate.notNull(digitalProductService, "digitalProductService must be set");
		Validate.notNull(productAttributeService, "productAttributeService must be set");


		try {
			ProductItem modelProduct = productService.getByUUID(source.getProduct().getUuid());
			if (modelProduct == null) {
				throw new ConversionException("Cannot get product with id (productId) " + source.getProduct().getUuid());
			}

			if (!modelProduct.getMerchantStore().getUuid().equals(store.getUuid())) {
				throw new ConversionException("Invalid product id " + source.getProduct().getUuid());
			}

			DigitalProductItem digitalProduct = digitalProductService.getByProduct(store, modelProduct);

			if (digitalProduct != null) {
				OrderProductDownloadItem orderProductDownload = new OrderProductDownloadItem();
				orderProductDownload.setOrderProductFilename(digitalProduct.getProductFileName());
				orderProductDownload.setOrderProduct(target);
				orderProductDownload.setDownloadCount(0);
				orderProductDownload.setMaxdays(ApplicationConstants.MAX_DOWNLOAD_DAYS);
				target.getDownloads().add(orderProductDownload);
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

			//OrderProductAttributeItem
			Set<ShoppingCartEntryAttributeItem> attributeItems = source.getAttributes();
			if (!CollectionUtils.isEmpty(attributeItems)) {
				Set<OrderProductAttributeItem> attributes = new HashSet<OrderProductAttributeItem>();
				for (ShoppingCartEntryAttributeItem attribute : attributeItems) {
					OrderProductAttributeItem orderProductAttribute = new OrderProductAttributeItem();
					orderProductAttribute.setOrderProduct(target);
					ProductAttributeItem attr = attribute.getProductAttribute();
					if (attr == null) {
						throw new ConversionException("Attribute does not exists");
					}

					if (!attr.getProduct().getMerchantStore().getUuid().equals(store.getUuid())) {
						throw new ConversionException("Attribute invalid for this store");
					}

					orderProductAttribute.setProductAttributeIsFree(attr.getProductAttributeIsFree());
					orderProductAttribute.setProductAttributePrice(attr.getProductAttributePrice());
					orderProductAttribute.setProductAttributeWeight(attr.getProductAttributeWeight());
					attributes.add(orderProductAttribute);
				}
				target.setOrderAttributes(attributes);
			}


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
