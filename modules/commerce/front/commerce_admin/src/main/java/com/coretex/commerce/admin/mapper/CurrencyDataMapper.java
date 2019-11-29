package com.coretex.commerce.admin.mapper;

import com.coretex.commerce.admin.data.CurrencyData;
import com.coretex.commerce.admin.data.LocaleData;
import com.coretex.items.commerce_core_model.CurrencyItem;
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

	@Override
	@InheritInverseConfiguration(name = "fromItem")
	CurrencyItem toItem(CurrencyData source);

	@Override
	@InheritConfiguration(name = "toItem")
	void updateToItem(CurrencyData source, @MappingTarget CurrencyItem target);
}