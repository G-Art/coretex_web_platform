package com.coretex.meta;

import com.coretex.core.services.items.context.ItemContext;
import com.coretex.core.services.items.context.factory.ItemContextFactory;
import com.coretex.server.ApplicationContextProvider;

import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import static com.coretex.core.services.bootstrap.BootstrapConfig.ITEM_CONTEXT_FACTORY_QUALIFIER;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 *         create by 12-02-2016
 */
public abstract class AbstractGenericItem {

    public static final String UUID = "uuid";

    private static final String ITEM_COX_FACTORY_UNAVAILABLE_ERROR_MSG =
            "Could't find item context factory to generate item context";

    private ItemContext ctx;

    private ItemContextFactory itemContextFactory;

    public AbstractGenericItem() {
        itemContextFactory = ApplicationContextProvider.getApplicationContext().getBean(ITEM_CONTEXT_FACTORY_QUALIFIER, ItemContextFactory.class);
        checkNotNull(this.itemContextFactory, ITEM_COX_FACTORY_UNAVAILABLE_ERROR_MSG);
        this.ctx = getFactory().create(this.getClass());
    }

    public AbstractGenericItem(ItemContext ctx) {
        this.ctx = ctx;
    }

    public UUID getUuid() {
        return getItemContext().getUuid();
    }

    public void setUuid(UUID uuid) {
        getItemContext().setUuid(uuid);
    }

    public ItemContext getItemContext() {
        return ctx;
    }

    public final ItemContextFactory getFactory() {
        return this.itemContextFactory;
    }

    public void setAttributeValue(String name, Object value) {
        getItemContext().setValue(name, value);
    }

    public Object getAttributeValue(String name) {
        return getItemContext().getValue(name);
    }

    public void setAttributeValue(String name, Object value, Locale locale) {
        getItemContext().setLocalizedValue(name, value, locale);
    }

    public Object getAttributeValue(String name, Locale locale) {
        return getItemContext().getLocalizedValue(name, locale);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        if(Objects.nonNull(getUuid()) && Objects.nonNull(((AbstractGenericItem) o).getUuid())) return Objects.equals(getUuid(), ((AbstractGenericItem) o).getUuid());

        return hashCode() == o.hashCode();

    }

    @Override
    public int hashCode() {
        return Objects.nonNull(getUuid()) ? Objects.hashCode(getUuid()) : super.hashCode();
    }
}
