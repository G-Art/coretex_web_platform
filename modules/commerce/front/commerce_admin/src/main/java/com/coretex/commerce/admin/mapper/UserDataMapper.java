package com.coretex.commerce.admin.mapper;

import com.coretex.commerce.admin.data.UserData;
import com.coretex.items.commerce_core_model.UserItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
		uses = {ReferenceMapper.class,
				LocaleDataMapper.class,
				GroupDataMapper.class,
				MerchantStoreDataMapper.class})
public interface UserDataMapper extends GenericDataMapper<UserItem, UserData>, GenericItemMapper<UserData, UserItem> {

}
