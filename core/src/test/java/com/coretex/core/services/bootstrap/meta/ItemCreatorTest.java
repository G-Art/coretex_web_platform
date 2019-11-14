package com.coretex.core.services.bootstrap.meta;

import com.coretex.core.tests.extentions.MockitoExtension;
import com.coretex.core.tests.tags.UnitTest;
import com.coretex.items.core.GenericItem;
import com.coretex.items.core.LocaleItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@UnitTest
@DisplayName("A Meta Item Creator")
@ExtendWith(MockitoExtension.class)
class ItemCreatorTest {

    private ItemCreator itemCreator;

    @Mock
    private MetaDataContext metaDataContext;

    @Mock
    private MetaItemContext metaItemContext;

    private UUID itemUUID;

    private Class<LocaleItem> genericItemClass;

    @BeforeEach
    void setUp() {
        itemUUID = UUID.randomUUID();
        genericItemClass = LocaleItem.class;
        itemCreator = new ItemCreator(metaDataContext, metaItemContext);

    }

    @Test
    @DisplayName("throws exception when item uuid is null")
    void shouldThrowsExceptionWhenUuidIsNull() {
        // then:
        assertThrows(IllegalArgumentException.class, () -> itemCreator.convert(null, null));
    }

    @Test
    @DisplayName("throws exception when item class is null")
    void shouldThrowsExceptionWhenItemClassIsNull() {
        assertThrows(IllegalArgumentException.class, () -> itemCreator.convert(itemUUID, null));
    }

    @Test
    @DisplayName("throws exception when item class is abstract")
    void shouldThrowsExceptionWhenItemClassIsAbstract() {
        assertThrows(IllegalStateException.class, () -> itemCreator.convert(itemUUID, GenericItem.class));
    }

    @Test
    @DisplayName("creates new item by specified uuid and class")
    void shouldCreateItemBySpecifiedUUIDAndClass() {
        // when:
        GenericItem resultItem = itemCreator.convert(itemUUID, genericItemClass);

        // then:
        assertAll("Item is not correctly fulfilled",
                () -> assertNotNull(resultItem),
                () ->  assertNotNull(resultItem.getItemContext()),
                () ->  assertNotNull(resultItem.getItemContext().getProvider()),
                () ->  assertEquals(itemUUID, resultItem.getUuid()));
    }


}