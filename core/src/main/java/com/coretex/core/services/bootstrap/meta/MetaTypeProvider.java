package com.coretex.core.services.bootstrap.meta;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.items.core.MetaEnumValueTypeItem;
import com.coretex.items.core.MetaTypeItem;
import com.coretex.items.core.RegularTypeItem;

public interface MetaTypeProvider {

	Map<Class, RegularTypeItem> getRegularType(String typeCode);

	Map<String, RegularTypeItem> getRegularType(Class typeCode);

	MetaTypeItem findMetaType(String typeCode);
	MetaTypeItem findMetaType(UUID typeCodeUUID);
	<E extends Enum> MetaEnumValueTypeItem findMetaEnumValueTypeItem(E enm);

	<E extends Enum> E findMetaEnumValueTypeItem(Class clazz, UUID uuid);

	MetaAttributeTypeItem findAttribute(String typeCode, String attributeName);

	Set<MetaTypeItem> getAllMetaTypes();

	Map<String, MetaAttributeTypeItem> getAllAttributes(String metaTypeCode);

	Map<String, MetaAttributeTypeItem> getAllAttributes(MetaTypeItem metaType);

	String getSqlTypeName(RegularTypeItem regularTypeItem);
	Integer getSqlType(RegularTypeItem regularTypeItem);
}
