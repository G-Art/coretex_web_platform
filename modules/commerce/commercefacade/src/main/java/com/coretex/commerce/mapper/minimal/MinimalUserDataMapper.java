package com.coretex.commerce.mapper.minimal;

import com.coretex.commerce.data.minimal.MinimalUserData;
import com.coretex.commerce.mapper.GenericDataMapper;
import com.coretex.commerce.mapper.LocaleDataMapper;
import com.coretex.commerce.mapper.ReferenceMapper;
import com.coretex.items.cx_core.GroupItem;
import com.coretex.items.cx_core.StoreItem;
import com.coretex.items.cx_core.UserItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
		uses = {ReferenceMapper.class,
				LocaleDataMapper.class})
public interface MinimalUserDataMapper extends GenericDataMapper<UserItem, MinimalUserData> {

	default String mapStore(StoreItem value) {
		return value.getName();
	}

	default String mapStore(GroupItem value) {
		return value.getGroupName();
	}
}
