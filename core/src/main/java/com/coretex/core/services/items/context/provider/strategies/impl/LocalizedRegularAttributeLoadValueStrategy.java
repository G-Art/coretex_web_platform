package com.coretex.core.services.items.context.provider.strategies.impl;

import com.coretex.core.activeorm.query.specs.select.SelectItemAttributeOperationSpec;
import com.coretex.core.activeorm.services.ReactiveSearchResult;
import com.coretex.core.services.items.context.ItemContext;
import com.coretex.core.services.items.context.provider.strategies.AbstractLoadAttributeValueStrategy;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.items.core.RegularTypeItem;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.coretex.core.utils.TypeUtil.toType;

public class LocalizedRegularAttributeLoadValueStrategy extends AbstractLoadAttributeValueStrategy {
	private Logger LOG = LoggerFactory.getLogger(LocalizedRegularAttributeLoadValueStrategy.class);

	@Override
	public Object load(ItemContext ctx, MetaAttributeTypeItem attribute) {
		var searchResult = getOperationExecutor().execute(new SelectItemAttributeOperationSpec(attribute, ctx, getDbDialectService()).createOperationContext());
		return processResult(searchResult, attribute, ctx);
	}

	private Object processResult(ReactiveSearchResult<?> searchResultStream, MetaAttributeTypeItem attribute, ItemContext ctx) {
		Object result = null;
		List<Object> searchResult = searchResultStream.getResultStream().collect(Collectors.toList());

		if (!searchResult.isEmpty()) {
			result = Maps.newHashMap();
			for (Object map : searchResult) {
				if (map instanceof Map) {
					((Map) result).put(((Map) map).get("localeiso"), toType(((Map) map).get("value"), ((RegularTypeItem) attribute.getAttributeType())));
				}
			}

		}
		return result;
	}
}
