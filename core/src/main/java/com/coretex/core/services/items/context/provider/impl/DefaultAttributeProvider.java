package com.coretex.core.services.items.context.provider.impl;


import com.coretex.core.activeorm.exceptions.QueryException;
import com.coretex.core.activeorm.exceptions.SearchException;
import com.coretex.core.activeorm.extractors.CoretexReactiveResultSetExtractor;
import com.coretex.core.activeorm.factories.RowMapperFactory;
import com.coretex.core.activeorm.query.operations.SelectOperation;
import com.coretex.core.activeorm.query.select.SelectQueryTransformationProcessor;
import com.coretex.core.activeorm.query.specs.select.SelectItemAttributeOperationSpec;
import com.coretex.core.services.bootstrap.meta.MetaTypeProvider;
import com.coretex.core.services.items.context.ItemContext;
import com.coretex.core.services.items.context.provider.AttributeProvider;
import com.coretex.core.services.items.exceptions.NotFoundAttributeException;
import com.coretex.items.core.GenericItem;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.items.core.RegularTypeItem;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.coretex.core.general.utils.AttributeTypeUtils.isCollection;
import static com.coretex.core.utils.TypeUtil.toType;
import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class DefaultAttributeProvider implements AttributeProvider {

	private static final String MISSED_ATTRIBUTE_TYPE_ERROR_MSG =
			"To retrieve attribute value attribute definition must be not null. For item: %s with Type %s";
	private static final String MISSED_CTX_ERROR_MSG = "To retrieve attribute value item context must be not null";

	private MetaTypeProvider metaTypeProvider;

	private SelectQueryTransformationProcessor transformationProcessor;

	private RowMapperFactory rowMapperFactory;


	@Override
	@SuppressWarnings("unchecked")
	public <T> T getValue(String attributeName, ItemContext ctx) {
		checkArgument(nonNull(ctx), MISSED_CTX_ERROR_MSG);
		checkArgument(nonNull(attributeName), MISSED_ATTRIBUTE_TYPE_ERROR_MSG, ctx.getUuid(), ctx.getTypeCode());

		if (GenericItem.META_TYPE.equals(attributeName)) {
			return (T) metaTypeProvider.findMetaType(ctx.getTypeCode());
		}

		MetaAttributeTypeItem attribute = metaTypeProvider.findAttribute(ctx.getTypeCode(), attributeName);
		if (!ctx.isNew() && nonNull(ctx.getUuid())) {
			return (T) retrieveValue(attribute, attributeName, ctx);
		}

		if (isCollection(attribute)) {
			Class containerType = attribute.getContainerType();
			if (Set.class.isAssignableFrom(containerType)) {
				return (T) Sets.newHashSet();
			}
			return (T) Lists.newArrayList();
		}
		return null;
	}

	private Object retrieveValue(MetaAttributeTypeItem attribute, String attributeName, ItemContext ctx) {

		if (isNull(attribute)) {
			throw new NotFoundAttributeException(format("Attribute '%s' is absent in '%s' type",
					attributeName, ctx.getTypeCode()));
		}
		return loadValue(ctx, attribute);
	}

	private Object loadValue(ItemContext ctx, MetaAttributeTypeItem attribute) {
		SelectItemAttributeOperationSpec<Object> selectItemAttributeOperationSpec = new SelectItemAttributeOperationSpec<>(attribute, ctx);
		SelectOperation<Object> selectOperation = selectItemAttributeOperationSpec.createOperation(transformationProcessor);
		selectOperation.setJdbcTemplateSupplier(this::getJdbcTemplate);
		selectOperation.setExtractorCreationFunction(select -> {
			CoretexReactiveResultSetExtractor<Object> extractor = new CoretexReactiveResultSetExtractor<>(select, metaTypeProvider);
			extractor.setMapperFactorySupplier(() -> rowMapperFactory);
			return extractor;
		});

		return processResult(selectOperation, attribute, ctx);

	}

	private Object processResult(SelectOperation<Object> selectOperation, MetaAttributeTypeItem attribute, ItemContext ctx) {
		Object result = null;
		List<Object> searchResult = selectOperation.searchResult();

		if(CollectionUtils.isEmpty(searchResult)){
			if (isCollection(attribute)) {
				Class containerType = attribute.getContainerType();
				if (Set.class.isAssignableFrom(containerType)) {
					return Sets.newHashSet();
				}
				return Lists.newArrayList();
			}
			return null;
		}

		if (isCollection(attribute)) {
			Class containerType = attribute.getContainerType();
			if (Set.class.isAssignableFrom(containerType)) {
				return Sets.newHashSet(searchResult);
			}
			return Lists.newArrayList(searchResult);
		}
		if(!searchResult.isEmpty()){
			if(attribute.getLocalized()){
				result = Maps.newHashMap();
				for (Object map : searchResult) {
					if(map instanceof Map){
						((Map) result).put(((Map)map).get("localeiso"), toType(((Map)map).get("value"), ((RegularTypeItem)attribute.getAttributeType())));
					}
				}
				return result;
			}else{
				if(searchResult.size()>1){
					throw new SearchException(String.format("Ambiguous search result for [%s:%s] attribute", ctx.getTypeCode(), attribute.getAttributeName()));
				}
			}

			result = searchResult.iterator().next();
		}



		if (result instanceof Map && attribute.getAttributeType() instanceof RegularTypeItem) {
			Object resultData = ((Map) result).get(attribute.getColumnName());
			if (Objects.isNull(resultData)) {
				resultData = ((Map) result).get(attribute.getAttributeName());
			}
			if(Objects.isNull(resultData)){
				return null;
			}
			RegularTypeItem attributeType = (RegularTypeItem) attribute.getAttributeType();
			if (resultData.getClass().isAssignableFrom(attributeType.getRegularClass())){
				return resultData;
			}else {
				if (resultData instanceof String && attributeType.getRegularClass().isAssignableFrom(Class.class)){
					try {
						return Class.forName((String) resultData);
					} catch (ClassNotFoundException e) {
						throw new QueryException("Cant retrieve a class", e);
					}
				}
			}


		}
		return result;
	}

	public void setMetaTypeProvider(MetaTypeProvider metaTypeProvider) {
		this.metaTypeProvider = metaTypeProvider;
	}

	public void setTransformationProcessor(SelectQueryTransformationProcessor transformationProcessor) {
		this.transformationProcessor = transformationProcessor;
	}

	public void setRowMapperFactory(RowMapperFactory rowMapperFactory) {
		this.rowMapperFactory = rowMapperFactory;
	}

	@Lookup
	public NamedParameterJdbcTemplate getJdbcTemplate() {
		return null;
	}
}
