package com.coretex.commerce.admin.mapper.minimal;

import com.coretex.commerce.admin.data.minimal.MinimalManufacturerData;
import com.coretex.commerce.admin.mapper.GenericDataMapper;
import com.coretex.commerce.admin.mapper.LocaleDataMapper;
import com.coretex.commerce.admin.mapper.ReferenceMapper;
import com.coretex.items.cx_core.ManufacturerItem;
import com.coretex.items.cx_core.StoreItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class, LocaleDataMapper.class})
public interface MinimalManufacturerDataMapper extends GenericDataMapper<ManufacturerItem, MinimalManufacturerData> {

	default String mapStore(StoreItem value) {
		return value.getName();
	}
}
