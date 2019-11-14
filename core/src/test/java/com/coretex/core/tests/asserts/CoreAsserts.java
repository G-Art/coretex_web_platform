package com.coretex.core.tests.asserts;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public final class CoreAsserts {

    public static void assertCollectionSize(int size, Collection<?> collection) {
        assertNotNull(collection, "Expected that collection is not null");
        assertEquals(size, collection.size(), "Size of result collection is mismatched with expected value");
    }

    public static void assertNotEmpty(Collection<?> collection) {
        assertNotNull(collection, "Expected that collection is not null");
        assertFalse(collection.isEmpty(), "Size of result collection is not empty");
    }

    public static void assertEmpty(Collection<?> collection) {
        assertNotNull(collection, "Expected that collection is not null");
        assertTrue(collection.isEmpty(), "Size of result collection is not empty");
    }

    private CoreAsserts() {
    }
}
