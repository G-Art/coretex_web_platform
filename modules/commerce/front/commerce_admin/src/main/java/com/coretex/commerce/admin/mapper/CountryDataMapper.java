package com.coretex.commerce.admin.mapper;

import com.coretex.commerce.admin.data.CountryData;
import com.coretex.commerce.admin.data.CurrencyData;
import com.coretex.items.cx_core.CurrencyItem;
import com.coretex.items.core.CountryItem;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class})
public interface CountryDataMapper extends GenericDataMapper<CountryItem, CountryData>{

}
