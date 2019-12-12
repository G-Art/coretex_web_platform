package com.coretex.commerce.admin.mapper;

import com.coretex.commerce.admin.data.ZoneData;
import com.coretex.items.cx_core.ZoneItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class})
public interface ZoneDataMapper extends GenericDataMapper<ZoneItem, ZoneData>{

}
