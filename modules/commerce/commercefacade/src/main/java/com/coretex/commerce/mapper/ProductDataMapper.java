package com.coretex.commerce.mapper;

import com.coretex.commerce.data.ProductData;
import com.coretex.core.business.utils.ProductUtils;
import com.coretex.items.cx_core.ProductItem;
import com.coretex.items.cx_core.StoreItem;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IteratorUtils;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class})
public interface ProductDataMapper extends GenericDataMapper<ProductItem, ProductData>{

	@Override
	@Mappings({
			@Mapping(target = "image", expression = "java(mapImageUrl(productItem))"),
			@Mapping(target = "store", source = "store")
	})
	ProductData fromItem(ProductItem productItem);

	@Override
	@InheritConfiguration(name = "fromItem")
	void updateFromItem(ProductItem productItem, @MappingTarget ProductData productData);

	default String mapImageUrl(ProductItem productItem){
		if(CollectionUtils.isNotEmpty(productItem.getImages())){
			var first = IteratorUtils.first(productItem.getImages().iterator());
			return ProductUtils.buildProductSmallImageUtils(productItem.getStore(), productItem.getCode(), first.getProductImage());
		}
		return null;
	}

	default String mapStore(StoreItem value) {
		return value.getName();
	}
}
