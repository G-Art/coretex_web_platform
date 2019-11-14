package com.coretex.core.activeorm.factories;

import com.coretex.core.activeorm.query.operations.ModificationOperation;
import com.coretex.core.activeorm.query.specs.ModificationOperationSpec;
import com.coretex.items.core.GenericItem;
import com.coretex.items.core.MetaAttributeTypeItem;
import net.sf.jsqlparser.statement.Statement;

import java.util.Collection;
import java.util.List;

public interface OperationFactory {

	<T extends GenericItem> ModificationOperation<? extends Statement, ? extends ModificationOperationSpec> createSaveOperation(T item);
	<T extends GenericItem> ModificationOperation<? extends Statement, ? extends ModificationOperationSpec> createSaveOperation(T item, MetaAttributeTypeItem attributeTypeItem, ModificationOperation<? extends Statement, ? extends ModificationOperationSpec> initiator);
	<T extends GenericItem> List<ModificationOperation<? extends Statement, ? extends ModificationOperationSpec>> createRelationSaveOperations(Collection<T> items, MetaAttributeTypeItem attributeTypeItem, ModificationOperation<? extends Statement, ? extends ModificationOperationSpec> initiator);
	<T extends GenericItem> List<ModificationOperation<? extends Statement, ? extends ModificationOperationSpec>> createRelationSaveOperations(T items, MetaAttributeTypeItem attributeTypeItem, ModificationOperation<? extends Statement, ? extends ModificationOperationSpec> initiator);
	<T extends GenericItem> ModificationOperation<? extends Statement, ? extends ModificationOperationSpec> createDeleteOperation(T item);
	<T extends GenericItem> ModificationOperation<? extends Statement, ? extends ModificationOperationSpec> createDeleteOperation(T item, MetaAttributeTypeItem attributeTypeItem, ModificationOperation<? extends Statement, ? extends ModificationOperationSpec> initiator);
	<T extends GenericItem> List<ModificationOperation<? extends Statement, ? extends ModificationOperationSpec>> createRelationDeleteOperations(Collection<T> items, MetaAttributeTypeItem attributeTypeItem, ModificationOperation<? extends Statement, ? extends ModificationOperationSpec> initiator);
	<T extends GenericItem> List<ModificationOperation<? extends Statement, ? extends ModificationOperationSpec>> createRelationDeleteOperations(T item, MetaAttributeTypeItem attributeTypeItem, ModificationOperation<? extends Statement, ? extends ModificationOperationSpec> initiator);

}
