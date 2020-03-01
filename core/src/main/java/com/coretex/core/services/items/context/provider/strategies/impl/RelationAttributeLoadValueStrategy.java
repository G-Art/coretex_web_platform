package com.coretex.core.services.items.context.provider.strategies.impl;

import com.coretex.core.activeorm.exceptions.SearchException;
import com.coretex.core.activeorm.extractors.CoretexReactiveResultSetExtractor;
import com.coretex.core.activeorm.query.operations.SelectOperation;
import com.coretex.core.activeorm.query.select.SelectQueryTransformationProcessor;
import com.coretex.core.activeorm.query.specs.select.SelectOperationSpec;
import com.coretex.core.services.items.context.ItemContext;
import com.coretex.core.services.items.context.provider.strategies.AbstractLoadAttributeValueStrategy;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.items.core.MetaRelationTypeItem;
import com.coretex.meta.AbstractGenericItem;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.coretex.core.general.utils.AttributeTypeUtils.isCollection;

public class RelationAttributeLoadValueStrategy extends AbstractLoadAttributeValueStrategy {

	private static final String SELECT_RELATION_ITEM_FIELD_BY_UUID_TEMPLATE = "select item.* FROM \"%s\" as item LEFT JOIN \"%s\" as rel ON item.uuid = rel.%s where rel.%s = :uuid ORDER BY rel.createDate";

	public RelationAttributeLoadValueStrategy(SelectQueryTransformationProcessor transformationProcessor) {
		super(transformationProcessor);
	}

	@Override
	public Object load(ItemContext ctx, MetaAttributeTypeItem attribute) {

		SelectOperationSpec<Object> selectItemAttributeOperationSpec = new SelectOperationSpec<>(createQuery(attribute), createParameters(attribute, ctx));
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

	protected String createQuery(MetaAttributeTypeItem attribute) {
		MetaRelationTypeItem metaRelationTypeItem = (MetaRelationTypeItem) attribute.getAttributeType();
		String targetColumnName = getColumnName(metaRelationTypeItem, "target");
		String sourceColumnName = getColumnName(metaRelationTypeItem, "source");
		return String.format(SELECT_RELATION_ITEM_FIELD_BY_UUID_TEMPLATE,
				attribute.getSource() ? metaRelationTypeItem.getSourceType().getTypeCode() : metaRelationTypeItem.getTargetType().getTypeCode(),
				metaRelationTypeItem.getTypeCode(),
				attribute.getSource() ? targetColumnName : sourceColumnName,
				attribute.getSource() ? sourceColumnName : targetColumnName);
	}

	protected static String getColumnName(MetaRelationTypeItem metaRelationTypeItem, String attributeName) {
		return metaRelationTypeItem.getItemAttributes().stream()
				.filter(metaAttr -> metaAttr.getAttributeName().equals(attributeName))
				.findFirst()
				.orElseThrow(() -> new SearchException(String.format("Column for attribute [%s] is not exist", attributeName))).getColumnName();
	}

	protected Object processResult(SelectOperation<Object> selectOperation, MetaAttributeTypeItem attribute, ItemContext ctx) {

		List<Object> searchResult = selectOperation.searchResult();

		if (CollectionUtils.isEmpty(searchResult) ) {
			if (isCollection(attribute)){
				Class containerType = attribute.getContainerType();
				if (Objects.nonNull(containerType) && Set.class.isAssignableFrom(containerType)) {
					return Sets.newHashSet();
				}
				return Lists.newArrayList();
			}
			return null;
		}

		if(isCollection(attribute)){
			Class containerType = attribute.getContainerType();
			if (Objects.nonNull(containerType) && Set.class.isAssignableFrom(containerType)) {
				return Sets.newHashSet(searchResult);
			}
			return Lists.newArrayList(searchResult);
		}
		return searchResult.iterator().next();
	}

}
