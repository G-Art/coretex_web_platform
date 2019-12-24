package com.coretex.commerce.mapper.minimal;

import com.coretex.commerce.data.minimal.MinimalManufacturerData;
import com.coretex.commerce.mapper.GenericDataMapper;
import com.coretex.commerce.mapper.LocaleDataMapper;
import com.coretex.commerce.mapper.ReferenceMapper;
import com.coretex.items.cx_core.ManufacturerItem;
import com.coretex.items.cx_core.StoreItem;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;

import java.util.Objects;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class, LocaleDataMapper.class})
public interface MinimalManufacturerDataMapper extends GenericDataMapper<ManufacturerItem, MinimalManufacturerData> {

	default String mapStore(StoreItem value) {
		if(Objects.nonNull(value)){
			return value.getName();
		}
		return StringUtils.EMPTY;
	}
}
