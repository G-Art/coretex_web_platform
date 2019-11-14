package com.coretex.shop.admin.mapppers.dto;

import com.coretex.items.commerce_core_model.ProductPriceItem;
import com.coretex.shop.admin.data.ProductPriceDto;
import com.coretex.shop.admin.mapppers.LocalizedFieldMapper;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class})
public interface ProductPriceDtoMapper extends GenericDtoMapper<ProductPriceItem, ProductPriceDto>, LocalizedFieldMapper<ProductPriceItem, ProductPriceDto> {

	@Override
	@Mappings({
			@Mapping(target = "name", ignore = true),
			@Mapping(target = "title", ignore = true),
			@Mapping(target = "description", ignore = true)
	})
	ProductPriceDto fromItem(ProductPriceItem productPriceItem);

	@Override
	@InheritConfiguration(name = "fromItem")
	void updateFromItem(ProductPriceItem productPriceItem, @MappingTarget ProductPriceDto productPriceDto);

	@Override
	@Mappings({
			@Mapping(target = "name", ignore = true),
			@Mapping(target = "title", ignore = true),
			@Mapping(target = "description", ignore = true)
	})
	ProductPriceItem toItem(ProductPriceDto source);

	@Override
	@InheritConfiguration(name = "toItem")
	void updateToItem(ProductPriceDto source, @MappingTarget ProductPriceItem target);
}
