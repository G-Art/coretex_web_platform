package com.coretex.core.services.items.context.factory.impl;

import java.util.UUID;

import com.coretex.core.services.items.context.ItemContext;
import com.coretex.core.services.items.context.factory.ItemContextFactory;
import com.coretex.core.services.items.context.impl.ItemContextBuilder;
import com.coretex.core.services.items.context.provider.AttributeProvider;
import com.coretex.meta.AbstractGenericItem;

import static com.coretex.core.services.items.context.impl.ItemContextBuilder.createDefaultContextBuilder;

public class DefaultItemContextFactory implements ItemContextFactory {

	private AttributeProvider attributeProvider;

	@Override
	public ItemContext create(Class<? extends AbstractGenericItem> itemClass) {
		return create(createDefaultContextBuilder(itemClass));
	}

	@Override
	public ItemContext create(String typeCode) {
		return create(createDefaultContextBuilder(typeCode));
	}

	@Override
	public ItemContext create(Class<? extends AbstractGenericItem> itemClass, UUID uuid) {
		return create(createDefaultContextBuilder(itemClass).setUuid(uuid));
	}

	private ItemContext create(ItemContextBuilder builder) {
		builder.addProvider(attributeProvider);
		return builder.build();
	}

	public void setAttributeProvider(AttributeProvider attributeProvider) {
		this.attributeProvider = attributeProvider;
	}
}
