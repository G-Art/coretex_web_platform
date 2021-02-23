package com.coretex.core.tests.extentions;

import io.r2dbc.spi.ConnectionFactories;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

public class JDBCTemplateEmulator implements TestInstancePostProcessor {

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        Field[] fields = testInstance.getClass().getDeclaredFields();
        ExtensionContext.Store mockStore = context.getStore(ExtensionContext.Namespace.create(MockitoExtension.class, DatabaseClient.class));
        for (Field field : fields) {
            if (field.getType().isAssignableFrom(DatabaseClient.class)) {
                field.setAccessible(true);
                Object template = mockStore.getOrComputeIfAbsent(field.getName(), key -> createDatabaseClient());
                ReflectionUtils.setField(field, testInstance, template);
            }
        }

    }

    private DatabaseClient createDatabaseClient() {
        return DatabaseClient.create( ConnectionFactories.get("r2dbc:pool:postgresql://postgres:postgres@localhost:5432/cortexdb_test"));
    }
}
