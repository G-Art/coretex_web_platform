package com.coretex.commerce.admin.mapper.minimal;

import com.coretex.commerce.admin.data.minimal.MinimalMerchantStoreData;
import com.coretex.commerce.admin.mapper.GenericDataMapper;
import com.coretex.commerce.admin.mapper.LocaleDataMapper;
import com.coretex.commerce.admin.mapper.ReferenceMapper;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class, LocaleDataMapper.class})
public interface MinimalMerchantStoreDataMapper extends GenericDataMapper<MerchantStoreItem, MinimalMerchantStoreData> {

}
