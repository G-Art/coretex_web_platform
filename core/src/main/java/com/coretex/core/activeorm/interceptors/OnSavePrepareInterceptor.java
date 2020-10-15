package com.coretex.core.activeorm.interceptors;

import com.coretex.meta.AbstractGenericItem;

public interface OnSavePrepareInterceptor<I extends AbstractGenericItem> {

	void onSavePrepareAction(I item);
}
