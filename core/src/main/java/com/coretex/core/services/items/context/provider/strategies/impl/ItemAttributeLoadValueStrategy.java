package com.coretex.core.services.items.context.provider.strategies.impl;

import com.coretex.core.activeorm.exceptions.SearchException;
import com.coretex.core.activeorm.extractors.CoretexReactiveResultSetExtractor;
import com.coretex.core.activeorm.query.operations.SelectOperation;
import com.coretex.core.activeorm.query.select.SelectQueryTransformationProcessor;
import com.coretex.core.activeorm.query.specs.select.SelectOperationSpec;
import com.coretex.core.services.items.context.ItemContext;
import com.coretex.core.services.items.context.provider.strategies.AbstractLoadAttributeValueStrategy;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.items.core.MetaTypeItem;
import com.coretex.meta.AbstractGenericItem;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ItemAttributeLoadValueStrategy extends AbstractLoadAttributeValueStrategy {

	private static final String SELECT_ITEM_FIELD_BY_UUID_TEMPLATE = "select item.* from \"%s\" as item left join \"%s\" as j on (j.%s = item.uuid) where j.uuid = :uuid";

	public ItemAttributeLoadValueStrategy(SelectQueryTransformationProcessor transformationProcessor) {
		super(transformationProcessor);
	}

	@Override
	public Object load(ItemContext ctx, MetaAttributeTypeItem attribute) {

		SelectOperationSpec<Object> selectItemAttributeOperationSpec = new SelectOperationSpec<>(createQuery(attribute, ctx), createParameters(ctx));
		SelectOperation<Object> selectOperation = selectItemAttributeOperationSpec.createOperation(getTransformationProcessor());
		selectOperation.setJdbcTemplateSupplier(this::getJdbcTemplate);
		selectOperation.setExtractorCreationFunction(select -> {
			CoretexReactiveResultSetExtractor<Object> extractor = new CoretexReactiveResultSetExtractor<>(select, getCortexContext());
			extractor.setMapperFactorySupplier(this::getRowMapperFactory);
			return extractor;
		});
		return processResult(selectOperation, attribute, ctx);
	}

	protected Map<String, Object> createParameters(ItemContext ctx) {
		return Collections.singletonMap(AbstractGenericItem.UUID, ctx.getUuid());
	}

	protected String createQuery(MetaAttributeTypeItem attribute, ItemContext ctx) {
		return String.format(SELECT_ITEM_FIELD_BY_UUID_TEMPLATE,
				((MetaTypeItem) attribute.getAttributeType()).getTypeCode(), ctx.getTypeCode(), attribute.getAttributeName());
	}

	private Object processResult(SelectOperation<Object> selectOperation, MetaAttributeTypeItem attribute, ItemContext ctx) {
		List<Object> searchResult = selectOperation.searchResult();
		if(CollectionUtils.isEmpty(searchResult)){
			return null;
		}
		if (!searchResult.isEmpty() && searchResult.size() > 1) {
			throw new SearchException(String.format("Ambiguous search result for [%s:%s] attribute", ctx.getTypeCode(), attribute.getAttributeName()));
		}

		return searchResult.get(0);
	}

}
