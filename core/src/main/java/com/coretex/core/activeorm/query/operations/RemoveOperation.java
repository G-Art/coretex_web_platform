package com.coretex.core.activeorm.query.operations;

import com.coretex.core.activeorm.query.QueryType;
import com.coretex.core.activeorm.query.operations.contexts.RemoveOperationConfigContext;
import com.coretex.core.activeorm.query.specs.CascadeRemoveOperationSpec;
import com.coretex.core.activeorm.query.specs.RemoveOperationSpec;
import com.coretex.core.activeorm.services.AbstractJdbcService;
import com.coretex.core.activeorm.services.ItemOperationInterceptorService;
import com.coretex.core.general.utils.AttributeTypeUtils;
import com.coretex.items.core.GenericItem;
import com.coretex.items.core.MetaTypeItem;
import net.sf.jsqlparser.statement.delete.Delete;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Objects;

import static com.coretex.core.general.utils.AttributeTypeUtils.isRegularTypeAttribute;
import static com.coretex.core.general.utils.ItemUtils.isSystemType;
import static java.lang.String.format;

public class RemoveOperation extends ModificationOperation<Delete, RemoveOperationSpec, RemoveOperationConfigContext> {
	private Logger LOG = LoggerFactory.getLogger(RemoveOperation.class);

	public RemoveOperation(AbstractJdbcService abstractJdbcService, ItemOperationInterceptorService itemOperationInterceptorService) {
		super(abstractJdbcService, itemOperationInterceptorService);
	}


	@Override
	public QueryType getQueryType() {
		return QueryType.DELETE;
	}

	@Override
	protected Mono<Integer> executeBefore(RemoveOperationConfigContext operationConfigContext) {
		var operationSpec = operationConfigContext.getOperationSpec();
		if (operationSpec.isCascadeEnabled()) {
			return Mono.fromSupplier(() -> operationSpec.getAllAttributes()
					.values()
					.stream()
					.filter(attributeTypeItem -> !isRegularTypeAttribute(attributeTypeItem) || attributeTypeItem.getLocalized())
					.map(attributeTypeItem -> {
						var i = 0;
						if (attributeTypeItem.getLocalized()) {
							i = i + getActiveOrmOperationExecutor().executeDeleteOperation(operationSpec.getItem(), attributeTypeItem, operationConfigContext);
						}
						if (attributeTypeItem.getAttributeTypeCode().equals(MetaTypeItem.ITEM_TYPE) && !isSystemType(attributeTypeItem.getAttributeType()) && attributeTypeItem.getAssociated()) {
							var attributeValue = operationSpec.getItem().getAttributeValue(attributeTypeItem.getAttributeName());
							if (Objects.nonNull(attributeValue)) {
								i = i + getActiveOrmOperationExecutor()
										.executeDeleteOperation((GenericItem) (attributeValue), attributeTypeItem, operationConfigContext);
							}
						}
						if (AttributeTypeUtils.isRelationAttribute(attributeTypeItem)) {
							Object value = operationSpec.getItem().getAttributeValue(attributeTypeItem.getAttributeName());
							if (Objects.nonNull(value)) {
								if (value instanceof Collection) {
									i = i + getActiveOrmOperationExecutor().executeRelationDeleteOperations((Collection<GenericItem>) value, attributeTypeItem, operationConfigContext);
								} else {
									i = i + getActiveOrmOperationExecutor().executeRelationDeleteOperations((GenericItem) value, attributeTypeItem, operationConfigContext);
								}
							}
						}
						return i;
					})
					.findFirst()
					.orElse(0));
		} else {
			LOG.warn("Cascade is switched off it may affect data consistent");
		}

		return Mono.just(0);
	}

	@Override
	public Mono<Integer> executeOperation(RemoveOperationConfigContext operationConfigContext) {
		var operationSpec = operationConfigContext.getOperationSpec();
		var query = operationConfigContext.getQuerySupplier().get();
		if (LOG.isDebugEnabled()) {
			LOG.debug(format("Execute query: [%s]; type: [%s]; cascade [%s]", query, getQueryType(), operationSpec instanceof CascadeRemoveOperationSpec));
		}
		return executeReactiveOperation(databaseClient -> {
			var sql = bindForEach(
					databaseClient.sql(query),
					operationSpec.getValueDatas(),
					(spec, entry) -> entry.getValue().bind(spec, entry.getKey())
			);
			return sql.fetch().rowsUpdated();
		});
	}

	@Override
	protected Mono<Integer> executeAfter(RemoveOperationConfigContext operationConfigContext) {
		return Mono.just(0).doOnSuccess(integer -> {
			var operationSpec = operationConfigContext.getOperationSpec();
			getItemOperationInterceptorService()
					.onRemove(operationSpec.getItem());
		});
	}

	@Override
	protected boolean useInterceptors(RemoveOperationConfigContext operationConfigContext) {
		return false;
	}

	@Override
	protected boolean isTransactionInitiator() {
		return true;
	}

}
