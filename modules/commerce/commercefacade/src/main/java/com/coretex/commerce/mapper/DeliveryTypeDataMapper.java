package com.coretex.commerce.mapper;

import com.coretex.commerce.data.DeliveryTypeData;
import com.coretex.items.cx_commercedelivery_api.DeliveryTypeItem;
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
public interface DeliveryTypeDataMapper extends GenericDataMapper<DeliveryTypeItem, DeliveryTypeData> {

	@Override
	@Mappings({
			@Mapping(target = "name", expression = "java(toLocalizedMap(source.allName()))")
	})
	DeliveryTypeData fromItem(DeliveryTypeItem source);

	@Override
	@InheritConfiguration(name = "fromItem")
	void updateFromItem(DeliveryTypeItem source, @MappingTarget DeliveryTypeData target);

	default Map<String, String> toLocalizedMap(Map<Locale, String> localized) {
		if(localized == null){
			return Maps.newHashMap();
		}
		return localized.entrySet()
				.stream()
				.collect(Collectors.toMap(entry -> entry.getKey().toString(), Map.Entry::getValue));
	}

	@AfterMapping
	default void defineTypeSpecificFields(DeliveryTypeItem source, @MappingTarget DeliveryTypeData target) {
		target.setType(source.getMetaType().getTypeCode());
	}
}
