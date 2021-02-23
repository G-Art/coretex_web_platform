package com.coretex.core.services.bootstrap.impl;

import com.coretex.core.services.bootstrap.DbDialectService;
import com.coretex.core.services.bootstrap.meta.MetaCollector;
import com.coretex.core.services.bootstrap.meta.MetaTypeProvider;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.items.core.MetaEnumValueTypeItem;
import com.coretex.items.core.MetaTypeItem;
import com.coretex.items.core.RegularTypeItem;
import com.coretex.meta.AbstractGenericItem;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.ImmutableTable.Builder;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

import static com.google.common.collect.ImmutableMap.toImmutableMap;
import static java.util.Objects.nonNull;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

public class CortexContext implements MetaTypeProvider {

	private Logger LOG = LoggerFactory.getLogger(CortexContext.class);

	private ImmutableTable<Class, String, RegularTypeItem> regularTypes;
	private ImmutableTable<Class, String, MetaEnumValueTypeItem> metaEnumValueTypes;
	private ImmutableTable<Class, UUID, MetaEnumValueTypeItem> metaEnumValueTypesUuid;

	private ImmutableMap<String, MetaTypeItem> metaTypes;
	private ImmutableMap<UUID, MetaTypeItem> uuidMetaTypes;
	private ImmutableMap<String, Set<MetaTypeItem>> metaTypesForTable;

	private ImmutableTable<String, String, MetaAttributeTypeItem> metaTypesAttributes;
	private ImmutableMap<UUID, MetaAttributeTypeItem> uuidMetaTypesAttributes;

	private MetaCollector metaCollector;
	private DbDialectService dbDialectService;


	@PostConstruct
	private void init() {
		this.metaCollector.load();

		metaTypes = this.metaCollector.collectSystemMetaTypes().stream()
				.collect(toImmutableMap(MetaTypeItem::getTypeCode, Function.identity()));

		metaTypesForTable = this.metaCollector.collectSystemMetaTypes().stream()
				.collect(toImmutableMap(MetaTypeItem::getTableName,
						Set::of,
						(o, o2) -> Sets.union(o, o2).immutableCopy()));

		uuidMetaTypes = metaTypes.entrySet().stream()
				.collect(toImmutableMap(entry -> entry.getValue().getUuid(), Map.Entry::getValue));

		Builder<String, String, MetaAttributeTypeItem> builder = ImmutableTable.builder();
		for (Map.Entry<String, MetaTypeItem> metaTypeEntry : metaTypes.entrySet()) {
			String metaTypeCode = metaTypeEntry.getKey();
			MetaTypeItem metaType = metaTypeEntry.getValue();
			collectAttributes(new ArrayList<>(), metaType).forEach(attributeType ->
					builder.put(metaTypeCode, attributeType.getAttributeName(), attributeType));
		}
		metaTypesAttributes = builder.build();

		uuidMetaTypesAttributes = metaTypesAttributes.values().stream().collect(toImmutableMap(AbstractGenericItem::getUuid, Function.identity(), (v1, v2) -> v1));

		Builder<Class, String, RegularTypeItem> regularTypeItemBuilder = ImmutableTable.builder();
		this.metaCollector.collectSystemRegularTypeTypes().forEach(regularTypeItem ->
				regularTypeItemBuilder.put(regularTypeItem.getRegularClass(), regularTypeItem.getDbType().toLowerCase()
						, regularTypeItem)
		);

		regularTypes = regularTypeItemBuilder.build();

		Builder<Class, String, MetaEnumValueTypeItem> metaEnumValueTypesItemBuilder = ImmutableTable.builder();
		this.metaCollector.collectMetaEnumValueTypeItems().forEach(enumValue ->
				metaEnumValueTypesItemBuilder.put(enumValue.getOwner().getEnumClass(), enumValue.getValue()
						, enumValue)
		);

		metaEnumValueTypes = metaEnumValueTypesItemBuilder.build();

		Builder<Class, UUID, MetaEnumValueTypeItem> classUUIDMetaEnumValueTypeItemBuilder = ImmutableTable.builder();
		this.metaCollector.collectMetaEnumValueTypeItems().forEach(enumValue ->
				classUUIDMetaEnumValueTypeItemBuilder.put(enumValue.getOwner().getEnumClass(), enumValue.getUuid()
						, enumValue)
		);

		metaEnumValueTypesUuid = classUUIDMetaEnumValueTypeItemBuilder.build();

	}

	public void reload() {
		init();
	}

	public MetaCollector getMetaCollector() {
		return metaCollector;
	}

	public void setMetaCollector(MetaCollector metaCollector) {
		this.metaCollector = metaCollector;
	}

	@Override
	public MetaTypeItem findMetaType(String typeCode) {
		return metaTypes.get(typeCode);
	}

	public Set<MetaTypeItem> findMetaTypeForTable(String table){
		return metaTypesForTable.get(table);
	}

	public MetaTypeItem findMetaType(UUID typeCodeUUID) {
		return uuidMetaTypes.get(typeCodeUUID);
	}

	public MetaAttributeTypeItem findMetaAttributeTypeItem(UUID typeCodeUUID) {
		return uuidMetaTypesAttributes.get(typeCodeUUID);
	}

	@Override
	public <E extends Enum> MetaEnumValueTypeItem findMetaEnumValueTypeItem(E enm) {
		return metaEnumValueTypes.get(enm.getDeclaringClass(), enm.name().toLowerCase());
	}

	@Override
	public <E extends Enum> E findMetaEnumValueTypeItem(Class clazz, UUID uuid) {
		return (E) Enum.valueOf(clazz, metaEnumValueTypesUuid.get(clazz, uuid).getValue().toUpperCase());
	}

	@Override
	public MetaAttributeTypeItem findAttribute(String typeCode, String attributeName) {
		return getAllAttributes(typeCode).get(attributeName);
	}

	@Override
	public Set<MetaTypeItem> getAllMetaTypes() {
		return new HashSet<>(metaTypes.values());
	}

	@Override
	public RegularTypeItem getRegularType(String typeCode) {
		var column = regularTypes.column(typeCode);
		if(column.isEmpty()){
			return null;
		}
		return column.values().iterator().next();
	}

	@Override
	public RegularTypeItem getRegularType(Class typeCode) {
		var row = regularTypes.row(typeCode);
		if(row.isEmpty()){
			return null;
		}
		return row.values().iterator().next();
	}

	@Override
	public Map<String, MetaAttributeTypeItem> getAllAttributes(String metaTypeCode) {
		return metaTypesAttributes.row(metaTypeCode);
	}

	@Override
	public Map<String, MetaAttributeTypeItem> getAllAttributes(MetaTypeItem metaType) {
		return getAllAttributes(metaType.getTypeCode());
	}

	@Override
	public String getSqlTypeName(RegularTypeItem regularTypeItem) {
		return regularTypeItem.getDbType();
	}

	private List<MetaAttributeTypeItem> collectAttributes(List<MetaAttributeTypeItem> typeAttributes, MetaTypeItem metaType) {
		if (nonNull(metaType) && isNotEmpty(metaType.getItemAttributes())) {
			typeAttributes.addAll(metaType.getItemAttributes());
		}
		if (nonNull(metaType) && nonNull(metaType.getParent())) {
			collectAttributes(typeAttributes, metaType.getParent());
		}
		return typeAttributes;
	}


	public void setDbDialectService(DbDialectService dbDialectService) {
		this.dbDialectService = dbDialectService;
	}
}
