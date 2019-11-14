package com.coretex.shop.admin.mapppers.dto;

import com.coretex.items.commerce_core_model.ManufacturerItem;
import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.shop.admin.data.ManufacturerDto;
import com.coretex.shop.admin.forms.ProductForm;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.UUID;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class})
public interface ManufacturerDtoMapper extends GenericDtoMapper<ManufacturerItem, ManufacturerDto> {

}
