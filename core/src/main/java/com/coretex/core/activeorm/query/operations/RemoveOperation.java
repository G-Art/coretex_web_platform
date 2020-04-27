package com.coretex.core.activeorm.query.operations;

import com.coretex.core.activeorm.query.QueryType;
import com.coretex.core.activeorm.query.operations.dataholders.RemoveValueDataHolder;
import com.coretex.core.activeorm.query.operations.sources.ModificationSqlParameterSource;
import com.coretex.core.activeorm.query.specs.CascadeRemoveOperationSpec;
import com.coretex.core.activeorm.query.specs.RemoveOperationSpec;
import com.coretex.core.general.utils.AttributeTypeUtils;
import com.coretex.items.core.GenericItem;
import com.coretex.items.core.MetaTypeItem;
import net.sf.jsqlparser.statement.delete.Delete;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Objects;

import static com.coretex.core.general.utils.AttributeTypeUtils.isRegularTypeAttribute;
import static com.coretex.core.general.utils.ItemUtils.isSystemType;

public class RemoveOperation extends ModificationOperation<Delete, RemoveOperationSpec> {
	private Logger LOG = LoggerFactory.getLogger(RemoveOperation.class);

	public RemoveOperation(RemoveOperationSpec operationSpec) {
		super(operationSpec);
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.DELETE;
	}

	@Override
	protected void executeBefore() {
		if (getOperationSpec().isCascadeEnabled()) {
			getOperationSpec().getAllAttributes()
					.values()
					.stream()
					.filter(attributeTypeItem -> !isRegularTypeAttribute(attributeTypeItem) || attributeTypeItem.getLocalized())
					.forEach(attributeTypeItem -> {
						if (attributeTypeItem.getLocalized()) {
							getOperationFactory().createDeleteOperation(getOperationSpec().getItem(), attributeTypeItem, this)
									.execute();
						}
						if (attributeTypeItem.getAttributeTypeCode().equals(MetaTypeItem.ITEM_TYPE) && !isSystemType(attributeTypeItem.getAttributeType()) && attributeTypeItem.getAssociated()) {
							var attributeValue = this.getOperationSpec().getItem().getAttributeValue(attributeTypeItem.getAttributeName());
							if (Objects.nonNull(attributeValue)) {
								getOperationSpec().getOperationFactory()
										.createDeleteOperation((GenericItem) (attributeValue), attributeTypeItem, this)
										.execute();
							}
						}
						if (AttributeTypeUtils.isRelationAttribute(attributeTypeItem)) {
							Object value = this.getOperationSpec().getItem().getAttributeValue(attributeTypeItem.getAttributeName());
							if (Objects.nonNull(value)) {
								if (value instanceof Collection) {
									getOperationFactory().createRelationDeleteOperations((Collection<GenericItem>) value, attributeTypeItem, this)
											.forEach(ModificationOperation::execute);
								} else {
									getOperationFactory().createRelationDeleteOperations((GenericItem) value, attributeTypeItem, this)
											.forEach(ModificationOperation::execute);
								}
							}
						}
					});
		} else {
			LOG.warn("Cascade is switched off it may affect data consistent");
		}

	}

	@Override
	public void executeOperation() {
		var query = getQuery();
		if (LOG.isDebugEnabled()) {
			LOG.debug(String.format("Execute query: [%s]; type: [%s]; cascade [%s]", query, getQueryType(), getOperationSpec() instanceof CascadeRemoveOperationSpec));
		}
		executeJdbcOperation(jdbcTemplate -> jdbcTemplate.update(query,
				new ModificationSqlParameterSource<RemoveValueDataHolder>(getOperationSpec().getValueDatas())));
	}

	@Override
	protected void executeAfter() {

	}

	@Override
	protected boolean isTransactionInitiator() {
		return true;
	}

}
