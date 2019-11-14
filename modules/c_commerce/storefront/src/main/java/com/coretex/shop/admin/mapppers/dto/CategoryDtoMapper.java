package com.coretex.shop.admin.mapppers.dto;

import com.coretex.items.commerce_core_model.CategoryItem;
import com.coretex.shop.admin.data.CategoryDto;
import com.coretex.shop.admin.mapppers.LocalizedFieldMapper;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class} )
public interface CategoryDtoMapper extends GenericDtoMapper<CategoryItem, CategoryDto>, LocalizedFieldMapper<CategoryItem, CategoryDto> {

	@Override
	@Mappings({
			@Mapping(target = "name", ignore = true),
			@Mapping(target = "title", ignore = true),
			@Mapping(target = "description", ignore = true),
			@Mapping(target = "categoryHighlight", ignore = true),
			@Mapping(target = "metatagTitle", ignore = true),
			@Mapping(target = "metatagKeywords", ignore = true),
			@Mapping(target = "metatagDescription", ignore = true)
	})
	CategoryDto fromItem(CategoryItem categoryItem);

	@Override
	@InheritConfiguration(name = "fromItem")
	void updateFromItem(CategoryItem categoryItem, @MappingTarget CategoryDto categoryDto);

	@Override
	@Mappings({
			@Mapping(target = "name", ignore = true),
			@Mapping(target = "title", ignore = true),
			@Mapping(target = "description", ignore = true),
			@Mapping(target = "categoryHighlight", ignore = true),
			@Mapping(target = "metatagTitle", ignore = true),
			@Mapping(target = "metatagKeywords", ignore = true),
			@Mapping(target = "metatagDescription", ignore = true)
	})
	CategoryItem toItem(CategoryDto source);

	@Override
	@InheritConfiguration(name = "toItem")
	void updateToItem(CategoryDto source, @MappingTarget CategoryItem target);
}
