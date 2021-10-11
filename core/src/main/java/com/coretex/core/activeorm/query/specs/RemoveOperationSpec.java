package com.coretex.core.activeorm.query.specs;

import com.coretex.core.activeorm.query.operations.contexts.RemoveOperationConfigContext;
import com.coretex.items.core.GenericItem;
import net.sf.jsqlparser.statement.delete.Delete;

public class RemoveOperationSpec extends ModificationOperationSpec<Delete> {

	public RemoveOperationSpec(GenericItem item) {
		super(item);
	}

	public RemoveOperationSpec(GenericItem item, boolean cascade) {
		super(item, cascade);
	}

	public RemoveOperationSpec(GenericItem item, boolean cascade, boolean transactional) {
		super(item, cascade, transactional);
	}

	@Override
	public RemoveOperationConfigContext createOperationContext() {
		return new RemoveOperationConfigContext(this);
	}
}
