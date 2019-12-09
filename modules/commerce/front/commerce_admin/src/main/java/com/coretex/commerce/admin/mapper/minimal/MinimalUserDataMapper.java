package com.coretex.commerce.admin.mapper.minimal;

import com.coretex.commerce.admin.data.minimal.MinimalUserData;
import com.coretex.commerce.admin.mapper.GenericDataMapper;
import com.coretex.commerce.admin.mapper.LocaleDataMapper;
import com.coretex.commerce.admin.mapper.ReferenceMapper;
import com.coretex.items.commerce_core_model.GroupItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.UserItem;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring",
		uses = {ReferenceMapper.class,
				LocaleDataMapper.class})
public interface MinimalUserDataMapper extends GenericDataMapper<UserItem, MinimalUserData> {

	default String mapStore(MerchantStoreItem value) {
		return value.getStoreName();
	}

	default String mapStore(GroupItem value) {
		return value.getGroupName();
	}
}
