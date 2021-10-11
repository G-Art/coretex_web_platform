package com.coretex.core.services.items.context.provider.strategies.impl;

import com.coretex.core.activeorm.exceptions.SearchException;
import com.coretex.core.activeorm.query.specs.select.SelectItemAttributeOperationSpec;
import com.coretex.core.activeorm.services.ReactiveSearchResult;
import com.coretex.core.services.items.context.ItemContext;
import com.coretex.core.services.items.context.provider.strategies.AbstractLoadAttributeValueStrategy;
import com.coretex.items.core.MetaAttributeTypeItem;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

public class ItemAttributeLoadValueStrategy extends AbstractLoadAttributeValueStrategy {

	@Override
	public Object load(ItemContext ctx, MetaAttributeTypeItem attribute) {
		var searchResult = getOperationExecutor().execute(new SelectItemAttributeOperationSpec(attribute, ctx, getDbDialectService()).createOperationContext());
		return processResult(searchResult, attribute, ctx);
	}

	private Object processResult(ReactiveSearchResult<?> searchResultStream, MetaAttributeTypeItem attribute, ItemContext ctx) {
		List<Object> searchResult = searchResultStream.getResultStream().collect(Collectors.toList());

		if(CollectionUtils.isEmpty(searchResult)){
			return null;
		}
		if (!searchResult.isEmpty() && searchResult.size() > 1) {
			throw new SearchException(String.format("Ambiguous search result for [%s:%s] attribute", ctx.getTypeCode(), attribute.getAttributeName()));
		}

		return searchResult.get(0);
	}

}
