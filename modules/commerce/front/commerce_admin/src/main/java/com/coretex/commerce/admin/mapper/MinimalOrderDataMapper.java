package com.coretex.commerce.admin.mapper;

import com.coretex.commerce.admin.data.MinimalOrderData;
import com.coretex.items.commerce_core_model.OrderItem;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class})
public interface MinimalOrderDataMapper extends GenericDataMapper<OrderItem, MinimalOrderData> {
	@Override
	@Mappings({
			@Mapping(target = "name", source = "delivery.firstName"),
			@Mapping(target = "date", source = "createDate", dateFormat = "dd-MM-yyyy HH:mm")
	})
	MinimalOrderData fromItem(OrderItem source);

	@Override
	@InheritConfiguration(name = "fromItem")
	void updateFromItem(OrderItem source, @MappingTarget MinimalOrderData target);

}
