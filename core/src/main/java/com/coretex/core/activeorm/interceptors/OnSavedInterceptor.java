package com.coretex.core.activeorm.interceptors;

import com.coretex.meta.AbstractGenericItem;

public interface OnSavedInterceptor<I extends AbstractGenericItem> {

	void onSavedAction(I item);
}
