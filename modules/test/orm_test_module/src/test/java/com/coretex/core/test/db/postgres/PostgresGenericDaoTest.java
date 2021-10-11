package com.coretex.core.test.db.postgres;

import com.coretex.core.test.db.GenericDaoTest;
import com.coretex.core.tests.tags.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("testpg")
@DisplayName("Default generic dao test (postgres)")
@IntegrationTest
public class PostgresGenericDaoTest extends GenericDaoTest {
}
