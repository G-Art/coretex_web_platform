package com.coretex.shop.admin.mapppers.dto;

import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.shop.admin.data.MerchantStoreDto;
import com.coretex.shop.admin.mapppers.LocalizedFieldMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class})
public interface MerchantStoreDtoMapper extends GenericDtoMapper<MerchantStoreItem, MerchantStoreDto>, LocalizedFieldMapper<MerchantStoreItem, MerchantStoreDto> {
}
