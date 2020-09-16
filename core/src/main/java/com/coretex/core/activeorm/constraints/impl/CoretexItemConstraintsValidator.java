package com.coretex.core.activeorm.constraints.impl;

import com.coretex.core.activeorm.constraints.ItemConstraint;
import com.coretex.core.activeorm.constraints.ItemConstraintsValidator;
import com.coretex.items.core.GenericItem;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CoretexItemConstraintsValidator implements ItemConstraintsValidator {

	@Autowired(required = false)
	private List<ItemConstraint> constraints;

	@Override
	public void validate(GenericItem item) {
		if (CollectionUtils.isNotEmpty(constraints)) {
			constraints.forEach(itemConstraint -> itemConstraint.check(item));
		}
	}
}
