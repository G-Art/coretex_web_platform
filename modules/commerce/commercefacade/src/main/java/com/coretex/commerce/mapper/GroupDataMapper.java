package com.coretex.commerce.mapper;

import com.coretex.commerce.data.GroupData;
import com.coretex.items.cx_core.GroupItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class, LocaleDataMapper.class})
public interface GroupDataMapper extends GenericDataMapper<GroupItem, GroupData> {

}
