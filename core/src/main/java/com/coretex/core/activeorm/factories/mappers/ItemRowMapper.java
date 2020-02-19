package com.coretex.core.activeorm.factories.mappers;

import com.coretex.core.general.utils.AttributeTypeUtils;
import com.coretex.core.general.utils.ItemUtils;
import com.coretex.core.services.bootstrap.impl.CortexContext;
import com.coretex.core.services.items.context.ItemContext;
import com.coretex.core.services.items.context.factory.ItemContextFactory;
import com.coretex.items.core.GenericItem;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.items.core.MetaEnumTypeItem;
import com.coretex.items.core.MetaRelationTypeItem;
import com.coretex.items.core.MetaTypeItem;
import com.coretex.items.core.RegularTypeItem;
import com.coretex.meta.AbstractGenericItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class ItemRowMapper<T extends AbstractGenericItem> implements RowMapper<T> {
	private Logger LOG = LoggerFactory.getLogger(ItemRowMapper.class);

	private CortexContext cortexContext;
	private ItemContextFactory itemContextFactory;
	private MetaAttributeTypeItem metaTypeAttributeTypeItem;
	private MetaAttributeTypeItem uuidAttributeTypeItem;

	public ItemRowMapper(CortexContext cortexContext, ItemContextFactory itemContextFactory) {
		this.cortexContext = cortexContext;
		this.itemContextFactory = itemContextFactory;

		this.metaTypeAttributeTypeItem = cortexContext.findAttribute(GenericItem.ITEM_TYPE, GenericItem.META_TYPE);
		this.uuidAttributeTypeItem = cortexContext.findAttribute(GenericItem.ITEM_TYPE, GenericItem.UUID);
	}

	@Override
	@SuppressWarnings("unchecked")
	public T mapRow(ResultSet rs, int rowNum) throws SQLException {

		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		Map<String, ColumnValueProvider> mapOfColValues = new LinkedCaseInsensitiveMap<>(columnCount);
		for (int i = 1; i <= columnCount; i++) {
			String key = JdbcUtils.lookupColumnName(rsmd, i);
			mapOfColValues.put(key, new ColumnValueProvider(rs, i));
		}

		MetaTypeItem typeMetaType = ((MetaTypeItem) mapOfColValues.get(metaTypeAttributeTypeItem.getColumnName()).apply(metaTypeAttributeTypeItem));
		Class<T> targetClass = typeMetaType.getItemClass();

		if (ItemUtils.isSystemType(typeMetaType)) {
			if (ItemUtils.isMetaAttributeType(typeMetaType)) {
				return (T) cortexContext.findMetaAttributeTypeItem((UUID) mapOfColValues.get(uuidAttributeTypeItem.getColumnName()).apply(uuidAttributeTypeItem));
			}
			return (T) cortexContext.findMetaType((UUID) mapOfColValues.get(uuidAttributeTypeItem.getColumnName()).apply(uuidAttributeTypeItem));
		}

		ItemContext item = itemContextFactory.create(targetClass, (UUID) mapOfColValues.get(uuidAttributeTypeItem.getColumnName()).apply(uuidAttributeTypeItem));

		cortexContext.getAllAttributes(typeMetaType.getTypeCode()).values().stream()
				.filter(metaAttributeTypeItem -> !(metaAttributeTypeItem.getAttributeType() instanceof MetaRelationTypeItem))
				.filter(metaAttributeTypeItem -> mapOfColValues.containsKey(metaAttributeTypeItem.getColumnName()))
				.filter(metaAttributeTypeItem ->  metaAttributeTypeItem.getAttributeType() instanceof MetaTypeItem &&
												 (((MetaTypeItem) metaAttributeTypeItem.getAttributeType()).getSubtypes() == null ||
												 ((MetaTypeItem) metaAttributeTypeItem.getAttributeType()).getSubtypes().isEmpty()))
				.forEach(metaAttributeTypeItem -> item.initValue(metaAttributeTypeItem.getAttributeName(), mapOfColValues.get(metaAttributeTypeItem.getColumnName()).apply(metaAttributeTypeItem)));
		return ItemUtils.createItem(targetClass, item);

	}


	private class ColumnValueProvider implements Function<MetaAttributeTypeItem, Object> {

		private ResultSet rs;
		private int index;

		private ColumnValueProvider(ResultSet rs, int index) {
			this.rs = rs;
			this.index = index;
		}

		@Override
		public Object apply(MetaAttributeTypeItem metaAttributeTypeItem) {
			try {
				if (metaAttributeTypeItem.getAttributeType() instanceof MetaTypeItem && Arrays.asList(MetaTypeItem.ITEM_TYPE, MetaRelationTypeItem.ITEM_TYPE).contains(((MetaTypeItem) metaAttributeTypeItem.getAttributeType()).getTypeCode())) {
					return cortexContext.findMetaType((UUID) JdbcUtils.getResultSetValue(rs, index));
				}
				if (AttributeTypeUtils.isEnumTypeAttribute(metaAttributeTypeItem)) {
					return cortexContext.findMetaEnumValueTypeItem(((MetaEnumTypeItem) metaAttributeTypeItem.getAttributeType()).getEnumClass(), (UUID) JdbcUtils.getResultSetValue(rs, index));
				}
				if (AttributeTypeUtils.isRegularTypeAttribute(metaAttributeTypeItem)) {
					return cortexContext.getTypeTranslator(((RegularTypeItem) metaAttributeTypeItem.getAttributeType()).getRegularClass()).read(rs, index);
				} else {
					Class<AbstractGenericItem> itemClass = ((MetaTypeItem) metaAttributeTypeItem.getAttributeType()).getItemClass();
					return ItemUtils.createItem(itemClass, itemContextFactory.create(itemClass, (UUID) JdbcUtils.getResultSetValue(rs, index)));
				}
			} catch (Exception e) {
				if (LOG.isDebugEnabled()) {
					LOG.error(String.format("Can't read column number %s", index), e);
				}
				try {
					return JdbcUtils.getResultSetValue(rs, index);
				} catch (SQLException e1) {
					throw new RuntimeException(e1);
				}
			}
		}
	}
}
