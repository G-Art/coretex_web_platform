package com.coretex.core.test;

import com.coretex.core.activeorm.services.ItemService;
import com.coretex.core.activeorm.services.SearchService;
import com.coretex.core.tests.extentions.MockitoExtension;
import com.coretex.core.tests.tags.IntegrationTest;
import com.coretex.items.core.GenericItem;
import com.coretex.items.core.MetaTypeItem;
import com.coretex.items.test_orm.*;
import com.coretex.server.spring.CortexEnvironmentInitializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ContextConfiguration(
		value = {"classpath:/config/spring/core-module-spring.xml", "classpath:/config/spring/test_orm-module-spring.xml"},
		initializers = CortexEnvironmentInitializer.class)
@DisplayName("Save update item test")
@ExtendWith({MockitoExtension.class, SpringExtension.class})
@IntegrationTest
public class SaveUpdateItemTest {

	@Resource
	private ItemService itemService;

	@Resource
	private SearchService searchService;

	@Test
	@DisplayName("should correctly update items with localized Date attribute")
	void shouldSaveDateLocalizedType() {

		LocalizedDateTypeContainerTestItem testItem = itemService.create(LocalizedDateTypeContainerTestItem.class);
		testItem.setDateType(new Date());
		testItem.setDateType(new Date(System.currentTimeMillis()+9999999), Locale.GERMAN);
		testItem.setDateType(new Date(System.currentTimeMillis()-9999999), Locale.UK);
		itemService.save(testItem);

		testItem.setDateType(new Date(System.currentTimeMillis()+9999999));
		testItem.setDateType(new Date(System.currentTimeMillis()-9999999), Locale.GERMAN);
		testItem.setDateType(new Date(), Locale.UK);
		itemService.save(testItem);

		LocalizedDateTypeContainerTestItem searchedTestItem = searchService.<LocalizedDateTypeContainerTestItem>search(format("select * from %s where uuid = :uuid", LocalizedDateTypeContainerTestItem.ITEM_TYPE), Map.of(LocalizedDateTypeContainerTestItem.UUID, testItem.getUuid())).getResult().get(0);

		assertNotNull(testItem.getUuid());
		assertEquals(testItem, searchedTestItem);
		assertEquals(testItem.getDateType(), searchedTestItem.getDateType());
		assertEquals(testItem.getDateType(Locale.GERMAN), searchedTestItem.getDateType(Locale.GERMAN));
		assertEquals(testItem.getDateType(Locale.UK), searchedTestItem.getDateType(Locale.UK));

		assertNull(testItem.getDateType(Locale.CHINA));
		assertNull(searchedTestItem.getDateType(Locale.CHINA));
		assertEquals(testItem.getDateType(Locale.FRENCH), searchedTestItem.getDateType(Locale.FRENCH));

	}

	@Test
	@DisplayName("should correctly update items with localized Short attribute")
	void shouldSaveShortLocalizedType() {

		LocalizedShortTypeContainerTestItem testItem = itemService.create(LocalizedShortTypeContainerTestItem.class);
		testItem.setShortType((short) 1);
		testItem.setShortType((short) 2, Locale.GERMAN);
		testItem.setShortType((short) 3, Locale.UK);
		itemService.save(testItem);

		testItem.setShortType((short) 3);
		testItem.setShortType((short) 2, Locale.GERMAN);
		testItem.setShortType((short) 1, Locale.UK);
		itemService.save(testItem);

		LocalizedShortTypeContainerTestItem searchedTestItem = searchService.<LocalizedShortTypeContainerTestItem>search(format("select * from %s where uuid = :uuid", LocalizedShortTypeContainerTestItem.ITEM_TYPE), Map.of(LocalizedShortTypeContainerTestItem.UUID, testItem.getUuid())).getResult().get(0);

		assertNotNull(testItem.getUuid());
		assertEquals(testItem, searchedTestItem);
		assertEquals(testItem.getShortType(), searchedTestItem.getShortType());
		assertEquals(testItem.getShortType(Locale.GERMAN), searchedTestItem.getShortType(Locale.GERMAN));
		assertEquals(testItem.getShortType(Locale.UK), searchedTestItem.getShortType(Locale.UK));

		assertNull(testItem.getShortType(Locale.CHINA));
		assertNull(searchedTestItem.getShortType(Locale.CHINA));
		assertEquals(testItem.getShortType(Locale.FRENCH), searchedTestItem.getShortType(Locale.FRENCH));

	}

	@Test
	@DisplayName("should correctly update items with localized Byte attribute")
	void shouldSaveByteLocalizedType() {

		LocalizedByteTypeContainerTestItem testItem = itemService.create(LocalizedByteTypeContainerTestItem.class);
		testItem.setByteType((byte) 1);
		testItem.setByteType((byte) 2, Locale.GERMAN);
		testItem.setByteType((byte) 3, Locale.UK);
		itemService.save(testItem);

		testItem.setByteType((byte) 3);
		testItem.setByteType((byte) 2, Locale.GERMAN);
		testItem.setByteType((byte) 1, Locale.UK);
		itemService.save(testItem);

		LocalizedByteTypeContainerTestItem searchedTestItem = searchService.<LocalizedByteTypeContainerTestItem>search(format("select * from %s where uuid = :uuid", LocalizedByteTypeContainerTestItem.ITEM_TYPE), Map.of(LocalizedByteTypeContainerTestItem.UUID, testItem.getUuid())).getResult().get(0);

		assertNotNull(testItem.getUuid());
		assertEquals(testItem, searchedTestItem);
		assertEquals(testItem.getByteType(), searchedTestItem.getByteType());
		assertEquals(testItem.getByteType(Locale.GERMAN), searchedTestItem.getByteType(Locale.GERMAN));
		assertEquals(testItem.getByteType(Locale.UK), searchedTestItem.getByteType(Locale.UK));

		assertNull(testItem.getByteType(Locale.CHINA));
		assertNull(searchedTestItem.getByteType(Locale.CHINA));
		assertEquals(testItem.getByteType(Locale.FRENCH), searchedTestItem.getByteType(Locale.FRENCH));

	}

	@Test
	@DisplayName("should correctly update items with localized Class attribute")
	void shouldSaveClassLocalizedType() {

		LocalizedClassTypeContainerTestItem testItem = itemService.create(LocalizedClassTypeContainerTestItem.class);
		testItem.setClassType(LocalizedClassTypeContainerTestItem.class);
		testItem.setClassType(GenericItem.class, Locale.GERMAN);
		testItem.setClassType(MetaTypeItem.class, Locale.UK);
		itemService.save(testItem);

		testItem.setClassType(MetaTypeItem.class);
		testItem.setClassType(GenericItem.class, Locale.GERMAN);
		testItem.setClassType(LocalizedClassTypeContainerTestItem.class, Locale.UK);
		itemService.save(testItem);

		LocalizedClassTypeContainerTestItem searchedTestItem = searchService.<LocalizedClassTypeContainerTestItem>search(format("select * from %s where uuid = :uuid", LocalizedClassTypeContainerTestItem.ITEM_TYPE), Map.of(LocalizedClassTypeContainerTestItem.UUID, testItem.getUuid())).getResult().get(0);

		assertNotNull(testItem.getUuid());
		assertEquals(testItem, searchedTestItem);
		assertEquals(testItem.getClassType(), searchedTestItem.getClassType());
		assertEquals(testItem.getClassType(Locale.GERMAN), searchedTestItem.getClassType(Locale.GERMAN));
		assertEquals(testItem.getClassType(Locale.UK), searchedTestItem.getClassType(Locale.UK));

		assertNull(testItem.getClassType(Locale.CHINA));
		assertNull(searchedTestItem.getClassType(Locale.CHINA));
		assertEquals(testItem.getClassType(Locale.FRENCH), searchedTestItem.getClassType(Locale.FRENCH));

	}

	@Test
	@DisplayName("should correctly update items with localized Float attribute")
	void shouldSaveFloatLocalizedType() {

		LocalizedFloatTypeContainerTestItem testItem = itemService.create(LocalizedFloatTypeContainerTestItem.class);
		testItem.setFloatType(100.011f);
		testItem.setFloatType(200.012f, Locale.GERMAN);
		testItem.setFloatType(300.013f, Locale.UK);
		itemService.save(testItem);

		testItem.setFloatType(100.013f);
		testItem.setFloatType(200.012f, Locale.GERMAN);
		testItem.setFloatType(300.011f, Locale.UK);
		itemService.save(testItem);

		LocalizedFloatTypeContainerTestItem searchedTestItem = searchService.<LocalizedFloatTypeContainerTestItem>search(format("select * from %s where uuid = :uuid", LocalizedFloatTypeContainerTestItem.ITEM_TYPE), Map.of(LocalizedFloatTypeContainerTestItem.UUID, testItem.getUuid())).getResult().get(0);

		assertNotNull(testItem.getUuid());
		assertEquals(testItem, searchedTestItem);
		assertEquals(testItem.getFloatType(), searchedTestItem.getFloatType());
		assertEquals(testItem.getFloatType(Locale.GERMAN), searchedTestItem.getFloatType(Locale.GERMAN));
		assertEquals(testItem.getFloatType(Locale.UK), searchedTestItem.getFloatType(Locale.UK));

		assertNull(testItem.getFloatType(Locale.CHINA));
		assertNull(searchedTestItem.getFloatType(Locale.CHINA));
		assertEquals(testItem.getFloatType(Locale.FRENCH), searchedTestItem.getFloatType(Locale.FRENCH));

	}

	@Test
	@DisplayName("should correctly update items with localized BigDecimal attribute")
	void shouldSaveBigDecimalLocalizedType() {
		LocalizedBigDecimalTypeContainerTestItem testItem = itemService.create(LocalizedBigDecimalTypeContainerTestItem.class);
		testItem.setBigDecimalType(BigDecimal.valueOf(100.001));
		testItem.setBigDecimalType(BigDecimal.valueOf(200.002), Locale.GERMAN);
		testItem.setBigDecimalType(BigDecimal.valueOf(300.003), Locale.UK);
		itemService.save(testItem);

		testItem.setBigDecimalType(BigDecimal.valueOf(100.003));
		testItem.setBigDecimalType(BigDecimal.valueOf(200.002), Locale.GERMAN);
		testItem.setBigDecimalType(BigDecimal.valueOf(300.001), Locale.UK);
		itemService.save(testItem);

		LocalizedBigDecimalTypeContainerTestItem searchedTestItem = searchService.<LocalizedBigDecimalTypeContainerTestItem>search(format("select * from %s where uuid = :uuid", LocalizedBigDecimalTypeContainerTestItem.ITEM_TYPE), Map.of(LocalizedBigDecimalTypeContainerTestItem.UUID, testItem.getUuid())).getResult().get(0);

		assertNotNull(testItem.getUuid());
		assertEquals(testItem, searchedTestItem);
		assertEquals(testItem.getBigDecimalType(), searchedTestItem.getBigDecimalType());
		assertEquals(testItem.getBigDecimalType(Locale.GERMAN), searchedTestItem.getBigDecimalType(Locale.GERMAN));
		assertEquals(testItem.getBigDecimalType(Locale.UK), searchedTestItem.getBigDecimalType(Locale.UK));

		assertNull(testItem.getBigDecimalType(Locale.CHINA));
		assertNull(searchedTestItem.getBigDecimalType(Locale.CHINA));
		assertEquals(testItem.getBigDecimalType(Locale.FRENCH), searchedTestItem.getBigDecimalType(Locale.FRENCH));

	}

	@Test
	@DisplayName("should correctly update items with localized Character attribute")
	void shouldSaveCharacterLocalizedType() {
		LocalizedCharacterTypeContainerTestItem testItem = itemService.create(LocalizedCharacterTypeContainerTestItem.class);
		testItem.setCharacterType('*');
		testItem.setCharacterType('@', Locale.GERMAN);
		testItem.setCharacterType('$', Locale.UK);
		itemService.save(testItem);

		testItem.setCharacterType('$');
		testItem.setCharacterType('@', Locale.GERMAN);
		testItem.setCharacterType('*', Locale.UK);
		itemService.save(testItem);

		LocalizedCharacterTypeContainerTestItem searchedTestItem = searchService.<LocalizedCharacterTypeContainerTestItem>search(format("select * from %s where uuid = :uuid", LocalizedCharacterTypeContainerTestItem.ITEM_TYPE), Map.of(LocalizedCharacterTypeContainerTestItem.UUID, testItem.getUuid())).getResult().get(0);

		assertNotNull(testItem.getUuid());
		assertEquals(testItem, searchedTestItem);
		assertEquals(testItem.getCharacterType(), searchedTestItem.getCharacterType());
		assertEquals(testItem.getCharacterType(Locale.GERMAN), searchedTestItem.getCharacterType(Locale.GERMAN));
		assertEquals(testItem.getCharacterType(Locale.UK), searchedTestItem.getCharacterType(Locale.UK));

		assertNull(testItem.getCharacterType(Locale.CHINA));
		assertNull(searchedTestItem.getCharacterType(Locale.CHINA));
		assertEquals(testItem.getCharacterType(Locale.FRENCH), searchedTestItem.getCharacterType(Locale.FRENCH));

	}

	@Test
	@DisplayName("should correctly update items with localized LocalDate attribute")
	void shouldSaveLocalDateLocalizedType() {
		LocalizedLocalDateTypeContainerTestItem testItem = itemService.create(LocalizedLocalDateTypeContainerTestItem.class);
		testItem.setLocalDateLocaleType(LocalDate.now());
		testItem.setLocalDateLocaleType(LocalDate.now().plus(1, ChronoUnit.DAYS), Locale.GERMAN);
		testItem.setLocalDateLocaleType(LocalDate.now().plus(2, ChronoUnit.DAYS), Locale.UK);
		itemService.save(testItem);

		testItem.setLocalDateLocaleType(LocalDate.now().plus(2, ChronoUnit.DAYS));
		testItem.setLocalDateLocaleType(LocalDate.now().plus(1, ChronoUnit.DAYS), Locale.GERMAN);
		testItem.setLocalDateLocaleType(LocalDate.now(), Locale.UK);
		itemService.save(testItem);

		LocalizedLocalDateTypeContainerTestItem searchedTestItem = searchService.<LocalizedLocalDateTypeContainerTestItem>search(format("select * from %s where uuid = :uuid", LocalizedLocalDateTypeContainerTestItem.ITEM_TYPE), Map.of(LocalizedLocalDateTypeContainerTestItem.UUID, testItem.getUuid())).getResult().get(0);

		assertNotNull(testItem.getUuid());
		assertEquals(testItem, searchedTestItem);
		assertEquals(testItem.getLocalDateLocaleType(), searchedTestItem.getLocalDateLocaleType());
		assertEquals(testItem.getLocalDateLocaleType(Locale.GERMAN), searchedTestItem.getLocalDateLocaleType(Locale.GERMAN));
		assertEquals(testItem.getLocalDateLocaleType(Locale.UK), searchedTestItem.getLocalDateLocaleType(Locale.UK));

		assertNull(testItem.getLocalDateLocaleType(Locale.CHINA));
		assertNull(searchedTestItem.getLocalDateLocaleType(Locale.CHINA));
		assertEquals(testItem.getLocalDateLocaleType(Locale.FRENCH), searchedTestItem.getLocalDateLocaleType(Locale.FRENCH));

	}

	@Test
	@DisplayName("should correctly update items with localized LocalDateTime attribute")
	void shouldSaveLocalDateTimeLocalizedType() {
		LocalizedLocalDateTimeTypeContainerTestItem testItem = itemService.create(LocalizedLocalDateTimeTypeContainerTestItem.class);
		testItem.setLocalDateTimeLocaleType(LocalDateTime.now());
		testItem.setLocalDateTimeLocaleType(LocalDateTime.now().plus(1, ChronoUnit.HOURS), Locale.GERMAN);
		testItem.setLocalDateTimeLocaleType(LocalDateTime.now().plus(2, ChronoUnit.HOURS), Locale.UK);
		itemService.save(testItem);

		testItem.setLocalDateTimeLocaleType(LocalDateTime.now().plus(2, ChronoUnit.HOURS));
		testItem.setLocalDateTimeLocaleType(LocalDateTime.now().plus(1, ChronoUnit.HOURS), Locale.GERMAN);
		testItem.setLocalDateTimeLocaleType(LocalDateTime.now(), Locale.UK);
		itemService.save(testItem);

		LocalizedLocalDateTimeTypeContainerTestItem searchedTestItem = searchService.<LocalizedLocalDateTimeTypeContainerTestItem>search(format("select * from %s where uuid = :uuid", LocalizedLocalDateTimeTypeContainerTestItem.ITEM_TYPE), Map.of(LocalizedLocalDateTimeTypeContainerTestItem.UUID, testItem.getUuid())).getResult().get(0);

		assertNotNull(testItem.getUuid());
		assertEquals(testItem, searchedTestItem);
		assertEquals(testItem.getLocalDateTimeLocaleType(), searchedTestItem.getLocalDateTimeLocaleType());
		assertEquals(testItem.getLocalDateTimeLocaleType(Locale.GERMAN), searchedTestItem.getLocalDateTimeLocaleType(Locale.GERMAN));
		assertEquals(testItem.getLocalDateTimeLocaleType(Locale.UK), searchedTestItem.getLocalDateTimeLocaleType(Locale.UK));

		assertNull(testItem.getLocalDateTimeLocaleType(Locale.CHINA));
		assertNull(searchedTestItem.getLocalDateTimeLocaleType(Locale.CHINA));
		assertEquals(testItem.getLocalDateTimeLocaleType(Locale.FRENCH), searchedTestItem.getLocalDateTimeLocaleType(Locale.FRENCH));

	}

	@Test
	@DisplayName("should correctly update items with localized LocalTime attribute")
	void shouldSaveLocalTimeLocalizedType() {
		LocalizedLocalTimeTypeContainerTestItem testItem = itemService.create(LocalizedLocalTimeTypeContainerTestItem.class);
		testItem.setLocalTimeLocaleType(LocalTime.MIDNIGHT.plus(1, ChronoUnit.HOURS));
		testItem.setLocalTimeLocaleType(LocalTime.now(), Locale.GERMAN);
		testItem.setLocalTimeLocaleType(LocalTime.now().plus(1, ChronoUnit.HOURS), Locale.UK);
		itemService.save(testItem);

		testItem.setLocalTimeLocaleType(LocalTime.now().plus(1, ChronoUnit.HOURS));
		testItem.setLocalTimeLocaleType(LocalTime.now(), Locale.GERMAN);
		testItem.setLocalTimeLocaleType(LocalTime.MIDNIGHT.plus(1, ChronoUnit.HOURS), Locale.UK);
		itemService.save(testItem);

		LocalizedLocalTimeTypeContainerTestItem searchedTestItem = searchService.<LocalizedLocalTimeTypeContainerTestItem>search(format("select * from %s where uuid = :uuid", LocalizedLocalTimeTypeContainerTestItem.ITEM_TYPE), Map.of(LocalizedLocalTimeTypeContainerTestItem.UUID, testItem.getUuid())).getResult().get(0);

		assertNotNull(testItem.getUuid());
		assertEquals(testItem, searchedTestItem);
		assertEquals(testItem.getLocalTimeLocaleType(), searchedTestItem.getLocalTimeLocaleType());
		assertEquals(testItem.getLocalTimeLocaleType(Locale.GERMAN), searchedTestItem.getLocalTimeLocaleType(Locale.GERMAN));
		assertEquals(testItem.getLocalTimeLocaleType(Locale.UK), searchedTestItem.getLocalTimeLocaleType(Locale.UK));

		assertNull(testItem.getLocalTimeLocaleType(Locale.CHINA));
		assertNull(searchedTestItem.getLocalTimeLocaleType(Locale.CHINA));
		assertEquals(testItem.getLocalTimeLocaleType(Locale.FRENCH), searchedTestItem.getLocalTimeLocaleType(Locale.FRENCH));

	}

	@Test
	@DisplayName("should correctly update items with localized UUID attribute")
	void shouldSaveUuidLocalizedType() {
		LocalizedUuidTypeContainerTestItem testItem = itemService.create(LocalizedUuidTypeContainerTestItem.class);
		testItem.setUuidLocaleType(UUID.randomUUID());
		testItem.setUuidLocaleType(UUID.randomUUID(), Locale.GERMAN);
		testItem.setUuidLocaleType(UUID.randomUUID(), Locale.UK);
		itemService.save(testItem);

		testItem.setUuidLocaleType(UUID.randomUUID());
		testItem.setUuidLocaleType(UUID.randomUUID(), Locale.GERMAN);
		testItem.setUuidLocaleType(UUID.randomUUID(), Locale.UK);
		itemService.save(testItem);

		LocalizedUuidTypeContainerTestItem searchedTestItem = searchService.<LocalizedUuidTypeContainerTestItem>search(format("select * from %s where uuid = :uuid", LocalizedUuidTypeContainerTestItem.ITEM_TYPE), Map.of(LocalizedUuidTypeContainerTestItem.UUID, testItem.getUuid())).getResult().get(0);

		assertNotNull(testItem.getUuid());
		assertEquals(testItem, searchedTestItem);
		assertEquals(testItem.getUuidLocaleType(), searchedTestItem.getUuidLocaleType());
		assertEquals(testItem.getUuidLocaleType(Locale.GERMAN), searchedTestItem.getUuidLocaleType(Locale.GERMAN));
		assertEquals(testItem.getUuidLocaleType(Locale.UK), searchedTestItem.getUuidLocaleType(Locale.UK));

		assertNull(testItem.getUuidLocaleType(Locale.CHINA));
		assertNull(searchedTestItem.getUuidLocaleType(Locale.CHINA));
		assertEquals(testItem.getUuidLocaleType(Locale.FRENCH), searchedTestItem.getUuidLocaleType(Locale.FRENCH));

	}

	@Test
	@DisplayName("should correctly update items with localized String attribute")
	void shouldSaveStringLocalizedType() {
		LocalizedStringTypeContainerTestItem testItem = itemService.create(LocalizedStringTypeContainerTestItem.class);
		testItem.setStringLocaleType("Default locale");
		testItem.setStringLocaleType("German locale", Locale.GERMAN);
		testItem.setStringLocaleType("UK locale", Locale.UK);
		itemService.save(testItem);

		testItem.setStringLocaleType("Default locale new");
		testItem.setStringLocaleType("German locale new", Locale.GERMAN);
		testItem.setStringLocaleType("UK locale new", Locale.UK);
		itemService.save(testItem);

		LocalizedStringTypeContainerTestItem searchedTestItem = searchService.<LocalizedStringTypeContainerTestItem>search(format("select * from %s where uuid = :uuid", LocalizedStringTypeContainerTestItem.ITEM_TYPE), Map.of(LocalizedStringTypeContainerTestItem.UUID, testItem.getUuid())).getResult().get(0);

		assertNotNull(testItem.getUuid());
		assertEquals(testItem, searchedTestItem);
		assertEquals(testItem.getStringLocaleType(), searchedTestItem.getStringLocaleType());
		assertEquals(testItem.getStringLocaleType(Locale.GERMAN), searchedTestItem.getStringLocaleType(Locale.GERMAN));
		assertEquals(testItem.getStringLocaleType(Locale.UK), searchedTestItem.getStringLocaleType(Locale.UK));

		assertNull(testItem.getStringLocaleType(Locale.CHINA));
		assertNull(searchedTestItem.getStringLocaleType(Locale.CHINA));
		assertEquals(testItem.getStringLocaleType(Locale.FRENCH), searchedTestItem.getStringLocaleType(Locale.FRENCH));

	}


	@Test
	@DisplayName("should correctly update items with regular types of attributes")
	void shouldSaveAllPrimitivesTypes() {
		// given:
		RegularTypeContainerTestItem testItem = itemService.create(RegularTypeContainerTestItem.class);
		testItem.setUuidType(UUID.randomUUID());
		testItem.setBigDecimalType(BigDecimal.valueOf(10.11));
		testItem.setByteType(Byte.MAX_VALUE);
		testItem.setStringType("String");
		testItem.setLongType(Long.MAX_VALUE);
		testItem.setBooleanType(Boolean.TRUE);
		testItem.setIntegerType(Integer.MAX_VALUE);
		testItem.setFloatType(Float.MAX_VALUE);
		testItem.setDoubleType(Double.MAX_VALUE);
		testItem.setCharacterType(Character.MAX_VALUE);
		testItem.setClassType(RegularTypeContainerTestItem.class);
		testItem.setLocalDateTimeType(LocalDateTime.now());
		testItem.setLocalTimeType(LocalTime.now());
		testItem.setLocalDateType(LocalDate.now());
		testItem.setShortType(Short.MAX_VALUE);
		testItem.setDateType(new Date());

		itemService.save(testItem);

		testItem.setUuidType(UUID.randomUUID());
		testItem.setBigDecimalType(BigDecimal.valueOf(10.12));
		testItem.setByteType((byte) (Byte.MAX_VALUE - 1));
		testItem.setStringType("String new ");
		testItem.setLongType(Long.MAX_VALUE - 1);
		testItem.setBooleanType(Boolean.FALSE);
		testItem.setIntegerType(Integer.MAX_VALUE - 1);
		testItem.setFloatType(Float.MAX_VALUE - 1 );
		testItem.setDoubleType(Double.MAX_VALUE - 1);
		testItem.setCharacterType('$');
		testItem.setClassType(GenericItem.class);
		testItem.setLocalDateTimeType(LocalDateTime.now().minus(1, ChronoUnit.MINUTES));
		testItem.setLocalTimeType(LocalTime.now().minus(1, ChronoUnit.MINUTES));
		testItem.setLocalDateType(LocalDate.now().minus(1, ChronoUnit.DAYS));
		testItem.setShortType((short) (Short.MAX_VALUE-1));
		testItem.setDateType(new Date());

		itemService.save(testItem);

		// when:

		RegularTypeContainerTestItem searchedTestItem = searchService.<RegularTypeContainerTestItem>search(format("select * from %s where uuid = :uuid", RegularTypeContainerTestItem.ITEM_TYPE), Map.of(RegularTypeContainerTestItem.UUID, testItem.getUuid())).getResult().get(0);

		//then:
		assertNotNull(testItem.getUuid());
		assertEquals(testItem, searchedTestItem);
		assertEquals(testItem.getUuidType(), searchedTestItem.getUuidType());
		assertEquals(testItem.getClassType(), searchedTestItem.getClassType());
		assertEquals(testItem.getBigDecimalType(), searchedTestItem.getBigDecimalType().setScale(2, RoundingMode.HALF_UP));
		assertEquals(testItem.getByteType(), searchedTestItem.getByteType());
		assertEquals(testItem.getStringType(), searchedTestItem.getStringType());
		assertEquals(testItem.getLongType(), searchedTestItem.getLongType());
		assertEquals(testItem.getIntegerType(), searchedTestItem.getIntegerType());
		assertEquals(testItem.getBooleanType(), searchedTestItem.getBooleanType());
		assertEquals(testItem.getCharacterType(), searchedTestItem.getCharacterType());
		assertEquals(testItem.getDoubleType(), searchedTestItem.getDoubleType());
		assertEquals(testItem.getLocalDateType(), searchedTestItem.getLocalDateType());
		assertEquals(testItem.getFloatType(), searchedTestItem.getFloatType());
		assertEquals(testItem.getDateType(), searchedTestItem.getDateType());
		assertEquals(testItem.getShortType(), searchedTestItem.getShortType());
		assertEquals(testItem.getLocalTimeType().getHour(), searchedTestItem.getLocalTimeType().getHour());
		assertEquals(testItem.getLocalTimeType().getMinute(), searchedTestItem.getLocalTimeType().getMinute());
		assertEquals(testItem.getLocalTimeType().getSecond(), searchedTestItem.getLocalTimeType().getSecond());
		assertEquals(testItem.getLocalDateTimeType(), searchedTestItem.getLocalDateTimeType());
	}

}
