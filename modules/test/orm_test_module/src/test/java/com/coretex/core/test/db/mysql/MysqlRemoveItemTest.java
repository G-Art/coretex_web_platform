package com.coretex.core.test.db.mysql;

import com.coretex.core.test.db.RemoveItemTest;
import com.coretex.core.tests.tags.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("testmysql")
@DisplayName("Remove item test (Mysql)")
@IntegrationTest
public class MysqlRemoveItemTest extends RemoveItemTest {

}
