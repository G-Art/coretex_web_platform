package com.coretex.core.test.db.postgres;

import com.coretex.core.test.db.SearchItemTest;
import com.coretex.core.tests.tags.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("testpg")
@DisplayName("Search item test (postgres)")
@IntegrationTest
public class PostgresSearchItemTest extends SearchItemTest {

}
