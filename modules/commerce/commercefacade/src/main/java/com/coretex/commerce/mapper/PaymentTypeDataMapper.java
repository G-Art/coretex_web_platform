package com.coretex.commerce.mapper;

import com.coretex.commerce.data.PaymentTypeData;
import com.coretex.items.cxpaymentapi.PaymentModeItem;
import com.google.common.collect.Maps;
import org.mapstruct.AfterMapping;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
		uses = {ReferenceMapper.class,
				StoreDataMapper.class,
				CountryDataMapper.class,
				LocaleDataMapper.class})
public abstract class PaymentTypeDataMapper implements GenericDataMapper<PaymentModeItem, PaymentTypeData> {

	@Override
	@Mappings({
			@Mapping(target = "name", expression = "java(toLocalizedMap(source.allName()))")
	})
	public abstract PaymentTypeData fromItem(PaymentModeItem source);

	@Override
	@InheritConfiguration(name = "fromItem")
	public abstract void updateFromItem(PaymentModeItem source, @MappingTarget PaymentTypeData target);

	public Map<String, String> toLocalizedMap(Map<Locale, String> localized) {
		if(localized == null){
			return Maps.newHashMap();
		}
		return localized.entrySet()
				.stream()
				.collect(Collectors.toMap(entry -> entry.getKey().toString(), Map.Entry::getValue));
	}

	@AfterMapping
	public void defineTypeSpecificFields(PaymentModeItem source, @MappingTarget PaymentTypeData target) {
		target.setType(source.getMetaType().getTypeCode());
	}
}
