package com.coretex.shop.admin.mapppers.dto;

import com.coretex.items.commerce_core_model.GroupItem;
import com.coretex.shop.admin.data.GroupDto;
import com.coretex.shop.admin.mapppers.LocalizedFieldMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class})
public interface GroupDtoMapper extends GenericDtoMapper<GroupItem, GroupDto>, LocalizedFieldMapper<GroupItem, GroupDto> {

}
