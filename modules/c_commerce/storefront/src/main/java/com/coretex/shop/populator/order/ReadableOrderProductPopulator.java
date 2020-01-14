package com.coretex.shop.populator.order;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.business.services.catalog.product.PricingService;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.OrderProductItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.shop.model.order.ReadableOrderProduct;
import com.coretex.shop.utils.ImageFilePath;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.math.BigDecimal;

public class ReadableOrderProductPopulator extends
		AbstractDataPopulator<OrderProductItem, ReadableOrderProduct> {

	private PricingService pricingService;
	private ImageFilePath imageUtils;


	public ImageFilePath getimageUtils() {
		return imageUtils;
	}

	public void setimageUtils(ImageFilePath imageUtils) {
		this.imageUtils = imageUtils;
	}

	@Override
	public ReadableOrderProduct populate(OrderProductItem source,
										 ReadableOrderProduct target, MerchantStoreItem store, LocaleItem language)
			throws ConversionException {

		Validate.notNull(pricingService, "Requires PricingService");
		Validate.notNull(imageUtils, "Requires imageUtils");
		target.setUuid(source.getUuid());
		target.setOrderedQuantity(source.getProductQuantity());
		try {
			target.setPrice(pricingService.getDisplayAmount(source.getOneTimeCharge(), store));
		} catch (Exception e) {
			throw new ConversionException("Cannot convert price", e);
		}
		target.setProductName(source.getProductName());
		target.setSku(source.getSku());

		//subtotal = price * quantity
		BigDecimal subTotal = source.getOneTimeCharge();
		subTotal = subTotal.multiply(new BigDecimal(source.getProductQuantity()));

		try {
			String subTotalPrice = pricingService.getDisplayAmount(subTotal, store);
			target.setSubTotal(subTotalPrice);
		} catch (Exception e) {
			throw new ConversionException("Cannot format price", e);
		}


		String productSku = source.getSku();
		if (!StringUtils.isBlank(productSku)) {
//			ProductItem product = productService.getByCode(productSku);
//			if (product != null) {
//
//
//				ReadableProductPopulator populator = new ReadableProductPopulator();
//				populator.setPricingService(pricingService);
//				populator.setimageUtils(imageUtils);
//
//				ReadableProduct productProxy = populator.populate(product, new ReadableProduct(), store, language);
//				target.setProduct(productProxy);
//
//				Set<ProductImageItem> images = product.getImages();
//				ProductImageItem defaultImage = null;
//				if (images != null) {
//					for (ProductImageItem image : images) {
//						if (defaultImage == null) {
//							defaultImage = image;
//						}
//						if (image.getDefaultImage()) {
//							defaultImage = image;
//						}
//					}
//				}
//				if (defaultImage != null) {
//					target.setImage(defaultImage.getProductImage());
//				}
//			}
		}


		return target;
	}

	@Override
	protected ReadableOrderProduct createTarget() {

		return null;
	}

	public PricingService getPricingService() {
		return pricingService;
	}

	public void setPricingService(PricingService pricingService) {
		this.pricingService = pricingService;
	}

}
