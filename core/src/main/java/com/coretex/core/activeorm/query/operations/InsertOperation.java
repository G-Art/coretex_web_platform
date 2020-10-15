package com.coretex.core.activeorm.query.operations;

import com.coretex.core.activeorm.query.QueryType;
import com.coretex.core.activeorm.query.operations.dataholders.InsertValueDataHolder;
import com.coretex.core.activeorm.query.operations.sources.ModificationSqlParameterSource;
import com.coretex.core.activeorm.query.specs.CascadeInsertOperationSpec;
import com.coretex.core.activeorm.query.specs.InsertOperationSpec;
import com.coretex.items.core.GenericItem;
import com.coretex.items.core.MetaAttributeTypeItem;
import net.sf.jsqlparser.statement.insert.Insert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Objects;

public class InsertOperation extends ModificationOperation<Insert, InsertOperationSpec> {

	private Logger LOG = LoggerFactory.getLogger(InsertOperation.class);

	public InsertOperation(InsertOperationSpec operationSpec) {
		super(operationSpec);
	}

	@Override
	protected void executeBefore() {
		if (getOperationSpec().isCascadeEnabled()) {
			getOperationSpec().getInsertValueDatas()
					.values()
					.stream()
					.filter(InsertValueDataHolder::isItemRelation)
					.filter(InsertValueDataHolder::availableForBeforeExecution)
					.forEach(insertValueDataHolder -> {
						ModificationOperation cascadeInsertOperation = getOperationFactory().createSaveOperation(insertValueDataHolder.getRelatedItem(), insertValueDataHolder.getAttributeTypeItem(), this);
						cascadeInsertOperation.execute();

					});
		} else {
			LOG.warn("Cascade is switched off it may affect data consistent");
		}
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.INSERT;
	}

	@Override
	public void executeOperation() {
		var query = getQuery();
		if (LOG.isDebugEnabled()) {
			LOG.debug(String.format("Execute query: [%s]; type: [%s]; cascade [%s]", query, getQueryType(), getOperationSpec() instanceof CascadeInsertOperationSpec));
		}
		executeJdbcOperation(jdbcTemplate -> jdbcTemplate.update(query,
				new ModificationSqlParameterSource<InsertValueDataHolder>(getOperationSpec().getInsertValueDatas())));
	}

	@Override
	protected void executeAfter() {

		getOperationSpec().getItem().setAttributeValue(GenericItem.UUID, getOperationSpec().getNewUuid());
		getOperationSpec().getItemOperationInterceptorService()
				.onSaved(getOperationSpec().getItem());

		if (getOperationSpec().getHasLocalizedFields()) {
			getOperationSpec().getLocalizedFields().stream()
					.filter(attr -> this.getOperationSpec().getItem().getItemContext().isDirty(attr.getAttributeName()))
					.forEach(field -> getOperationFactory().createSaveOperation(null, field, this).execute());
		}

		if (getOperationSpec().getHasRelationAttributes()) {
			getOperationSpec().getRelationAttributes().stream()
					.filter(attr -> this.getOperationSpec().getItem().getItemContext().isDirty(attr.getAttributeName()))
					.forEach(field -> {
						Object value = this.getOperationSpec().getItem().getAttributeValue(field.getAttributeName());
						if (Objects.nonNull(value)) {
							if (value instanceof Collection) {
								getOperationFactory().createRelationSaveOperations((Collection<GenericItem>) value, field, this).forEach(ModificationOperation::execute);
							} else {
								getOperationFactory().createRelationSaveOperations((GenericItem) value, field, this).forEach(ModificationOperation::execute);
							}
						}
					});
		}
	}


	public static InsertValueDataHolder createInsertValueDataHolder(InsertOperationSpec operationSpec, MetaAttributeTypeItem attributeTypeItem) {
		return new InsertValueDataHolder(attributeTypeItem, operationSpec);
	}

	@Override
	protected boolean isTransactionInitiator() {
		return true;
	}
}
