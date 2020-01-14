package com.coretex.shop.admin.mapppers;

import com.coretex.items.cx_core.CategoryItem;
import com.coretex.shop.admin.forms.CategoryForm;
import com.coretex.shop.admin.mapppers.dto.CategoryDtoMapper;
import com.coretex.shop.admin.mapppers.dto.ReferenceMapper;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.UUID;

@Mapper(componentModel = "spring", imports = UUID.class, uses = {ReferenceMapper.class, CategoryDtoMapper.class})
public abstract class CategoryFormMapper implements LocalizedFieldMapper<CategoryItem, CategoryForm>  {

	@Mappings({
			@Mapping(target = "uuid", source = "uuid", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE),
			@Mapping(target = "name", ignore = true),
			@Mapping(target = "title", ignore = true),
			@Mapping(target = "description", ignore = true),
			@Mapping(target = "metaTitle", ignore = true),
			@Mapping(target = "metaKeywords", ignore = true),
			@Mapping(target = "metaDescription", ignore = true)
	})
	public abstract CategoryForm fromCategoryItem(CategoryItem source);

	@InheritConfiguration(name = "fromCategoryItem")
	public abstract void updateFromCategoryItem(CategoryItem source, @MappingTarget CategoryForm target);

	@InheritInverseConfiguration(name = "fromCategoryItem")
	@Mappings({
			@Mapping(target = "uuid", source = "uuid", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE),
			@Mapping(target = "name", ignore = true),
			@Mapping(target = "title", ignore = true),
			@Mapping(target = "description", ignore = true),
			@Mapping(target = "metaTitle", ignore = true),
			@Mapping(target = "metaKeywords", ignore = true),
			@Mapping(target = "metaDescription", ignore = true)
	})
	public abstract CategoryItem toCategoryItem(CategoryForm source);

	@InheritConfiguration(name = "toCategoryItem")
	public abstract void updateToCategoryItem(CategoryForm source, @MappingTarget CategoryItem target);
}
