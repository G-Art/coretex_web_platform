package com.coretex.commerce.mapper.minimal;

import com.coretex.commerce.data.minimal.MinimalLocaleData;
import com.coretex.commerce.mapper.GenericDataMapper;
import com.coretex.commerce.mapper.ReferenceMapper;
import com.coretex.items.core.LocaleItem;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class})
public interface MinimalLocaleDataMapper extends GenericDataMapper<LocaleItem, MinimalLocaleData>{

	@Override
	@Mappings({
			@Mapping(target = "isocode", source = "iso")
	})
	MinimalLocaleData fromItem(LocaleItem localeItem);

	@Override
	@InheritConfiguration(name = "fromItem")
	void updateFromItem(LocaleItem localeItem, @MappingTarget MinimalLocaleData localeData);
}
