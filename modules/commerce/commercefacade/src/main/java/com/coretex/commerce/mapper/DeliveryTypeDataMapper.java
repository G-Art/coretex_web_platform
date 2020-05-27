package com.coretex.commerce.mapper;

import com.coretex.commerce.data.DeliveryTypeData;
import com.coretex.commerce.delivery.api.actions.DeliveryTypeActionHandler;
import com.coretex.items.cx_commercedelivery_api.DeliveryTypeItem;
import com.google.common.collect.Maps;
import org.mapstruct.AfterMapping;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import javax.annotation.Resource;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
		uses = {ReferenceMapper.class,
				StoreDataMapper.class,
				CountryDataMapper.class,
				LocaleDataMapper.class})
public abstract class DeliveryTypeDataMapper implements GenericDataMapper<DeliveryTypeItem, DeliveryTypeData> {

	@Resource
	private DeliveryTypeActionHandler deliveryTypeActionHandler;

	@Override
	@Mappings({
			@Mapping(target = "name", expression = "java(toLocalizedMap(source.allName()))"),
			@Mapping(target = "deliveryService", source = "deliveryService.uuid"),
			@Mapping(target = "deliveryServiceCode", source = "deliveryService.code")
	})
	public abstract DeliveryTypeData fromItem(DeliveryTypeItem source);

	@Override
	@InheritConfiguration(name = "fromItem")
	public abstract void updateFromItem(DeliveryTypeItem source, @MappingTarget DeliveryTypeData target);

	public Map<String, String> toLocalizedMap(Map<Locale, String> localized) {
		if(localized == null){
			return Maps.newHashMap();
		}
		return localized.entrySet()
				.stream()
				.collect(Collectors.toMap(entry -> entry.getKey().toString(), Map.Entry::getValue));
	}

	@AfterMapping
	public void defineTypeSpecificFields(DeliveryTypeItem source, @MappingTarget DeliveryTypeData target) {
		target.setType(source.getMetaType().getTypeCode());
		target.setAdditionalInfo(deliveryTypeActionHandler.getAdditionalInfo(source));
	}
}
