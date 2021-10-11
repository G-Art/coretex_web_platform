package com.coretex.core.test.db.postgres;

import com.coretex.core.test.db.SerializableItemTest;
import com.coretex.core.tests.tags.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("testpg")
@DisplayName("Serializable item test (postgres)")
@IntegrationTest
public class PostgresSerializableItemTest extends SerializableItemTest {

}
