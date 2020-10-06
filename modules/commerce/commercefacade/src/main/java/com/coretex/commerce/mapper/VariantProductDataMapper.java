package com.coretex.commerce.mapper;

import com.coretex.commerce.core.utils.ProductUtils;
import com.coretex.commerce.data.ImageData;
import com.coretex.commerce.data.VariantProductData;
import com.coretex.items.cx_core.ProductItem;
import com.coretex.items.cx_core.SizeVariantProductItem;
import com.coretex.items.cx_core.StoreItem;
import com.coretex.items.cx_core.StyleVariantProductItem;
import com.coretex.items.cx_core.VariantProductItem;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import java.math.RoundingMode;
import java.util.Objects;

import static com.coretex.commerce.core.utils.ProductUtils.buildProductSmallImageUtils;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class})
public interface VariantProductDataMapper extends GenericDataMapper<VariantProductItem, VariantProductData> {
	@Override
	@Mappings({
			@Mapping(target = "images", expression = "java(this.mapImageUrl(productItem))"),
			@Mapping(target = "price", expression = "java(this.getPrice(productItem))"),
			@Mapping(target = "colorCssCode", expression = "java(this.getColorCode(productItem))"),
			@Mapping(target = "size", expression = "java(this.getSize(productItem))"),
			@Mapping(target = "baseProductCode", expression = "java(this.getBaseProductCode(productItem))"),
			@Mapping(target = "colorName", expression = "java(this.getColorName(productItem))"),
			@Mapping(target = "name", expression = "java(this.getName(productItem))")
	})
	VariantProductData fromItem(VariantProductItem productItem);

	@Override
	@InheritConfiguration(name = "fromItem")
	void updateFromItem(VariantProductItem productItem, @MappingTarget VariantProductData productData);

	default String getBaseProductCode(ProductItem productItem) {
		if (productItem instanceof VariantProductItem) {
			return getBaseProductCode(((VariantProductItem) productItem).getBaseProduct());
		}

		return productItem.getCode();
	}

	default String getSize(ProductItem productItem) {
		if (productItem instanceof SizeVariantProductItem) {
			return ((SizeVariantProductItem) productItem).getSize();
		}

		return null;
	}

	default String getColorCode(ProductItem productItem) {
		if (productItem instanceof StyleVariantProductItem && Objects.nonNull(((StyleVariantProductItem) productItem).getStyle())) {
			var style = ((StyleVariantProductItem) productItem).getStyle();
			return style.getCssColorCode();
		}
		if (productItem instanceof SizeVariantProductItem) {
			return getColorCode(((SizeVariantProductItem) productItem).getBaseProduct());
		}
		return null;
	}

	default String getName(ProductItem productItem) {
		if (Objects.nonNull(productItem.getName())) {
			return productItem.getName();
		}
		if (productItem instanceof VariantProductItem) {
			return getName(((VariantProductItem) productItem).getBaseProduct());
		}
		return null;
	}

	default String getColorName(ProductItem productItem) {
		if (productItem instanceof StyleVariantProductItem && Objects.nonNull(((StyleVariantProductItem) productItem).getStyle())) {
			var style = ((StyleVariantProductItem) productItem).getStyle();
			return style.getStyleName();
		}
		if (productItem instanceof SizeVariantProductItem) {
			return getColorName(((SizeVariantProductItem) productItem).getBaseProduct());
		}
		return null;
	}

	default ImageData[] mapImageUrl(VariantProductItem productItem) {
		var images = Lists.<ImageData>newArrayList();
		if (CollectionUtils.isEmpty(productItem.getImages())) {
			var baseProduct = productItem.getBaseProduct();
			if (baseProduct instanceof VariantProductItem) {
				images.addAll(Lists.newArrayList(mapImageUrl((VariantProductItem) baseProduct)));
			}
		} else {
			productItem.getImages()
					.forEach(image -> images.add(new ImageData(buildProductSmallImageUtils(image.getProduct().getStore(), image.getProduct().getCode(), image.getProductImage()))));
		}

		return images.toArray(new ImageData[0]);
	}

	default String getPrice(ProductItem productItem) {
		var availabilities = ProductUtils.getAvailabilities(productItem);
		return availabilities
				.stream()
				.findFirst()
				.map(productAvailabilityItem ->
						productAvailabilityItem.getPrices()
								.stream()
								.findFirst()
								.map(productPriceItem -> productPriceItem.getProductPriceAmount()
										.setScale(2, RoundingMode.HALF_UP)
										.toPlainString())
								.orElse("0.00"))
				.orElse("0.00");
	}

	default String mapStore(StoreItem value) {
		return value.getName();
	}
}
