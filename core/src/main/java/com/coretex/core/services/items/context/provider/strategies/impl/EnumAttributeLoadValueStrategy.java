package com.coretex.core.services.items.context.provider.strategies.impl;

import com.coretex.core.activeorm.exceptions.SearchException;
import com.coretex.core.activeorm.query.specs.select.SelectOperationSpec;
import com.coretex.core.services.items.context.ItemContext;
import com.coretex.core.services.items.context.provider.strategies.AbstractLoadAttributeValueStrategy;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.items.core.MetaEnumTypeItem;
import com.coretex.items.core.MetaRelationTypeItem;
import com.coretex.meta.AbstractGenericItem;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;

public class EnumAttributeLoadValueStrategy extends AbstractLoadAttributeValueStrategy {
	private Logger LOG = LoggerFactory.getLogger(EnumAttributeLoadValueStrategy.class);
	private static final String SELECT_ENUM_FIELD_BY_UUID_TEMPLATE = "select item.%s from \"%s\" as item where item.uuid = :uuid";

	@Override
	public Object load(ItemContext ctx, MetaAttributeTypeItem attribute) {

		return getOperationExecutor().execute(
				new SelectOperationSpec(
						createQuery(attribute, ctx),
						createParameters(ctx),
						createMapper(attribute)).createOperationContext())
				.getResultStream()
				.single()
				.block();
	}

	private BiFunction<Row, RowMetadata, ?> createMapper(MetaAttributeTypeItem attribute) {
		return (row, rowMetadata) -> {
			try {
				return getCortexContext().findMetaEnumValueTypeItem(((MetaEnumTypeItem) attribute.getAttributeType()).getEnumClass(), row.get(attribute.getColumnName(), UUID.class));
			} catch (Exception e) {
				if (LOG.isDebugEnabled()) {
					LOG.error(String.format("Can't read attribute name [%s] column number %s", attribute.getAttributeName(), attribute.getColumnName()), e);
				}
				try {
					return row.get(attribute.getColumnName());
				} catch (Exception e1) {
					throw new RuntimeException(e1);
				}
			}
		};
	}

	protected Map<String, Object> createParameters(ItemContext ctx) {
		return Collections.singletonMap(AbstractGenericItem.UUID, ctx.getUuid());
	}

	protected String createQuery(MetaAttributeTypeItem attribute, ItemContext ctx) {
		return String.format(SELECT_ENUM_FIELD_BY_UUID_TEMPLATE, attribute.getColumnName(), ctx.getTypeCode());
	}

	protected static String getColumnName(MetaRelationTypeItem metaRelationTypeItem, String attributeName) {
		return metaRelationTypeItem.getItemAttributes().stream()
				.filter(metaAttr -> metaAttr.getAttributeName().equals(attributeName))
				.findFirst()
				.orElseThrow(() -> new SearchException(String.format("Column for attribute [%s] is not exist", attributeName))).getColumnName();
	}

}
