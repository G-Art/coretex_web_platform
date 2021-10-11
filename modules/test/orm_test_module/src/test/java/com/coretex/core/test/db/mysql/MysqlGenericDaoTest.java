package com.coretex.core.test.db.mysql;

import com.coretex.core.test.db.GenericDaoTest;
import com.coretex.core.tests.tags.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("testmysql")
@DisplayName("Default generic dao test (Mysql)")
@IntegrationTest
public class MysqlGenericDaoTest extends GenericDaoTest {
}
