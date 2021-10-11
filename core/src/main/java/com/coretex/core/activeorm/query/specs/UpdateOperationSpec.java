package com.coretex.core.activeorm.query.specs;

import com.coretex.core.activeorm.query.operations.contexts.UpdateOperationConfigContext;
import com.coretex.items.core.GenericItem;
import net.sf.jsqlparser.statement.update.Update;

public class UpdateOperationSpec extends ModificationOperationSpec<Update> {

	public UpdateOperationSpec(GenericItem item) {
		super(item);
	}

	public UpdateOperationSpec(GenericItem item, boolean cascade) {
		super(item, cascade);
	}

	public UpdateOperationSpec(GenericItem item, boolean cascade, boolean transactional) {
		super(item, cascade, transactional);
	}
	@Override
	public UpdateOperationConfigContext createOperationContext() {
		return new UpdateOperationConfigContext(this);
	}
}
