package com.coretex.core.services.bootstrap.meta.resolvers;

import com.coretex.core.services.bootstrap.meta.MetaItemContext;
import com.coretex.core.tests.tags.UnitTest;
import com.coretex.items.core.GenericItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;

import java.util.*;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@UnitTest
@DisplayName("A One To Relation Resolver")
public class OneToResolverUnitTest extends DataResolverUnitTest {

    @Mock
    private MetaItemContext metaItemContext;

    @Mock
    private GenericItem sourceItem;

    @Mock
    private GenericItem targetItem;

    private String sourceAttributeName;

    private String targetAttributeName;

    private Function<Object, ? extends Collection<Object>> targetContainerResolver;

    private UUID sourceUuid;

    private Map<String, Object> targetData;

    private boolean failOnNotFound;

    @BeforeEach
    void initRelation() {
        // given:
        // resolver init configuration
        sourceAttributeName = "testSourceAttribute";
        targetAttributeName = "testTargetAttribute";
        targetContainerResolver = ReferenceResolver::toList;
        failOnNotFound = false;

        sourceUuid = UUID.randomUUID();
        dataResult.put(sourceAttributeName, sourceUuid);

        targetData = new HashMap<>();
        targetData.put(targetAttributeName, testUuid);

        dataResolver = mock(OneToResolver.class);

        // when:
        doAnswer(this::generateResolver).when(dataResolver).resolve(any(UUID.class));
        when(metaDataContext.getItemData(sourceUuid)).thenAnswer(ctx -> targetData);
        when(metaItemContext.getGenericItem(sourceUuid)).thenAnswer(ctx -> targetItem);
        when(metaItemContext.getGenericItem(testUuid)).thenAnswer(ctx -> sourceItem);
        when(targetItem.getUuid()).thenAnswer(ctx -> sourceUuid);
        when(sourceItem.getUuid()).thenAnswer(ctx -> testUuid);
    }

    @Test
    @DisplayName("resolves target uuid to target item")
    void shouldResolveTargetItemInItemDataByUUID() {
        // given:
        targetAttributeName = null;

        // when:
        dataResolver.resolve(testUuid);

        // then:
        assertEquals(targetItem, dataResult.get(sourceAttributeName));
    }

    @Test
    @DisplayName("resolves target item to target item")
    void shouldResolveTargetItemInItemData() {
        //given:
        targetAttributeName = null;
        dataResult.put(sourceAttributeName, targetItem);

        // when:
        dataResolver.resolve(testUuid);

        // then:
        assertEquals(targetItem, dataResult.get(sourceAttributeName));
    }

    @Test
    @DisplayName("throws exception when can't find target item by target uuid")
    void shouldThrowExceptionWhenTargetItemIsNotFound() {
        // given:
        targetAttributeName = null;
        targetItem = null;
        failOnNotFound = true;

        // then:
        assertThrows(IllegalStateException.class, () -> dataResolver.resolve(testUuid));
    }

    @Test
    @DisplayName("resolves null value when can't find target item by target uuid")
    void shouldResolveNullValueWhenTargetItemIsNotFound() {
        // given:
        targetAttributeName = null;
        targetItem = null;

        // when:
        dataResolver.resolve(testUuid);

        //then:
        assertNull(dataResult.get(sourceAttributeName));
    }

    @Test
    @DisplayName("resolves one to one relation")
    void shouldResolveTargetOneToOneRelation() {
        // given:
        targetContainerResolver = null;

        // when:
        dataResolver.resolve(testUuid);

        // when:
        assertEquals(sourceItem, targetData.get(targetAttributeName));
    }

    @Test
    @DisplayName("resolves null source value when source item is not found")
    void shouldResolveNullSourceValueInTargetData() {
        // given:
        sourceItem = null;

        // when:
        dataResolver.resolve(testUuid);

        // when:
        assertNull(targetData.get(targetAttributeName));
    }

    @Test
    @DisplayName("throws exception when source item is not found")
    void shouldThrowExceptionWhenSourceItemWasNotFound() {
        // given:
        failOnNotFound = true;
        sourceItem = null;

        // then:
        assertThrows(IllegalStateException.class, () -> dataResolver.resolve(testUuid));
    }


    @Test
    @DisplayName("resolves one to many relation with first item in list")
    void shouldResolveOneToManyRelationWithFirstItemInList() {
        // when:
        dataResolver.resolve(testUuid);

        // when:
        assertIterableEquals(singletonList(sourceItem), (Iterable) targetData.get(targetAttributeName));
    }

    @Test
    @DisplayName("resolves one to many relation with second item in list")
    void shouldResolveOneToManyRelationWithSecondItemInList() {
        //given:
        GenericItem firstElement = mock(GenericItem.class);
        targetData.put(targetAttributeName, new ArrayList<>(singletonList(firstElement)));

        // when:
        dataResolver.resolve(testUuid);

        // when:
        assertIterableEquals(asList(firstElement, sourceItem), (Iterable) targetData.get(targetAttributeName));
    }

    private Object generateResolver(InvocationOnMock ctx) {
        new OneToResolver(metaDataContext, metaItemContext, sourceAttributeName,
                targetAttributeName, targetContainerResolver, failOnNotFound).resolve(((UUID) ctx.getArguments()[0]));
        return null;
    }
}
