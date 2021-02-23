package com.coretex.core.activeorm.query;

import com.coretex.core.activeorm.query.operations.contexts.AbstractOperationConfigContext;
import com.coretex.core.activeorm.query.operations.contexts.OperationConfigContext;
import com.coretex.core.activeorm.query.specs.ModificationOperationSpec;
import com.coretex.core.activeorm.services.ReactiveSearchResult;
import com.coretex.items.core.GenericItem;
import com.coretex.items.core.MetaAttributeTypeItem;

import java.util.Collection;

public interface ActiveOrmOperationExecutor {

	<T, R extends ReactiveSearchResult<T>, CTX extends OperationConfigContext<?,?,CTX>> R execute(CTX context);

	<T extends GenericItem> Integer executeSaveOperation(T item);

	<T extends GenericItem> Integer executeSaveOperation(T item, MetaAttributeTypeItem attributeTypeItem, AbstractOperationConfigContext<?, ? extends ModificationOperationSpec<?, ?, ?>, ?> initiator);

	<T extends GenericItem> Integer executeRelationSaveOperations(Collection<T> items, MetaAttributeTypeItem attributeTypeItem, AbstractOperationConfigContext<?, ? extends ModificationOperationSpec<?, ?, ?>, ?> initiator);

	<T extends GenericItem> Integer executeRelationSaveOperations(T items, MetaAttributeTypeItem attributeTypeItem, AbstractOperationConfigContext<?, ? extends ModificationOperationSpec<?, ?, ?>, ?> initiator);

	<T extends GenericItem> Integer executeDeleteOperation(T item);

	<T extends GenericItem> Integer executeDeleteOperation(T item, MetaAttributeTypeItem attributeTypeItem, AbstractOperationConfigContext<?, ? extends ModificationOperationSpec<?, ?, ?>, ?> initiator);

	<T extends GenericItem> Integer executeRelationDeleteOperations(Collection<T> items, MetaAttributeTypeItem attributeTypeItem, AbstractOperationConfigContext<?, ? extends ModificationOperationSpec<?, ?, ?>, ?> initiator);

	<T extends GenericItem> Integer executeRelationDeleteOperations(T item, MetaAttributeTypeItem attributeTypeItem, AbstractOperationConfigContext<?, ? extends ModificationOperationSpec<?, ?, ?>, ?> initiator);

}
