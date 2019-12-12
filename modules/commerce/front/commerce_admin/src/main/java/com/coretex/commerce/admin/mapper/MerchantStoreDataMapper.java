package com.coretex.commerce.admin.mapper;

import com.coretex.commerce.admin.data.MerchantStoreData;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class, LocaleDataMapper.class})
@Deprecated(forRemoval = true)
public interface MerchantStoreDataMapper extends GenericDataMapper<MerchantStoreItem, MerchantStoreData> {

}
