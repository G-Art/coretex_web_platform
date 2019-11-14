package com.coretex.core.services.bootstrap.meta;

import com.coretex.core.tests.tags.UnitTest;
import com.coretex.items.core.GenericItem;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.items.core.MetaTypeItem;
import com.coretex.items.core.RegularTypeItem;
import com.coretex.meta.AbstractGenericItem;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonMap;
import static java.util.Objects.isNull;
import static org.apache.commons.lang3.tuple.Triple.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@UnitTest
@DisplayName("A Meta Data Context")
class MetaDataContextTest {

    private MetaDataContext metaDataContext = new MetaDataContext();

    private Map<String, BiConsumer<UUID, Map<String, Object>>> populators;
    private Map<String, Supplier<List<Map<String, Object>>>> extractors;
    private Map<String, Function<UUID, Map<String, Object>>> finders;

    private UUID typeItemUuid;
    private UUID attributeItemUuid;
    private UUID regularItemUuid;

    @BeforeEach
    void setUp() {
        //given:
        typeItemUuid = UUID.randomUUID();
        attributeItemUuid = UUID.randomUUID();
        regularItemUuid = UUID.randomUUID();


        populators = new HashMap<>();
        populators.put(MetaTypeItem.ITEM_TYPE, metaDataContext::addTypeData);
        populators.put(MetaAttributeTypeItem.ITEM_TYPE, metaDataContext::addAttributeData);
        populators.put(RegularTypeItem.ITEM_TYPE, metaDataContext::addRegularData);

        extractors = new HashMap<>();
        extractors.put(MetaTypeItem.ITEM_TYPE, metaDataContext::getAllMetaTypes);
        extractors.put(MetaAttributeTypeItem.ITEM_TYPE, metaDataContext::getAllAttributes);
        extractors.put(RegularTypeItem.ITEM_TYPE, metaDataContext::getAllRegularTypes);

        finders = new HashMap<>();
        finders.put(MetaTypeItem.ITEM_TYPE, metaDataContext::getTypeData);
        finders.put(MetaAttributeTypeItem.ITEM_TYPE, metaDataContext::getAttributeData);
        finders.put(RegularTypeItem.ITEM_TYPE, metaDataContext::getRegularData);
    }

    @TestFactory
    @DisplayName("doesn't add new item data to context ...")
    Stream<DynamicTest> shouldNotAddItemDataToContext() {
        return DynamicTest.stream(asList(
                of(MetaTypeItem.ITEM_TYPE, (UUID) null, Collections.<String, Object>emptyMap()),
                of(MetaTypeItem.ITEM_TYPE, typeItemUuid, Collections.<String, Object>emptyMap()),
                of(MetaTypeItem.ITEM_TYPE, typeItemUuid, singletonMap(AbstractGenericItem.UUID, null)),
                of(MetaAttributeTypeItem.ITEM_TYPE, (UUID) null, Collections.<String, Object>emptyMap()),
                of(MetaAttributeTypeItem.ITEM_TYPE, attributeItemUuid, Collections.<String, Object>emptyMap()),
                of(MetaAttributeTypeItem.ITEM_TYPE, attributeItemUuid, singletonMap(AbstractGenericItem.UUID, null)),
                of(RegularTypeItem.ITEM_TYPE, (UUID) null, Collections.<String, Object>emptyMap()),
                of(RegularTypeItem.ITEM_TYPE, regularItemUuid, Collections.<String, Object>emptyMap()),
                of(RegularTypeItem.ITEM_TYPE, regularItemUuid, singletonMap(AbstractGenericItem.UUID, null))
        ).iterator(), this::messageOnContextEmptiness, this::checkContextEmptiness);
    }

    @TestFactory
    @DisplayName("adds new item data to context ...")
    Stream<DynamicTest> shouldAddItemDataToContext() {
        return DynamicTest.stream(asList(
                of(MetaTypeItem.ITEM_TYPE, typeItemUuid, singletonMap(AbstractGenericItem.UUID, (Object) typeItemUuid)),
                of(MetaAttributeTypeItem.ITEM_TYPE, attributeItemUuid, singletonMap(AbstractGenericItem.UUID, (Object) attributeItemUuid)),
                of(RegularTypeItem.ITEM_TYPE, regularItemUuid, singletonMap(AbstractGenericItem.UUID, (Object) regularItemUuid)))
                        .iterator(),
                data -> String.format("of %s type has uuid value within", data.getLeft()),
                this::checkThatContextContainsMetaTypeUuid);

    }

    private void checkContextEmptiness(Triple<String, UUID, Map<String, Object>> data) {
        // given:
        String typeCode = data.getLeft();
        UUID uuid = data.getMiddle();
        Map<String, Object> itemData = data.getRight();
        populators.get(typeCode).accept(uuid, itemData);

        // when:
        List<Map<String, Object>> itemDatas = extractors.get(typeCode).get();

        // then:
        assertThat(typeCode + " data in context must be empty", CollectionUtils.isEmpty(itemDatas));
    }

    private String messageOnContextEmptiness(Triple<String, UUID, Map<String, Object>> data) {
        String typeCode = data.getLeft();
        UUID uuid = data.getMiddle();
        Map<String, Object> itemData = data.getRight();

        String resultMessage = "of type " + typeCode + " when ";

        if (isNull(uuid)) {
            return resultMessage + "uuid is null";
        }
        if (MapUtils.isEmpty(itemData)) {
            return resultMessage + "item data is empty";
        }
        if (isNull(itemData.get(GenericItem.META_TYPE))) {
            return resultMessage + "item data contains null value";
        }
        return resultMessage;

    }

    private void checkThatContextContainsMetaTypeUuid(Triple<String, UUID, Map<String, Object>> data) {
        // given:
        String typeCode = data.getLeft();
        UUID uuid = data.getMiddle();
        Map<String, Object> itemData = data.getRight();
        populators.get(typeCode).accept(uuid, itemData);

        // when:
        Map<String, Object> resultItemData = finders.get(typeCode).apply(uuid);

        // then:
        assertTrue(MapUtils.isNotEmpty(resultItemData), typeCode + " data is not empty");
        assertEquals(uuid, resultItemData.get(AbstractGenericItem.UUID), typeCode + " data in context contains value");

        // and:
        resultItemData = metaDataContext.getItemData(uuid);
        assertTrue(MapUtils.isNotEmpty(resultItemData), typeCode + " data is not empty for global select");
        assertEquals(uuid, resultItemData.get(AbstractGenericItem.UUID), typeCode + " data in context contains value for global select");
    }

}