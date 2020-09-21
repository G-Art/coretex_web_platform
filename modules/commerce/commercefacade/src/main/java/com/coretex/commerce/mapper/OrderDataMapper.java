package com.coretex.commerce.mapper;

import com.coretex.commerce.data.OrderData;
import com.coretex.commerce.delivery.api.actions.DeliveryTypeActionHandler;
import com.coretex.items.cx_core.OrderItem;
import com.coretex.items.cx_core.StoreItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import javax.annotation.Resource;
import java.util.Objects;

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
public abstract class OrderDataMapper implements GenericDataMapper<OrderItem, OrderData> {
	@Resource
	private DeliveryTypeActionHandler deliveryTypeActionHandler;

	@Override
	@Mappings({
			@Mapping(target = "date", source = "createDate", dateFormat = "dd-MM-yyyy HH:mm")
	})
	public abstract OrderData fromItem(OrderItem source);

	@Override
	@InheritConfiguration(name = "fromItem")
	public abstract void updateFromItem(OrderItem source, @MappingTarget OrderData target);


	protected String mapStore(StoreItem value) {
		return value.getName();
	}

	@AfterMapping
	public void defineTypeSpecificFields(OrderItem source, @MappingTarget OrderData target) {
		if(Objects.nonNull(source.getDeliveryType())){
			var deliveryType = source.getDeliveryType();
			if(Objects.nonNull(source.getAddress())){
				var address = source.getAddress();
				var addressData = target.getAddress();
				if (Objects.nonNull(addressData)) {
					addressData.setAdditionalInfo(deliveryTypeActionHandler.addressAdditionalInfo(address, deliveryType));
				}
			}
		}
	}
}
