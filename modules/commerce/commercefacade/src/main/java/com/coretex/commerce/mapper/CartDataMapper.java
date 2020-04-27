package com.coretex.commerce.mapper;

import com.coretex.commerce.data.CartData;
import com.coretex.items.cx_core.CartItem;
import com.coretex.items.cx_core.StoreItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
		uses = {ReferenceMapper.class,
				AddressDataMapper.class,
				DeliveryTypeDataMapper.class,
				OrderEntryDataMapper.class,
				LocaleDataMapper.class,
				CustomerDataMapper.class,
				StoreDataMapper.class,
				CurrencyDataMapper.class})
public interface CartDataMapper extends GenericDataMapper<CartItem, CartData> {


	default String mapStore(StoreItem value) {
		return value.getName();
	}
}
