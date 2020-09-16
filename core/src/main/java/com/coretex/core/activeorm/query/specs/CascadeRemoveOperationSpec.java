package com.coretex.core.activeorm.query.specs;

import com.coretex.core.activeorm.query.QueryTransformationProcessor;
import com.coretex.core.activeorm.query.operations.CascadeRemoveOperation;
import com.coretex.core.activeorm.query.operations.ModificationOperation;
import com.coretex.items.core.GenericItem;
import com.coretex.items.core.MetaAttributeTypeItem;
import net.sf.jsqlparser.statement.Statement;

public class CascadeRemoveOperationSpec extends RemoveOperationSpec implements CascadeModificationOperationSpec {


	private ModificationOperation<? extends Statement, ? extends ModificationOperationSpec> initiator;

	public CascadeRemoveOperationSpec(ModificationOperation<? extends Statement, ? extends ModificationOperationSpec> initiator, GenericItem item, MetaAttributeTypeItem attributeTypeItem) {
		super(	item,
				initiator.getOperationSpec().isCascadeEnabled(),
				initiator.getOperationSpec().isTransactionEnabled());
		this.initiator = initiator;
	}


	@Override
	public CascadeRemoveOperation createOperation(QueryTransformationProcessor processor) {
		return new CascadeRemoveOperation(this);
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
}
