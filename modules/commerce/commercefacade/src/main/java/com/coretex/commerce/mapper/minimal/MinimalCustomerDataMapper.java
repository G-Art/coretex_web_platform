package com.coretex.commerce.mapper.minimal;

import com.coretex.commerce.data.minimal.MinimalCustomerData;
import com.coretex.commerce.mapper.GenericDataMapper;
import com.coretex.commerce.mapper.LocaleDataMapper;
import com.coretex.commerce.mapper.ReferenceMapper;
import com.coretex.items.cx_core.CustomerItem;
import com.coretex.items.cx_core.GroupItem;
import com.coretex.items.cx_core.StoreItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
		uses = {ReferenceMapper.class,
				LocaleDataMapper.class})
public interface MinimalCustomerDataMapper extends GenericDataMapper<CustomerItem, MinimalCustomerData> {

	default String mapStore(StoreItem value) {
		return value.getName();
	}

	default String mapStore(GroupItem value) {
		return value.getGroupName();
	}
}
