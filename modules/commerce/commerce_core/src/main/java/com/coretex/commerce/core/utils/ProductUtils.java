package com.coretex.commerce.core.utils;

import com.coretex.items.cx_core.ProductAvailabilityItem;
import com.coretex.items.cx_core.ProductItem;
import com.coretex.items.cx_core.ProductPriceItem;
import com.coretex.items.cx_core.StoreItem;
import com.coretex.items.cx_core.VariantProductItem;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;

public class ProductUtils {

	public final static String SLASH = "/";
	public final static String SMALL_IMAGE = "SMALL";
	public final static String LARGE_IMAGE = "LARGE";
	public final static String PRODUCTS_URI = "/products";

	public static String buildProductSmallImageUtils(StoreItem store, String sku, String imageName) {
		return buildProductImageUtils(store, sku, imageName, SMALL_IMAGE);
	}

	public static String buildProductLargeImageUtils(StoreItem store, String sku, String imageName) {
		return buildProductImageUtils(store, sku, imageName, LARGE_IMAGE);
	}

	public static String buildProductImageUtils(StoreItem store, String sku, String imageName, String size) {
		return "/static" + PRODUCTS_URI + SLASH + store.getCode() + SLASH +
				sku + SLASH + size + SLASH + imageName;
	}

	public static ProductPriceItem getDefaultPrice(ProductItem productItem){
		return getAvailabilities(productItem)
				.stream()
				.filter(productAvailabilityItem -> productAvailabilityItem.getPrices()
						.stream()
						.anyMatch(ProductPriceItem::getDefaultPrice))
				.findFirst()
				.flatMap(productAvailabilityItem -> productAvailabilityItem.getPrices()
						.stream()
						.filter(ProductPriceItem::getDefaultPrice)
						.findFirst())
				.orElse(null);
	}

	public static Collection<ProductAvailabilityItem> getAvailabilities(ProductItem source) {
		if (CollectionUtils.isEmpty(source.getAvailabilities())) {
			if (source instanceof VariantProductItem) {
				return getAvailabilities(((VariantProductItem) source).getBaseProduct());
			} else {
				return Sets.newHashSet();
			}
		} else {
			return source.getAvailabilities();
		}
	}
}
