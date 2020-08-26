package com.coretex.commerce.admin.init.data;


import com.coretex.commerce.admin.Constants;
import com.coretex.commerce.admin.init.apparel.ApparelCategories;
import com.coretex.commerce.core.dto.FileContentType;
import com.coretex.commerce.core.dto.ImageContentFile;
import com.coretex.commerce.core.services.CategoryService;
import com.coretex.commerce.core.services.CurrencyService;
import com.coretex.commerce.core.services.CustomerService;
import com.coretex.commerce.core.services.GroupService;
import com.coretex.commerce.core.services.LocaleService;
import com.coretex.commerce.core.services.ManufacturerService;
import com.coretex.commerce.core.services.OrderService;
import com.coretex.commerce.core.services.ProductImageService;
import com.coretex.commerce.core.services.ProductService;
import com.coretex.commerce.core.services.StoreService;
import com.coretex.commerce.core.utils.ProductUtils;
import com.coretex.enums.cx_core.GroupTypeEnum;
import com.coretex.enums.cx_core.OrderStatusEnum;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.cx_core.CategoryItem;
import com.coretex.items.cx_core.CurrencyItem;
import com.coretex.items.cx_core.CustomerItem;
import com.coretex.items.cx_core.GroupItem;
import com.coretex.items.cx_core.ManufacturerItem;
import com.coretex.items.cx_core.OrderEntryItem;
import com.coretex.items.cx_core.OrderItem;
import com.coretex.items.cx_core.ProductAvailabilityItem;
import com.coretex.items.cx_core.ProductImageItem;
import com.coretex.items.cx_core.ProductItem;
import com.coretex.items.cx_core.ProductPriceItem;
import com.coretex.items.cx_core.SizeVariantProductItem;
import com.coretex.items.cx_core.StoreItem;
import com.coretex.items.cx_core.StyleDescriptionItem;
import com.coretex.items.cx_core.StyleVariantProductItem;
import com.coretex.items.cx_core.VariantProductItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import static com.coretex.commerce.core.constants.Constants.DEFAULT_STORE;

@Component
public class InitStoreData implements InitData {

	private static final Logger LOGGER = LoggerFactory.getLogger(InitStoreData.class);
	public static final String STORES = "stores";
	public static final String BASE_PRODUCT = "baseProduct";
	public static final String BASE_CODE = "baseCode";

	@Resource
	private InitProductUtil initProductUtil;

	@Resource
	private ProductService productService;

	@Resource
	private CategoryService categoryService;

	@Resource
	private LocaleService localeService;

	@Resource
	private CustomerService customerService;

	@Resource
	private ManufacturerService manufacturerService;

	@Resource
	private CurrencyService currencyService;

	@Resource
	private OrderService orderService;

	@Resource
	private PasswordEncoder passwordEncoder;

	@Resource
	private GroupService groupService;

	@Resource
	private StoreService storeService;

	@Resource
	private ResourceLoader resourceLoader;

	@Resource
	protected ProductImageService productImageService;

	@Resource
	private ObjectMapper jacksonObjectMapper;

	public void initInitialData() {
		initSampleData();
		initApparelData();

	}

	private void initApparelData() {
		LOGGER.info("Init apparel data");
		loadApparelCategories();
		loadApparelProducts();
		LOGGER.info("Apparel data loaded");
	}

	private void loadApparelProducts() {
		org.springframework.core.io.Resource apparelProducts = resourceLoader.getResource("classpath:/apparel/apparelProducts.csv");
		try (Reader reader = Files.newBufferedReader(apparelProducts.getFile().toPath())) {
			CSVParser csvParser =
					new CSVParser(
							reader,
							CSVFormat.DEFAULT
									.withFirstRecordAsHeader()
									.withIgnoreHeaderCase()
									.withTrim());
			var dataset = csvParser.getRecords();

			ProductItem product = null;
			for (CSVRecord record : dataset) {
				var stores = record.get(STORES);
				var splitedStores = stores.split(";");// tip for a future
				var baseProduct = BooleanUtils.toBoolean(record.get(BASE_PRODUCT));
				var baseCode = record.get(BASE_CODE);
				var store = storeService.getByCode(splitedStores[0]);

				if (baseProduct && StringUtils.isNotBlank(baseCode)) {
					product = getBaseProduct(baseCode, record);
					product.setStore(store);
				}
				if (Objects.nonNull(product)) {
					product.getVariants().add(creteVariant(product, record));
					productService.save(product);
				}
			}

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	private VariantProductItem creteVariant(ProductItem productBase, CSVRecord record) {
		var colorVariantCode = record.get("colorVariantCode");
		var variant = productService.getByCode(colorVariantCode);
		if (Objects.nonNull(variant) && variant instanceof StyleVariantProductItem) {
			return (VariantProductItem) variant;
		}
		variant = new StyleVariantProductItem();
		variant.setCode(colorVariantCode);
		variant.setStore(productBase.getStore());
		((StyleVariantProductItem) variant).setBaseProduct(productBase);


		var colorCode = record.get("colorCode");
		var colorName = record.get("colorName");
		var styleDescription = new StyleDescriptionItem();
		styleDescription.setStyleName(colorName);
		styleDescription.setCssColorCode(colorCode);

		var stock = record.get("stock");

		if (StringUtils.isNotBlank(stock)) {
			ProductAvailabilityItem availabilityItem;
			if (CollectionUtils.isEmpty(variant.getAvailabilities())) {
				availabilityItem = new ProductAvailabilityItem();
			} else {
				availabilityItem = IterableUtils.first(variant.getAvailabilities());
			}

			availabilityItem.setProduct(variant);
			availabilityItem.setProductQuantity(NumberUtils.toInt(stock, 0));

			var price = record.get("price");
			if (StringUtils.isNotBlank(price)) {
				ProductPriceItem productPriceItem;
				if (CollectionUtils.isEmpty(availabilityItem.getPrices())) {
					productPriceItem = new ProductPriceItem();
					productPriceItem.setCode("price_" + colorVariantCode);
					productPriceItem.setProductPriceAmount(BigDecimal.valueOf(NumberUtils.toDouble(price, 0.0d)));
					productPriceItem.setDefaultPrice(true);
					productPriceItem.setProductAvailability(availabilityItem);
					availabilityItem.getPrices().add(productPriceItem);
				}
			}

			variant.getAvailabilities().add(availabilityItem);
		}

		((StyleVariantProductItem) variant).setStyle(styleDescription);
		var images = record.get("images");

		if (StringUtils.isNotBlank(images)) {
			addImages(images.split("\\|"), (StyleVariantProductItem) variant);
		}

		variant.getVariants().add(creteSizeVariant(variant, record));

		return (VariantProductItem) variant;
	}

	private void addImages(String[] images, StyleVariantProductItem variant) {
		if (Objects.nonNull(variant.getStyle())) {
			Arrays.stream(images)
					.forEach(image -> {
						var i = ArrayUtils.indexOf(images, image);
						var name = String.format("genImage_%s_%s_%s.jpg",
								variant.getCode(),
								variant.getStyle().getCssColorCode().replaceAll("#", ""),
								i >= 0 ? i : UUID.randomUUID().toString());
						try {
							ClassPathResource classPathResource = new ClassPathResource(image);
							InputStream inStream = classPathResource.getInputStream();
							saveFile(inStream, name, variant);

						} catch (Exception e) {
							LOGGER.error(String.format("Error while reading file [%s]", image), e);
						}
					});
		}
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

	private VariantProductItem creteSizeVariant(ProductItem variant, CSVRecord record) {
		var code = record.get("code");
		VariantProductItem size = (VariantProductItem) productService.getByCode(code);
		if (Objects.isNull(size)) {
			size = new SizeVariantProductItem();
			size.setCode(code);
		}
		size.setStore(variant.getStore());
		size.setBaseProduct(variant);

		((SizeVariantProductItem) size).setSize(record.get("size"));

		return size;
	}

	private ProductItem getBaseProduct(String baseCode, CSVRecord record) {
		var baseProduct = productService.getByCode(baseCode);
		if (Objects.isNull(baseProduct)) {
			baseProduct = new ProductItem();
			baseProduct.setCategory(categoryService.findByCode(record.get("category")));
			baseProduct.setCode(baseCode);
			baseProduct.setAvailable(true);
			baseProduct.setName(record.get("name"));
			baseProduct.setDescription(record.get("description"));
			baseProduct.setPreOrder(false);
			baseProduct.setProductShippable(true);

		}
		return baseProduct;
	}

	private void loadApparelCategories() {
		LOGGER.info("Loading apparel categories...");
		org.springframework.core.io.Resource apparelCategories = resourceLoader.getResource("classpath:/apparel/apparelCategory.json");

		try {
			InputStream xmlSource = apparelCategories.getInputStream();
			ApparelCategories ac = jacksonObjectMapper.readValue(xmlSource, ApparelCategories.class);
			ac.getCategories().forEach(apCategory -> {
				var store = storeService.getByCode(apCategory.getStore());
				CategoryItem cat = new CategoryItem();
				cat.setStore(store);
				cat.setCode(apCategory.getCode());
				cat.setVisible(apCategory.getActive());

				apCategory.getName()
						.getAdditionalProperties()
						.forEach((s, o) -> cat.setName(o.toString(), LocaleUtils.toLocale(s)));
				categoryService.save(cat);

			});
			ac.getHierarchy()
					.forEach(apHierarchy -> {
						var cat = categoryService.findByCode(apHierarchy.getCategory());

						apHierarchy.getSubCategories()
								.forEach(subCatCode -> cat.getCategories().add(categoryService.findByCode(subCatCode)));
						categoryService.save(cat);

					});

			LOGGER.info("Loading apparel categories \"SUCCESS\"");
		} catch (Exception e) {
			LOGGER.error("Loading apparel categories \"FAILURE\"", e);
		}

	}

	private void initSampleData() {

		LOGGER.info("Starting the initialization of test data");
		Date date = new Date(System.currentTimeMillis());

		//2 languages by default
		LocaleItem en = localeService.getByIso("ru");

		//create a merchant
		var s = storeService.getByCode(DEFAULT_STORE);

		List<CategoryItem> allCategories = Lists.newArrayList();

		CategoryItem book = new CategoryItem();
		book.setStore(s);
		book.setCode("computerbooks");
		book.setVisible(true);

		book.setName("Computer Books");

		book.setName("Livres d'informatique", Locale.FRENCH);

		categoryService.create(book);

		allCategories.add(book);

		CategoryItem novs = new CategoryItem();
		novs.setStore(s);
		novs.setCode("novels");
		novs.setVisible(false);

		novs.setName("Novels");

		novs.setName("Romans", Locale.FRENCH);

		categoryService.create(novs);
		allCategories.add(novs);

		CategoryItem tech = new CategoryItem();
		tech.setStore(s);
		tech.setCode("tech");
		tech.setVisible(true);

		tech.setName("Technology");

		tech.setName("Technologie", Locale.FRENCH);

		tech.setParent(book);
		book.getCategories().add(tech);
		categoryService.create(tech);
		allCategories.add(tech);

		CategoryItem web = new CategoryItem();
		web.setStore(s);
		web.setCode("web");
		web.setVisible(true);

		web.setName("Web");

		web.setName("Web", Locale.FRENCH);

		web.setParent(book);
		book.getCategories().add(web);
		categoryService.create(web);
		allCategories.add(web);

		CategoryItem fiction = new CategoryItem();
		fiction.setStore(s);
		fiction.setCode("fiction");
		fiction.setVisible(true);

		fiction.setName("Fiction");

		fiction.setName("Sc Fiction", Locale.FRENCH);

		fiction.setParent(novs);
		novs.getCategories().add(fiction);
		categoryService.create(fiction);
		allCategories.add(fiction);

		CategoryItem cloud = new CategoryItem();
		cloud.setStore(s);
		cloud.setCode("cloud");
		cloud.setVisible(true);

		cloud.setName("Cloud computing");

		cloud.setName("Programmation pour le cloud", Locale.FRENCH);

		cloud.setParent(tech);
		tech.getCategories().add(cloud);
		categoryService.create(cloud);
		allCategories.add(cloud);


		List<ManufacturerItem> manufacturerItemList = Lists.newArrayList();
		ManufacturerItem oreilley = new ManufacturerItem();
		oreilley.setStore(s);
		oreilley.setCode("oreilley");
		oreilley.setName("O\'Reilley");

		manufacturerService.create(oreilley);
		manufacturerItemList.add(oreilley);


		ManufacturerItem sams = new ManufacturerItem();
		sams.setStore(s);
		sams.setCode("sams");
		sams.setName("Sams");

		manufacturerService.create(sams);
		manufacturerItemList.add(sams);

		ManufacturerItem packt = new ManufacturerItem();
		packt.setStore(s);
		packt.setCode("packt");
		packt.setName("Packt");

		manufacturerService.create(packt);
		manufacturerItemList.add(packt);

		ManufacturerItem manning = new ManufacturerItem();
		manning.setStore(s);
		manning.setCode("manning");
		manning.setName("Manning");

		manufacturerService.create(manning);
		manufacturerItemList.add(manning);

		ManufacturerItem novells = new ManufacturerItem();
		novells.setStore(s);
		novells.setCode("novells");
		novells.setName("Novells publishing");

		manufacturerService.create(novells);
		manufacturerItemList.add(novells);

		var productItem = generateTestProducts(300, manufacturerItemList, allCategories, s, date);


		//Create a customer (user name[nick] :: password)

		CustomerItem customer = new CustomerItem();
		customer.setStore(s);
		customer.setEmail("test@shopizer.com");
		customer.setFirstName("Leonardo");
		customer.setLastName("DiCaprio");
		customer.setAnonymous(false);
		customer.setDateOfBirth(new Date());

		customer.setDefaultLanguage(en);

		String password = passwordEncoder.encode("password");
		customer.setPassword(password);

		List<GroupItem> groups = groupService.listGroup(GroupTypeEnum.CUSTOMER);


		for (GroupItem group : groups) {
			if (group.getGroupName().equals(Constants.GROUP_CUSTOMER)) {
				customer.getGroups().add(group);
			}
		}
		customerService.create(customer);

		CurrencyItem currency = currencyService.getByCode("UAH");


		//create an order

		OrderItem order = new OrderItem();
		order.setDatePurchased(new Date());
		order.setCurrency(currency);
		order.setLastModified(new Date());
		order.setCustomer(customer);
		order.setStore(s);
		order.setOrderDateFinished(new Date());//committed date


		order.setStatus(OrderStatusEnum.DELIVERED);
		order.setTotal(new BigDecimal(23.99));


		//OrderProductItem
		OrderEntryItem oproduct = new OrderEntryItem();
		oproduct.setOrder(order);
		oproduct.setPrice(new BigDecimal(19.99));
		oproduct.setCalculated(false);
		oproduct.setQuantity(2);
		oproduct.setProduct(productItem.getVariants().iterator().next().getVariants().iterator().next());

		order.getEntries().add(oproduct);

		orderService.create(order);

		LOGGER.info("Ending the initialization of test data");

	}

	private ProductItem generateTestProducts(int count, List<ManufacturerItem> manufacturerItemList, List<CategoryItem> allCategories, StoreItem store, Date date) {

		int r = RandomUtils.nextInt(0, count);
		ProductItem randomProduct = null;
		for (int i = 0; i < count; i++) {
			// Gen test product
			var product = initProductUtil.createBaseProduct(i, store);

			product.setManufacturer(manufacturerItemList.get(RandomUtils.nextInt(0, manufacturerItemList.size())));

			product.setCategory(allCategories.get(RandomUtils.nextInt(0, allCategories.size())));

			productService.create(product);

			if (r == i) {
				randomProduct = product;
			}
		}

		return randomProduct;
	}

}
