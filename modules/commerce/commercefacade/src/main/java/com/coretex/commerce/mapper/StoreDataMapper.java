package com.coretex.commerce.mapper;

import com.coretex.commerce.data.StoreData;
import com.coretex.items.cx_core.StoreItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {
		ReferenceMapper.class,
		ZoneDataMapper.class,
		LocaleDataMapper.class,
		AddressDataMapper.class,
		CurrencyDataMapper.class})

public interface StoreDataMapper extends GenericDataMapper<StoreItem, StoreData> {

}
