package com.coretex.commerce.mapper;

import com.coretex.commerce.data.ShortProductData;
import com.coretex.items.cx_core.ProductItem;
import com.coretex.items.cx_core.StoreItem;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class, ShortVariantProductDataMapper.class})
public interface ShortProductDataMapper extends GenericDataMapper<ProductItem, ShortProductData>{

	@Override
	@Mappings({
			@Mapping(target = "price", expression = "java(getPrice(productItem))")
	})
	ShortProductData fromItem(ProductItem productItem);

	@Override
	@InheritConfiguration(name = "fromItem")
	void updateFromItem(ProductItem productItem, @MappingTarget ShortProductData productData);


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
