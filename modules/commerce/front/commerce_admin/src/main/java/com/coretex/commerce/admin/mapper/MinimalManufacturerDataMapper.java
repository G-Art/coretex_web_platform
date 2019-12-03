package com.coretex.commerce.admin.mapper;

import com.coretex.commerce.admin.data.MinimalManufacturerData;
import com.coretex.items.commerce_core_model.ManufacturerItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class, LocaleDataMapper.class})
public interface MinimalManufacturerDataMapper extends GenericDataMapper<ManufacturerItem, MinimalManufacturerData> {

	@Override
	@Mappings({
			@Mapping(target = "merchantStore", ignore = true)
	})
	ManufacturerItem toItem(MinimalManufacturerData source);

	@Override
	@InheritConfiguration(name = "toItem")
	void updateToItem(MinimalManufacturerData source, @MappingTarget ManufacturerItem target);

	default String mapStore(MerchantStoreItem value) {
		return value.getStoreName();
	}
}
