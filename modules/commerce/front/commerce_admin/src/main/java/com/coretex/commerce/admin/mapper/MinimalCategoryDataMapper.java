package com.coretex.commerce.admin.mapper;

import com.coretex.commerce.admin.data.MinimalCategoryData;
import com.coretex.items.commerce_core_model.CategoryItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
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

	@Override
	@Mappings({
			@Mapping(target = "merchantStore", ignore = true)
	})
	CategoryItem toItem(MinimalCategoryData source);

	@Override
	@InheritConfiguration(name = "toItem")
	void updateToItem(MinimalCategoryData source, @MappingTarget CategoryItem target);

	default String mapStore(MerchantStoreItem value) {
		return value.getStoreName();
	}
}
