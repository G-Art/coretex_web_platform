package com.coretex.core.services.bootstrap.meta;

import com.coretex.core.services.bootstrap.meta.resolvers.*;
import com.coretex.items.core.*;
import com.coretex.meta.AbstractGenericItem;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@DisplayName("A Meta Item Resolver")
public class MetaItemResolverTest {

    @Mock
    private MetaDataContext metaDataContext;

    @Mock
    private MetaItemContext metaItemContext;


    private MetaItemResolver resolver;

}
