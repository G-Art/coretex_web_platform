package com.coretex.shop.admin.mapppers.dto;

import com.coretex.items.core.GenericItem;
import com.coretex.shop.admin.data.GenericDto;
import org.mapstruct.MappingTarget;

public interface GenericDtoMapper<SOURCE extends GenericItem, TARGET extends GenericDto> {

	TARGET fromItem(SOURCE source);
	void updateFromItem(SOURCE source, @MappingTarget TARGET target);

	SOURCE toItem(TARGET source);
	void updateToItem(TARGET source, @MappingTarget SOURCE target);
}
