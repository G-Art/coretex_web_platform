package com.coretex.core.activeorm.constraints;

import com.coretex.core.activeorm.constraints.exceptions.ItemConstraintViolationException;
import com.coretex.items.core.GenericItem;

public interface ItemConstraint {

	void check(GenericItem item) throws ItemConstraintViolationException;
}
