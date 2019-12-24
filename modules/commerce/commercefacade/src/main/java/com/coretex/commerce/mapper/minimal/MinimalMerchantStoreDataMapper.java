package com.coretex.commerce.mapper.minimal;

import com.coretex.commerce.data.minimal.MinimalMerchantStoreData;
import com.coretex.commerce.mapper.GenericDataMapper;
import com.coretex.commerce.mapper.LocaleDataMapper;
import com.coretex.commerce.mapper.ReferenceMapper;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class, LocaleDataMapper.class})
public interface MinimalMerchantStoreDataMapper extends GenericDataMapper<MerchantStoreItem, MinimalMerchantStoreData> {

}
