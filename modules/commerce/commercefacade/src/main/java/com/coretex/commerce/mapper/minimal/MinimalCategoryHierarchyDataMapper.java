package com.coretex.commerce.mapper.minimal;

import com.coretex.commerce.data.minimal.MinimalCategoryHierarchyData;
import com.coretex.commerce.mapper.GenericDataMapper;
import com.coretex.commerce.mapper.ReferenceMapper;
import com.coretex.items.cx_core.CategoryItem;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class})
public interface MinimalCategoryHierarchyDataMapper extends GenericDataMapper<CategoryItem, MinimalCategoryHierarchyData> {


	@Override
	@Mappings({
			@Mapping(target = "name", source = "name"),
			@Mapping(target = "root", expression = "java(source.getParent() == null)"),
			@Mapping(target = "children", source = "categories", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT)

	})
	MinimalCategoryHierarchyData fromItem(CategoryItem source);

	@Override
	@InheritConfiguration(name = "fromItem")
	void updateFromItem(CategoryItem source, @MappingTarget MinimalCategoryHierarchyData target);

}
