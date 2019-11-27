package com.coretex.commerce.admin.mapper;

import com.coretex.commerce.admin.data.LocaleData;
import com.coretex.items.core.LocaleItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class})
public interface LocaleDataMapper extends GenericDataMapper<LocaleItem, LocaleData>{

}
