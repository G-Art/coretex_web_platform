package com.coretex.core.test;

import com.coretex.core.activeorm.services.ItemService;
import com.coretex.core.activeorm.services.SearchService;
import com.coretex.core.tests.extentions.MockitoExtension;
import com.coretex.core.tests.tags.IntegrationTest;
import com.coretex.items.test_orm.RegularTypeContainerTestItem;
import com.coretex.server.spring.CortexEnvironmentInitializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@ContextConfiguration(
		value = {"classpath:/config/spring/core-module-spring.xml", "classpath:/config/spring/test_orm-module-spring.xml"},
		initializers = CortexEnvironmentInitializer.class)
@DisplayName("Serializable item test")
@ExtendWith({MockitoExtension.class, SpringExtension.class})
@IntegrationTest
public class SerializableItemTest {


	@Resource
	private ItemService itemService;

	@Resource
	private SearchService searchService;

	@Test
	@DisplayName("should serialize generic item")
	void shouldSelectAllRequestedTypeItem() {

		// given:
		RegularTypeContainerTestItem testItem = itemService.create(RegularTypeContainerTestItem.class);
		testItem.setStringType("TEST_STRING");
		itemService.save(testItem);

		RegularTypeContainerTestItem searchedTestItem = searchService.<RegularTypeContainerTestItem>search(format("select * from %s where uuid = :uuid", RegularTypeContainerTestItem.ITEM_TYPE), Map.of(RegularTypeContainerTestItem.UUID, testItem.getUuid())).getResult().get(0);

		ObjectOutputStream oos;
		ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
		try {
			oos = new ObjectOutputStream(arrayOutputStream);
			oos.writeObject(searchedTestItem);

			ObjectInputStream oin;
			oin = new ObjectInputStream(new ByteArrayInputStream(arrayOutputStream.toByteArray()));
			testItem = (RegularTypeContainerTestItem) oin.readObject();

		} catch (Exception  e) {
			e.printStackTrace();
		}

		assertNotNull(testItem.getUuid());
		assertEquals(testItem, searchedTestItem);
		assertEquals(testItem.getStringType(), searchedTestItem.getStringType());
	}
}
