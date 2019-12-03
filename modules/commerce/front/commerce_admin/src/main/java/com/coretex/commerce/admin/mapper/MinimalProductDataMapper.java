package com.coretex.commerce.admin.mapper;

import com.coretex.commerce.admin.data.MinimalProductData;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.ProductItem;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class, LocaleDataMapper.class})
public interface MinimalProductDataMapper extends GenericDataMapper<ProductItem, MinimalProductData> {

	@Override
	@Mappings({
			@Mapping(target = "merchantStore", ignore = true)
	})
	ProductItem toItem(MinimalProductData source);

	@Override
	@InheritConfiguration(name = "toItem")
	void updateToItem(MinimalProductData source, @MappingTarget ProductItem target);

	default String mapStore(MerchantStoreItem value) {
		return value.getStoreName();
	}
}
