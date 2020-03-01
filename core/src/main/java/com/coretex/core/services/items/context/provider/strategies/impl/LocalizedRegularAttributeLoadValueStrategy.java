package com.coretex.core.services.items.context.provider.strategies.impl;

import com.coretex.core.activeorm.extractors.CoretexReactiveResultSetExtractor;
import com.coretex.core.activeorm.query.operations.SelectOperation;
import com.coretex.core.activeorm.query.select.SelectQueryTransformationProcessor;
import com.coretex.core.activeorm.query.specs.select.SelectOperationSpec;
import com.coretex.core.services.items.context.ItemContext;
import com.coretex.core.services.items.context.provider.strategies.AbstractLoadAttributeValueStrategy;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.items.core.RegularTypeItem;
import com.coretex.meta.AbstractGenericItem;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.coretex.core.utils.TypeUtil.toType;

public class LocalizedRegularAttributeLoadValueStrategy extends AbstractLoadAttributeValueStrategy {
	private Logger LOG = LoggerFactory.getLogger(LocalizedRegularAttributeLoadValueStrategy.class);

	private static final String SELECT_LOCALIZED_ITEM_FIELD_BY_UUID_TEMPLATE = "select item.localeiso, item.value FROM %s_LOC as item WHERE item.owner = :ownerUuid AND item.attribute = :uuid";

	public LocalizedRegularAttributeLoadValueStrategy(SelectQueryTransformationProcessor transformationProcessor) {
		super(transformationProcessor);
	}

	@Override
	public Object load(ItemContext ctx, MetaAttributeTypeItem attribute) {
		SelectOperationSpec<Object> selectItemAttributeOperationSpec = new SelectOperationSpec<>(createQuery(attribute, ctx), createParameters(attribute, ctx));
		SelectOperation<Object> selectOperation = selectItemAttributeOperationSpec.createOperation(getTransformationProcessor());
		selectOperation.setJdbcTemplateSupplier(this::getJdbcTemplate);
		selectOperation.setExtractorCreationFunction(select -> {
			CoretexReactiveResultSetExtractor<Object> extractor = new CoretexReactiveResultSetExtractor<>(select, getCortexContext());
			extractor.setMapperFactorySupplier(this::getRowMapperFactory);
			return extractor;
		});
		return processResult(selectOperation, attribute, ctx);
	}

	protected Map<String, Object> createParameters(MetaAttributeTypeItem attribute, ItemContext ctx) {
		if (attribute.getLocalized()) {
			return new HashMap<>() {{
				put(AbstractGenericItem.UUID, attribute.getUuid());
				put("ownerUuid", ctx.getUuid());
			}};
		}
		return Collections.singletonMap(AbstractGenericItem.UUID, ctx.getUuid());
	}

	protected String createQuery(MetaAttributeTypeItem attribute, ItemContext ctx) {
		return String.format(SELECT_LOCALIZED_ITEM_FIELD_BY_UUID_TEMPLATE, attribute.getOwner().getTableName());
	}

	private Object processResult(SelectOperation<Object> selectOperation, MetaAttributeTypeItem attribute, ItemContext ctx) {
		Object result = null;
		List<Object> searchResult = selectOperation.searchResult();

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
