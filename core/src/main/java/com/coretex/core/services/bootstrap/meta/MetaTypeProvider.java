package com.coretex.core.services.bootstrap.meta;

import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.items.core.MetaEnumValueTypeItem;
import com.coretex.items.core.MetaTypeItem;
import com.coretex.items.core.RegularTypeItem;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface MetaTypeProvider {

	RegularTypeItem getRegularType(String typeCode);

	RegularTypeItem getRegularType(Class typeCode);

	MetaTypeItem findMetaType(String typeCode);
	MetaTypeItem findMetaType(UUID typeCodeUUID);
	<E extends Enum> MetaEnumValueTypeItem findMetaEnumValueTypeItem(E enm);

	<E extends Enum> E findMetaEnumValueTypeItem(Class clazz, UUID uuid);

	MetaAttributeTypeItem findAttribute(String typeCode, String attributeName);

	Set<MetaTypeItem> getAllMetaTypes();

	Map<String, MetaAttributeTypeItem> getAllAttributes(String metaTypeCode);

	Map<String, MetaAttributeTypeItem> getAllAttributes(MetaTypeItem metaType);

	String getSqlTypeName(RegularTypeItem regularTypeItem);;
}
