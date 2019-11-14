package com.coretex.core.general.converters;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.BeanCreationException;

import com.coretex.core.tests.extentions.MockitoExtension;
import com.coretex.core.tests.tags.UnitTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@UnitTest
@DisplayName("A General Converter")
@ExtendWith(MockitoExtension.class)
class GeneralConverterTest {

	private GeneralConverter<Source, Target> generalConverter = new GeneralConverter<>();

	@Mock
	private Populator<Source, Target> populator;

	@Mock
	private Source source;

	@Mock
	private Target target;

	@BeforeEach
	void setUp() {
		generalConverter.setPopulators(Collections.singletonList(populator));
		generalConverter.setSupplier(() -> target);
		given(populator.isSuitable(source, target)).willReturn(Boolean.TRUE);
	}

	@Test
	@DisplayName("is throwing exception when target class/supplier is not specified")
	void isThrowingExceptionWhenTargetClassOrSupplierIsNotSpecified() {
		// given
		generalConverter.setSupplier(null);

		// then:
		assertThrows(BeanCreationException.class,
				() -> generalConverter.convert(source));
	}

	@Test
	@DisplayName("is creating new target using supplier")
	void shouldCreateNewTargetUsingSupplier() {
		// when:
		Target result = generalConverter.convert(source);

		// then:
		assertEquals(target, result, "Expected result of convert operation");
	}


	@Test
	@DisplayName("should populate target with suitable populator")
	void shouldPopulateTargetWithSuitablePopulator() {
		// when:
		Target result = generalConverter.convert(source);

		// then:
		assertNotNull(result);
		verify(populator).populate(source, target);
	}


	@Test
	@DisplayName("should skip population of target object")
	void shouldSkipPopulationOfTargetObject() {
		// given:
		given(populator.isSuitable(source, target)).willReturn(Boolean.FALSE);

		// when:
		Target result = generalConverter.convert(source);

		// then:
		assertNotNull(result);
		verify(populator, never()).populate(source, target);
	}

	private class Source {
	}


	private class Target {
	}
}