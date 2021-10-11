package com.coretex.core.test.db.postgres;

import com.coretex.core.activeorm.services.ItemService;
import com.coretex.core.activeorm.services.SearchResult;
import com.coretex.core.activeorm.services.SearchService;
import com.coretex.core.test.db.RemoveItemTest;
import com.coretex.core.tests.tags.IntegrationTest;
import com.coretex.items.test_orm.RemoveRelatedAssociatedItem;
import com.coretex.items.test_orm.RemoveRelatedItem;
import com.coretex.items.test_orm.RemoveRelationAssociatedTestItem;
import com.coretex.items.test_orm.RemoveRelationTestItem;
import com.coretex.items.test_orm.RemoveTestItem;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("testpg")
@DisplayName("Remove item test (postgres)")
@IntegrationTest
public class PostgresRemoveItemTest extends RemoveItemTest {

	@Resource
	private ItemService itemService;

	@Resource
	private SearchService searchService;

	@Test
	@DisplayName("should remove related and associated items")
	void shouldRemoveAssociatedItems() {
		// given:
		String CODE = "remove_test_" + UUID.randomUUID();
		String RELATED_CODE = "remove_test_related" + UUID.randomUUID();

		RemoveRelationAssociatedTestItem removeTestItem = itemService.create(RemoveRelationAssociatedTestItem.class);

		RemoveRelatedAssociatedItem removeRelatedItem1 = itemService.create(RemoveRelatedAssociatedItem.class);
		RemoveRelatedAssociatedItem removeRelatedItem2 = itemService.create(RemoveRelatedAssociatedItem.class);
		RemoveRelatedAssociatedItem removeRelatedItem3 = itemService.create(RemoveRelatedAssociatedItem.class);
		RemoveRelatedAssociatedItem removeRelatedItem4 = itemService.create(RemoveRelatedAssociatedItem.class);
		RemoveRelatedAssociatedItem removeRelatedItem5 = itemService.create(RemoveRelatedAssociatedItem.class);
		RemoveRelatedAssociatedItem removeRelatedItem6 = itemService.create(RemoveRelatedAssociatedItem.class);
		RemoveRelatedAssociatedItem removeRelatedItem7 = itemService.create(RemoveRelatedAssociatedItem.class);

		removeRelatedItem1.setUniqueCode(RELATED_CODE);
		removeRelatedItem2.setUniqueCode(RELATED_CODE);
		removeRelatedItem3.setUniqueCode(RELATED_CODE);
		removeRelatedItem4.setUniqueCode(RELATED_CODE);
		removeRelatedItem5.setUniqueCode(RELATED_CODE);
		removeRelatedItem6.setUniqueCode(RELATED_CODE);
		removeRelatedItem7.setUniqueCode(RELATED_CODE);

		removeTestItem.setRelatedType(removeRelatedItem1);
		removeTestItem.setOneToOneTarget(removeRelatedItem2);
		removeTestItem.setOneToManyTargets(Set.of(removeRelatedItem3, removeRelatedItem4));
		removeTestItem.setManyToOneTarget(removeRelatedItem5);
		removeTestItem.setManyToManyTargets(Set.of(removeRelatedItem6, removeRelatedItem7));

		removeTestItem.setUniqueCode(CODE);

		itemService.save(removeTestItem);
		// when:

		itemService.delete(removeTestItem);
		//then:

		SearchResult<RemoveRelationAssociatedTestItem> search = searchService.search(format("select * from %s where uniqueCode = :uniqueCode", RemoveRelationAssociatedTestItem.ITEM_TYPE), Map.of(RemoveRelationAssociatedTestItem.UNIQUE_CODE, CODE));

		assertEquals(0, search.getCount());

		SearchResult<RemoveRelatedItem> searchRelated = searchService.search(format("select * from %s where uniqueCode = :uniqueCode", RemoveRelatedItem.ITEM_TYPE), Map.of(RemoveRelatedItem.UNIQUE_CODE, RELATED_CODE));

		assertEquals(0, searchRelated.getCount());

	}

	@Test
	@DisplayName("should not remove related not associated items")
	void shouldNotRemoveUnassociatedItems() {
		// given:
		String CODE = "remove_test_" + UUID.randomUUID().toString();
		String RELATED_CODE = "remove_test_related" + UUID.randomUUID().toString();

		RemoveRelationTestItem removeTestItem = itemService.create(RemoveRelationTestItem.class);

		RemoveRelatedItem removeRelatedItem1 = itemService.create(RemoveRelatedItem.class);
		RemoveRelatedItem removeRelatedItem2 = itemService.create(RemoveRelatedItem.class);
		RemoveRelatedItem removeRelatedItem3 = itemService.create(RemoveRelatedItem.class);
		RemoveRelatedItem removeRelatedItem4 = itemService.create(RemoveRelatedItem.class);
		RemoveRelatedItem removeRelatedItem5 = itemService.create(RemoveRelatedItem.class);
		RemoveRelatedItem removeRelatedItem6 = itemService.create(RemoveRelatedItem.class);
		RemoveRelatedItem removeRelatedItem7 = itemService.create(RemoveRelatedItem.class);

		removeRelatedItem1.setUniqueCode(RELATED_CODE);
		removeRelatedItem2.setUniqueCode(RELATED_CODE);
		removeRelatedItem3.setUniqueCode(RELATED_CODE);
		removeRelatedItem4.setUniqueCode(RELATED_CODE);
		removeRelatedItem5.setUniqueCode(RELATED_CODE);
		removeRelatedItem6.setUniqueCode(RELATED_CODE);
		removeRelatedItem7.setUniqueCode(RELATED_CODE);

		removeTestItem.setRelatedType(removeRelatedItem1);
		removeTestItem.setOneToOneTarget(removeRelatedItem2);
		removeTestItem.setOneToManyTargets(Set.of(removeRelatedItem3, removeRelatedItem4));
		removeTestItem.setManyToOneTarget(removeRelatedItem5);
		removeTestItem.setManyToManyTargets(Set.of(removeRelatedItem6, removeRelatedItem7));

		removeTestItem.setUniqueCode(CODE);

		itemService.save(removeTestItem);
		// when:

		itemService.delete(removeTestItem);
		//then:

		SearchResult<RemoveRelationTestItem> search = searchService.search(format("select * from %s where uniqueCode = :uniqueCode", RemoveRelationTestItem.ITEM_TYPE), Map.of(RemoveRelationTestItem.UNIQUE_CODE, CODE));

		assertEquals(0, search.getCount());

		SearchResult<RemoveRelatedItem> searchRelated = searchService.search(format("select * from %s where uniqueCode = :uniqueCode", RemoveRelatedItem.ITEM_TYPE), Map.of(RemoveRelatedItem.UNIQUE_CODE, RELATED_CODE));

		assertEquals(7, searchRelated.getCount());

		searchRelated.getResultStream().forEach(removeRelatedItem ->
				assertAll(() -> assertTrue(CollectionUtils.isEmpty(removeRelatedItem.getManyToManySources())),
				() -> assertTrue(CollectionUtils.isEmpty(removeRelatedItem.getManyToOneSources())),
				() -> assertNull(removeRelatedItem.getOneToManySource()),
				() -> assertNull(removeRelatedItem.getOneToOneSource())));
	}

	@Test
	@DisplayName("should correctly remove item")
	void shouldRemoveItem() {
		// given:
		String UNIQUE_CODE = "remove_test_" + UUID.randomUUID();

		RemoveTestItem removeTestItem = itemService.create(RemoveTestItem.class);

		removeTestItem.setUniqueCode(UNIQUE_CODE);

		itemService.save(removeTestItem);
		// when:

		itemService.delete(removeTestItem);
		//then:

		SearchResult<RemoveTestItem> search = searchService.search(format("select * from %s where uniqueCode = :uniqueCode", RemoveTestItem.ITEM_TYPE), Map.of(RemoveTestItem.UNIQUE_CODE, UNIQUE_CODE));

		assertEquals(0, search.getCount());
	}
}
