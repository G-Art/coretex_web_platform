package com.coretex.core.test.db.postgres;

import com.coretex.core.test.db.SaveItemRestrictionsTest;
import com.coretex.core.tests.tags.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("testpg")
@DisplayName("Save item restrictions test (postgres)")
@IntegrationTest
public class PostgresSaveItemRestrictionsTest extends SaveItemRestrictionsTest {

}
