package com.coretex.core.test.transformation.query;

import com.coretex.core.tests.extentions.MockitoExtension;
import com.coretex.core.tests.tags.IntegrationTest;
import com.coretex.server.spring.CortexEnvironmentInitializer;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles("testmysql")
@ContextConfiguration(
		value = {"classpath:/config/spring/core-module-spring.xml",
				"classpath:/config/spring/test_orm-module-spring.xml"},
		initializers = CortexEnvironmentInitializer.class)
@DisplayName("Query Transformation Test (MySql)")
@ExtendWith({MockitoExtension.class, SpringExtension.class})
@IntegrationTest
@Disabled
public class QueryTransformationMysqlTest {

//	@Resource
//	SelectQueryTransformationProcessor queryTransformationProcessor;

	@Test
	void name() {
//		queryTransformationProcessor.transform(null);
	}
}
