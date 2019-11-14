package com.coretex.shop.admin.mapppers.dto;

import com.coretex.items.commerce_core_model.ProductTypeItem;
import com.coretex.shop.admin.data.ProductTypeDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring" )
public interface ProductTypeDtoMapper extends GenericDtoMapper<ProductTypeItem, ProductTypeDto> {
}
