package com.coretex.commerce.mapper;

import com.coretex.commerce.data.DeliveryServiceData;
import com.coretex.items.cx_commercedelivery_api.DeliveryServiceItem;
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
public interface DeliveryServiceDataMapper extends GenericDataMapper<DeliveryServiceItem, DeliveryServiceData> {

	@Override
	@Mappings({
			@Mapping(target = "name", expression = "java(toLocalizedMap(source.allName()))")
	})
	DeliveryServiceData fromItem(DeliveryServiceItem source);

	@Override
	@InheritConfiguration(name = "fromItem")
	void updateFromItem(DeliveryServiceItem source, @MappingTarget DeliveryServiceData target);

	default Map<String, String> toLocalizedMap(Map<Locale, String> localized) {
		if (localized == null) {
			return Maps.newHashMap();
		}
		return localized.entrySet()
				.stream()
				.collect(Collectors.toMap(entry -> entry.getKey().toString(), Map.Entry::getValue));
	}

	@AfterMapping
	default void defineTypeSpecificFields(DeliveryServiceItem source, @MappingTarget DeliveryServiceData target) {
		target.setType(source.getMetaType().getTypeCode());

		source.getMetaType()
				.getItemAttributes()
				.forEach(metaAttributeTypeItem -> {
					var value = metaAttributeTypeItem.getLocalized() ?
							source.getItemContext().getOriginLocalizedValues(metaAttributeTypeItem.getAttributeName()) :
							source.getAttributeValue(metaAttributeTypeItem.getAttributeName());
					target.getAdditionalData().put(metaAttributeTypeItem.getAttributeName(), value);
				});

	}
}
