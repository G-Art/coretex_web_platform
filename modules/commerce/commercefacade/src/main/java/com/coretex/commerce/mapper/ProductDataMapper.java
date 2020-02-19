package com.coretex.commerce.mapper;

import com.coretex.commerce.core.utils.ProductUtils;
import com.coretex.commerce.data.ProductData;
import com.coretex.items.cx_core.ProductItem;
import com.coretex.items.cx_core.StoreItem;
import com.coretex.items.cx_core.VariantProductItem;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IteratorUtils;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import java.util.Objects;

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
		if(productItem instanceof VariantProductItem){
			var baseProduct = ((VariantProductItem) productItem).getBaseProduct();
			if(Objects.nonNull(baseProduct)){
				if(CollectionUtils.isNotEmpty(baseProduct.getImages())){
					var first = IteratorUtils.first(baseProduct.getImages().iterator());
					return ProductUtils.buildProductSmallImageUtils(baseProduct.getStore(), baseProduct.getCode(), first.getProductImage());
				}
			}
		}
		return null;
	}

	default String mapStore(StoreItem value) {
		return value.getName();
	}
}
