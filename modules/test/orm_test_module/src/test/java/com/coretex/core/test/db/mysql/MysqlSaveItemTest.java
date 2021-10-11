package com.coretex.core.test.db.mysql;

import com.coretex.core.test.db.SaveItemTest;
import com.coretex.core.tests.tags.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("testmysql")
@DisplayName("Save item test (Mysql)")
@IntegrationTest
public class MysqlSaveItemTest extends SaveItemTest {

}
