package com.coretex.commerce.mapper;

import com.coretex.commerce.data.OrderData;
import com.coretex.items.cx_core.OrderItem;
import com.coretex.items.cx_core.StoreItem;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring",
		uses = {ReferenceMapper.class,
				AddressDataMapper.class,
				OrderEntryDataMapper.class,
				DeliveryTypeDataMapper.class,
				LocaleDataMapper.class,
				CustomerDataMapper.class,
				StoreDataMapper.class,
				CurrencyDataMapper.class,
				PaymentTypeDataMapper.class})
public interface OrderDataMapper extends GenericDataMapper<OrderItem, OrderData> {
	@Override
	@Mappings({
			@Mapping(target = "date", source = "createDate", dateFormat = "dd-MM-yyyy HH:mm")
	})
	OrderData fromItem(OrderItem source);

	@Override
	@InheritConfiguration(name = "fromItem")
	void updateFromItem(OrderItem source, @MappingTarget OrderData target);


	default String mapStore(StoreItem value) {
		return value.getName();
	}
}
