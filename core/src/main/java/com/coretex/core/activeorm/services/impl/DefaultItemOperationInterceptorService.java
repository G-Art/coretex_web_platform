package com.coretex.core.activeorm.services.impl;

import com.coretex.core.activeorm.services.ItemInterceptorsRegistry;
import com.coretex.core.activeorm.services.ItemOperationInterceptorService;
import com.coretex.meta.AbstractGenericItem;

import javax.annotation.Resource;

public class DefaultItemOperationInterceptorService implements ItemOperationInterceptorService {

	@Resource
	private ItemInterceptorsRegistry itemInterceptorsRegistry;

	@Override
	public void onCreate(AbstractGenericItem item) {
		itemInterceptorsRegistry.getCreateInterceptors(item.getItemContext().getTypeCode())
				.forEach(interceptor -> interceptor.onCreateAction(item));
	}

	@Override
	public void onSavePrepare(AbstractGenericItem item) {
		itemInterceptorsRegistry.getSavePrepareInterceptors(item.getItemContext().getTypeCode())
				.forEach(interceptor -> interceptor.onSavePrepareAction(item));
	}

	@Override
	public void onSaved(AbstractGenericItem item) {
		itemInterceptorsRegistry.getSavedInterceptors(item.getItemContext().getTypeCode())
				.forEach(interceptor -> interceptor.onSavedAction(item));
	}

	@Override
	public void onRemove(AbstractGenericItem item) {
		itemInterceptorsRegistry.getRemoveInterceptors(item.getItemContext().getTypeCode())
				.forEach(interceptor -> interceptor.onRemoveAction(item));
	}

	@Override
	public void onLoad(AbstractGenericItem item) {
		itemInterceptorsRegistry.getLoadInterceptors(item.getItemContext().getTypeCode())
				.forEach(interceptor -> interceptor.onLoadAction(item));
	}
}
