package com.coretex.commerce.mapper;

import com.coretex.commerce.data.CurrencyData;
import com.coretex.commerce.data.LocaleData;
import com.coretex.items.cx_core.CurrencyItem;
import com.coretex.items.core.LocaleItem;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class})
public interface CurrencyDataMapper extends GenericDataMapper<CurrencyItem, CurrencyData>{

	@Override
	@Mappings({
			@Mapping(source = "code", target = "isocode"),
			@Mapping(source = "supported", target = "active")
	})
	CurrencyData fromItem(CurrencyItem currencyItem);

	@Override
	@InheritConfiguration(name = "fromItem")
	void updateFromItem(CurrencyItem currencyItem, @MappingTarget CurrencyData currencyData);

}
