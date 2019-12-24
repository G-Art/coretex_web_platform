package com.coretex.commerce.mapper;

import com.coretex.commerce.data.GenericItemData;
import com.coretex.items.core.GenericItem;
import org.mapstruct.MappingTarget;

public interface GenericItemMapper<SOURCE extends GenericItemData, TARGET extends GenericItem> {

	TARGET toItem(SOURCE source);
	void updateToItem(SOURCE source, @MappingTarget  TARGET target);
}
