package com.coretex.core.services.items.context.factory;

import java.util.UUID;

import com.coretex.core.services.items.context.ItemContext;
import com.coretex.meta.AbstractGenericItem;

public interface ItemContextFactory {

	ItemContext create(Class<? extends AbstractGenericItem> itemClass);

	ItemContext create(String typeCode);

	ItemContext create(Class<? extends AbstractGenericItem> itemClass, UUID uuid);
}
