package com.coretex.shop.populator.order;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.services.catalog.product.PricingService;
import com.coretex.core.business.services.catalog.product.ProductService;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.commerce_core_model.ProductImageItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.OrderProductItem;
import com.coretex.items.commerce_core_model.OrderProductAttributeItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.shop.model.catalog.product.ReadableProduct;
import com.coretex.shop.model.order.ReadableOrderProduct;
import com.coretex.shop.model.order.ReadableOrderProductAttribute;
import com.coretex.shop.populator.catalog.ReadableProductPopulator;
import com.coretex.shop.utils.ImageFilePath;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ReadableOrderProductPopulator extends
		AbstractDataPopulator<OrderProductItem, ReadableOrderProduct> {

	private ProductService productService;
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

		Validate.notNull(productService, "Requires ProductService");
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

		if (source.getOrderAttributes() != null) {
			List<ReadableOrderProductAttribute> attributes = new ArrayList<ReadableOrderProductAttribute>();
			for (OrderProductAttributeItem attr : source.getOrderAttributes()) {
				ReadableOrderProductAttribute readableAttribute = new ReadableOrderProductAttribute();
				try {
					String price = pricingService.getDisplayAmount(attr.getProductAttributePrice(), store);
					readableAttribute.setAttributePrice(price);
				} catch (ServiceException e) {
					throw new ConversionException("Cannot format price", e);
				}

				readableAttribute.setAttributeName(attr.getProductAttributeName());
				readableAttribute.setAttributeValue(attr.getProductAttributeValueName());
				attributes.add(readableAttribute);
			}
			target.setAttributes(attributes);
		}


		String productSku = source.getSku();
		if (!StringUtils.isBlank(productSku)) {
			ProductItem product = productService.getByCode(productSku);
			if (product != null) {


				ReadableProductPopulator populator = new ReadableProductPopulator();
				populator.setPricingService(pricingService);
				populator.setimageUtils(imageUtils);

				ReadableProduct productProxy = populator.populate(product, new ReadableProduct(), store, language);
				target.setProduct(productProxy);

				Set<ProductImageItem> images = product.getImages();
				ProductImageItem defaultImage = null;
				if (images != null) {
					for (ProductImageItem image : images) {
						if (defaultImage == null) {
							defaultImage = image;
						}
						if (image.getDefaultImage()) {
							defaultImage = image;
						}
					}
				}
				if (defaultImage != null) {
					target.setImage(defaultImage.getProductImage());
				}
			}
		}


		return target;
	}

	@Override
	protected ReadableOrderProduct createTarget() {

		return null;
	}

	public ProductService getProductService() {
		return productService;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	public PricingService getPricingService() {
		return pricingService;
	}

	public void setPricingService(PricingService pricingService) {
		this.pricingService = pricingService;
	}

}