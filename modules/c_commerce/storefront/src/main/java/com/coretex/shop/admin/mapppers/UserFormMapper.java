package com.coretex.shop.admin.mapppers;

import com.coretex.items.commerce_core_model.GroupItem;
import com.coretex.items.commerce_core_model.UserItem;
import com.coretex.shop.admin.data.GroupDto;
import com.coretex.shop.admin.forms.UserForm;
import com.coretex.shop.admin.mapppers.dto.GroupDtoMapper;
import com.coretex.shop.admin.mapppers.dto.LanguageDtoMapper;
import com.coretex.shop.admin.mapppers.dto.MerchantStoreDtoMapper;
import com.coretex.shop.admin.mapppers.dto.ReferenceMapper;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.UUID;

@Mapper(componentModel = "spring",
		uses = {ReferenceMapper.class,
				MerchantStoreDtoMapper.class,
				LanguageDtoMapper.class,
				GroupDtoMapper.class})
public abstract class UserFormMapper implements LocalizedFieldMapper<UserItem, UserForm> {

	@Mappings({
			@Mapping(target = "uuid", source = "uuid", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE),
			@Mapping(target = "password", ignore = true)
	})
	public abstract UserForm fromUserItem(UserItem source);

	@InheritConfiguration(name = "fromUserItem")
	public abstract void updateFromUserItem(UserItem source, @MappingTarget UserForm target);

	@Mappings({
			@Mapping(target = "uuid", source = "uuid", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE),
			@Mapping(target = "password", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	})
	public abstract UserItem toUserItem(UserForm source);

	@InheritConfiguration(name = "toUserItem")
	public abstract void updateToUserItem(UserForm source, @MappingTarget UserItem target);

	protected UUID mapToUUID(GroupItem groupItem){
		return groupItem.getUuid();
	}

	protected GroupDto mapToGroup(UUID uuid){
		var group = new GroupDto();
		group.setUuid(uuid);
		return group;
	}
}
