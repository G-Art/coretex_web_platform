package com.coretex.core.services.bootstrap.impl;

import com.coretex.core.activeorm.translator.TypeTranslator;
import com.coretex.core.activeorm.translator.standard.BigDecimalTranslator;
import com.coretex.core.activeorm.translator.standard.BooleanTranslator;
import com.coretex.core.activeorm.translator.standard.ByteTranslator;
import com.coretex.core.activeorm.translator.standard.CharacterTranslator;
import com.coretex.core.activeorm.translator.standard.ClassTranslator;
import com.coretex.core.activeorm.translator.standard.DateTranslator;
import com.coretex.core.activeorm.translator.standard.DoubleTranslator;
import com.coretex.core.activeorm.translator.standard.FloatTranslator;
import com.coretex.core.activeorm.translator.standard.GregorianCalendarTranslator;
import com.coretex.core.activeorm.translator.standard.InstantTranslator;
import com.coretex.core.activeorm.translator.standard.IntegerTranslator;
import com.coretex.core.activeorm.translator.standard.LocalDateTimeTranslator;
import com.coretex.core.activeorm.translator.standard.LocalDateTranslator;
import com.coretex.core.activeorm.translator.standard.LocalTimeTranslator;
import com.coretex.core.activeorm.translator.standard.LongTranslator;
import com.coretex.core.activeorm.translator.standard.ObjectTranslator;
import com.coretex.core.activeorm.translator.standard.ShortTranslator;
import com.coretex.core.activeorm.translator.standard.SqlDateTranslator;
import com.coretex.core.activeorm.translator.standard.SqlTimeTranslator;
import com.coretex.core.activeorm.translator.standard.SqlTimestampTranslator;
import com.coretex.core.activeorm.translator.standard.StringTranslator;
import com.coretex.core.activeorm.translator.standard.UUIDTranslator;
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
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
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

	private Map<String, TypeTranslator<?>> typeTranslatorMap;

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

		typeTranslatorMap = new HashMap<>(50);

		// standard primitives (used by RowTranslator#initColumnTranslators)
		putTypeTranslator("boolean", new BooleanTranslator());
		putTypeTranslator("byte", new ByteTranslator());
		putTypeTranslator("double", new DoubleTranslator());
		putTypeTranslator("float", new FloatTranslator());
		putTypeTranslator("int", new IntegerTranslator());
		putTypeTranslator("long", new LongTranslator());
		putTypeTranslator("short", new ShortTranslator());
		putTypeTranslator("char", new CharacterTranslator());

		// standard types
		putTypeTranslator(BigDecimal.class, new BigDecimalTranslator());
		putTypeTranslator(Class.class, new ClassTranslator());
		putTypeTranslator(Character.class, new CharacterTranslator());
		putTypeTranslator(Boolean.class, new BooleanTranslator());
		putTypeTranslator(Byte.class, new ByteTranslator());
		putTypeTranslator(Double.class, new DoubleTranslator());
		putTypeTranslator(Float.class, new FloatTranslator());
		putTypeTranslator(Integer.class, new IntegerTranslator());
		putTypeTranslator(Long.class, new LongTranslator());
		putTypeTranslator(Short.class, new ShortTranslator());
		putTypeTranslator(Object.class, new ObjectTranslator());
		putTypeTranslator(String.class, new StringTranslator());
		putTypeTranslator(java.util.Date.class, new DateTranslator());
		putTypeTranslator(java.sql.Date.class, new SqlDateTranslator());
		putTypeTranslator(java.sql.Time.class, new SqlTimeTranslator());
		putTypeTranslator(java.sql.Timestamp.class, new SqlTimestampTranslator());
		putTypeTranslator(GregorianCalendar.class, new GregorianCalendarTranslator());
		putTypeTranslator(LocalDate.class, new LocalDateTranslator());
		putTypeTranslator(LocalTime.class, new LocalTimeTranslator());
		putTypeTranslator(LocalDateTime.class, new LocalDateTimeTranslator());
		putTypeTranslator(Instant.class, new InstantTranslator());
		putTypeTranslator(UUID.class, new UUIDTranslator());
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

	@Override
	public Integer getSqlType(RegularTypeItem regularTypeItem) {
		return dbDialectService.getSqlTypeId(getSqlTypeName(regularTypeItem));
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

	private void putTypeTranslator(Class<?> typeClass, TypeTranslator<?> typeTranslator) {
		putTypeTranslator(typeClass.getCanonicalName(), typeTranslator);
	}

	private void putTypeTranslator(String typeClassName, TypeTranslator<?> typeTranslator) {
		if (LOG.isDebugEnabled()) LOG.debug("adding " + typeTranslator + " for " + typeClassName);
		typeTranslatorMap.put(typeClassName, typeTranslator);
	}

	public TypeTranslator<?> getTypeTranslator(Class<?> typeClass) {
		String typeClassName = typeClass.getCanonicalName();
		return typeTranslatorMap.get(typeClassName);
	}

	public void setDbDialectService(DbDialectService dbDialectService) {
		this.dbDialectService = dbDialectService;
	}
}
