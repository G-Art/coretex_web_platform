package com.coretex.core.activeorm.query.specs;

import com.coretex.items.core.GenericItem;

public interface CascadeModificationOperationSpec {
	boolean existInCascade(GenericItem item);
}
