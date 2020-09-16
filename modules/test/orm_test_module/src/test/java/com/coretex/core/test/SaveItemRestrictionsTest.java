package com.coretex.core.test;

import com.coretex.core.activeorm.constraints.exceptions.ItemUniqueFieldConstraintViolationException;
import com.coretex.core.activeorm.exceptions.MandatoryAttributeException;
import com.coretex.core.activeorm.services.ItemService;
import com.coretex.core.tests.extentions.MockitoExtension;
import com.coretex.core.tests.tags.IntegrationTest;
import com.coretex.items.test_orm.RequiredFieldContainerTestItem;
import com.coretex.items.test_orm.UniqueFieldContainerTestItem;
import com.coretex.server.spring.CortexEnvironmentInitializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@ContextConfiguration(
		value = {"classpath:/config/spring/core-module-spring.xml", "classpath:/config/spring/test_orm-module-spring.xml"},
		initializers = CortexEnvironmentInitializer.class)
@DisplayName("Save item restrictions test")
@ExtendWith({MockitoExtension.class, SpringExtension.class})
@IntegrationTest
public class SaveItemRestrictionsTest {

	@Resource
	private ItemService itemService;

	@Test
	@DisplayName("should throw exception if mandatory field is not defined")
	void shouldThrowExceptionIfMandatoryFieldNotDefined() {

		// given:
		RequiredFieldContainerTestItem testItem = itemService.create(RequiredFieldContainerTestItem.class);

		//then:
		assertThrows(MandatoryAttributeException.class, () -> {
			// when:
			itemService.save(testItem);
		});
		assertNull(testItem.getUuid());
	}

	@Test
	@DisplayName("should throw exception if mandatory field set null")
	void shouldThrowExceptionIfMandatoryFieldSetNull() {

		// given:
		RequiredFieldContainerTestItem testItem = itemService.create(RequiredFieldContainerTestItem.class);
		testItem.setMandatoryTestField("MANDATORY");
		itemService.save(testItem);

		testItem.setMandatoryTestField(null);
		//then:
		assertThrows(MandatoryAttributeException.class, () -> {
			// when:
			itemService.save(testItem);
		});
		assertNotNull(testItem.getUuid());
	}

	@Test
	@DisplayName("should throw exception if unique field already exist")
	void shouldThrowExceptionIfItemWithUniqueFieldAlreadyExist() {

		// given:
		final String UNIQUE_TEST_FIELD = "Unique::"+ UUID.randomUUID().toString();
		UniqueFieldContainerTestItem testItem = itemService.create(UniqueFieldContainerTestItem.class);
		testItem.setUniqueTestField(UNIQUE_TEST_FIELD);
		itemService.save(testItem);

		UniqueFieldContainerTestItem testItem2 = itemService.create(UniqueFieldContainerTestItem.class);


		//then:
		assertThrows(ItemUniqueFieldConstraintViolationException.class, () -> {
			// when:
			testItem2.setUniqueTestField(UNIQUE_TEST_FIELD);
			itemService.save(testItem2);
		});
	}

	@Test
	@DisplayName("should throw exception if update unique field already exist")
	void shouldThrowExceptionIfIUpdateItemWithUniqueFieldAlreadyExist() {

		// given:
		final String UNIQUE_TEST_FIELD = "Unique::"+ UUID.randomUUID().toString();
		UniqueFieldContainerTestItem testItem = itemService.create(UniqueFieldContainerTestItem.class);
		testItem.setUniqueTestField(UNIQUE_TEST_FIELD);
		itemService.save(testItem);

		UniqueFieldContainerTestItem testItem2 = itemService.create(UniqueFieldContainerTestItem.class);
		testItem2.setUniqueTestField(UUID.randomUUID().toString());
		itemService.save(testItem2);

		//then:
		assertThrows(ItemUniqueFieldConstraintViolationException.class, () -> {
			// when:
			testItem2.setUniqueTestField(UNIQUE_TEST_FIELD);
			itemService.save(testItem2);
		});
	}

}
