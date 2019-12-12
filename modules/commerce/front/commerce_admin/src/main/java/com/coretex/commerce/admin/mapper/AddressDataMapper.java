package com.coretex.commerce.admin.mapper;

import com.coretex.commerce.admin.data.AddressData;
import com.coretex.items.cx_core.AddressItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
		uses = {ReferenceMapper.class,
				StoreDataMapper.class,
				CountryDataMapper.class,
				ZoneDataMapper.class})
public interface AddressDataMapper extends GenericDataMapper<AddressItem, AddressData>{

}
