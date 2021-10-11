package com.coretex.core.test.db.mysql;

import com.coretex.core.test.db.SearchItemTest;
import com.coretex.core.tests.tags.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("testmysql")
@DisplayName("Search item test (Mysql)")
@IntegrationTest
public class MysqlSearchItemTest extends SearchItemTest {

}
