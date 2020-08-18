package com.coretex.commerce.mapper;

import com.coretex.commerce.data.ImageData;
import com.coretex.commerce.data.ShortVariantProductData;
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

import java.util.Objects;

import static com.coretex.commerce.core.utils.ProductUtils.buildProductSmallImageUtils;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class})
public interface ShortVariantProductDataMapper extends GenericDataMapper<VariantProductItem, ShortVariantProductData>{

	@Override
	@Mappings({
			@Mapping(target = "images", expression = "java(this.mapImageUrl(productItem))"),
			@Mapping(target = "price", expression = "java(this.getPrice(productItem))"),
			@Mapping(target = "colorCssCode", expression = "java(this.getColorCode(productItem))"),
			@Mapping(target = "size", expression = "java(this.getSize(productItem))"),
	})
	ShortVariantProductData fromItem(VariantProductItem productItem);

	@Override
	@InheritConfiguration(name = "fromItem")
	void updateFromItem(VariantProductItem productItem, @MappingTarget ShortVariantProductData productData);

	default String getColorCode(ProductItem productItem){
		if(productItem instanceof StyleVariantProductItem && Objects.nonNull(((StyleVariantProductItem) productItem).getStyle())){
			var style = ((StyleVariantProductItem) productItem).getStyle();
			return style.getCssColorCode();
		}
		if(productItem instanceof SizeVariantProductItem){
			return getColorCode(((SizeVariantProductItem) productItem).getBaseProduct());
		}
		return null;
	}

	default ImageData[] mapImageUrl(VariantProductItem productItem){
		var images = Lists.<ImageData>newArrayList();
		if(CollectionUtils.isEmpty(productItem.getImages())){
			var baseProduct = productItem.getBaseProduct();
			if(baseProduct instanceof VariantProductItem){
				images.addAll(Lists.newArrayList(mapImageUrl((VariantProductItem) baseProduct)));
			}
		}else {
			productItem.getImages()
					.forEach(image -> images.add(new ImageData(buildProductSmallImageUtils(image.getProduct().getStore(), image.getProduct().getCode(), image.getProductImage()))));
		}

		return images.toArray(new ImageData[0]);
	}

	default String getSize(ProductItem productItem) {
		if (productItem instanceof SizeVariantProductItem) {
			return ((SizeVariantProductItem) productItem).getSize();
		}

		return null;
	}

	default String getPrice(ProductItem productItem){
		return productItem.getAvailabilities().stream()
				.findFirst()
				.map(productAvailabilityItem ->
						productAvailabilityItem.getPrices().stream()
						.findFirst()
						.map(productPriceItem -> productPriceItem.getProductPriceAmount().toPlainString())
						.orElse("0.00"))
				.orElse("0.00");
	}

	default String mapStore(StoreItem value) {
		return value.getName();
	}
}
