package com.coretex.commerce.admin.mapper;

import com.coretex.commerce.admin.data.GenericItemData;
import com.coretex.items.core.GenericItem;
import org.mapstruct.MappingTarget;

public interface GenericDataMapper<SOURCE extends GenericItem, TARGET extends GenericItemData> {

	TARGET fromItem(SOURCE source);
	void updateFromItem(SOURCE source, @MappingTarget TARGET target);

}
