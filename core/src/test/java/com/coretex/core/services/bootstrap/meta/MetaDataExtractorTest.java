package com.coretex.core.services.bootstrap.meta;

import com.coretex.core.services.bootstrap.dialect.postgres.PostgresMetaDataExtractor;
import com.coretex.core.tests.asserts.CoreAsserts;
import com.coretex.core.tests.extentions.JDBCTemplateEmulator;
import com.coretex.core.tests.tags.UnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.Map;

@UnitTest
@DisplayName("A Meta Extractor")
@ExtendWith(JDBCTemplateEmulator.class)
public class MetaDataExtractorTest {

    private MetaDataExtractor metaDataExtractor;

    private NamedParameterJdbcTemplate mockJdbcTemplate;

    @BeforeEach
    void setUp() {
        metaDataExtractor = new PostgresMetaDataExtractor(mockJdbcTemplate);
    }

    @Test
    @DisplayName("is returning all meta types on request")
    void shouldReturnMetaTypesDataOnRequest() {
        // when:
        List<Map<String, Object>> result = metaDataExtractor.selectTypeItems();

        //then:
        CoreAsserts.assertNotEmpty(result);
    }

    @Test
    @DisplayName("is returning all meta attributes on request ")
    void shouldReturnAttributesTypesDataOnRequest() {
        // when:
        List<Map<String, Object>> result = metaDataExtractor.selectAttributeItems();

        //then:
        CoreAsserts.assertNotEmpty(result);
    }

    @Test
    @DisplayName("is returning all meta regular on request ")
    void shouldReturnRegularTypesDataOnRequest() {
        // when:
        List<Map<String, Object>> result = metaDataExtractor.selectRegularItems();

        //then:
        CoreAsserts.assertNotEmpty(result);
    }


    @Test
    @DisplayName("is returning all meta enum types on request ")
    void shouldReturnEnumTypesDataOnRequest() {
        // when:
        List<Map<String, Object>> result = metaDataExtractor.selectEnumTypes();

        //then:
        CoreAsserts.assertNotEmpty(result);
    }

    @Test
    @DisplayName("is returning all enum values on request ")
    void shouldReturnEnumValuesDataOnRequest() {
        // when:
        List<Map<String, Object>> result = metaDataExtractor.selectEnumValues();

        //then:
        CoreAsserts.assertNotEmpty(result);
    }
}
