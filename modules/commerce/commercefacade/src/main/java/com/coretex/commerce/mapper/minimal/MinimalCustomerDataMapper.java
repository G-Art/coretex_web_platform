package com.coretex.commerce.mapper.minimal;

import com.coretex.commerce.data.minimal.MinimalCustomerData;
import com.coretex.commerce.mapper.GenericDataMapper;
import com.coretex.commerce.mapper.LocaleDataMapper;
import com.coretex.commerce.mapper.ReferenceMapper;
import com.coretex.items.commerce_core_model.CustomerItem;
import com.coretex.items.commerce_core_model.GroupItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
		uses = {ReferenceMapper.class,
				LocaleDataMapper.class})
public interface MinimalCustomerDataMapper extends GenericDataMapper<CustomerItem, MinimalCustomerData> {

	default String mapStore(MerchantStoreItem value) {
		return value.getStoreName();
	}

	default String mapStore(GroupItem value) {
		return value.getGroupName();
	}
}
