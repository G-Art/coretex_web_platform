package com.coretex.commerce.mapper.minimal;

import com.coretex.commerce.data.minimal.MinimalOrderData;
import com.coretex.commerce.mapper.GenericDataMapper;
import com.coretex.commerce.mapper.ReferenceMapper;
import com.coretex.items.cx_core.OrderItem;
import com.coretex.items.cx_core.StoreItem;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class})
public interface MinimalOrderDataMapper extends GenericDataMapper<OrderItem, MinimalOrderData> {
	@Override
	@Mappings({
			@Mapping(target = "date", source = "createDate", dateFormat = "dd-MM-yyyy HH:mm")
	})
	MinimalOrderData fromItem(OrderItem source);

	@Override
	@InheritConfiguration(name = "fromItem")
	void updateFromItem(OrderItem source, @MappingTarget MinimalOrderData target);

	default String mapStore(StoreItem value) {
		return value.getName();
	}
}
