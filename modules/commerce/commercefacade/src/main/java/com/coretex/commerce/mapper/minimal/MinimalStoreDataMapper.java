package com.coretex.commerce.mapper.minimal;

import com.coretex.commerce.data.minimal.MinimalStoreData;
import com.coretex.commerce.mapper.GenericDataMapper;
import com.coretex.commerce.mapper.LocaleDataMapper;
import com.coretex.commerce.mapper.ReferenceMapper;
import com.coretex.items.cx_core.StoreItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class, LocaleDataMapper.class})
public interface MinimalStoreDataMapper extends GenericDataMapper<StoreItem, MinimalStoreData> {

}
