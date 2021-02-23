package com.coretex.core.services.items.context.provider.strategies.impl;

import com.coretex.core.activeorm.query.specs.select.SelectOperationSpec;
import com.coretex.core.services.items.context.ItemContext;
import com.coretex.core.services.items.context.provider.strategies.AbstractLoadAttributeValueStrategy;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.items.core.MetaTypeItem;
import com.coretex.meta.AbstractGenericItem;

import java.util.Collections;
import java.util.Map;

public class ItemAttributeLoadValueStrategy extends AbstractLoadAttributeValueStrategy {

	private static final String SELECT_ITEM_FIELD_BY_UUID_TEMPLATE = "select item.* from \"%s\" as item left join \"%s\" as j on (j.%s = item.uuid) where j.uuid = :uuid";

	@Override
	public Object load(ItemContext ctx, MetaAttributeTypeItem attribute) {
		return getOperationExecutor().execute(new SelectOperationSpec(createQuery(attribute, ctx), createParameters(ctx)).createOperationContext())
				.getResultStream()
				.blockFirst();
	}

	protected Map<String, Object> createParameters(ItemContext ctx) {
		return Collections.singletonMap(AbstractGenericItem.UUID, ctx.getUuid());
	}

	protected String createQuery(MetaAttributeTypeItem attribute, ItemContext ctx) {
		return String.format(SELECT_ITEM_FIELD_BY_UUID_TEMPLATE,
				((MetaTypeItem) attribute.getAttributeType()).getTypeCode(), ctx.getTypeCode(), attribute.getAttributeName());
	}

}
