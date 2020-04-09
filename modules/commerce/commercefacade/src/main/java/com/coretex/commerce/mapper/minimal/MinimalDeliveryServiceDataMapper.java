package com.coretex.commerce.mapper.minimal;

import com.coretex.commerce.data.minimal.MinimalDeliveryServiceData;
import com.coretex.commerce.mapper.GenericDataMapper;
import com.coretex.commerce.mapper.LocaleDataMapper;
import com.coretex.commerce.mapper.ReferenceMapper;
import com.coretex.items.cx_commercedelivery_api.DeliveryServiceItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring",
		uses = {ReferenceMapper.class,
				LocaleDataMapper.class})
public interface MinimalDeliveryServiceDataMapper extends GenericDataMapper<DeliveryServiceItem, MinimalDeliveryServiceData> {

	@AfterMapping
	default void defineTypeSpecificFields(DeliveryServiceItem source, @MappingTarget MinimalDeliveryServiceData target) {
		target.setType(source.getMetaType().getTypeCode());
	}
}
