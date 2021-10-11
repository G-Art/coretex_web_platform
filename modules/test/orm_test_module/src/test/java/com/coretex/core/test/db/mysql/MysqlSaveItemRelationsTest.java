package com.coretex.core.test.db.mysql;

import com.coretex.core.test.db.SaveItemRelationsTest;
import com.coretex.core.tests.tags.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("testmysql")
@DisplayName("Save relation item test (Mysql)")
@IntegrationTest
public class MysqlSaveItemRelationsTest extends SaveItemRelationsTest {

}
