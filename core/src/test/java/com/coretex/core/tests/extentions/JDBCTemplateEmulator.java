package com.coretex.core.tests.extentions;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

public class JDBCTemplateEmulator implements TestInstancePostProcessor {

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        Field[] fields = testInstance.getClass().getDeclaredFields();
        ExtensionContext.Store mockStore = context.getStore(ExtensionContext.Namespace.create(MockitoExtension.class, NamedParameterJdbcTemplate.class));
        for (Field field : fields) {
            if (field.getType().isAssignableFrom(NamedParameterJdbcTemplate.class)) {
                field.setAccessible(true);
                Object template = mockStore.getOrComputeIfAbsent(field.getName(), key -> createJDBCTemplate());
                ReflectionUtils.setField(field, testInstance, template);
            }
        }

    }

    private NamedParameterJdbcTemplate createJDBCTemplate() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/cortexdb_test");//todo: do this configurable from properties
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres");
        return new NamedParameterJdbcTemplate(dataSource);
    }
}
