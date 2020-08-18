package com.coretex.commerce.admin.init.data;

import com.coretex.commerce.core.dto.FileContentType;
import com.coretex.commerce.core.dto.ImageContentFile;
import com.coretex.commerce.core.services.ProductImageService;
import com.coretex.commerce.core.utils.ProductUtils;
import com.coretex.items.cx_core.ProductAvailabilityItem;
import com.coretex.items.cx_core.ProductImageItem;
import com.coretex.items.cx_core.ProductItem;
import com.coretex.items.cx_core.ProductPriceItem;
import com.coretex.items.cx_core.SizeVariantProductItem;
import com.coretex.items.cx_core.StoreItem;
import com.coretex.items.cx_core.StyleDescriptionItem;
import com.coretex.items.cx_core.StyleVariantProductItem;
import com.coretex.items.cx_core.VariantProductItem;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;
import java.util.Map;

@Component
public class InitProductUtil {

	private static final Logger LOG = LoggerFactory.getLogger(InitProductUtil.class);

	@Resource
	protected ProductImageService productImageService;

	private static Map<String, Map<Locale, String>> colorVariants = Maps.newHashMap();

	static {
		colorVariants.put("#000000", Map.of(Locale.ENGLISH, "Black",
				LocaleUtils.toLocale("ru"), "Черний",
				LocaleUtils.toLocale("ua"), "Чорний"));

		colorVariants.put("#FF0000", Map.of(Locale.ENGLISH, "Red",
				LocaleUtils.toLocale("ru"), "Красный",
				LocaleUtils.toLocale("ua"), "Червоний"));

		colorVariants.put("#00FF00", Map.of(Locale.ENGLISH, "Green",
				LocaleUtils.toLocale("ru"), "Зеленый",
				LocaleUtils.toLocale("ua"), "Зелений"));

		colorVariants.put("#0000FF", Map.of(Locale.ENGLISH, "Blue",
				LocaleUtils.toLocale("ru"), "Синий",
				LocaleUtils.toLocale("ua"), "Синый"));

		colorVariants.put("#FFFF00", Map.of(Locale.ENGLISH, "Yellow",
				LocaleUtils.toLocale("ru"), "Желтый",
				LocaleUtils.toLocale("ua"), "Жовтий"));

		colorVariants.put("#FF00FF", Map.of(Locale.ENGLISH, "Мagenta",
				LocaleUtils.toLocale("ru"), "Малиновый",
				LocaleUtils.toLocale("ua"), "Малиновий"));

		colorVariants.put("#00FFFF", Map.of(Locale.ENGLISH, "Aqua",
				LocaleUtils.toLocale("ru"), "Голубой",
				LocaleUtils.toLocale("ua"), "Голубий"));
		colorVariants.put("#FFFFFF", Map.of(Locale.ENGLISH, "White",
				LocaleUtils.toLocale("ru"), "Белый",
				LocaleUtils.toLocale("ua"), "Білий"));
	}

	public ProductItem createBaseProduct(int index, StoreItem store) {
		var product = new ProductItem();
		product.setCode("b00" + index);

		product.setName("Test product name en #" + index, Locale.ENGLISH);
		product.setName("Тестовый продукт ru #" + index, LocaleUtils.toLocale("ru"));
		product.setName("Тестовий продукт ua #" + index, LocaleUtils.toLocale("ua"));

		product.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
				"sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
				"Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. " +
				"Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. " +
				"Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. #" + index, Locale.ENGLISH);
		product.setDescription("Текст-«рыба» — это заготовленный, скопированный или собственноручно написанный текст для экономии времени, " +
				"который вставляется в макет страницы для демонстрации его условного внешнего наполнения в процессе разработки или для тестирования шрифта. " +
				"Текст-рыба также используется для проверки передачи текста по линиям связи, тестирования программ и печатных устройств.#" + index, LocaleUtils.toLocale("ru"));
		product.setDescription("Lorem ipsum – назва класичного тексту-«риби». " +
				"«Риба» – слово з жаргону дизайнерів, що позначає умовний, часто безглуздий текст, що вставляється в макет сторінки. " +
				"Lorem ipsum являє собою спотворений уривок з філософського трактату Цицерона «Про межі добра і зла», " +
				"написаного в 45 році до нашої ери латинською мовою." +
				" Вперше цей текст був застосований для набору шрифтових зразків невідомим друкарем у XVI столітті. #" + index, LocaleUtils.toLocale("ua"));

		product.setStore(store);
		product.setAvailable(true);
		var variants = RandomUtils.nextInt(1, colorVariants.size());
		for (int i = 0; i < variants; i++) {
			product.getVariants().add(creteStyleVariant(i, store, product.getCode()));
		}
		return product;
	}

	public VariantProductItem creteStyleVariant(int index, StoreItem store, String codePrefix) {
		var product = new StyleVariantProductItem();
		var entries = Lists.newArrayList(colorVariants.entrySet());
		var stringMapEntry = entries.get(index);
		product.setCode(codePrefix + "-c00" + index);
		product.setStore(store);
		product.setAvailable(true);

		var styleDescription = new StyleDescriptionItem();
		styleDescription.setCssColorCode(stringMapEntry.getKey());
		stringMapEntry.getValue().forEach((locale, name) -> {
			styleDescription.setStyleName(name, locale);
		});

		product.setStyle(styleDescription);

		var variants = RandomUtils.nextInt(1, 6);
		for (int i = 0; i < variants; i++) {
			product.getVariants().add(creteSizeVariant(i, store, product.getCode(), stringMapEntry));
		}

		try {
			ClassPathResource classPathResource = new ClassPathResource("/demo/product-9-1-600x800_" + stringMapEntry.getKey() + ".jpeg");
			InputStream inStream = classPathResource.getInputStream();
			saveFile(inStream, "pImage_" + product.getCode() + "_" + stringMapEntry.getKey().replaceAll("#", "") + "_1.jpg", product);

			ClassPathResource classPathResource2 = new ClassPathResource("/demo/product-9-2-600x800_" + stringMapEntry.getKey() + ".jpeg");
			InputStream inStream2 = classPathResource2.getInputStream();
			saveFile(inStream2, "pImage_" + product.getCode() + "_" + stringMapEntry.getKey().replaceAll("#", "") + "_2.jpg", product);
		} catch (Exception e) {
			LOG.error("Error while reading demo file spring.png", e);
		}


		return product;
	}

	public VariantProductItem creteSizeVariant(int index, StoreItem store, String codePrefix, Map.Entry<String, Map<Locale, String>> stringMapEntry) {
		var product = new SizeVariantProductItem();
		product.setCode(codePrefix + "-s00" + index);
		product.setStore(store);
		product.setAvailable(true);
		product.setSize("Size #" + index + 10);

		// Availability
		ProductAvailabilityItem availability = new ProductAvailabilityItem();
		availability.setProductQuantity(RandomUtils.nextInt(0, 10) + 10 * index);
		availability.setRegion("*");
		availability.setProduct(product);// associate with product


		ProductPriceItem dprice = new ProductPriceItem();
		dprice.setCode("basePrice_" + product.getCode() + "_" + index);
		dprice.setDefaultPrice(true);
		dprice.setProductPriceAmount(new BigDecimal(39.99 + 10 * index).setScale(2, RoundingMode.HALF_UP));

		if (RandomUtils.nextBoolean()) {
			dprice.setProductPriceSpecialAmount(dprice.getProductPriceAmount()
					.subtract(new BigDecimal("10.00"))
					.setScale(2, RoundingMode.HALF_UP));
		}

		dprice.setProductAvailability(availability);

		dprice.setName("Base price");

		availability.getPrices().add(dprice);
		product.getAvailabilities().add(availability);

		return product;
	}

	public void saveFile(InputStream fis, String name, ProductItem product) throws Exception {

		if (fis == null) {
			return;
		}

		final byte[] is = IOUtils.toByteArray(fis);
		final ByteArrayInputStream inputStream = new ByteArrayInputStream(is);
		final ImageContentFile cmsContentImage = new ImageContentFile();
		cmsContentImage.setFileName(name);
		cmsContentImage.setFile(inputStream);
		cmsContentImage.setFileContentType(FileContentType.PRODUCT);

		ProductImageItem productImage = new ProductImageItem();
		productImage.setProductImage(name);
		productImage.setProduct(product);
		productImage.setProductImageUrl(ProductUtils.buildProductSmallImageUtils(product.getStore(), product.getCode(), name));
		product.getImages().add(productImage);

		productImageService.addProductImage(product, productImage, cmsContentImage);
	}
}
