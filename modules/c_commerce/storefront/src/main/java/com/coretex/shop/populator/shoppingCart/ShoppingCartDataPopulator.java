
package com.coretex.shop.populator.shoppingCart;


import com.coretex.core.business.services.catalog.product.PricingService;
import com.coretex.core.business.services.reference.language.LanguageService;
import com.coretex.core.business.services.shoppingcart.ShoppingCartCalculationService;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.items.commerce_core_model.ProductImageItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.core.model.order.OrderSummary;
import com.coretex.items.commerce_core_model.OrderTotalItem;
import com.coretex.core.model.order.OrderTotalSummary;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.commerce_core_model.ShoppingCartItem;
import com.coretex.items.commerce_core_model.ShoppingCartEntryItem;
import com.coretex.shop.model.order.total.OrderTotal;
import com.coretex.shop.model.shoppingcart.ShoppingCartAttribute;
import com.coretex.shop.model.shoppingcart.ShoppingCartData;
import com.coretex.shop.utils.ImageFilePath;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class ShoppingCartDataPopulator extends AbstractDataPopulator<ShoppingCartItem, ShoppingCartData> {

	private static final Logger LOG = LoggerFactory.getLogger(ShoppingCartDataPopulator.class);

	private PricingService pricingService;
	private LanguageService languageService;

	private ShoppingCartCalculationService shoppingCartCalculationService;

	private ImageFilePath imageUtils;

	public ImageFilePath getimageUtils() {
		return imageUtils;
	}


	public void setimageUtils(ImageFilePath imageUtils) {
		this.imageUtils = imageUtils;
	}

	public void setLanguageService(LanguageService languageService) {
		this.languageService = languageService;
	}

	@Override
	public ShoppingCartData createTarget() {
		return new ShoppingCartData();
	}


	public ShoppingCartCalculationService getOrderService() {
		return shoppingCartCalculationService;
	}


	public PricingService getPricingService() {
		return pricingService;
	}


	@Override
	public ShoppingCartData populate(final ShoppingCartItem shoppingCart,
									 final ShoppingCartData cart, final MerchantStoreItem store, final LocaleItem language) {

		Validate.notNull(shoppingCart, "Requires ShoppingCartItem");
		Validate.notNull(language, "Requires LocaleItem not null");
		int cartQuantity = 0;
		cart.setCode(shoppingCart.getShoppingCartCode());
		Set<ShoppingCartEntryItem> items = shoppingCart.getLineItems();
		List<com.coretex.shop.model.shoppingcart.ShoppingCartItem> shoppingCartItemsList = Collections.emptyList();

		var locale = languageService.toLocale(language, store);

		if(Objects.nonNull(shoppingCart.getSettlement())){
			var settlement = shoppingCart.getSettlement();
			cart.setCityName(settlement.getName(locale));
			cart.setCityCode(settlement.getCode());
		}

		if (Objects.nonNull(shoppingCart.getDeliveryType())) {
			cart.setDeliveryMethod(shoppingCart.getDeliveryType().getCode());
		}

		cart.setEmail(shoppingCart.getEmail());
		cart.setUserName(shoppingCart.getUserName());
		cart.setPhone(shoppingCart.getPhone());

		if (items != null) {
			shoppingCartItemsList = new ArrayList<>();
			for (ShoppingCartEntryItem item : items) {

				com.coretex.shop.model.shoppingcart.ShoppingCartItem shoppingCartItem = new com.coretex.shop.model.shoppingcart.ShoppingCartItem();
				shoppingCartItem.setCode(cart.getCode());
				shoppingCartItem.setProductCode(item.getProduct().getCode());
				shoppingCartItem.setProductVirtual(item.getProductVirtual() != null ? item.getProductVirtual() : false);

				shoppingCartItem.setProductId(item.getProduct().getUuid());
				shoppingCartItem.setUuid(item.getUuid());

				String itemName = item.getProduct().getName();

				shoppingCartItem.setName(itemName);

				shoppingCartItem.setPrice(pricingService.getDisplayAmount(item.getItemPrice(), store));
				shoppingCartItem.setQuantity(item.getQuantity() != null ? item.getQuantity() : 1 );

				cartQuantity = cartQuantity + (item.getQuantity() != null ? item.getQuantity() : 1) ;

				shoppingCartItem.setProductPrice(item.getItemPrice());
				shoppingCartItem.setSubTotal(pricingService.getDisplayAmount(item.getSubTotal(), store));
				Optional<ProductImageItem> image = item.getProduct().getImages().stream().filter(ProductImageItem::getDefaultImage).findAny();
				if (image.isPresent() && imageUtils != null) {
					String imagePath = imageUtils.buildProductImageUtils(store, item.getProduct().getCode(), image.get().getProductImage());
					shoppingCartItem.setImage(imagePath);
				}
				shoppingCartItemsList.add(shoppingCartItem);
			}
		}
		if (CollectionUtils.isNotEmpty(shoppingCartItemsList)) {
			cart.setShoppingCartItems(shoppingCartItemsList);
		}

		OrderSummary summary = new OrderSummary();
		List<ShoppingCartEntryItem> productsList = new ArrayList<ShoppingCartEntryItem>();
		productsList.addAll(shoppingCart.getLineItems());
		summary.setProducts(productsList);
		OrderTotalSummary orderSummary = shoppingCartCalculationService.calculate(shoppingCart, store, language);

		if (CollectionUtils.isNotEmpty(orderSummary.getTotals())) {
			List<OrderTotal> totals = new ArrayList<OrderTotal>();
			for (OrderTotalItem t : orderSummary.getTotals()) {
				OrderTotal total = new OrderTotal();
				total.setCode(t.getOrderTotalCode());
				total.setValue(t.getValue());
				totals.add(total);
			}
			cart.setTotals(totals);
		}

		cart.setSubTotal(pricingService.getDisplayAmount(orderSummary.getSubTotal(), store));
		cart.setTotal(pricingService.getDisplayAmount(orderSummary.getTotal(), store));
		cart.setQuantity(cartQuantity);
		cart.setUuid(shoppingCart.getUuid());
		return cart;


	}


	public void setPricingService(final PricingService pricingService) {
		this.pricingService = pricingService;
	}


	public void setShoppingCartCalculationService(final ShoppingCartCalculationService shoppingCartCalculationService) {
		this.shoppingCartCalculationService = shoppingCartCalculationService;
	}


}
