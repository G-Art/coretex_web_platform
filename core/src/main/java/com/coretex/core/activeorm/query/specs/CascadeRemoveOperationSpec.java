package com.coretex.core.activeorm.query.specs;

import com.coretex.core.activeorm.query.operations.contexts.AbstractOperationConfigContext;
import com.coretex.items.core.GenericItem;
import com.coretex.items.core.MetaAttributeTypeItem;

public class CascadeRemoveOperationSpec extends RemoveOperationSpec implements CascadeModificationOperationSpec {

	private AbstractOperationConfigContext<?, ? extends ModificationOperationSpec<?, ?, ?>, ?> initiator;

	public CascadeRemoveOperationSpec(AbstractOperationConfigContext<?, ? extends ModificationOperationSpec<?, ?, ?>, ?> initiator, GenericItem item, MetaAttributeTypeItem attributeTypeItem) {
		super(item,
				initiator.getOperationSpec().isCascadeEnabled(),
				initiator.getOperationSpec().isTransactionEnabled());
		this.initiator = initiator;
	}

	@Override
	public boolean existInCascade(GenericItem item) {
		if (getItem().equals(item)) {
			return true;
		}
		if (initiator.getOperationSpec() instanceof CascadeModificationOperationSpec) {
			return ((CascadeModificationOperationSpec) initiator.getOperationSpec()).existInCascade(item);
		} else {
			return (initiator.getOperationSpec()).getItem().equals(item);
		}
	}
}
