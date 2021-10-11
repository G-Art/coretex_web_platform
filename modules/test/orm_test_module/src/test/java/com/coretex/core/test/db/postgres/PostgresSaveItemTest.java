package com.coretex.core.test.db.postgres;

import com.coretex.core.test.db.SaveItemTest;
import com.coretex.core.tests.tags.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("testpg")
@DisplayName("Save item test (postgres)")
@IntegrationTest
public class PostgresSaveItemTest extends SaveItemTest {

}
