package com.coretex.commerce.admin.mapper;

import com.coretex.commerce.admin.data.AddressData;
import com.coretex.commerce.admin.data.ZoneData;
import com.coretex.items.commerce_core_model.AbstractAddressItem;
import com.coretex.items.commerce_core_model.ZoneItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
		uses = {ReferenceMapper.class,
				CountryDataMapper.class,
				ZoneDataMapper.class})
public interface AddressDataMapper extends GenericDataMapper<AbstractAddressItem, AddressData>{

}
