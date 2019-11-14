package com.coretex.core.test;

import com.coretex.core.activeorm.services.ItemService;
import com.coretex.core.activeorm.services.SearchService;
import com.coretex.core.tests.extentions.MockitoExtension;
import com.coretex.core.tests.tags.IntegrationTest;
import com.coretex.enums.test_orm.TestRelationEnum;
import com.coretex.items.test_orm.*;
import com.coretex.server.spring.CortexEnvironmentInitializer;
import org.apache.commons.collections4.CollectionUtils;
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
@DisplayName("Save update relation item test")
@ExtendWith({MockitoExtension.class, SpringExtension.class})
@IntegrationTest
public class SaveUpdateItemRelationsTest {


	@Resource
	private ItemService itemService;

	@Resource
	private SearchService searchService;

	@Test
	@DisplayName("should correctly update items with many to many item relation attribute")
	void shouldSaveManyToManyRelation() {
		// given:
		final String MANY_TO_MANY_ID = "manyToMany_update_" + UUID.randomUUID().toString();

		BiDirectionalRelationTestItem relationTestItem1_1 = itemService.create(BiDirectionalRelationTestItem.class);

		BiDirectionalRelatedItem relatedItem1_1 = itemService.create(BiDirectionalRelatedItem.class);
		BiDirectionalRelatedItem relatedItem1_2 = itemService.create(BiDirectionalRelatedItem.class);

		relationTestItem1_1.setTestId(MANY_TO_MANY_ID);

		relationTestItem1_1.setManyToManyTargets(Set.of(relatedItem1_1, relatedItem1_2));

		itemService.save(relationTestItem1_1);

		BiDirectionalRelatedItem relatedItemNew1_1 = itemService.create(BiDirectionalRelatedItem.class);
		BiDirectionalRelatedItem relatedItemNew1_2 = itemService.create(BiDirectionalRelatedItem.class);

		relationTestItem1_1.setManyToManyTargets(Set.of(relatedItemNew1_1, relatedItemNew1_2));
		itemService.save(relationTestItem1_1);

		// when:

		List<BiDirectionalRelationTestItem> searchedTestItem = searchService.<BiDirectionalRelationTestItem>search(format("select * from %s where testId = :testId", BiDirectionalRelationTestItem.ITEM_TYPE), Map.of(BiDirectionalRelationTestItem.TEST_ID, MANY_TO_MANY_ID)).getResult();

		//then:
		assertNotNull(searchedTestItem);
		assertFalse(searchedTestItem.isEmpty());
		assertEquals(1, searchedTestItem.size());

		assertTrue(searchedTestItem.get(0).getManyToManyTargets().containsAll(Set.of(relatedItemNew1_1, relatedItemNew1_2)));

		assertTrue(CollectionUtils.isEmpty(relatedItem1_1.getManyToManySources()));
		assertTrue(CollectionUtils.isEmpty(relatedItem1_2.getManyToManySources()));

		assertEquals(1, relatedItemNew1_1.getManyToManySources().size());
		assertEquals(1, relatedItemNew1_2.getManyToManySources().size());

		assertEquals(relationTestItem1_1, relatedItemNew1_2.getManyToManySources().iterator().next());

	}

	@Test
	@DisplayName("should correctly update items with many to one item relation attribute")
	void shouldSaveManyToOneRelation() {
		// given:
		BiDirectionalRelationTestItem relationTestItem1 = itemService.create(BiDirectionalRelationTestItem.class);
		BiDirectionalRelationTestItem relationTestItem2 = itemService.create(BiDirectionalRelationTestItem.class);
		BiDirectionalRelatedItem relatedItem = itemService.create(BiDirectionalRelatedItem.class);

		relatedItem.setManyToOneSources(Set.of(relationTestItem1, relationTestItem2));
		itemService.save(relatedItem);

		BiDirectionalRelationTestItem relationTestItem1New = itemService.create(BiDirectionalRelationTestItem.class);
		relatedItem.setManyToOneSources(Set.of(relationTestItem1New));
		itemService.save(relatedItem);

		// when:
		BiDirectionalRelatedItem searchedTestItem = searchService.<BiDirectionalRelatedItem>search(format("select * from %s where uuid = :uuid", BiDirectionalRelatedItem.ITEM_TYPE), Map.of(BiDirectionalRelatedItem.UUID, relatedItem.getUuid())).getResult().get(0);

		//then:
		assertNotNull(searchedTestItem.getUuid());
		assertNotNull(relationTestItem1.getUuid());
		assertNotNull(relationTestItem2.getUuid());
		assertNotNull(relationTestItem1New.getUuid());

		assertNotNull(searchedTestItem.getManyToOneSources());
		assertNull(relationTestItem1.getManyToOneTarget());
		assertNull(relationTestItem2.getManyToOneTarget());
		assertNotNull(relationTestItem1New.getManyToOneTarget());

		assertEquals(relationTestItem1New.getManyToOneTarget().getUuid(), relatedItem.getUuid());

		assertEquals(searchedTestItem.getManyToOneSources().size(), 1);
	}

	@Test
	@DisplayName("should correctly update items with one to many item relation attribute")
	void shouldSaveOneToManyRelation() {
		// given:
		BiDirectionalRelationTestItem relationTestItem = itemService.create(BiDirectionalRelationTestItem.class);
		BiDirectionalRelatedItem relatedItem1 = itemService.create(BiDirectionalRelatedItem.class);
		BiDirectionalRelatedItem relatedItem2 = itemService.create(BiDirectionalRelatedItem.class);

		relationTestItem.setOneToManyTargets(Set.of(relatedItem1, relatedItem2));
		itemService.save(relationTestItem);

		BiDirectionalRelatedItem relatedItem1New = itemService.create(BiDirectionalRelatedItem.class);

		relationTestItem.setOneToManyTargets(Set.of(relatedItem1New));
		itemService.save(relationTestItem);

		// when:
		BiDirectionalRelationTestItem searchedTestItem = searchService.<BiDirectionalRelationTestItem>search(format("select * from %s where uuid = :uuid", BiDirectionalRelationTestItem.ITEM_TYPE), Map.of(BiDirectionalRelationTestItem.UUID, relationTestItem.getUuid())).getResult().get(0);

		//then:
		assertNotNull(searchedTestItem.getUuid());
		assertNotNull(relatedItem1.getUuid());
		assertNotNull(relatedItem2.getUuid());
		assertNotNull(relatedItem1New.getUuid());

		assertNotNull(searchedTestItem.getOneToManyTargets());
		assertNotNull(relatedItem1New.getOneToManySource());
		assertEquals(searchedTestItem.getOneToManyTargets().size(), 1);

		assertEquals(relatedItem1New.getOneToManySource(), searchedTestItem);
	}

	@Test
	@DisplayName("should correctly update items with one to one item relation attribute")
	void shouldSaveOneToOneRelation() {
		// given:
		BiDirectionalRelationTestItem relationTestItem = itemService.create(BiDirectionalRelationTestItem.class);
		BiDirectionalRelatedItem relatedItem = itemService.create(BiDirectionalRelatedItem.class);

		relationTestItem.setOneToOneTarget(relatedItem);
		itemService.save(relationTestItem);

		BiDirectionalRelatedItem relatedItemNew = itemService.create(BiDirectionalRelatedItem.class);

		relationTestItem.setOneToOneTarget(relatedItemNew);
		itemService.save(relationTestItem);

		// when:
		BiDirectionalRelationTestItem searchedTestItem = searchService.<BiDirectionalRelationTestItem>search(format("select * from %s where uuid = :uuid", BiDirectionalRelationTestItem.ITEM_TYPE), Map.of(BiDirectionalRelationTestItem.UUID, relationTestItem.getUuid())).getResult().get(0);

		//then:
		assertNotNull(searchedTestItem.getUuid());
		assertNotNull(relatedItem.getUuid());

		assertNotNull(searchedTestItem.getOneToOneTarget());
		assertEquals(searchedTestItem.getOneToOneTarget().getUuid(), relatedItemNew.getUuid());
	}

	@Test
	@DisplayName("should correctly update items with item relation attribute")
	void shouldSaveOneDirectionalRelationType() {
		// given:
		OneDirectionalRelationTestItem testItem = itemService.create(OneDirectionalRelationTestItem.class);
		OneDirectionalRelatedItem relatedTestItem = itemService.create(OneDirectionalRelatedItem.class);

		testItem.setRelatedType(relatedTestItem);
		itemService.save(testItem);

		OneDirectionalRelatedItem relatedTestItemNew = itemService.create(OneDirectionalRelatedItem.class);

		testItem.setRelatedType(relatedTestItemNew);
		itemService.save(testItem);

		// when:
		OneDirectionalRelationTestItem searchedTestItem = searchService.<OneDirectionalRelationTestItem>search(format("select * from %s where uuid = :uuid", OneDirectionalRelationTestItem.ITEM_TYPE), Map.of(OneDirectionalRelationTestItem.UUID, testItem.getUuid())).getResult().get(0);

		//then:
		assertNotNull(searchedTestItem.getUuid());
		assertNotNull(relatedTestItem.getUuid());

		assertNotNull(searchedTestItem.getRelatedType());
		assertEquals(searchedTestItem.getRelatedType().getUuid(), relatedTestItemNew.getUuid());
	}

	@Test
	@DisplayName("should correctly update items with enum type of attribute")
	void shouldSaveEnumRelationType() {
		// given:
		EnumRelationTestItem testItem = itemService.create(EnumRelationTestItem.class);

		testItem.setEnumFirstType(TestRelationEnum.FIRST);
		testItem.setEnumSecondType(TestRelationEnum.SECOND);
		testItem.setEnumThirdType(TestRelationEnum.THIRD);
		itemService.save(testItem);

		testItem.setEnumFirstType(TestRelationEnum.THIRD);
		testItem.setEnumSecondType(TestRelationEnum.SECOND);
		testItem.setEnumThirdType(TestRelationEnum.FIRST);
		itemService.save(testItem);

		// when:

		EnumRelationTestItem searchedTestItem = searchService.<EnumRelationTestItem>search(format("select * from %s where uuid = :uuid", EnumRelationTestItem.ITEM_TYPE), Map.of(EnumRelationTestItem.UUID, testItem.getUuid())).getResult().get(0);

		//then:
		assertNotNull(searchedTestItem.getEnumFirstType());
		assertEquals(searchedTestItem.getEnumFirstType(), TestRelationEnum.THIRD);

		assertNotNull(searchedTestItem.getEnumSecondType());
		assertEquals(searchedTestItem.getEnumSecondType(), TestRelationEnum.SECOND);

		assertNotNull(searchedTestItem.getEnumThirdType());
		assertEquals(searchedTestItem.getEnumThirdType(), TestRelationEnum.FIRST);
	}

}
