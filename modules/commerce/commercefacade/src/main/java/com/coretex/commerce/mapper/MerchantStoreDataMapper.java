package com.coretex.commerce.mapper;

import com.coretex.commerce.data.MerchantStoreData;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class, LocaleDataMapper.class})
@Deprecated()
public interface MerchantStoreDataMapper extends GenericDataMapper<MerchantStoreItem, MerchantStoreData> {

}
