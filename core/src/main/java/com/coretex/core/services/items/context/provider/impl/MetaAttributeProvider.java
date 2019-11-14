package com.coretex.core.services.items.context.provider.impl;

import java.util.Map;
import java.util.Objects;

import com.coretex.core.services.items.context.ItemContext;
import com.coretex.core.services.items.context.provider.AttributeProvider;
import com.google.common.collect.ImmutableMap;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;

public class MetaAttributeProvider implements AttributeProvider {

	private final ImmutableMap<String, Object> initialValues;

	public MetaAttributeProvider(Map<String, Object> initValues) {
		checkArgument(nonNull(initValues), "Initial values for meta attribute provider must present");
		initValues.values().removeIf(Objects::isNull);
		this.initialValues = ImmutableMap.copyOf(initValues);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getValue(String metaAttributeName, ItemContext ctx) {
		return (T) initialValues.get(metaAttributeName);
	}
}
