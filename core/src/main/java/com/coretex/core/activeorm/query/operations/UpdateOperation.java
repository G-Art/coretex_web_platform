package com.coretex.core.activeorm.query.operations;

import com.coretex.core.activeorm.query.QueryType;
import com.coretex.core.activeorm.query.operations.dataholders.InsertValueDataHolder;
import com.coretex.core.activeorm.query.operations.dataholders.UpdateValueDataHolder;
import com.coretex.core.activeorm.query.operations.sources.ModificationSqlParameterSource;
import com.coretex.core.activeorm.query.specs.UpdateOperationSpec;
import com.coretex.core.general.utils.AttributeTypeUtils;
import com.coretex.core.general.utils.OperationUtils;
import com.coretex.items.core.GenericItem;
import com.google.common.collect.Lists;
import com.google.common.collect.Streams;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class UpdateOperation extends ModificationOperation<Update, UpdateOperationSpec> {

	private Logger LOG = LoggerFactory.getLogger(UpdateOperation.class);

	public UpdateOperation(UpdateOperationSpec operationSpec) {
		super(operationSpec);
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.UPDATE;
	}

	@Override
	protected void executeBefore() {
		if (getOperationSpec().isCascadeEnabled()) {
			getOperationSpec().getUpdateValueDatas()
					.values()
					.stream()
					.filter(UpdateValueDataHolder::isItemRelation)
					.forEach(valueDataHolder -> {
						if (Objects.isNull(valueDataHolder.getRelatedItem()) && valueDataHolder.getAttributeTypeItem().getAssociated()) {
							GenericItem val = getOperationSpec().getItem().getItemContext().getOriginValue(valueDataHolder.getAttributeTypeItem().getAttributeName());
							getOperationFactory()
									.createDeleteOperation(val, valueDataHolder.getAttributeTypeItem(), this)
									.execute();
						} else {
							if (valueDataHolder.availableForBeforeExecution()) {
								getOperationFactory()
										.createSaveOperation(valueDataHolder.getRelatedItem(), valueDataHolder.getAttributeTypeItem(), this)
										.execute();
							}
						}

					});
		} else {
			LOG.warn("Cascade is switched off it may affect data consistent");
		}
	}

	@Override
	public void executeOperation() {
		executeJdbcOperation(jdbcTemplate -> jdbcTemplate.update(getStatement().toString(),
				new ModificationSqlParameterSource<UpdateValueDataHolder>(getOperationSpec().getUpdateValueDatas())));
	}

	@Override
	protected void executeAfter() {
		if (getOperationSpec().getHasLocalizedFields()) {
			getOperationSpec().getLocalizedFields().stream()
					.filter(attr -> this.getOperationSpec().getItem().getItemContext().isDirty(attr.getAttributeName()))
					.forEach(field -> getOperationFactory().createSaveOperation(null, field, this).execute());
		}
		if (getOperationSpec().isCascadeEnabled()) {
			if (getOperationSpec().getHasRelationAttributes()) {
				getOperationSpec().getRelationAttributes().stream()
						.filter(attr -> this.getOperationSpec().getItem().getItemContext().isDirty(attr.getAttributeName()))
						.forEach(field -> {
							var value = this.getOperationSpec().getItem().getAttributeValue(field.getAttributeName());
							var originValue = this.getOperationSpec().getItem().getItemContext().getOriginValue(field.getAttributeName());
							if (AttributeTypeUtils.isCollection(field)) {
								Collection<GenericItem> values = (Collection<GenericItem>) value;
								Collection<GenericItem> originValues = (Collection<GenericItem>) originValue;

								List<GenericItem> toSave = Optional.ofNullable(values).map(val -> val.stream()
										.filter(it -> it.getItemContext().isNew()
												|| it.getItemContext().isDirty()
												|| !OperationUtils.haveRelation(it, this.getOperationSpec().getItem(), field))
										.collect(Collectors.toList())

								).orElseGet(Lists::newArrayList);
								if (CollectionUtils.isNotEmpty(toSave)) {
									getOperationFactory().createRelationSaveOperations(toSave, field, this).forEach(ModificationOperation::execute);
								}

								List<GenericItem> toRemove = CollectionUtils.isEmpty(originValues) ? List.of() : List.copyOf(originValues);

								if (CollectionUtils.isNotEmpty(values)) {
									toRemove = Optional.ofNullable(originValues).map(val -> val.stream()
											.filter(it -> !values.contains(it)).collect(Collectors.toList())).orElseGet(Lists::newArrayList);
								}

								if (CollectionUtils.isNotEmpty(toRemove)) {
									getOperationFactory().createRelationDeleteOperations(toRemove, field, this).forEach(ModificationOperation::execute);
								}

							} else {
								if (Objects.isNull(value) && Objects.nonNull(originValue)) {
									getOperationFactory().createRelationDeleteOperations((GenericItem) originValue, field, this).forEach(ModificationOperation::execute);
								} else {
									if (Objects.nonNull(value)) {
										if (!value.equals(originValue) && Objects.nonNull(originValue)) {
											getOperationFactory().createRelationDeleteOperations((GenericItem) originValue, field, this).forEach(ModificationOperation::execute);
										}
										getOperationFactory().createRelationSaveOperations((GenericItem) value, field, this).forEach(ModificationOperation::execute);
									}
								}

							}
						});
			}
		} else {
			LOG.warn("Cascade is switched off it may affect data consistent");
		}
	}

	@Override
	protected boolean isTransactionInitiator() {
		return true;
	}
}
