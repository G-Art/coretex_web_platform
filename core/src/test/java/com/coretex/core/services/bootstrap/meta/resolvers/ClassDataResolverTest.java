package com.coretex.core.services.bootstrap.meta.resolvers;

import com.coretex.core.tests.tags.UnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@UnitTest
@DisplayName("A Class Data Resolver")
class ClassDataResolverTest extends DataResolverUnitTest {

    private static final String JAVA_LANG_OBJECT_NAME = "java.lang.Object";

    private String testAttributeName = "testAttribute";

    @BeforeEach
    void setUp() {
        // given:
        dataResult.put(testAttributeName, JAVA_LANG_OBJECT_NAME);
        dataResolver = new ClassDataResolver(metaDataContext, testAttributeName);
    }

    @Test
    @DisplayName("resolves string value of given attribute to class type")
    void shouldResolveClassForGivenAttributeName() {
        // when:
        dataResolver.resolve(testUuid);

        //then:
        assertEquals(Object.class ,dataResult.get(testAttributeName));
    }

    @Test
    @DisplayName("doesn't resolve value that is null")
    void shouldNotResolveClassWhenValueIsNull() {
        // given:
        dataResult.put(testAttributeName, null);

        // when:
        dataResolver.resolve(testUuid);

        // then:
        assertNull(dataResult.get(testAttributeName));
    }

    @Test
    @DisplayName("doesn't resolve value to class when can't find class")
    void shouldSetAttributeValueAsNullWhenCantFindClass() {
        // given:
        dataResult.put(testAttributeName, "IllegalClassName");

        // then:
        assertThrows(RuntimeException.class, () -> dataResolver.resolve(testUuid));
    }
}