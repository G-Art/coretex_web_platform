package com.coretex.core.activeorm.query.specs;

import com.coretex.core.activeorm.query.QueryTransformationProcessor;
import com.coretex.core.activeorm.query.operations.CascadeInsertOperation;
import com.coretex.core.activeorm.query.operations.ModificationOperation;
import com.coretex.items.core.GenericItem;
import com.coretex.items.core.MetaAttributeTypeItem;
import net.sf.jsqlparser.statement.Statement;

public class CascadeInsertOperationSpec extends InsertOperationSpec implements CascadeModificationOperationSpec {

	private MetaAttributeTypeItem attributeTypeItem;

	private ModificationOperation<? extends Statement, ? extends ModificationOperationSpec> initiator;

	public CascadeInsertOperationSpec(ModificationOperation<? extends Statement, ? extends ModificationOperationSpec> initiator, GenericItem savingItem, MetaAttributeTypeItem attributeTypeItem) {
		super(savingItem, initiator.getOperationSpec().isCascadeEnabled(), initiator.getOperationSpec().isTransactionEnabled());
		this.initiator = initiator;
		this.attributeTypeItem = attributeTypeItem;
	}

	@Override
	public boolean existInCascade(GenericItem item) {
		if (getItem().equals(item)) {
			return true;
		}
		if (initiator.getOperationSpec() instanceof CascadeModificationOperationSpec) {
			return ((CascadeModificationOperationSpec) initiator.getOperationSpec()).existInCascade(item);
		} else {
			return (initiator.getOperationSpec()).getItem() == item;
		}
	}

	@Override
	public CascadeInsertOperation createOperation(QueryTransformationProcessor processor) {
		return new CascadeInsertOperation(this);
	}
}
