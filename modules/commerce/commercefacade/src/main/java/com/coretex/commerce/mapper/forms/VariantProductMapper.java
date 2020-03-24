package com.coretex.commerce.mapper.forms;

import com.coretex.commerce.data.forms.ProductForm;
import com.coretex.items.cx_core.ProductItem;
import com.coretex.items.cx_core.VariantProductItem;
import org.apache.commons.lang3.LocaleUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;

public interface VariantProductMapper<T extends VariantProductItem> {

	T toItem(ProductForm source);

	@AfterMapping
	default void convertLocalizedFields(ProductForm source, @MappingTarget ProductItem target) {
		source.getName().forEach((key, value) -> target.setName(value, LocaleUtils.toLocale(key)));

		source.getDescription().forEach((key, value) -> target.setDescription(value, LocaleUtils.toLocale(key)));

		source.getTitle().forEach((key, value) -> target.setTitle(value, LocaleUtils.toLocale(key)));

		source.getMetaDescription().forEach((key, value) -> target.setMetaDescription(value, LocaleUtils.toLocale(key)));
		source.getMetaKeywords().forEach((key, value) -> target.setMetaKeywords(value, LocaleUtils.toLocale(key)));
	}
}
