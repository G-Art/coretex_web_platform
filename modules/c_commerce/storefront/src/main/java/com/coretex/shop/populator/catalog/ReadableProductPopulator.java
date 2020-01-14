package com.coretex.shop.populator.catalog;

import com.coretex.core.business.constants.Constants;
import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.business.services.catalog.product.PricingService;
import com.coretex.core.model.catalog.product.price.FinalPrice;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.items.cx_core.CategoryItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.ProductAttributeItem;
import com.coretex.items.commerce_core_model.ProductAvailabilityItem;
import com.coretex.items.commerce_core_model.ProductImageItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.cx_core.ProductItem;
import com.coretex.shop.model.catalog.category.ReadableCategory;
import com.coretex.shop.model.catalog.manufacturer.ReadableManufacturer;
import com.coretex.shop.model.catalog.product.ReadableImage;
import com.coretex.shop.model.catalog.product.ReadableProduct;
import com.coretex.shop.model.catalog.product.attribute.ReadableProductAttribute;
import com.coretex.shop.model.catalog.product.attribute.ReadableProductAttributeValue;
import com.coretex.shop.model.catalog.product.attribute.ReadableProductOption;
import com.coretex.shop.model.catalog.product.attribute.ReadableProductOptionValue;
import com.coretex.shop.utils.ImageFilePath;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;


public class ReadableProductPopulator extends
		AbstractDataPopulator<ProductItem, ReadableProduct> {

	private PricingService pricingService;

	private ImageFilePath imageUtils;

	public ImageFilePath getimageUtils() {
		return imageUtils;
	}


	public void setimageUtils(ImageFilePath imageUtils) {
		this.imageUtils = imageUtils;
	}


	public PricingService getPricingService() {
		return pricingService;
	}


	public void setPricingService(PricingService pricingService) {
		this.pricingService = pricingService;
	}


	@Override
	public ReadableProduct populate(ProductItem source,
									ReadableProduct target, MerchantStoreItem store, LocaleItem language)
			throws ConversionException {
		Validate.notNull(pricingService, "Requires to set PricingService");
		Validate.notNull(imageUtils, "Requires to set imageUtils");
		Validate.notNull(language, "LocaleItem cannot be null");

		try {

			target.setUuid(source.getUuid());
			target.setAvailable(source.getAvailable());

			com.coretex.shop.model.catalog.product.ProductDescription tragetDescription = new com.coretex.shop.model.catalog.product.ProductDescription();

			tragetDescription.setName(source.getName());
			tragetDescription.setUuid(source.getUuid());
			tragetDescription.setTitle(source.getName());
			tragetDescription.setDescription(source.getDescription());
			target.setDescription(tragetDescription);

			if (source.getManufacturer() != null) {
				ReadableManufacturer manufacturerEntity = new ReadableManufacturer();
				com.coretex.shop.model.catalog.manufacturer.ManufacturerDescription d = new com.coretex.shop.model.catalog.manufacturer.ManufacturerDescription();
				d.setName(source.getManufacturer().getName());
				manufacturerEntity.setUuid(source.getManufacturer().getUuid());
				manufacturerEntity.setCode(source.getManufacturer().getCode());
				target.setManufacturer(manufacturerEntity);
			}

			Set<ProductImageItem> images = source.getImages();
			if (images != null && images.size() > 0) {
				List<ReadableImage> imageList = new ArrayList<ReadableImage>();

				String contextPath = imageUtils.getContextPath();

				for (ProductImageItem img : images) {
					ReadableImage prdImage = new ReadableImage();
					prdImage.setImageName(img.getProductImage());
					prdImage.setDefaultImage(img.getDefaultImage() != null ? img.getDefaultImage() : false);

					StringBuilder imgPath = new StringBuilder();
					imgPath.append(contextPath).append(imageUtils.buildProductImageUtils(store, source.getCode(), img.getProductImage()));

					prdImage.setImageUrl(imgPath.toString());
					prdImage.setUuid(img.getUuid());
					prdImage.setImageType(img.getImageType() != null ? img.getImageType() : 0);
					if (img.getProductImageUrl() != null) {
						prdImage.setExternalUrl(img.getProductImageUrl());
					}
					if (img.getImageType() != null && img.getImageType() == 1 && img.getProductImageUrl() != null) {//video
						prdImage.setVideoUrl(img.getProductImageUrl());
					}

					if (prdImage.isDefaultImage()) {
						target.setImage(prdImage);
					}

					imageList.add(prdImage);
				}
				target
						.setImages(imageList);
			}

			if (!CollectionUtils.isEmpty(source.getCategories())) {

				ReadableCategoryPopulator categoryPopulator = new ReadableCategoryPopulator();
				List<ReadableCategory> categoryList = new ArrayList<ReadableCategory>();

				for (CategoryItem category : source.getCategories()) {

					ReadableCategory readableCategory = new ReadableCategory();
					categoryPopulator.populate(category, readableCategory, store, language);
					categoryList.add(readableCategory);

				}

				target.setCategories(categoryList);

			}

			if (!CollectionUtils.isEmpty(source.getAttributes())) {

				Set<ProductAttributeItem> attributes = source.getAttributes();


				//split read only and options
				Map<UUID, ReadableProductAttribute> readOnlyAttributes = null;
				Map<UUID, ReadableProductOption> selectableOptions = null;

				if (!CollectionUtils.isEmpty(attributes)) {

					for (ProductAttributeItem attribute : attributes) {
						ReadableProductOption opt = null;
						ReadableProductAttribute attr = null;
						ReadableProductOptionValue optValue = new ReadableProductOptionValue();
						ReadableProductAttributeValue attrValue = new ReadableProductAttributeValue();


						if (attribute.getAttributeDisplayOnly()) {//read only attribute
							if (readOnlyAttributes == null) {
								readOnlyAttributes = new TreeMap<>();
							}

							attrValue.setDefaultValue(attribute.getAttributeDefault());
							attrValue.setUuid(attribute.getUuid());//id of the attribute
							attrValue.setLang(language.getIso());


							attrValue.setSortOrder(0);
							if (attribute.getProductOptionSortOrder() != null) {
								attrValue.setSortOrder(attribute.getProductOptionSortOrder());
							}
							if (attr != null) {
								attr.getAttributeValues().add(attrValue);
							}


						} else {//selectable option

							if (selectableOptions == null) {
								selectableOptions = new TreeMap<>();
							}

							optValue.setDefaultValue(attribute.getAttributeDefault());
							optValue.setUuid(attribute.getUuid());//id of the attribute
							optValue.setLang(language.getIso());
							if (attribute.getProductAttributePrice() != null && attribute.getProductAttributePrice().doubleValue() > 0) {
								String formatedPrice = pricingService.getDisplayAmount(attribute.getProductAttributePrice(), store);
								optValue.setPrice(formatedPrice);
							}

							optValue.setSortOrder(0);
							if (attribute.getProductOptionSortOrder() != null) {
								optValue.setSortOrder(attribute.getProductOptionSortOrder().intValue());
							}

							if (opt != null) {
								opt.getOptionValues().add(optValue);
							}
						}

					}

				}

				if (selectableOptions != null) {
					List<ReadableProductOption> options = new ArrayList<ReadableProductOption>(selectableOptions.values());
					target.setOptions(options);
				}


			}


			//remove products from invisible category -> set visible = false
/*			Set<CategoryItem> categories = source.getCategories();
			boolean isVisible = true;
			if(!CollectionUtils.isEmpty(categories)) {
				for(CategoryItem c : categories) {
					if(c.isVisible()) {
						isVisible = true;
						break;
					} else {
						isVisible = false;
					}
				}
			}*/

			//target.setVisible(isVisible);


			target.setSku(source.getCode());

			FinalPrice price = pricingService.calculateProductPrice(source);

			target.setFinalPrice(pricingService.getDisplayAmount(price.getFinalPrice(), store));
			target.setPrice(price.getFinalPrice());
			target.setOriginalPrice(pricingService.getDisplayAmount(price.getOriginalPrice(), store));

			if (price.isDiscounted()) {
				target.setDiscounted(true);
			}

			//availability
			for (ProductAvailabilityItem availability : source.getAvailabilities()) {
				if (availability.getRegion().equals(Constants.ALL_REGIONS)) {//TODO REL 2.1 accept a region
					target.setQuantity(availability.getProductQuantity() != null ? availability.getProductQuantity() : 0);
					target.setQuantityOrderMaximum(availability.getProductQuantityOrderMax() != null ? availability.getProductQuantityOrderMax() : 0);
					target.setQuantityOrderMinimum(availability.getProductQuantityOrderMin() != null ? availability.getProductQuantityOrderMin() : 0);
					if (availability.getProductQuantity().intValue() > 0 && target.isAvailable()) {
						target.setCanBePurchased(true);
					}
				}
			}


			return target;

		} catch (Exception e) {
			throw new ConversionException(e);
		}
	}

	@Override
	protected ReadableProduct createTarget() {
		// TODO Auto-generated method stub
		return null;
	}

}
