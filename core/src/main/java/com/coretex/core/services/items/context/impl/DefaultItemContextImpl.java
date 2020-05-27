package com.coretex.core.services.items.context.impl;

import com.coretex.core.services.items.context.ItemContext;
import com.coretex.core.services.items.context.attributes.AttributeValueHolder;
import com.coretex.core.services.items.context.attributes.LocalizedAttributeValueHolder;
import com.coretex.items.core.GenericItem;
import com.coretex.meta.AbstractGenericItem;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 * create by 13-02-2016
 */
public class DefaultItemContextImpl extends ItemContext {

	private Map<String, AttributeValueHolder> attributeHolders;
	private Map<String, LocalizedAttributeValueHolder> localizedAttributeHolders;

	public DefaultItemContextImpl(ItemContextBuilder builder) {
		super(builder);
		attributeHolders = new HashMap<>();
		localizedAttributeHolders = new HashMap<>();
		if (isNew()) {
			attributeHolders.put(GenericItem.META_TYPE, AttributeValueHolder.createLazyValueHolder(GenericItem.META_TYPE, this));
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getValue(String attributeName) {

		if (AbstractGenericItem.UUID.equals(attributeName)) {
			return (T) getUuid();
		}

		HolderProcessor<AttributeValueHolder, T> holderProcessor = new HolderProcessor<>(attributeHolders,
				() -> AttributeValueHolder.createLazyValueHolder(attributeName, this),
				holder -> (T) holder.get(getProvider()),
				AttributeValueHolder::isLoaded);

		return holderProcessor.getValue(attributeName);
	}

	@Override
	public <T> T getLocalizedValue(String attributeName) {
		return getLocalizedValue(attributeName, null);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> Map<Locale, T> getLocalizedValues(String attributeName) {

		HolderProcessor<LocalizedAttributeValueHolder, Map<Locale, T>> holderProcessor = new HolderProcessor<>(localizedAttributeHolders,
				() -> LocalizedAttributeValueHolder.initValueHolder(attributeName, this, getProvider().getValue(attributeName, this)),
				holder -> (Map<Locale, T>) holder.getAll(getProvider()),
				LocalizedAttributeValueHolder::isLoaded);

		return holderProcessor.getValue(attributeName);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getLocalizedValue(String attributeName, Locale locale) {
		HolderProcessor<LocalizedAttributeValueHolder, T> holderProcessor = new HolderProcessor<>(localizedAttributeHolders,
				() -> LocalizedAttributeValueHolder.initValueHolder(attributeName, this, getProvider().getValue(attributeName, this)),
				holder -> (T) holder.get(getProvider(), locale),
				LocalizedAttributeValueHolder::isLoaded);

		return holderProcessor.getValue(attributeName);
	}

	@Override
	public <T> void setValue(String attributeName, T value) {

		if (AbstractGenericItem.UUID.equals(attributeName)) {
			setUuid((UUID) value);
		} else {
			AttributeValueHolder valueHolder = attributeHolders.get(attributeName);
			if (isNull(valueHolder)) {
				Object initialValue = getProvider().getValue(attributeName, this);
				valueHolder = AttributeValueHolder.initValueHolder(attributeName, this, initialValue);
				attributeHolders.put(attributeName, valueHolder);
			}
			valueHolder.set(value);
		}

	}

	@Override
	public <T> void setLocalizedValue(String attributeName, T value) {
		this.setLocalizedValue(attributeName, value, null);
	}

	@Override
	public <T> void setLocalizedValue(String attributeName, T value, Locale locale) {
		LocalizedAttributeValueHolder valueHolder = localizedAttributeHolders.get(attributeName);
		if (isNull(valueHolder)) {
			Map<String, Object> initialValue = getProvider().getValue(attributeName, this);
			valueHolder = LocalizedAttributeValueHolder.initValueHolder(attributeName, this, initialValue);
			localizedAttributeHolders.put(attributeName, valueHolder);
		}
		valueHolder.set(value, locale);
	}

	@Override
	public void initValue(String attributeName, Object initialValue) {
		if (AbstractGenericItem.UUID.equals(attributeName)) {
			setUuid((UUID) initialValue);
		} else {
			AttributeValueHolder valueHolder = AttributeValueHolder.initValueHolder(attributeName, this, initialValue);
			attributeHolders.put(attributeName, valueHolder);
		}
	}

	@Override
	public Collection<String> loadedAttributes() {
		return Sets.union(attributeHolders.keySet(),
				localizedAttributeHolders.keySet());
	}

	@Override
	public boolean isNew() {
		return Objects.isNull(getUuid());
	}

	@Override
	public boolean isExist() {
		var loadedUUID = getProvider().getValue(AbstractGenericItem.UUID, this);
		if (Objects.isNull(loadedUUID)) { // prevents implicit removing
			uuid = null;
			return false;
		}
		return loadedUUID.equals(getUuid());
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getOriginValue(String attributeName) {
		if (AbstractGenericItem.UUID.equals(attributeName)) {
			return (T) getUuid();
		}

		HolderProcessor<AttributeValueHolder, T> holderProcessor = new HolderProcessor<>(attributeHolders,
				() -> AttributeValueHolder.createLazyValueHolder(attributeName, this),
				holder -> (T) holder.getOriginalValue(getProvider()),
				AttributeValueHolder::isLoaded);

		return holderProcessor.getValue(attributeName);
	}

	@Override
	public <T> T getOriginLocalizedValue(String attributeName) {
		return getOriginLocalizedValue(attributeName, null);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getOriginLocalizedValue(String attributeName, Locale locale) {

		HolderProcessor<LocalizedAttributeValueHolder, T> holderProcessor = new HolderProcessor<>(localizedAttributeHolders,
				() -> LocalizedAttributeValueHolder.createLazyValueHolder(attributeName, this),
				holder -> (T) holder.getOriginalValue(getProvider(), locale),
				LocalizedAttributeValueHolder::isLoaded);

		return holderProcessor.getValue(attributeName);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> Map<Locale, T> getOriginLocalizedValues(String attributeName) {

		HolderProcessor<LocalizedAttributeValueHolder, Map<Locale, T>> holderProcessor = new HolderProcessor<>(localizedAttributeHolders,
				() -> LocalizedAttributeValueHolder.createLazyValueHolder(attributeName, this),
				holder -> (Map<Locale, T>) holder.getOriginalValues(getProvider()),
				LocalizedAttributeValueHolder::isLoaded);

		return holderProcessor.getValue(attributeName);
	}

	@Override
	public boolean isDirty() {
		return attributeHolders.values().stream().anyMatch(AttributeValueHolder::isDirty) || localizedAttributeHolders.values().stream().anyMatch(LocalizedAttributeValueHolder::isDirty);
	}

	@Override
	public boolean isDirty(String attributeName) {
		checkArgument(isNotBlank(attributeName), "Attribute name must be not null or empty");
		AttributeValueHolder attributeValueHolder = attributeHolders.get(attributeName);
		if (nonNull(attributeValueHolder)) {
			return attributeValueHolder.isDirty();
		}

		LocalizedAttributeValueHolder localizedAttributeValueHolder = localizedAttributeHolders.get(attributeName);
		if (nonNull(localizedAttributeValueHolder)) {
			return localizedAttributeValueHolder.isDirty();
		}
		return false;
	}

	@Override
	public void flush() {
		attributeHolders.values().stream()
				.filter(AttributeValueHolder::isDirty)
				.forEach(AttributeValueHolder::flush);

		localizedAttributeHolders.values().stream()
				.filter(LocalizedAttributeValueHolder::isDirty)
				.forEach(LocalizedAttributeValueHolder::flush);
	}

	@Override
	public void refresh() {
		attributeHolders.values().stream()
				.filter(AttributeValueHolder::isLoaded)
				.forEach(AttributeValueHolder::refresh);

		localizedAttributeHolders.values().stream()
				.filter(LocalizedAttributeValueHolder::isLoaded)
				.forEach(LocalizedAttributeValueHolder::refresh);
	}


	private final class HolderProcessor<H, T> {

		private Map<String, H> holders;
		private Supplier<H> holderSupplier;
		private Function<H, T> valueEjector;
		private Function<H, Boolean> isLoadedF;

		private HolderProcessor(Map<String, H> holders, Supplier<H> holderSupplier, Function<H, T> valueEjector, Function<H, Boolean> isLoadedF) {
			this.holders = holders;
			this.holderSupplier = holderSupplier;
			this.valueEjector = valueEjector;
			this.isLoadedF = isLoadedF;
		}

		public T getValue(String attributeName) {
			H valueHolder = holders.get(attributeName);

			if (isNull(valueHolder)) {
				valueHolder = holderSupplier.get();
			}

			T value = valueEjector.apply(valueHolder);

			if (isLoadedF.apply(valueHolder)) {
				holders.put(attributeName, valueHolder);
			}
			return value;
		}
	}

}
