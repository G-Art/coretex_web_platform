package com.coretex.core.test;

import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.core.activeorm.dao.SortParameters;
import com.coretex.core.activeorm.services.ItemService;
import com.coretex.core.activeorm.services.SearchService;
import com.coretex.core.services.bootstrap.impl.CortexContext;
import com.coretex.core.tests.extentions.MockitoExtension;
import com.coretex.core.tests.tags.IntegrationTest;
import com.coretex.items.test_orm.SearchExtendsWithTableTestItem;
import com.coretex.items.test_orm.SearchExtendsWithoutTableTestItem;
import com.coretex.items.test_orm.SearchTestItem;
import com.coretex.server.spring.CortexEnvironmentInitializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@ContextConfiguration(
		value = {"classpath:/config/spring/core-module-spring.xml", "classpath:/config/spring/test_orm-module-spring.xml"},
		initializers = CortexEnvironmentInitializer.class)
@DisplayName("Default generic dao test")
@ExtendWith({MockitoExtension.class, SpringExtension.class})
@IntegrationTest
public class GenericDaoTest {

	@Resource
	private ItemService itemService;

	@Resource
	private SearchService searchService;

	@Resource
	private CortexContext cortexContext;

	@Test
	@DisplayName("should select all requested type items though dao")
	void shouldSelectAllRequestedTypeItemThroughDao() {
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

		var defaultGenericDao = new DefaultGenericDao<SearchTestItem>(SearchTestItem.ITEM_TYPE);
		defaultGenericDao.setSearchService(searchService);

		List<SearchTestItem> result = defaultGenericDao.find(Map.of(SearchTestItem.CODE, CODE));
		//then:

		assertEquals(3, result.size());

	}

	@Test
	@DisplayName("should select all requested type items though dao with sorting")
	void shouldSelectAllRequestedTypeItemThroughDaoWithSorting() {
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

		var defaultGenericDao = new DefaultGenericDao<SearchTestItem>(SearchTestItem.ITEM_TYPE);
		defaultGenericDao.setSearchService(searchService);

		List<SearchTestItem> result = defaultGenericDao.find(Map.of(SearchTestItem.CODE, CODE), SortParameters.singletonAscending(SearchTestItem.CREATE_DATE));
		//then:

		assertEquals(3, result.size());

	}

	@Test
	@DisplayName("should select all requested type items though dao with sorting and limits")
	void shouldSelectAllRequestedTypeItemThroughDaoWithSortingAndLimits() {
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

		var defaultGenericDao = new DefaultGenericDao<SearchTestItem>(SearchTestItem.ITEM_TYPE);
		defaultGenericDao.setSearchService(searchService);

		List<SearchTestItem> result = defaultGenericDao.find(Collections.emptyMap(), SortParameters.singletonAscending(SearchTestItem.CODE), 3L, 0);
		//then:

		assertEquals(3, result.size());

	}

}
