package com.coretex.commerce.mapper;

import com.coretex.commerce.data.GroupData;
import com.coretex.items.commerce_core_model.GroupItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class, LocaleDataMapper.class})
public interface GroupDataMapper extends GenericDataMapper<GroupItem, GroupData> {

}
