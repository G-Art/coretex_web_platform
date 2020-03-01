package com.coretex.commerce.mapper;

import com.coretex.commerce.data.OrderEntryData;
import com.coretex.items.cx_core.OrderEntryItem;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring",
		uses = {
				ReferenceMapper.class,
				VariantProductDataMapper.class})
public abstract class OrderEntryDataMapper implements GenericDataMapper<OrderEntryItem, OrderEntryData> {

	@Override
	public abstract OrderEntryData fromItem(OrderEntryItem orderProductItem);

	@Override
	@InheritConfiguration(name = "fromItem")
	public abstract void updateFromItem(OrderEntryItem orderProductItem, @MappingTarget OrderEntryData orderEntryData);

}
