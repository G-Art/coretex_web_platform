package com.coretex.core.activeorm.query;

import com.coretex.core.activeorm.query.operations.SqlOperation;
import com.coretex.core.activeorm.query.operations.contexts.AbstractOperationConfigContext;
import com.coretex.core.activeorm.query.operations.contexts.OperationConfigContext;
import com.coretex.core.activeorm.query.operations.suppliers.OperationSupplier;
import com.coretex.core.activeorm.query.specs.CascadeInsertOperationSpec;
import com.coretex.core.activeorm.query.specs.CascadeRemoveOperationSpec;
import com.coretex.core.activeorm.query.specs.CascadeUpdateOperationSpec;
import com.coretex.core.activeorm.query.specs.InsertOperationSpec;
import com.coretex.core.activeorm.query.specs.LocalizedDataRemoveOperationSpec;
import com.coretex.core.activeorm.query.specs.LocalizedDataSaveOperationSpec;
import com.coretex.core.activeorm.query.specs.ModificationOperationSpec;
import com.coretex.core.activeorm.query.specs.RemoveOperationSpec;
import com.coretex.core.activeorm.query.specs.UpdateOperationSpec;
import com.coretex.core.activeorm.services.ItemService;
import com.coretex.core.activeorm.services.ReactiveSearchResult;
import com.coretex.core.general.utils.OperationUtils;
import com.coretex.items.core.GenericItem;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.items.core.MetaRelationTypeItem;
import com.coretex.meta.AbstractGenericItem;
import org.springframework.beans.factory.annotation.Lookup;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.coretex.core.general.utils.OperationUtils.isLoopSafe;

public class DefaultActiveOrmOperationExecutor implements ActiveOrmOperationExecutor {

	private Map<QueryType, OperationSupplier<?, ? extends OperationConfigContext<?, ?, ?>>> activeOrmOperationMap;

	public DefaultActiveOrmOperationExecutor(Map<QueryType, OperationSupplier<?, ? extends OperationConfigContext<?, ?, ?>>> activeOrmOperationMap) {

		this.activeOrmOperationMap = activeOrmOperationMap;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T, R extends ReactiveSearchResult<T>, CTX extends OperationConfigContext<?, ?, CTX>> R execute(CTX context) {
		OperationSupplier<?, CTX> operationSupplier = (OperationSupplier<?, CTX>) activeOrmOperationMap.get(context.getQueryType());
		SqlOperation<?, ?, CTX> sqlOperation = (SqlOperation<?, ?, CTX>) operationSupplier.get(context);
		var result = sqlOperation.<T>execute(context);
		return context.wrapResult(result);
	}


	@Override
	public <T extends GenericItem> Integer executeSaveOperation(T item) {

		ReactiveSearchResult<Mono<Integer>> result;
		if (isNew(item)) {
			result = execute(new InsertOperationSpec(item).createOperationContext());
		} else {
			result = execute(new UpdateOperationSpec(item).createOperationContext());
		}
		return Flux.concat(result.getResultStream())
				.reduce(0, Integer::sum).block();
	}

	@Override
	public <T extends GenericItem> Integer executeSaveOperation(T item,
	                                                            MetaAttributeTypeItem attributeTypeItem,
	                                                            AbstractOperationConfigContext<?, ? extends ModificationOperationSpec<?, ?, ?>, ?> initiator) {
		ReactiveSearchResult<Mono<Integer>> result;
		if (attributeTypeItem.getLocalized()) {
			result = execute(new LocalizedDataSaveOperationSpec(initiator, attributeTypeItem).createOperationContext());
		} else {
			if (isNew(item)) {
				result = execute(new CascadeInsertOperationSpec(initiator, item, attributeTypeItem).createOperationContext());
			} else {
				result = execute(new CascadeUpdateOperationSpec(initiator, item, attributeTypeItem).createOperationContext());
			}
		}
		return Flux.concat(result.getResultStream())
				.reduce(0, Integer::sum).block();
	}

	@Override
	public <T extends GenericItem> Integer executeRelationSaveOperations(Collection<T> items,
	                                                                     MetaAttributeTypeItem attributeTypeItem,
	                                                                     AbstractOperationConfigContext<?, ? extends ModificationOperationSpec<?, ?, ?>, ?> initiator) {
		ModificationOperationSpec<?, ?, ?> initiatorOperationSpec = initiator.getOperationSpec();
		GenericItem ownerItem = initiatorOperationSpec.getItem();

		return items.stream()
				.filter(Objects::nonNull)
				.filter(item -> OperationUtils.isLoopSafe(initiatorOperationSpec, item))
				.mapToInt(item -> {
					var i = 0;
					if (item.getItemContext().isDirty()) {
						i = i + executeSaveOperation(item, attributeTypeItem, initiator);
					}
					if (isNew(item) || !OperationUtils.haveRelation(item, ownerItem, attributeTypeItem)) {
						i = i + executeRelationSaveOperation(item, ownerItem, attributeTypeItem, initiator);
					}
					return i;
				}).sum();

	}

	@Override
	public <T extends GenericItem> Integer executeRelationSaveOperations(T item,
	                                                                     MetaAttributeTypeItem attributeTypeItem,
	                                                                     AbstractOperationConfigContext<?, ? extends ModificationOperationSpec<?, ?, ?>, ?> initiator) {

		return executeRelationSaveOperations(List.of(item), attributeTypeItem, initiator);
	}

	@Override
	public <T extends GenericItem> Integer executeDeleteOperation(T item) {
		ReactiveSearchResult<Mono<Integer>> searchResult = execute(new RemoveOperationSpec(item).createOperationContext());
		return Flux.concat(searchResult.getResultStream())
				.subscribeOn(Schedulers.boundedElastic())
				.publishOn(Schedulers.boundedElastic()).reduce(0, Integer::sum).block();
	}

	public <T extends GenericItem> Integer executeDeleteOperation(T item,
	                                                              MetaAttributeTypeItem attributeTypeItem,
	                                                              AbstractOperationConfigContext<?, ? extends ModificationOperationSpec<?, ?, ?>, ?> initiator) {
		ReactiveSearchResult<Mono<Integer>> searchResult;
		if (attributeTypeItem.getLocalized()) {
			searchResult = execute(new LocalizedDataRemoveOperationSpec(initiator, attributeTypeItem).createOperationContext());
		} else {
			searchResult = execute(new CascadeRemoveOperationSpec(initiator, item, attributeTypeItem).createOperationContext());
		}
		return Flux.concat(searchResult.getResultStream())
				.reduce(0, Integer::sum).block();
	}

	@Override
	public <T extends GenericItem> Integer executeRelationDeleteOperations(Collection<T> items,
	                                                                       MetaAttributeTypeItem attributeTypeItem,
	                                                                       AbstractOperationConfigContext<?, ? extends ModificationOperationSpec<?, ?, ?>, ?> initiator) {
		GenericItem ownerItem = initiator.getOperationSpec().getItem();
		return items.stream().filter(Objects::nonNull)
				.mapToInt(item -> {
					var i = 0;
					boolean loopSave = isLoopSafe(initiator.getOperationSpec(), item);
					if (attributeTypeItem.getAssociated() && loopSave) {
						i = i+ executeDeleteOperation(item, attributeTypeItem, initiator);
					}
					if (loopSave) {
						i = i+ executeRelationDeleteOperation(item, ownerItem, attributeTypeItem, initiator);
					}
					return i;
				}).sum();
	}

	@Override
	public <T extends GenericItem> Integer executeRelationDeleteOperations(T item,
	                                                                       MetaAttributeTypeItem attributeTypeItem,
	                                                                       AbstractOperationConfigContext<?, ? extends ModificationOperationSpec<?, ?, ?>, ?> initiator) {

		return executeRelationDeleteOperations(List.of(item), attributeTypeItem, initiator);
	}


	private <T extends GenericItem> Integer executeRelationDeleteOperation(T item,
	                                                                       GenericItem ownerItem,
	                                                                       MetaAttributeTypeItem attributeTypeItem,
	                                                                       AbstractOperationConfigContext<?, ? extends ModificationOperationSpec<?, ?, ?>, ?> initiator) {

		GenericItem relation = (GenericItem) getItemService().create(((MetaRelationTypeItem) attributeTypeItem.getAttributeType()).getItemClass());

		if (attributeTypeItem.getSource()) {
			setRelations(ownerItem, item, relation);
		} else {
			setRelations(item, ownerItem, relation);
		}

		return executeDeleteOperation(relation, attributeTypeItem, initiator);
	}

	private <T extends GenericItem> Integer executeRelationSaveOperation(T item,
	                                                                     GenericItem ownerItem,
	                                                                     MetaAttributeTypeItem attributeTypeItem,
	                                                                     AbstractOperationConfigContext<?, ? extends ModificationOperationSpec<?, ?, ?>, ?> initiator) {

		GenericItem relation = (GenericItem) getItemService().create(((MetaRelationTypeItem) attributeTypeItem.getAttributeType()).getItemClass());

		LocalDateTime now = LocalDateTime.now();
		relation.setAttributeValue(GenericItem.CREATE_DATE, now);
		relation.setAttributeValue(GenericItem.UPDATE_DATE, now);

		if (attributeTypeItem.getSource()) {
			setRelations(ownerItem, item, relation);
		} else {
			setRelations(item, ownerItem, relation);
		}

		return executeSaveOperation(relation, attributeTypeItem, initiator);
	}

	private void setRelations(GenericItem source, GenericItem target, AbstractGenericItem relation) {
		relation.setAttributeValue("source", source);
		relation.setAttributeValue("sourceType", source.getMetaType());
		relation.setAttributeValue("target", target);
		relation.setAttributeValue("targetType", target.getMetaType());
	}

	private <T extends GenericItem> boolean isNew(T item) {
		return item.getItemContext().isNew() || !item.getItemContext().isExist();
	}

	@Lookup
	public ItemService getItemService() {
		return null;
	}
}
