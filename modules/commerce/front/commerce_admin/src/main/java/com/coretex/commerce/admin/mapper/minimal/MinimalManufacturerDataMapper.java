package com.coretex.commerce.admin.mapper.minimal;

import com.coretex.commerce.admin.data.minimal.MinimalManufacturerData;
import com.coretex.commerce.admin.mapper.GenericDataMapper;
import com.coretex.commerce.admin.mapper.LocaleDataMapper;
import com.coretex.commerce.admin.mapper.ReferenceMapper;
import com.coretex.items.commerce_core_model.ManufacturerItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class, LocaleDataMapper.class})
public interface MinimalManufacturerDataMapper extends GenericDataMapper<ManufacturerItem, MinimalManufacturerData> {

	default String mapStore(MerchantStoreItem value) {
		return value.getStoreName();
	}
}
