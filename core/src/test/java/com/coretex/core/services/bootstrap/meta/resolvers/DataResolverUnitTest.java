package com.coretex.core.services.bootstrap.meta.resolvers;

import com.coretex.core.services.bootstrap.meta.MetaDataContext;
import com.coretex.core.tests.extentions.MockitoExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public abstract class DataResolverUnitTest {

    @Mock
    protected MetaDataContext metaDataContext;

    protected Map<String, Object> dataResult;

    protected DataResolver dataResolver;

    protected UUID testUuid;

    @BeforeEach
    void initData() {
        testUuid = UUID.randomUUID();
        dataResult = new HashMap<>();

        // when:
        when(metaDataContext.getItemData(testUuid)).thenAnswer(ctx -> dataResult);
    }

    @Test
    @DisplayName("throws exception when data result is null by given uuid")
    void shouldThrowExceptionWhenDataResultIsNull() {
        // given:
        dataResult = null;

        // when:
        assertThrows(IllegalArgumentException.class, () -> dataResolver.resolve(testUuid));
    }


    @Test
    @DisplayName("throws exception when data result is empty by given uuid")
    void shouldThrowExceptionWhenDataResultIsEmpty() {
        // given:
        dataResult = Collections.emptyMap();

        // when:
        assertThrows(IllegalArgumentException.class, () -> dataResolver.resolve(testUuid));
    }
}
