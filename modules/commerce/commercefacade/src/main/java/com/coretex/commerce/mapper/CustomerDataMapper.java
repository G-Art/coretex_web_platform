package com.coretex.commerce.mapper;

import com.coretex.commerce.data.CustomerData;
import com.coretex.commerce.data.UserData;
import com.coretex.items.cx_core.CustomerItem;
import com.coretex.items.cx_core.UserItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
		uses = {ReferenceMapper.class,
				LocaleDataMapper.class,
				GroupDataMapper.class,
				StoreDataMapper.class})
public interface CustomerDataMapper extends GenericDataMapper<CustomerItem, CustomerData>, GenericItemMapper<UserData, UserItem> {

}
