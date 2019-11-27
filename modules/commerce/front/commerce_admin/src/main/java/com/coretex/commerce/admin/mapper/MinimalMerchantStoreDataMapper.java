package com.coretex.commerce.admin.mapper;

import com.coretex.commerce.admin.data.MinimalMerchantStoreData;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class, LocaleDataMapper.class})
public interface MinimalMerchantStoreDataMapper extends GenericDataMapper<MerchantStoreItem, MinimalMerchantStoreData> {

}
