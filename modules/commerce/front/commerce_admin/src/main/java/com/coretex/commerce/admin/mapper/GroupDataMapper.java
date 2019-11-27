package com.coretex.commerce.admin.mapper;

import com.coretex.commerce.admin.data.GroupData;
import com.coretex.items.commerce_core_model.GroupItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class, LocaleDataMapper.class})
public interface GroupDataMapper extends GenericDataMapper<GroupItem, GroupData> {

}
