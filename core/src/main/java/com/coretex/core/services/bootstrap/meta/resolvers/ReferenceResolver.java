package com.coretex.core.services.bootstrap.meta.resolvers;

import com.coretex.core.services.bootstrap.meta.MetaDataContext;
import com.coretex.core.services.bootstrap.meta.MetaItemContext;
import com.coretex.items.core.GenericItem;
import org.apache.commons.collections4.MapUtils;

import java.util.*;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;

public abstract class ReferenceResolver extends DataResolver {

    @SuppressWarnings("unchecked")
    public static  <T> List<T> toList(Object list) {
        if (!(list instanceof List)) {
            return new ArrayList<>();
        }
        return (List<T>) list;
    }

    @SuppressWarnings("unchecked")
    public static  <T> Set<T> toSet(Object set) {
        if (!(set instanceof Set)) {
            return new HashSet<>();
        }
        return (Set<T>) set;
    }

    protected MetaItemContext itemContext;

    public ReferenceResolver(MetaDataContext dataContext, MetaItemContext itemContext) {
        super(dataContext);
        this.itemContext = itemContext;
    }

    protected UUID toUUID(Object link) {
        checkArgument(nonNull(link), "Link must be not null");
        if (UUID.class == link.getClass()) {
            return (UUID) link;
        }
        if (link instanceof GenericItem) {
            return ((GenericItem) link).getUuid();
        }
        return UUID.fromString(link.toString());
    }

}
