package com.coretex.commerce.mapper.minimal;

import com.coretex.commerce.data.minimal.MinimalCategoryData;
import com.coretex.commerce.mapper.GenericDataMapper;
import com.coretex.commerce.mapper.ReferenceMapper;
import com.coretex.items.cx_core.CategoryItem;
import com.coretex.items.cx_core.StoreItem;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class})
public interface MinimalCategoryDataMapper extends GenericDataMapper<CategoryItem, MinimalCategoryData> {


	@Override
	@Mappings({
			@Mapping(target = "name", source = "name", defaultValue = "#"),
			@Mapping(target = "productCount", expression = "java(source.getProducts().size())"),
			@Mapping(target = "subCategoriesCount", expression = "java(source.getCategories().size())"),
			@Mapping(target = "parent", source = "parent", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT)

	})
	MinimalCategoryData fromItem(CategoryItem source);

	@Override
	@InheritConfiguration(name = "fromItem")
	void updateFromItem(CategoryItem source, @MappingTarget MinimalCategoryData target);

	default String mapStore(StoreItem value) {
		return value.getName();
	}
}
