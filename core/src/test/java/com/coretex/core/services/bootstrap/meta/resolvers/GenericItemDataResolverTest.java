package com.coretex.core.services.bootstrap.meta.resolvers;

import com.coretex.core.tests.tags.UnitTest;
import com.coretex.items.core.GenericItem;
import org.junit.jupiter.api.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.stream.Stream;

import static com.coretex.core.general.constants.GeneralConstants.SYSTEM_TIME_ZONE;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.ObjectUtils.NULL;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.tuple.Pair.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@UnitTest
@DisplayName("A Generic Item Data Resolver")
class GenericItemDataResolverTest extends DataResolverUnitTest {

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    @BeforeEach
    void setUp() {
        // given:
        createDate = LocalDateTime.now(SYSTEM_TIME_ZONE);
        updateDate = LocalDateTime.now(SYSTEM_TIME_ZONE);

        dataResult.put(GenericItem.CREATE_DATE, Timestamp.from(createDate.atZone(SYSTEM_TIME_ZONE).toInstant()));
        dataResult.put(GenericItem.UPDATE_DATE, Timestamp.from(updateDate.atZone(SYSTEM_TIME_ZONE).toInstant()));
        dataResolver = new GenericItemDataResolver(metaDataContext);
    }

    @Test
    @DisplayName("resolves create and update date to local date time")
    void shouldResolveUpdateAndCreateDateToLocalDateTime() {
        // when:
        dataResolver.resolve(testUuid);

        //then:
        assertEquals(createDate, dataResult.get(GenericItem.CREATE_DATE));
        assertEquals(updateDate, dataResult.get(GenericItem.UPDATE_DATE));
    }



    @TestFactory
    @DisplayName("Throws exception when ...")
    Stream<DynamicTest> checkCreateUpdateClass() {
        return DynamicTest.stream(asList(
                of(GenericItem.CREATE_DATE, createDate),
                of(GenericItem.CREATE_DATE, null),
                of(GenericItem.UPDATE_DATE, null),
                of(GenericItem.UPDATE_DATE, updateDate)).iterator(),
                entry -> format("%s attribute with value type %s", entry.getKey(), defaultIfNull(entry.getValue(), NULL).getClass().getSimpleName()),
                entry -> checkIllegalInstance(entry.getKey(), entry.getValue()));

    }

    private void checkIllegalInstance(String attributeName, Object value) {
        // given:
        dataResult.put(attributeName, value);

        // when:
        assertThrows(IllegalStateException.class, () -> dataResolver.resolve(testUuid));
    }
}