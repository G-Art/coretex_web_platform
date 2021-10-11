package com.coretex.core.test.db.mysql;

import com.coretex.core.test.db.SaveItemRestrictionsTest;
import com.coretex.core.tests.tags.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("testmysql")
@DisplayName("Save item restrictions test (Mysql)")
@IntegrationTest
public class MysqlSaveItemRestrictionsTest extends SaveItemRestrictionsTest {

}
