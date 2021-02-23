package com.coretex.core.activeorm.query.operations;

import com.coretex.core.activeorm.query.QueryType;
import com.coretex.core.activeorm.query.operations.contexts.InsertOperationConfigContext;
import com.coretex.core.activeorm.query.operations.dataholders.InsertValueDataHolder;
import com.coretex.core.activeorm.query.specs.CascadeInsertOperationSpec;
import com.coretex.core.activeorm.query.specs.InsertOperationSpec;
import com.coretex.core.activeorm.services.AbstractJdbcService;
import com.coretex.core.activeorm.services.ItemOperationInterceptorService;
import com.coretex.items.core.GenericItem;
import com.coretex.items.core.MetaAttributeTypeItem;
import net.sf.jsqlparser.statement.insert.Insert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Objects;

public class InsertOperation extends ModificationOperation<
		Insert,
		InsertOperationSpec,
		InsertOperationConfigContext> {

	private Logger LOG = LoggerFactory.getLogger(InsertOperation.class);

	public InsertOperation(AbstractJdbcService abstractJdbcService, ItemOperationInterceptorService itemOperationInterceptorService) {
		super(abstractJdbcService, itemOperationInterceptorService);
	}

	@Override
	protected Mono<Integer> executeBefore(InsertOperationConfigContext operationConfigContext) {
		var operationSpec = operationConfigContext.getOperationSpec();
		if (operationSpec.isCascadeEnabled()) {
			return Flux.fromIterable(operationSpec.getInsertValueDatas()
					.values())
					.filter(InsertValueDataHolder::isItemRelation)
					.filter(InsertValueDataHolder::availableForBeforeExecution)
					.map(insertValueDataHolder -> getActiveOrmOperationExecutor()
							.executeSaveOperation(
									insertValueDataHolder.getRelatedItem(),
									insertValueDataHolder.getAttributeTypeItem(),
									operationConfigContext)).reduce(0, Integer::sum);
		} else {
			LOG.warn("Cascade is switched off it may affect data consistent");
			return Mono.just(0);
		}
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.INSERT;
	}

	@Override
	public Mono<Integer> executeOperation(InsertOperationConfigContext operationConfigContext) {
		var operationSpec = operationConfigContext.getOperationSpec();
		var query = operationConfigContext.getQuerySupplier().get();
		if (LOG.isDebugEnabled()) {
			LOG.debug(String.format("Execute query: [%s]; type: [%s]; cascade [%s]", query, getQueryType(), operationSpec instanceof CascadeInsertOperationSpec));
		}
		return executeReactiveOperation(databaseClient -> {
			var sql = bindForEach(
					databaseClient.sql(query),
					operationSpec.getInsertValueDatas(),
					(spec, entry) -> entry.getValue().bind(spec, entry.getKey())
			);
			return sql.fetch().rowsUpdated().doOnSuccess(i -> {
				operationSpec.getItem().setAttributeValue(GenericItem.UUID, operationSpec.getNewUuid());
			});
		});
	}

	@Override
	protected Mono<Integer> executeAfter(InsertOperationConfigContext operationConfigContext) {
		var operationSpec = operationConfigContext.getOperationSpec();
		return Mono.fromSupplier(() -> {
			var i = 0;
			if (operationSpec.getHasLocalizedFields()) {
				i = i + operationSpec.getLocalizedFields().stream()
						.filter(attr -> operationSpec.getItem().getItemContext().isDirty(attr.getAttributeName()))
						.mapToInt(field -> getActiveOrmOperationExecutor().executeSaveOperation(null, field, operationConfigContext))
						.sum();
			}

			if (operationSpec.getHasRelationAttributes()) {
				i = i + operationSpec.getRelationAttributes()
						.stream()
						.filter(attr -> operationSpec.getItem().getItemContext().isDirty(attr.getAttributeName()))
						.mapToInt(field -> {
							var sum = 0;
							Object value = operationSpec.getItem().getAttributeValue(field.getAttributeName());
							if (Objects.nonNull(value)) {
								if (value instanceof Collection) {
									sum = sum + getActiveOrmOperationExecutor().executeRelationSaveOperations((Collection<GenericItem>) value, field, operationConfigContext);
								} else {
									sum = sum + getActiveOrmOperationExecutor().executeRelationSaveOperations((GenericItem) value, field, operationConfigContext);
								}
							}
							return sum;
						}).sum();
			}
			return i;
		}).doOnSuccess(i -> {
			operationConfigContext.getOperationSpec().flush();
			getItemOperationInterceptorService()
					.onSaved(operationSpec.getItem());
		});

	}


	public static InsertValueDataHolder createInsertValueDataHolder(InsertOperationSpec operationSpec, MetaAttributeTypeItem attributeTypeItem) {
		return new InsertValueDataHolder(attributeTypeItem, operationSpec);
	}

	@Override
	protected boolean isTransactionInitiator() {
		return true;
	}
}
