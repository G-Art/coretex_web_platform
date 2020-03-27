package com.coretex.commerce.mapper.forms;

import com.coretex.commerce.data.forms.ProductForm;
import com.coretex.commerce.mapper.ReferenceMapper;
import com.coretex.items.cx_core.StyleDescriptionItem;
import com.coretex.items.cx_core.StyleVariantProductItem;
import org.apache.commons.lang3.LocaleUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;

import java.util.Objects;

@Mapper(componentModel = "spring",
		uses = {ReferenceMapper.class})
public interface StyleVariantProductMapper extends VariantProductMapper<StyleVariantProductItem> {

	@Mappings({
			@Mapping(target = "uuid", source = "uuid", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS),
			@Mapping(target = "name", ignore = true),
			@Mapping(target = "title", ignore = true),
			@Mapping(target = "description", ignore = true),
			@Mapping(target = "metaDescription", ignore = true),
			@Mapping(target = "metaKeywords", ignore = true)
	})
	StyleVariantProductItem toItem(ProductForm source);

	@AfterMapping
	default void defineTypeSpecificFields(ProductForm source, @MappingTarget StyleVariantProductItem target) {
		var style = target.getStyle();
		if (Objects.isNull(style)){
			style = new StyleDescriptionItem();
		}
		style.setCssColorCode(source.getColorCode());
		StyleDescriptionItem finalStyle = style;
		source.getColorName().forEach((key, value) -> finalStyle.setStyleName(value, LocaleUtils.toLocale(key)));
		target.setStyle(finalStyle);
	}
}
