package com.coretex.commerce.mapper.minimal;

import com.coretex.commerce.core.services.ProductService;
import com.coretex.commerce.core.utils.ProductUtils;
import com.coretex.commerce.data.minimal.MinimalProductData;
import com.coretex.commerce.mapper.GenericDataMapper;
import com.coretex.commerce.mapper.LocaleDataMapper;
import com.coretex.commerce.mapper.ReferenceMapper;
import com.coretex.items.cx_core.ProductItem;
import com.coretex.items.cx_core.StoreItem;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import javax.annotation.Resource;
import java.util.Objects;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class, LocaleDataMapper.class})
public abstract class MinimalProductDataMapper implements GenericDataMapper<ProductItem, MinimalProductData> {

	@Resource
	private ProductService productService;

	@Override
	@Mappings({
			@Mapping(target = "image", expression = "java(getDefaultImage(productItem))")
	})
	public abstract MinimalProductData fromItem(ProductItem productItem);

	@Override
	@InheritConfiguration(name = "fromItem")
	public abstract void updateFromItem(ProductItem productItem,@MappingTarget MinimalProductData minimalProductData);


	protected String getDefaultImage(ProductItem productItem){
		var defaultImage = productService.getDefaultImage(productItem);


		if(Objects.nonNull(defaultImage)){
			var product = defaultImage.getProduct();
			return ProductUtils.buildProductSmallImageUtils(product.getStore(), product.getCode(), defaultImage.getProductImage());
		}
		return null;
	}

	protected String mapStore(StoreItem value) {
		return value.getName();
	}
}
