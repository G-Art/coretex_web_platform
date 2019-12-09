package com.coretex.shop.admin.mapppers;

import com.coretex.core.business.services.SessionService;
import com.coretex.core.business.services.catalog.product.image.ProductImageService;
import com.coretex.core.business.utils.ProductPriceUtils;
import com.coretex.core.model.content.FileContentType;
import com.coretex.core.model.content.ImageContentFile;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.ProductAvailabilityItem;
import com.coretex.items.commerce_core_model.ProductImageItem;
import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.commerce_core_model.ProductPriceItem;
import com.coretex.shop.admin.forms.ProductForm;
import com.coretex.shop.admin.mapppers.dto.ManufacturerDtoMapper;
import com.coretex.shop.admin.mapppers.dto.ProductAvailabilityDtoMapper;
import com.coretex.shop.admin.mapppers.dto.ProductPriceDtoMapper;
import com.coretex.shop.admin.mapppers.dto.ReferenceMapper;
import com.coretex.shop.constants.Constants;
import org.apache.commons.io.IOUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static com.coretex.core.business.constants.Constants.ALL_REGIONS;
import static com.coretex.core.business.constants.Constants.DEFAULT_PRICE_DESCRIPTION;

@Mapper(componentModel = "spring", imports = UUID.class, uses = {ManufacturerDtoMapper.class, ReferenceMapper.class})
public abstract class ProductFormMapper implements LocalizedFieldMapper<ProductItem, ProductForm> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductFormMapper.class);

	@Resource
	private ProductImageService productImageService;

	@Resource
	private SessionService sessionService;

	@Resource
	private ProductPriceUtils priceUtil;

	@Resource
	private ProductAvailabilityDtoMapper productAvailabilityDtoMapper;

	@Resource
	private ProductPriceDtoMapper productPriceDtoMapper;

	@Mappings({
			@Mapping(target = "uuid", source = "uuid", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE),
			@Mapping(target = "name", ignore = true),
			@Mapping(target = "available", source = "available", defaultValue = "true"),
			@Mapping(target = "title", ignore = true),
			@Mapping(target = "description", ignore = true),
			@Mapping(target = "productHighlight", ignore = true),
			@Mapping(target = "productExternalDl", ignore = true),
			@Mapping(target = "metatagTitle", ignore = true),
			@Mapping(target = "metatagKeywords", ignore = true),
			@Mapping(target = "metatagDescription", ignore = true),
			@Mapping(target = "dateAvailable", source = "dateAvailable", dateFormat = "yyyy-MM-dd")
	})
	public abstract ProductForm fromProductItem(ProductItem source);

	@InheritConfiguration(name = "fromProductItem")
	public abstract void updateFromProductItem(ProductItem source, @MappingTarget ProductForm target);

	@InheritInverseConfiguration(name = "fromProductItem")
	@Mappings({
			@Mapping(target = "uuid", source = "uuid", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE),
			@Mapping(target = "name", ignore = true),
			@Mapping(target = "title", ignore = true),
			@Mapping(target = "description", ignore = true),
			@Mapping(target = "productHighlight", ignore = true),
			@Mapping(target = "productExternalDl", ignore = true),
			@Mapping(target = "metatagTitle", ignore = true),
			@Mapping(target = "metatagKeywords", ignore = true),
			@Mapping(target = "metatagDescription", ignore = true)
	})
	public abstract ProductItem toProductItem(ProductForm source);

	@InheritConfiguration(name = "toProductItem")
	public abstract void updateToProductItem(ProductForm source, @MappingTarget ProductItem target);


	@AfterMapping
	protected void defineMissedFields(ProductItem source, @MappingTarget ProductForm target) {
		for (ProductImageItem image : source.getImages()) {
			if (image.getDefaultImage()) {
				target.setProductImage(image);
				break;
			}

		}

		ProductAvailabilityItem productAvailability = new ProductAvailabilityItem();
		ProductPriceItem productPrice = new ProductPriceItem();

		Set<ProductAvailabilityItem> availabilities = source.getAvailabilities();
		if (availabilities != null && availabilities.size() > 0) {

			for (ProductAvailabilityItem availability : availabilities) {
				if (availability.getRegion().equals(ALL_REGIONS)) {
					productAvailability = availability;
					Set<ProductPriceItem> prices = availability.getPrices();
					for (ProductPriceItem price : prices) {
						if (price.getDefaultPrice()) {
							productPrice = price;
							target.setProductPrice(priceUtil.getAdminFormatedAmount(productPrice.getProductPriceAmount()));
						}
					}
				}
			}
		}

		target.setAvailability(productAvailabilityDtoMapper.fromItem(productAvailability));
		target.setPrice(productPriceDtoMapper.fromItem(productPrice));
	}

	@AfterMapping
	protected void defineMissedFields(ProductForm source, @MappingTarget ProductItem target) {

		var store = sessionService.getSessionAttribute(Constants.ADMIN_STORE, MerchantStoreItem.class);
		BigDecimal submitedPrice = priceUtil.getAmount(source.getProductPrice());
		ProductAvailabilityItem newProductAvailability = new ProductAvailabilityItem();
		newProductAvailability.setRegion(ALL_REGIONS);

		ProductPriceItem newProductPrice = new ProductPriceItem();
		newProductPrice.setCode("basePrice_"+source.getSku());
		newProductPrice.setDefaultPrice(true);
		newProductPrice.setProductPriceAmount(submitedPrice);

		Set<ProductPriceItem> prices = new HashSet<>();
		Set<ProductAvailabilityItem> availabilities = new HashSet<>();

		newProductPrice.setName(DEFAULT_PRICE_DESCRIPTION);

		target.setMerchantStore(store);

		Set<ProductAvailabilityItem> avails = target.getAvailabilities();
		if (avails != null && avails.size() > 0) {

			for (ProductAvailabilityItem availability : avails) {
				if (availability.getRegion().equals(ALL_REGIONS)) {

					newProductAvailability = availability;
					Set<ProductPriceItem> productPrices = availability.getPrices();

					for (ProductPriceItem price : productPrices) {
						if (price.getDefaultPrice()) {
							newProductPrice = price;
							newProductPrice.setProductPriceAmount(submitedPrice);
						} else {
							prices.add(price);
						}
					}
				} else {
					availabilities.add(availability);
				}
			}
		}

		newProductAvailability.setProductQuantity(source.getAvailability().getProductQuantity());
		newProductAvailability.setProductQuantityOrderMin(source.getAvailability().getProductQuantityOrderMin());
		newProductAvailability.setProductQuantityOrderMax(source.getAvailability().getProductQuantityOrderMax());
		newProductAvailability.setProduct(target);

		prices.add(newProductPrice);
		newProductAvailability.setPrices(prices);
		availabilities.add(newProductAvailability);

		newProductPrice.setProductAvailability(newProductAvailability);

		target.setAvailabilities(availabilities);

		var image = source.getImage();
		if(Objects.nonNull(image) && !image.isEmpty()){
			try {
				saveFile(image.getInputStream(), image.getOriginalFilename() , target);
			} catch (Exception e) {
				LOGGER.error(String.format("Save image exception [product: %s]:", source.getSku()), e);
			}
		}
	}

	private void saveFile(InputStream fis, String name, ProductItem product) throws Exception {

		if (Objects.isNull(fis)) {
			return;
		}

		final byte[] is = IOUtils.toByteArray(fis);
		final ByteArrayInputStream inputStream = new ByteArrayInputStream(is);
		final ImageContentFile cmsContentImage = new ImageContentFile();
		cmsContentImage.setFileName(name);
		cmsContentImage.setFile(inputStream);
		cmsContentImage.setFileContentType(FileContentType.PRODUCT);

		ProductImageItem productImage = new ProductImageItem();
		productImage.setProductImage(name);
		productImage.setProduct(product);
		productImage.setDefaultImage(true);

		product.getImages().add(productImage);

		productImageService.addProductImage(product, productImage, cmsContentImage);
	}

}
