package com.coretex.shop.populator.shoppingCart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.coretex.items.commerce_core_model.ProductAttributeItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.OrderTotalItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.commerce_core_model.ShoppingCartEntryAttributeItem;
import com.coretex.items.commerce_core_model.ShoppingCartEntryItem;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.business.services.catalog.product.PricingService;
import com.coretex.core.business.services.catalog.product.attribute.ProductAttributeService;
import com.coretex.core.business.services.shoppingcart.ShoppingCartCalculationService;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.core.model.order.OrderSummary;
import com.coretex.core.model.order.OrderTotalSummary;
import com.coretex.items.commerce_core_model.ShoppingCartItem;
import com.coretex.shop.model.order.total.ReadableOrderTotal;
import com.coretex.shop.model.shoppingcart.ReadableShoppingCart;
import com.coretex.shop.model.shoppingcart.ReadableShoppingCartItem;
import com.coretex.shop.populator.catalog.ReadableProductPopulator;
import com.coretex.shop.utils.ImageFilePath;

public class ReadableShoppingCartPopulator extends AbstractDataPopulator<ShoppingCartItem, ReadableShoppingCart> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReadableShoppingCartPopulator.class);

	private PricingService pricingService;
	private ShoppingCartCalculationService shoppingCartCalculationService;
	private ProductAttributeService productAttributeService;

	private ImageFilePath imageUtils;

	@Override
	public ReadableShoppingCart populate(ShoppingCartItem source, ReadableShoppingCart target, MerchantStoreItem store,
										 LocaleItem language) throws ConversionException {
		Validate.notNull(source, "Requires ShoppingCartItem");
		Validate.notNull(language, "Requires LocaleItem not null");
		Validate.notNull(store, "Requires MerchantStoreItem not null");
		Validate.notNull(pricingService, "Requires to set pricingService");
		Validate.notNull(productAttributeService, "Requires to set productAttributeService");
		Validate.notNull(shoppingCartCalculationService, "Requires to set shoppingCartCalculationService");
		Validate.notNull(imageUtils, "Requires to set imageUtils");

		if (target == null) {
			target = new ReadableShoppingCart();
		}
		target.setCode(source.getShoppingCartCode());
		int cartQuantity = 0;

		target.setCustomer(source.getCustomerId());

		try {

			Set<ShoppingCartEntryItem> items = source.getLineItems();

			if (items != null) {

				for (ShoppingCartEntryItem item : items) {


					ReadableShoppingCartItem shoppingCartItem = new ReadableShoppingCartItem();

					ReadableProductPopulator readableProductPopulator = new ReadableProductPopulator();
					readableProductPopulator.setPricingService(pricingService);
					readableProductPopulator.setimageUtils(imageUtils);
					readableProductPopulator.populate(item.getProduct(), shoppingCartItem, store, language);


					shoppingCartItem.setPrice(item.getItemPrice());
					shoppingCartItem.setFinalPrice(pricingService.getDisplayAmount(item.getItemPrice(), store));

					shoppingCartItem.setQuantity(item.getQuantity());

					cartQuantity = cartQuantity + item.getQuantity();

					BigDecimal subTotal = pricingService.calculatePriceQuantity(item.getItemPrice(), item.getQuantity());

					//calculate sub total (price * quantity)
					shoppingCartItem.setSubTotal(subTotal);

					shoppingCartItem.setDisplaySubTotal(pricingService.getDisplayAmount(subTotal, store));


					Set<ShoppingCartEntryAttributeItem> attributes = item.getAttributes();
					if (attributes != null) {
						for (ShoppingCartEntryAttributeItem attribute : attributes) {

							ProductAttributeItem productAttribute = productAttributeService.getByUUID(attribute.getProductAttribute().getUuid());

							if (productAttribute == null) {
								LOGGER.warn("ProductItem attribute with ID " + attribute.getUuid() + " not found, skipping cart attribute " + attribute.getUuid());
								continue;
							}
						}

					}
					target.getProducts().add(shoppingCartItem);
				}
			}

			//Calculate totals using shoppingCartService
			//OrderSummary contains ShoppingCartItem items

			OrderSummary summary = new OrderSummary();
			List<ShoppingCartEntryItem> productsList = new ArrayList<ShoppingCartEntryItem>();
			productsList.addAll(source.getLineItems());
			summary.setProducts(productsList);

			//OrdetTotalSummary contains all calculations

			OrderTotalSummary orderSummary = shoppingCartCalculationService.calculate(source, store, language);

			if (CollectionUtils.isNotEmpty(orderSummary.getTotals())) {
				List<ReadableOrderTotal> totals = new ArrayList<ReadableOrderTotal>();
				for (OrderTotalItem t : orderSummary.getTotals()) {
					ReadableOrderTotal total = new ReadableOrderTotal();
					total.setCode(t.getOrderTotalCode());
					total.setValue(t.getValue());
					totals.add(total);
				}
				target.setTotals(totals);
			}

			target.setSubtotal(orderSummary.getSubTotal());
			target.setDisplaySubTotal(pricingService.getDisplayAmount(orderSummary.getSubTotal(), store));


			target.setTotal(orderSummary.getTotal());
			target.setDisplayTotal(pricingService.getDisplayAmount(orderSummary.getTotal(), store));


			target.setQuantity(cartQuantity);
			target.setUuid(source.getUuid());


		} catch (Exception e) {
			throw new ConversionException(e);
		}

		return target;


	}

	@Override
	protected ReadableShoppingCart createTarget() {
		return null;
	}

	public PricingService getPricingService() {
		return pricingService;
	}

	public void setPricingService(PricingService pricingService) {
		this.pricingService = pricingService;
	}

	public ShoppingCartCalculationService getShoppingCartCalculationService() {
		return shoppingCartCalculationService;
	}

	public void setShoppingCartCalculationService(ShoppingCartCalculationService shoppingCartCalculationService) {
		this.shoppingCartCalculationService = shoppingCartCalculationService;
	}

	public ImageFilePath getImageUtils() {
		return imageUtils;
	}

	public void setImageUtils(ImageFilePath imageUtils) {
		this.imageUtils = imageUtils;
	}

	public ProductAttributeService getProductAttributeService() {
		return productAttributeService;
	}

	public void setProductAttributeService(ProductAttributeService productAttributeService) {
		this.productAttributeService = productAttributeService;
	}

}
