package com.coretex.commerce.admin.mapper;

import com.coretex.commerce.admin.data.OrderData;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.OrderItem;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring",
		uses = {ReferenceMapper.class,
				MerchantStoreDataMapper.class,
				AddressDataMapper.class,
				OrderProductDataMapper.class,
				OrderTotalDataMapper.class,
				CurrencyDataMapper.class})
public interface OrderDataMapper extends GenericDataMapper<OrderItem, OrderData> {
	@Override
	@Mappings({
			@Mapping(target = "name", source = "delivery.firstName"),
			@Mapping(target = "phone", source = "delivery.telephone"),
			@Mapping(target = "email", source = "customerEmailAddress"),
			@Mapping(target = "date", source = "createDate", dateFormat = "dd-MM-yyyy HH:mm")
	})
	OrderData fromItem(OrderItem source);

	@Override
	@InheritConfiguration(name = "fromItem")
	void updateFromItem(OrderItem source, @MappingTarget OrderData target);


	default String mapStore(MerchantStoreItem value) {
		return value.getStoreName();
	}
}
