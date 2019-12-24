package com.coretex.commerce.mapper;

import com.coretex.commerce.data.ProductData;
import com.coretex.core.business.utils.ProductUtils;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.ProductItem;
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
			@Mapping(target = "store", source = "merchantStore")
	})
	ProductData fromItem(ProductItem productItem);

	@Override
	@InheritConfiguration(name = "fromItem")
	void updateFromItem(ProductItem productItem, @MappingTarget ProductData productData);

	default String mapImageUrl(ProductItem productItem){
		if(CollectionUtils.isNotEmpty(productItem.getImages())){
			var first = IteratorUtils.first(productItem.getImages().iterator());
			return ProductUtils.buildProductSmallImageUtils(productItem.getMerchantStore(), productItem.getSku(), first.getProductImage());
		}
		return null;
	}

	default String mapStore(MerchantStoreItem value) {
		return value.getStoreName();
	}
}
