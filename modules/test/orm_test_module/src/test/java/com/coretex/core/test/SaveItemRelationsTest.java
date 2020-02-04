package com.coretex.core.test;

import com.coretex.core.activeorm.services.ItemService;
import com.coretex.core.activeorm.services.SearchService;
import com.coretex.core.tests.extentions.MockitoExtension;
import com.coretex.core.tests.tags.IntegrationTest;
import com.coretex.enums.test_orm.TestRelationEnum;
import com.coretex.items.test_orm.BiDirectionalRelatedItem;
import com.coretex.items.test_orm.BiDirectionalRelationTestItem;
import com.coretex.items.test_orm.EnumRelationTestItem;
import com.coretex.items.test_orm.OneDirectionalRelatedItem;
import com.coretex.items.test_orm.OneDirectionalRelationTestItem;
import com.coretex.server.spring.CortexEnvironmentInitializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ContextConfiguration(
		value = {"classpath:/config/spring/core-module-spring.xml", "classpath:/config/spring/test_orm-module-spring.xml"},
		initializers = CortexEnvironmentInitializer.class)
@DisplayName("Save relation item test")
@ExtendWith({MockitoExtension.class, SpringExtension.class})
@IntegrationTest
public class SaveItemRelationsTest {


	@Resource
	private ItemService itemService;

	@Resource
	private SearchService searchService;

	@Test
	@DisplayName("should correctly save items with many to many item relation attribute")
	void shouldSaveManyToManyRelation() {
		// given:
		final String MANY_TO_MANY_ID = "manyToMany_" + UUID.randomUUID().toString();

		BiDirectionalRelationTestItem relationTestItem1_1 = itemService.create(BiDirectionalRelationTestItem.class);
		BiDirectionalRelationTestItem relationTestItem1_2 = itemService.create(BiDirectionalRelationTestItem.class);

		BiDirectionalRelationTestItem relationTestItem2_1 = itemService.create(BiDirectionalRelationTestItem.class);
		BiDirectionalRelationTestItem relationTestItem2_2 = itemService.create(BiDirectionalRelationTestItem.class);

		BiDirectionalRelatedItem relatedItem1_1 = itemService.create(BiDirectionalRelatedItem.class);
		BiDirectionalRelatedItem relatedItem1_2 = itemService.create(BiDirectionalRelatedItem.class);

		BiDirectionalRelatedItem relatedItem2_1 = itemService.create(BiDirectionalRelatedItem.class);
		BiDirectionalRelatedItem relatedItem2_2 = itemService.create(BiDirectionalRelatedItem.class);

		relationTestItem1_1.setTestId(MANY_TO_MANY_ID);
		relationTestItem1_2.setTestId(MANY_TO_MANY_ID);

		relationTestItem2_1.setTestId(MANY_TO_MANY_ID);
		relationTestItem2_2.setTestId(MANY_TO_MANY_ID);

		relationTestItem1_1.setManyToManyTargets(Set.of(relatedItem1_1, relatedItem1_2));
		relationTestItem1_2.setManyToManyTargets(Set.of(relatedItem1_1, relatedItem1_2));

		relatedItem2_1.setManyToManySources(Set.of(relationTestItem2_1, relationTestItem2_2));
		relatedItem2_2.setManyToManySources(Set.of(relationTestItem2_1, relationTestItem2_2));

		// when:
		itemService.saveAll(Set.of(relationTestItem1_1, relationTestItem1_2, relatedItem2_1, relatedItem2_2));

		List<BiDirectionalRelationTestItem> searchedTestItem = searchService.<BiDirectionalRelationTestItem>search(format("select * from %s where testId = :testId", BiDirectionalRelationTestItem.ITEM_TYPE), Map.of(BiDirectionalRelationTestItem.TEST_ID, MANY_TO_MANY_ID)).getResult();

		//then:
		assertNotNull(searchedTestItem);
		assertFalse(searchedTestItem.isEmpty());
		assertEquals(4, searchedTestItem.size());

		searchedTestItem.forEach(biDirectionalRelationTestItem -> {
			if (biDirectionalRelationTestItem.equals(relationTestItem1_1) || biDirectionalRelationTestItem.equals(relationTestItem1_2)) {
				assertTrue(biDirectionalRelationTestItem.getManyToManyTargets().containsAll(Set.of(relatedItem1_1, relatedItem1_2)));
			} else {
				assertTrue(biDirectionalRelationTestItem.getManyToManyTargets().containsAll(Set.of(relatedItem2_1, relatedItem2_2)));
			}
		});
	}

	@Test
	@DisplayName("should correctly save items with many to one item relation attribute")
	void shouldSaveManyToOneRelation() {
		// given:
		BiDirectionalRelationTestItem relationTestItem1 = itemService.create(BiDirectionalRelationTestItem.class);
		BiDirectionalRelationTestItem relationTestItem2 = itemService.create(BiDirectionalRelationTestItem.class);
		BiDirectionalRelatedItem relatedItem = itemService.create(BiDirectionalRelatedItem.class);

		relatedItem.setManyToOneSources(Set.of(relationTestItem1, relationTestItem2));

		// when:
		itemService.save(relatedItem);

		BiDirectionalRelatedItem searchedTestItem = searchService.<BiDirectionalRelatedItem>search(format("select * from %s where uuid = :uuid", BiDirectionalRelatedItem.ITEM_TYPE), Map.of(BiDirectionalRelatedItem.UUID, relatedItem.getUuid())).getResult().get(0);

		//then:
		assertNotNull(searchedTestItem.getUuid());
		assertNotNull(relationTestItem1.getUuid());
		assertNotNull(relationTestItem2.getUuid());

		assertNotNull(searchedTestItem.getManyToOneSources());
		assertNotNull(relationTestItem1.getManyToOneTarget());
		assertNotNull(relationTestItem2.getManyToOneTarget());

		assertEquals(relationTestItem1.getManyToOneTarget().getUuid(), relatedItem.getUuid());
		assertEquals(relationTestItem2.getManyToOneTarget().getUuid(), relatedItem.getUuid());

		assertEquals(searchedTestItem.getManyToOneSources().size(), 2);
	}

	@Test
	@DisplayName("should correctly save items with one to many item relation attribute")
	void shouldSaveOneToManyRelation() {
		// given:
		BiDirectionalRelationTestItem relationTestItem = itemService.create(BiDirectionalRelationTestItem.class);
		BiDirectionalRelatedItem relatedItem1 = itemService.create(BiDirectionalRelatedItem.class);
		BiDirectionalRelatedItem relatedItem2 = itemService.create(BiDirectionalRelatedItem.class);

		relationTestItem.setOneToManyTargets(Set.of(relatedItem1, relatedItem2));

		// when:
		itemService.save(relationTestItem);

		BiDirectionalRelationTestItem searchedTestItem = searchService.<BiDirectionalRelationTestItem>search(format("select * from %s where uuid = :uuid", BiDirectionalRelationTestItem.ITEM_TYPE), Map.of(BiDirectionalRelationTestItem.UUID, relationTestItem.getUuid())).getResult().get(0);

		//then:
		assertNotNull(searchedTestItem.getUuid());
		assertNotNull(relatedItem1.getUuid());
		assertNotNull(relatedItem2.getUuid());

		assertNotNull(searchedTestItem.getOneToManyTargets());
		assertEquals(searchedTestItem.getOneToManyTargets().size(), 2);
	}

	@Test
	@DisplayName("should correctly save items with one to one item relation attribute")
	void shouldSaveOneToOneRelation() {
		// given:
		BiDirectionalRelationTestItem relationTestItem = itemService.create(BiDirectionalRelationTestItem.class);
		BiDirectionalRelatedItem relatedItem = itemService.create(BiDirectionalRelatedItem.class);

		relationTestItem.setOneToOneTarget(relatedItem);

		// when:
		itemService.save(relationTestItem);

		BiDirectionalRelationTestItem searchedTestItem = searchService.<BiDirectionalRelationTestItem>search(format("select * from %s where uuid = :uuid", BiDirectionalRelationTestItem.ITEM_TYPE), Map.of(BiDirectionalRelationTestItem.UUID, relationTestItem.getUuid())).getResult().get(0);

		//then:
		assertNotNull(searchedTestItem.getUuid());
		assertNotNull(relatedItem.getUuid());

		assertNotNull(searchedTestItem.getOneToOneTarget());
		assertEquals(searchedTestItem.getOneToOneTarget().getUuid(), relatedItem.getUuid());
	}

	@Test
	@DisplayName("should correctly save items with item relation attribute")
	void shouldSaveOneDirectionalRelationType() {
		// given:
		OneDirectionalRelationTestItem testItem = itemService.create(OneDirectionalRelationTestItem.class);
		OneDirectionalRelatedItem relatedTestItem = itemService.create(OneDirectionalRelatedItem.class);

		testItem.setRelatedType(relatedTestItem);

		// when:
		itemService.save(testItem);

		OneDirectionalRelationTestItem searchedTestItem = searchService.<OneDirectionalRelationTestItem>search(format("select * from %s where uuid = :uuid", OneDirectionalRelationTestItem.ITEM_TYPE), Map.of(OneDirectionalRelationTestItem.UUID, testItem.getUuid())).getResult().get(0);

		//then:
		assertNotNull(searchedTestItem.getUuid());
		assertNotNull(relatedTestItem.getUuid());

		assertNotNull(searchedTestItem.getRelatedType());
		assertEquals(searchedTestItem.getRelatedType().getUuid(), relatedTestItem.getUuid());
	}

	@Test
	@DisplayName("should correctly save items with enum type of attribute")
	void shouldSaveEnumRelationType() {
		// given:
		EnumRelationTestItem testItem = itemService.create(EnumRelationTestItem.class);

		testItem.setEnumFirstType(TestRelationEnum.FIRST);
		testItem.setEnumSecondType(TestRelationEnum.SECOND);
		testItem.setEnumThirdType(TestRelationEnum.THIRD);

		// when:
		itemService.save(testItem);

		EnumRelationTestItem searchedTestItem = searchService.<EnumRelationTestItem>search(format("select * from %s where uuid = :uuid", EnumRelationTestItem.ITEM_TYPE), Map.of(EnumRelationTestItem.UUID, testItem.getUuid())).getResult().get(0);

		//then:
		assertNotNull(searchedTestItem.getEnumFirstType());
		assertEquals(searchedTestItem.getEnumFirstType(), TestRelationEnum.FIRST);

		assertNotNull(searchedTestItem.getEnumSecondType());
		assertEquals(searchedTestItem.getEnumSecondType(), TestRelationEnum.SECOND);

		assertNotNull(searchedTestItem.getEnumThirdType());
		assertEquals(searchedTestItem.getEnumThirdType(), TestRelationEnum.THIRD);
	}

}
