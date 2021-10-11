package com.coretex.core.services.items.context.provider.strategies.impl;

import com.coretex.core.activeorm.query.specs.select.SelectItemAttributeOperationSpec;
import com.coretex.core.activeorm.services.ReactiveSearchResult;
import com.coretex.core.services.items.context.ItemContext;
import com.coretex.core.services.items.context.provider.strategies.AbstractLoadAttributeValueStrategy;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.coretex.core.general.utils.AttributeTypeUtils.isCollection;

public class RelationAttributeLoadValueStrategy extends AbstractLoadAttributeValueStrategy {

	@Override
	public Object load(ItemContext ctx, MetaAttributeTypeItem attribute) {
		var searchResult = getOperationExecutor().execute(new SelectItemAttributeOperationSpec(attribute, ctx, getDbDialectService()).createOperationContext());
		return processResult(searchResult, attribute, ctx);
	}


	protected Object processResult(ReactiveSearchResult<Object> searchResultStream, MetaAttributeTypeItem attribute, ItemContext ctx) {

		List<Object> searchResult = searchResultStream.getResultStream().collect(Collectors.toList());

		if (CollectionUtils.isEmpty(searchResult) ) {
			if (isCollection(attribute)){
				Class containerType = attribute.getContainerType();
				if (Objects.nonNull(containerType) && Set.class.isAssignableFrom(containerType)) {
					return Sets.newLinkedHashSet();
				}
				return Lists.newArrayList();
			}
			return null;
		}

		if(isCollection(attribute)){
			Class containerType = attribute.getContainerType();
			if (Objects.nonNull(containerType) && Set.class.isAssignableFrom(containerType)) {
				return Sets.newLinkedHashSet(searchResult);
			}
			return Lists.newArrayList(searchResult);
		}
		return searchResult.iterator().next();
	}

}
