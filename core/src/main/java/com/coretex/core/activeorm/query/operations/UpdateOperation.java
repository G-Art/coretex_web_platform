package com.coretex.core.activeorm.query.operations;

import com.coretex.core.activeorm.query.QueryType;
import com.coretex.core.activeorm.query.operations.contexts.UpdateOperationConfigContext;
import com.coretex.core.activeorm.query.operations.dataholders.UpdateValueDataHolder;
import com.coretex.core.activeorm.query.specs.CascadeUpdateOperationSpec;
import com.coretex.core.activeorm.query.specs.UpdateOperationSpec;
import com.coretex.core.activeorm.services.AbstractJdbcService;
import com.coretex.core.activeorm.services.ItemOperationInterceptorService;
import com.coretex.core.general.utils.AttributeTypeUtils;
import com.coretex.core.general.utils.OperationUtils;
import com.coretex.items.core.GenericItem;
import com.google.common.collect.Lists;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class UpdateOperation extends ModificationOperation<Update, UpdateOperationSpec, UpdateOperationConfigContext> {

	private Logger LOG = LoggerFactory.getLogger(UpdateOperation.class);

	public UpdateOperation(AbstractJdbcService abstractJdbcService, ItemOperationInterceptorService itemOperationInterceptorService) {
		super(abstractJdbcService, itemOperationInterceptorService);
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.UPDATE;
	}

	@Override
	protected Mono<Integer> executeBefore(UpdateOperationConfigContext operationConfigContext) {
		var operationSpec = operationConfigContext.getOperationSpec();
		if (operationSpec.isCascadeEnabled()) {
			return Flux.fromIterable(operationSpec.getUpdateValueDatas()
					.values())
					.filter(UpdateValueDataHolder::isItemRelation)
					.map(valueDataHolder -> {
						var i = 0;
						if (Objects.isNull(valueDataHolder.getRelatedItem()) && valueDataHolder.getAttributeTypeItem().getAssociated()) {
							GenericItem val = operationSpec.getItem().getItemContext().getOriginValue(valueDataHolder.getAttributeTypeItem().getAttributeName());
							i = i + getActiveOrmOperationExecutor()
									.executeDeleteOperation(val, valueDataHolder.getAttributeTypeItem(), operationConfigContext);
						} else {
							if (valueDataHolder.availableForBeforeExecution()) {
								i = i + getActiveOrmOperationExecutor()
										.executeSaveOperation(valueDataHolder.getRelatedItem(), valueDataHolder.getAttributeTypeItem(), operationConfigContext);
							}
						}
						return i;
					}).reduce(0, Integer::sum);
		} else {
			LOG.warn("Cascade is switched off it may affect data consistent");
		}
		return Mono.just(0);
	}

	@Override
	public Mono<Integer> executeOperation(UpdateOperationConfigContext operationConfigContext) {
		var operationSpec = operationConfigContext.getOperationSpec();
		var query = operationConfigContext.getQuerySupplier().get();
		if (LOG.isDebugEnabled()) {
			LOG.debug(String.format("Execute query: [%s]; type: [%s]; cascade [%s]", query, getQueryType(), operationSpec instanceof CascadeUpdateOperationSpec));
		}
		return executeReactiveOperation(databaseClient -> {
			var sql = bindForEach(
					databaseClient.sql(query),
					operationSpec.getUpdateValueDatas(),
					(spec, entry) -> entry.getValue().bind(spec, entry.getKey())
			);
			return sql.fetch().rowsUpdated();
		});
	}

	@Override
	protected Mono<Integer> executeAfter(UpdateOperationConfigContext operationConfigContext) {
		var operationSpec = operationConfigContext.getOperationSpec();
		return Mono.fromSupplier(() -> {
			var i = 0;
			if (operationSpec.getHasLocalizedFields()) {
				i = i + operationSpec.getLocalizedFields().stream()
						.filter(attr -> operationSpec.getItem().getItemContext().isDirty(attr.getAttributeName()))
						.mapToInt(field -> getActiveOrmOperationExecutor().executeSaveOperation(null, field, operationConfigContext))
						.sum();
			}
			if (operationSpec.isCascadeEnabled()) {
				if (operationSpec.getHasRelationAttributes()) {
					i = i + operationSpec.getRelationAttributes().stream()
							.filter(attr -> operationSpec.getItem().getItemContext().isDirty(attr.getAttributeName()))
							.mapToInt(field -> {
								var mi = 0;
								var value = operationSpec.getItem().getAttributeValue(field.getAttributeName());
								var originValue = operationSpec.getItem().getItemContext().getOriginValue(field.getAttributeName());
								if (AttributeTypeUtils.isCollection(field)) {
									Collection<GenericItem> values = (Collection<GenericItem>) value;
									Collection<GenericItem> originValues = (Collection<GenericItem>) originValue;

									List<GenericItem> toSave = Optional.ofNullable(values).map(val -> val.stream()
											.filter(it -> it.getItemContext().isNew()
													|| it.getItemContext().isDirty()
													|| !OperationUtils.haveRelation(it, operationSpec.getItem(), field))
											.collect(Collectors.toList())

									).orElseGet(Lists::newArrayList);
									if (CollectionUtils.isNotEmpty(toSave)) {
										mi = mi + getActiveOrmOperationExecutor().executeRelationSaveOperations(toSave, field, operationConfigContext);
									}

									List<GenericItem> toRemove = CollectionUtils.isEmpty(originValues) ? List.of() : List.copyOf(originValues);

									if (CollectionUtils.isNotEmpty(values)) {
										toRemove = Optional.ofNullable(originValues).map(val -> val.stream()
												.filter(it -> !values.contains(it)).collect(Collectors.toList())).orElseGet(Lists::newArrayList);
									}

									if (CollectionUtils.isNotEmpty(toRemove)) {
										mi = mi + (getActiveOrmOperationExecutor().executeRelationDeleteOperations(toRemove, field, operationConfigContext));
									}

								} else {
									if (Objects.isNull(value) && Objects.nonNull(originValue)) {
										mi = mi + (getActiveOrmOperationExecutor().executeRelationDeleteOperations((GenericItem) originValue, field, operationConfigContext));
									} else {
										if (Objects.nonNull(value)) {
											if (!value.equals(originValue) && Objects.nonNull(originValue)) {
												mi = mi + (getActiveOrmOperationExecutor().executeRelationDeleteOperations((GenericItem) originValue, field, operationConfigContext));
											}
											mi = mi + (getActiveOrmOperationExecutor().executeRelationSaveOperations((GenericItem) value, field, operationConfigContext));
										}
									}

								}
								return mi;
							}).sum();
				}
			} else {
				LOG.warn("Cascade is switched off it may affect data consistent");
			}
			return i;
		}).doOnSuccess(i -> {
					operationConfigContext.getOperationSpec().flush();
					getItemOperationInterceptorService()
							.onSaved(operationSpec.getItem());
				}
		);
	}

	@Override
	protected boolean isTransactionInitiator() {
		return true;
	}
}
