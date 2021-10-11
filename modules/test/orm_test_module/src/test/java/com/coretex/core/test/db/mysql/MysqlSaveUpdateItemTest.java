package com.coretex.core.test.db.mysql;

import com.coretex.core.test.db.SaveUpdateItemTest;
import com.coretex.core.tests.tags.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("testmysql")
@DisplayName("Save update item test (Mysql)")
@IntegrationTest
public class MysqlSaveUpdateItemTest extends SaveUpdateItemTest {

}
