package com.coretex.shop.admin.mapppers.dto;

import com.coretex.items.core.LocaleItem;
import com.coretex.items.commerce_core_model.ManufacturerItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.shop.admin.data.LanguageDto;
import com.coretex.shop.admin.data.ManufacturerDto;
import com.coretex.shop.admin.data.MerchantStoreDto;
import com.coretex.shop.admin.mapppers.LocalizedFieldMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class})
public interface LanguageDtoMapper extends GenericDtoMapper<LocaleItem, LanguageDto>, LocalizedFieldMapper<LocaleItem, LanguageDto> {

}
