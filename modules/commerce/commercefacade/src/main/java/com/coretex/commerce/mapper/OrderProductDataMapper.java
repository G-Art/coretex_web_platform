package com.coretex.commerce.mapper;

import com.coretex.commerce.core.services.ProductService;
import com.coretex.commerce.data.OrderProductData;
import com.coretex.commerce.data.ProductData;
import com.coretex.items.commerce_core_model.OrderProductItem;
import com.coretex.items.cx_core.ProductItem;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import javax.annotation.Resource;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class})
public abstract class OrderProductDataMapper implements GenericDataMapper<OrderProductItem, OrderProductData>{

	@Resource
	private ProductDataMapper productDataMapper;
	@Resource
	private ProductService productService;

	@Override
	@Mappings({
			@Mapping(target = "originalProduct", expression = "java(mapOriginal(orderProductItem))")
	})
	public abstract OrderProductData fromItem(OrderProductItem orderProductItem);

	@Override
	@InheritConfiguration(name = "fromItem")
	public abstract void updateFromItem(OrderProductItem orderProductItem, @MappingTarget OrderProductData orderProductData);

	public ProductData mapOriginal(OrderProductItem orderProductItem){
		ProductItem productItem = productService.getByCode(orderProductItem.getSku());
		return productDataMapper.fromItem(productItem);
	}
}
