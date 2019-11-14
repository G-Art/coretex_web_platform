package com.coretex.core.test;

import com.coretex.core.activeorm.services.ItemService;
import com.coretex.core.activeorm.services.SearchResult;
import com.coretex.core.activeorm.services.SearchService;
import com.coretex.core.tests.extentions.MockitoExtension;
import com.coretex.core.tests.tags.IntegrationTest;
import com.coretex.items.test_orm.*;
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
import java.util.UUID;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@ContextConfiguration(
		value = {"classpath:/config/spring/core-module-spring.xml", "classpath:/config/spring/test_orm-module-spring.xml"},
		initializers = CortexEnvironmentInitializer.class)
@DisplayName("Search item test")
@ExtendWith({MockitoExtension.class, SpringExtension.class})
@IntegrationTest
public class SearchItemTest {

	@Resource
	private ItemService itemService;

	@Resource
	private SearchService searchService;

	@Test
	@DisplayName("should select all requested type items")
	void shouldSelectAllRequestedTypeItem() {
		// given:
		String CODE = "search_test_" + UUID.randomUUID().toString();

		SearchTestItem searchTestItem = itemService.create(SearchTestItem.class);
		searchTestItem.setCode(CODE);

		SearchExtendsWithoutTableTestItem searchExtendsWithoutTableTestItem = itemService.create(SearchExtendsWithoutTableTestItem.class);
		searchExtendsWithoutTableTestItem.setCode(CODE);
		searchExtendsWithoutTableTestItem.setIndicatorNoTableField("test indicator");

		SearchExtendsWithTableTestItem searchExtendsWithTableTestItem = itemService.create(SearchExtendsWithTableTestItem.class);
		searchExtendsWithTableTestItem.setCode(CODE);
		searchExtendsWithTableTestItem.setIndicatorTableField("test separate table indicator");

		itemService.saveAll(List.of(searchTestItem, searchExtendsWithTableTestItem, searchExtendsWithoutTableTestItem));
		// when:

		SearchResult<SearchTestItem> search = searchService.search(format("select * from %s where code = :code", SearchTestItem.ITEM_TYPE), Map.of(SearchTestItem.CODE, CODE));
		//then:

		assertEquals(3, search.getCount());

	}

	@Test
	@DisplayName("should select execute count expression for complex item")
	void shouldSelectExecuteCountExpressionForComplexItem() {
		// given:
		String CODE = "search_test_count_expression" + UUID.randomUUID().toString();

		SearchTestItem searchTestItem = itemService.create(SearchTestItem.class);
		searchTestItem.setCode(CODE);

		SearchExtendsWithoutTableTestItem searchExtendsWithoutTableTestItem = itemService.create(SearchExtendsWithoutTableTestItem.class);
		searchExtendsWithoutTableTestItem.setCode(CODE);
		searchExtendsWithoutTableTestItem.setIndicatorNoTableField("test indicator");

		SearchExtendsWithTableTestItem searchExtendsWithTableTestItem = itemService.create(SearchExtendsWithTableTestItem.class);
		searchExtendsWithTableTestItem.setCode(CODE);
		searchExtendsWithTableTestItem.setIndicatorTableField("test separate table indicator");

		itemService.saveAll(List.of(searchTestItem, searchExtendsWithTableTestItem, searchExtendsWithoutTableTestItem));
		// when:

		SearchResult<Map<String, Long>> search = searchService.search(format("select count(*) from %s where code = :code", SearchTestItem.ITEM_TYPE), Map.of(SearchTestItem.CODE, CODE));
		//then:

		assertEquals(1, search.getCount());

		assertEquals(3L, search.getResult().get(0).entrySet().iterator().next().getValue().longValue());

	}

	@Test
	@DisplayName("should execute query wish jointed items")
	void shouldExecuteQueryWithJoinedItem() {
		// given:
		String CODE = "search_test_join_expression" + UUID.randomUUID().toString();

		SearchJoinTestItem searchJoinTestItem = itemService.create(SearchJoinTestItem.class);
		searchJoinTestItem.setCode(CODE);

		SearchTestItem searchTestItem = itemService.create(SearchTestItem.class);
		searchTestItem.setCode("JoinTest");

		searchTestItem.setJoinTestItem(searchJoinTestItem);

		SearchExtendsWithoutTableTestItem searchExtendsWithoutTableTestItem = itemService.create(SearchExtendsWithoutTableTestItem.class);
		searchExtendsWithoutTableTestItem.setCode("JoinTest");
		searchExtendsWithoutTableTestItem.setIndicatorNoTableField("test join indicator");

		searchExtendsWithoutTableTestItem.setJoinTestItem(searchJoinTestItem);

		SearchExtendsWithTableTestItem searchExtendsWithTableTestItem = itemService.create(SearchExtendsWithTableTestItem.class);
		searchExtendsWithTableTestItem.setCode("JoinTest");
		searchExtendsWithTableTestItem.setIndicatorTableField("test join separate table indicator");

		searchExtendsWithTableTestItem.setJoinTestItem(searchJoinTestItem);

		itemService.saveAll(List.of(searchTestItem, searchExtendsWithTableTestItem, searchExtendsWithoutTableTestItem));
		// when:

		SearchResult<Long> search = searchService.search(format("select * from %s as s join %s as j on j.uuid = s.%s where j.code = :code", SearchTestItem.ITEM_TYPE, SearchJoinTestItem.ITEM_TYPE, SearchTestItem.JOIN_TEST_ITEM), Map.of(SearchTestItem.CODE, CODE));
		//then:

		assertEquals(3, search.getCount());

	}

	@Test
	@DisplayName("should execute query wish jointed items case 2")
	void shouldExecuteQueryWithJoinedItemCase2() {
		// given:
		String CODE = "search_test_join_expression_case_2" + UUID.randomUUID().toString();

		SearchJoinTestCase2Item searchJoinTestItem = itemService.create(SearchJoinTestCase2Item.class);
		searchJoinTestItem.setCode(CODE);

		SearchJoinTestCase2Item searchJoinTestCase2ExtendsWithoutTableItem = itemService.create(SearchJoinTestCase2ExtendsWithoutTableItem.class);
		searchJoinTestCase2ExtendsWithoutTableItem.setCode(CODE);

		SearchJoinTestCase2Item searchJoinTestCase2ExtendsWithTableItem = itemService.create(SearchJoinTestCase2ExtendsWithTableItem.class);
		searchJoinTestCase2ExtendsWithTableItem.setCode(CODE);

		SearchTestCase2Item searchTestItem = itemService.create(SearchTestCase2Item.class);
		searchTestItem.setCode("JoinTest");
		searchTestItem.setJoinTestItem(searchJoinTestItem);

		SearchExtendsWithoutTableTestCase2Item searchExtendsWithoutTableTestItem = itemService.create(SearchExtendsWithoutTableTestCase2Item.class);
		searchExtendsWithoutTableTestItem.setCode("JoinTest");
		searchExtendsWithoutTableTestItem.setIndicatorNoTableField("test join indicator");

		searchExtendsWithoutTableTestItem.setJoinTestItem(searchJoinTestCase2ExtendsWithoutTableItem);

		SearchExtendsWithTableTestCase2Item searchExtendsWithTableTestItem = itemService.create(SearchExtendsWithTableTestCase2Item.class);
		searchExtendsWithTableTestItem.setCode("JoinTest");
		searchExtendsWithTableTestItem.setIndicatorTableField("test join separate table indicator");

		searchExtendsWithTableTestItem.setJoinTestItem(searchJoinTestCase2ExtendsWithTableItem);

		itemService.saveAll(List.of(searchTestItem, searchExtendsWithTableTestItem, searchExtendsWithoutTableTestItem));
		// when:

		SearchResult<Long> search = searchService.search(format("select * from %s as s join %s as j on j.uuid = s.%s where j.code = :code", SearchTestCase2Item.ITEM_TYPE, SearchJoinTestCase2Item.ITEM_TYPE, SearchTestCase2Item.JOIN_TEST_ITEM), Map.of(SearchTestCase2Item.CODE, CODE));
		//then:

		assertEquals(3, search.getCount());

	}


	@Test
	@DisplayName("should select requested type items by limit and offset")
	void shouldSelectRequestedTypeItemByLimitAndOffset() {
		// given:
		String CODE = "search_test_" + UUID.randomUUID().toString();

		SearchTestItem searchTestItem = itemService.create(SearchTestItem.class);
		searchTestItem.setCode(CODE);

		SearchExtendsWithoutTableTestItem searchExtendsWithoutTableTestItem = itemService.create(SearchExtendsWithoutTableTestItem.class);
		searchExtendsWithoutTableTestItem.setCode(CODE);
		searchExtendsWithoutTableTestItem.setIndicatorNoTableField("test indicator");

		SearchExtendsWithTableTestItem searchExtendsWithTableTestItem = itemService.create(SearchExtendsWithTableTestItem.class);
		searchExtendsWithTableTestItem.setCode(CODE);
		searchExtendsWithTableTestItem.setIndicatorTableField("test separate table indicator");

		itemService.saveAll(List.of(searchTestItem, searchExtendsWithTableTestItem, searchExtendsWithoutTableTestItem));
		// when:

		SearchResult<SearchTestItem> search = searchService.search(format("select * from %s where code = :code ORDER BY uuid LIMIT 2 OFFSET 1", SearchTestItem.ITEM_TYPE), Map.of(SearchTestItem.CODE, CODE));
		//then:

		assertEquals(2, search.getCount());

	}

}
