package com.coretex.core.services.bootstrap.dialect.postgres;

import com.coretex.core.activeorm.query.operations.contexts.SelectItemAttributeOperationConfigContext;
import com.coretex.core.services.bootstrap.dialect.QueryBuilder;
import com.coretex.core.services.items.context.ItemContext;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.meta.AbstractGenericItem;

import java.util.Collections;
import java.util.HashMap;

public class PostgresLocalizedItemAttributeQueryBuilder implements QueryBuilder<SelectItemAttributeOperationConfigContext> {
	private static final String SELECT_LOCALIZED_ITEM_FIELD_BY_UUID_TEMPLATE = "select item.localeiso, item.value FROM %s_LOC as item WHERE item.owner = :ownerUuid AND item.attribute = :uuid";

	@Override
	public String createQuery(SelectItemAttributeOperationConfigContext context) {
		var operationSpec = context.getOperationSpec();
		MetaAttributeTypeItem attribute = operationSpec.getAttribute();
		return String.format(SELECT_LOCALIZED_ITEM_FIELD_BY_UUID_TEMPLATE, attribute.getOwner().getTableName());
	}

	@Override
	public void prepareValueData(SelectItemAttributeOperationConfigContext context) {
		var operationSpec = context.getOperationSpec();
		MetaAttributeTypeItem attribute = operationSpec.getAttribute();
		ItemContext ctx = operationSpec.getCtx();
		if (attribute.getLocalized()) {
			operationSpec.addQueryParameters(new HashMap<>() {{
				put(AbstractGenericItem.UUID, attribute.getUuid());
				put("ownerUuid", ctx.getUuid());
			}});
		} else {
			operationSpec.addQueryParameters(Collections.singletonMap(AbstractGenericItem.UUID, ctx.getUuid()));
		}
	}
}
