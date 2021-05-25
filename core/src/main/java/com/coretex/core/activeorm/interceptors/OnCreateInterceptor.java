package com.coretex.core.activeorm.interceptors;

import com.coretex.meta.AbstractGenericItem;

public interface OnCreateInterceptor<I extends AbstractGenericItem> {

	void onCreateAction(I item);
}
