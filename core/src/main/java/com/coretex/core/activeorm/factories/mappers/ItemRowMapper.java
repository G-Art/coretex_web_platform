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
import io.r2dbc.spi.ColumnMetadata;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
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
	public T mapRow(Row row, RowMetadata rowMetadata) {

		int columnCount = rowMetadata.getColumnNames().size();
		Map<String, ColumnValueProvider> mapOfColValues = new LinkedCaseInsensitiveMap<>();
		for (int i = 0; i < columnCount; i++) {
			ColumnMetadata columnMetadata = rowMetadata.getColumnMetadata(i);
			mapOfColValues.put(columnMetadata.getName(), new ColumnValueProvider(row, columnMetadata));
		}

		MetaTypeItem typeMetaType = ((MetaTypeItem) mapOfColValues.get(metaTypeAttributeTypeItem.getColumnName()).apply(metaTypeAttributeTypeItem));

		if (ItemUtils.isSystemType(typeMetaType)) {
			if (ItemUtils.isMetaAttributeType(typeMetaType)) {
				return (T) cortexContext.findMetaAttributeTypeItem((UUID) mapOfColValues.get(uuidAttributeTypeItem.getColumnName()).apply(uuidAttributeTypeItem));
			}
			return (T) cortexContext.findMetaType((UUID) mapOfColValues.get(uuidAttributeTypeItem.getColumnName()).apply(uuidAttributeTypeItem));
		}
		Class<T> targetClass = typeMetaType.getItemClass();
		ItemContext item = itemContextFactory.create(targetClass, (UUID) mapOfColValues.get(uuidAttributeTypeItem.getColumnName()).apply(uuidAttributeTypeItem));

		cortexContext.getAllAttributes(typeMetaType.getTypeCode()).values().stream()
				.filter(metaAttributeTypeItem -> mapOfColValues.containsKey(metaAttributeTypeItem.getColumnName()))
				.filter(metaAttributeTypeItem -> AttributeTypeUtils.isRegularTypeAttribute(metaAttributeTypeItem) ||
						(AttributeTypeUtils.isItemAttribute(metaAttributeTypeItem) &&
								(((MetaTypeItem) metaAttributeTypeItem.getAttributeType()).getSubtypes() == null ||
										((MetaTypeItem) metaAttributeTypeItem.getAttributeType()).getSubtypes().isEmpty())))
				.forEach(metaAttributeTypeItem -> item.initValue(metaAttributeTypeItem.getAttributeName(), mapOfColValues.get(metaAttributeTypeItem.getColumnName()).apply(metaAttributeTypeItem)));
		return ItemUtils.createItem(targetClass, item);

	}


	private class ColumnValueProvider implements Function<MetaAttributeTypeItem, Object> {

		private Row row;
		private ColumnMetadata columnMetadata;

		private ColumnValueProvider(Row row, ColumnMetadata columnMetadata) {
			this.row = row;
			this.columnMetadata = columnMetadata;
		}

		@Override
		public Object apply(MetaAttributeTypeItem metaAttributeTypeItem) {
			try {
				if (metaAttributeTypeItem.getAttributeType() instanceof MetaTypeItem && Arrays.asList(MetaTypeItem.ITEM_TYPE, MetaRelationTypeItem.ITEM_TYPE).contains(((MetaTypeItem) metaAttributeTypeItem.getAttributeType()).getTypeCode())) {
					return cortexContext.findMetaType(row.get(columnMetadata.getName(), UUID.class));
				}
				if (AttributeTypeUtils.isEnumTypeAttribute(metaAttributeTypeItem)) {
					return cortexContext.findMetaEnumValueTypeItem(((MetaEnumTypeItem) metaAttributeTypeItem.getAttributeType()).getEnumClass(), row.get(columnMetadata.getName(), UUID.class));
				}
				if (AttributeTypeUtils.isRegularTypeAttribute(metaAttributeTypeItem)) {
					return row.get(columnMetadata.getName(), ((RegularTypeItem) metaAttributeTypeItem.getAttributeType()).getRegularClass());
				} else {
					Class<AbstractGenericItem> itemClass = ((MetaTypeItem) metaAttributeTypeItem.getAttributeType()).getItemClass();
					var value = row.get(columnMetadata.getName(), UUID.class);
					return Objects.nonNull(value) ?
							ItemUtils.createItem(itemClass, itemContextFactory.create(itemClass, value)) :
							null;
				}
			} catch (Exception e) {
				if (LOG.isDebugEnabled()) {
					LOG.error(String.format("Can't read column %s", columnMetadata.getName()), e);
				}
				try {
					return row.get(columnMetadata.getName(), columnMetadata.getJavaType());
				} catch (Exception e1) {
					throw new RuntimeException(e1);
				}
			}
		}
	}
}
