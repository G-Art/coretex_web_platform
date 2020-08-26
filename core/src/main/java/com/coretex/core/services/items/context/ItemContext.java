package com.coretex.core.services.items.context;

import com.coretex.core.services.items.context.impl.ItemContextBuilder;
import com.coretex.core.services.items.context.provider.AttributeProvider;
import com.coretex.server.ApplicationContextProvider;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Objects.nonNull;

/**
 * @author Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 * create by 13-02-2016
 */
public abstract class ItemContext implements Serializable {

	private String typeCode;

	protected UUID uuid;

	private transient AttributeProvider provider;

	public ItemContext(ItemContextBuilder builder) {
		checkArgument(nonNull(builder), "Context builder is required");
		typeCode = builder.getTypeCode();
		uuid = builder.getUuid();
		provider = builder.getProvider();
	}

	protected Object readResolve() throws ObjectStreamException {
		provider = ApplicationContextProvider.getApplicationContext().getBean("defaultAttributeProvider", AttributeProvider.class);
		return this;
	}

	public abstract Collection<String> loadedAttributes();

	public abstract boolean isNew();

	public abstract boolean isExist();

	public abstract <T> T getOriginValue(String attributeName);

	public abstract <T> T getOriginLocalizedValue(String attributeName);

	public abstract <T> T getOriginLocalizedValue(String attributeName, Locale locale);

	public abstract <T> Map<Locale, T> getOriginLocalizedValues(String attributeName);

	public abstract <T> T getValue(String attributeName);

	public abstract <T> T getLocalizedValue(String attributeName);

	public abstract <T> Map<Locale, T> getLocalizedValues(String attributeName);

	public abstract <T> T getLocalizedValue(String attributeName, Locale locale);

	public abstract <T> void setValue(String attributeName, T value);

	public abstract <T> void setLocalizedValue(String attributeName, T value);

	public abstract <T> void setLocalizedValue(String attributeName, T value, Locale locale);

	public abstract void initValue(String attributeName, Object initialValue);

	public abstract void flush();

	public abstract void refresh();

	public String getTypeCode() {
		return typeCode;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		checkNotNull(uuid, "Uuid can't be null");
		this.uuid = uuid;
	}

	public AttributeProvider getProvider() {
		return provider;
	}

	public boolean isDirty() {
		return false;
	}

	public boolean isDirty(String attributeName) {
		return false;
	}
}
