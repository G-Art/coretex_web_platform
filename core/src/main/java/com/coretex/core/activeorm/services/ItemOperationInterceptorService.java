package com.coretex.core.activeorm.services;

import com.coretex.meta.AbstractGenericItem;

public interface ItemOperationInterceptorService {

	void onCreate(AbstractGenericItem item);
	void onSavePrepare(AbstractGenericItem item);

	void onSaved(AbstractGenericItem item);

	void onRemove(AbstractGenericItem item);
	void onLoad(AbstractGenericItem item);
}
