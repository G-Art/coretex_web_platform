package com.coretex.core.activeorm.interceptors.impl;

import com.coretex.core.activeorm.constraints.ItemConstraintsValidator;
import com.coretex.core.activeorm.interceptors.Interceptor;
import com.coretex.core.activeorm.interceptors.OnSavePrepareInterceptor;
import com.coretex.items.core.GenericItem;

import javax.annotation.Resource;

@Interceptor(
		items = GenericItem.class
)
public class ItemConstraintValidationInterceptor<I extends GenericItem>
		implements OnSavePrepareInterceptor<I> {

	@Resource
	private ItemConstraintsValidator itemConstraintsValidator;

	@Override
	public void onSavePrepareAction(I item) {
		itemConstraintsValidator.validate(item);
	}

}
