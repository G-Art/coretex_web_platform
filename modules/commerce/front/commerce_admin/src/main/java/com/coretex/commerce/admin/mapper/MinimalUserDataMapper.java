package com.coretex.commerce.admin.mapper;

import com.coretex.commerce.admin.data.MinimalUserData;
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

	@Override
	@Mappings({
			@Mapping(target = "merchantStore", ignore = true),
			@Mapping(target = "groups", ignore = true)
	})
	UserItem toItem(MinimalUserData source);

	@Override
	@InheritConfiguration(name = "toItem")
	void updateToItem(MinimalUserData source, @MappingTarget UserItem target);

	default String mapStore(MerchantStoreItem value) {
		return value.getStoreName();
	}

	default String mapStore(GroupItem value) {
		return value.getGroupName();
	}
}
