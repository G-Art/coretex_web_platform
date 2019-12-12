package com.coretex.commerce.admin.mapper;

import com.coretex.commerce.admin.data.MerchantStoreData;
import com.coretex.commerce.admin.mapper.GenericDataMapper;
import com.coretex.commerce.admin.mapper.LocaleDataMapper;
import com.coretex.commerce.admin.mapper.ReferenceMapper;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.cx_core.StoreItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class, LocaleDataMapper.class})

public interface StoreDataMapper extends GenericDataMapper<StoreItem, MerchantStoreData> {

}
