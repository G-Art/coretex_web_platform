package com.coretex.core.activeorm.interceptors;

import com.coretex.meta.AbstractGenericItem;

public interface OnRemoveInterceptor<I extends AbstractGenericItem> {

	void onRemoveAction(I item);
}
