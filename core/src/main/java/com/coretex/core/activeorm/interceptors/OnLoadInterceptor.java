package com.coretex.core.activeorm.interceptors;

import com.coretex.meta.AbstractGenericItem;

public interface OnLoadInterceptor<I extends AbstractGenericItem> {

	void onLoadAction(I item);
}
