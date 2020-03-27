package com.coretex.commerce.mapper.forms;

import com.coretex.commerce.data.forms.ProductForm;
import com.coretex.commerce.mapper.ReferenceMapper;
import com.coretex.items.cx_core.ProductItem;
import org.apache.commons.lang3.LocaleUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring",
		uses = {ReferenceMapper.class})
public interface ProductFormMapper {

	@Mappings({
			@Mapping(target = "uuid", source = "uuid", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS),
			@Mapping(target = "name", ignore = true),
			@Mapping(target = "title", ignore = true),
			@Mapping(target = "description", ignore = true),
			@Mapping(target = "metaDescription", ignore = true),
			@Mapping(target = "metaKeywords", ignore = true)
	})
	ProductItem toItem(ProductForm source);

	@AfterMapping
	default void convertLocalizedFields(ProductForm source, @MappingTarget ProductItem target) {
		source.getName().forEach((key, value) -> target.setName(value, LocaleUtils.toLocale(key)));

		source.getDescription().forEach((key, value) -> target.setDescription(value, LocaleUtils.toLocale(key)));

		source.getTitle().forEach((key, value) -> target.setTitle(value, LocaleUtils.toLocale(key)));

		source.getMetaDescription().forEach((key, value) -> target.setMetaDescription(value, LocaleUtils.toLocale(key)));
		source.getMetaKeywords().forEach((key, value) -> target.setMetaKeywords(value, LocaleUtils.toLocale(key)));


	}
}
