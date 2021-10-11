package com.coretex.core.test.db.postgres;

import com.coretex.core.test.db.SaveItemRelationsTest;
import com.coretex.core.tests.tags.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("testpg")
@DisplayName("Save relation item test (postgres)")
@IntegrationTest
public class PostgresSaveItemRelationsTest extends SaveItemRelationsTest {

}
