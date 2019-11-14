package com.coretex.core.tests.tags;

import org.junit.jupiter.api.Tag;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.TestPropertySource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Tag("CortexIntegrationTest")
@Profile("test")
public @interface IntegrationTest {
}
