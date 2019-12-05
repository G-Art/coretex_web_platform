package com.coretex.commerce.admin.mapper;

import com.coretex.commerce.admin.data.OrderData;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.OrderItem;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class})
public interface OrderDataMapper extends GenericDataMapper<OrderItem, OrderData> {
	@Override
	@Mappings({
			@Mapping(target = "name", source = "delivery.firstName"),
			@Mapping(target = "date", source = "createDate", dateFormat = "dd-MM-yyyy HH:mm")
	})
	OrderData fromItem(OrderItem source);

	@Override
	@InheritConfiguration(name = "fromItem")
	void updateFromItem(OrderItem source, @MappingTarget OrderData target);

	@Override
	@Mappings({
			@Mapping(target = "merchant", ignore = true)
	})
	OrderItem toItem(OrderData source);

	@Override
	@InheritConfiguration(name = "toItem")
	void updateToItem(OrderData source, @MappingTarget OrderItem target);

	default String mapStore(MerchantStoreItem value) {
		return value.getStoreName();
	}
}
