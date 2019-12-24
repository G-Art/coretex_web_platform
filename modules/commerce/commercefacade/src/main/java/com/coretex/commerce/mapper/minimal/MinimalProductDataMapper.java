package com.coretex.commerce.mapper.minimal;

import com.coretex.commerce.data.minimal.MinimalProductData;
import com.coretex.commerce.mapper.GenericDataMapper;
import com.coretex.commerce.mapper.LocaleDataMapper;
import com.coretex.commerce.mapper.ReferenceMapper;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.ProductItem;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class, LocaleDataMapper.class})
public interface MinimalProductDataMapper extends GenericDataMapper<ProductItem, MinimalProductData> {

	default String mapStore(MerchantStoreItem value) {
		return value.getStoreName();
	}
}
