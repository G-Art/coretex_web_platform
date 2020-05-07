package com.coretex.commerce.mapper;

import com.coretex.commerce.data.CartData;
import com.coretex.commerce.delivery.api.actions.DeliveryTypeActionHandler;
import com.coretex.items.cx_core.CartItem;
import com.coretex.items.cx_core.StoreItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import javax.annotation.Resource;
import java.util.Objects;

@Mapper(componentModel = "spring",
		uses = {ReferenceMapper.class,
				AddressDataMapper.class,
				DeliveryTypeDataMapper.class,
				OrderEntryDataMapper.class,
				LocaleDataMapper.class,
				CustomerDataMapper.class,
				StoreDataMapper.class,
				CurrencyDataMapper.class})
public abstract class CartDataMapper implements GenericDataMapper<CartItem, CartData> {

	@Resource
	private DeliveryTypeActionHandler deliveryTypeActionHandler;

	protected String mapStore(StoreItem value) {
		return value.getName();
	}

	@AfterMapping
	public void defineTypeSpecificFields(CartItem source, @MappingTarget CartData target) {
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
