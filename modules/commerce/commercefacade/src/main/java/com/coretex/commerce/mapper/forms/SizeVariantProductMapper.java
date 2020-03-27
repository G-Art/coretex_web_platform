package com.coretex.commerce.mapper.forms;

import com.coretex.commerce.data.forms.ProductForm;
import com.coretex.commerce.mapper.ReferenceMapper;
import com.coretex.items.cx_core.SizeVariantProductItem;
import org.apache.commons.lang3.LocaleUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring",
		uses = {ReferenceMapper.class})
public interface SizeVariantProductMapper extends VariantProductMapper<SizeVariantProductItem> {

	@Mappings({
			@Mapping(target = "uuid", source = "uuid", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS),
			@Mapping(target = "name", ignore = true),
			@Mapping(target = "title", ignore = true),
			@Mapping(target = "description", ignore = true),
			@Mapping(target = "size", ignore = true),
			@Mapping(target = "metaDescription", ignore = true),
			@Mapping(target = "metaKeywords", ignore = true)
	})
	SizeVariantProductItem toItem(ProductForm source);

	@AfterMapping
	default void defineTypeSpecificFields(ProductForm source, @MappingTarget SizeVariantProductItem target) {
		source.getSize().forEach((key, value) -> target.setSize(value, LocaleUtils.toLocale(key)));
	}
}
