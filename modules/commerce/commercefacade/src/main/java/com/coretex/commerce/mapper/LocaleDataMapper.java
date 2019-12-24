package com.coretex.commerce.mapper;

import com.coretex.commerce.data.LocaleData;
import com.coretex.items.core.LocaleItem;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class})
public interface LocaleDataMapper extends GenericDataMapper<LocaleItem, LocaleData>{

	@Override
	@Mappings({
			@Mapping(target = "isocode", source = "iso")
	})
	LocaleData fromItem(LocaleItem localeItem);

	@Override
	@InheritConfiguration(name = "fromItem")
	void updateFromItem(LocaleItem localeItem, @MappingTarget LocaleData localeData);
}
