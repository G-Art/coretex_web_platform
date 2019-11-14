package com.coretex.shop.admin.mapppers.dto;

import com.coretex.items.commerce_core_model.ProductAvailabilityItem;
import com.coretex.shop.admin.data.ProductAvailabilityDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductAvailabilityDtoMapper extends GenericDtoMapper<ProductAvailabilityItem, ProductAvailabilityDto> {
}
