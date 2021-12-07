package com.coretex.core.test.transformation.query;

import com.coretex.core.activeorm.query.select.SelectQueryOptimizer;
import com.coretex.core.tests.extentions.MockitoExtension;
import com.coretex.core.tests.tags.IntegrationTest;
import com.coretex.server.spring.CortexEnvironmentInitializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;

@ActiveProfiles("testmysql")
@ContextConfiguration(
		value = {"classpath:/config/spring/core-module-spring.xml",
				"classpath:/config/spring/test_orm-module-spring.xml"},
		initializers = CortexEnvironmentInitializer.class)
@DisplayName("Query Transformation Test (PostgreSQL)")
@ExtendWith({MockitoExtension.class, SpringExtension.class})
@IntegrationTest
//@Disabled
public class QueryTransformationPGTest {

	@Resource
	private SelectQueryOptimizer selectQueryOptimizer;

	@Test
	void name() {

		String query = "SELECT c.*, c.metaType as mt FROM SearchTest AS c " +
				"ORDER BY c.code asc " +
				"LIMIT 3  " +
				"OFFSET 0";

		System.out.println("ORIGIN QUERY :: "+ query);
		final String sql = selectQueryOptimizer.process(query);
		System.out.println("AFTER OPTIMIZATION :: "+ sql);
	}

}
