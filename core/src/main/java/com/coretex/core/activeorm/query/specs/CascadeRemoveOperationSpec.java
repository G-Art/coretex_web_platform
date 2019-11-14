package com.coretex.core.activeorm.query.specs;

import com.coretex.core.activeorm.query.QueryTransformationProcessor;
import com.coretex.core.activeorm.query.operations.CascadeInsertOperation;
import com.coretex.core.activeorm.query.operations.CascadeRemoveOperation;
import com.coretex.core.activeorm.query.operations.ModificationOperation;
import com.coretex.core.activeorm.query.operations.RemoveOperation;
import com.coretex.items.core.GenericItem;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.meta.AbstractGenericItem;
import com.google.common.collect.Maps;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;

import java.util.Map;
import java.util.stream.Collectors;

public class CascadeRemoveOperationSpec extends RemoveOperationSpec implements CascadeModificationOperationSpec {

	private MetaAttributeTypeItem attributeTypeItem;

	private ModificationOperation<? extends Statement, ? extends ModificationOperationSpec> initiator;

	public CascadeRemoveOperationSpec(ModificationOperation<? extends Statement, ? extends ModificationOperationSpec> initiator, GenericItem item, MetaAttributeTypeItem attributeTypeItem) {
		super(	item,
				initiator.getOperationSpec().isCascadeEnabled(),
				initiator.getOperationSpec().isTransactionEnabled());
		this.initiator = initiator;
		this.attributeTypeItem = attributeTypeItem;
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
