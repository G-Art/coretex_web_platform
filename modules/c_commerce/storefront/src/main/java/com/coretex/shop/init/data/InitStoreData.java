package com.coretex.shop.init.data;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.services.catalog.category.CategoryService;
import com.coretex.core.business.services.catalog.product.ProductService;
import com.coretex.core.business.services.catalog.product.attribute.ProductAttributeService;
import com.coretex.core.business.services.catalog.product.attribute.ProductOptionService;
import com.coretex.core.business.services.catalog.product.attribute.ProductOptionValueService;
import com.coretex.core.business.services.catalog.product.availability.ProductAvailabilityService;
import com.coretex.core.business.services.catalog.product.image.ProductImageService;
import com.coretex.core.business.services.catalog.product.manufacturer.ManufacturerService;
import com.coretex.core.business.services.catalog.product.price.ProductPriceService;
import com.coretex.core.business.services.catalog.product.relationship.ProductRelationshipService;
import com.coretex.core.business.services.catalog.product.type.ProductTypeService;
import com.coretex.core.business.services.customer.CustomerService;
import com.coretex.core.business.services.merchant.MerchantStoreService;
import com.coretex.core.business.services.order.OrderService;
import com.coretex.core.business.services.reference.country.CountryService;
import com.coretex.core.business.services.reference.currency.CurrencyService;
import com.coretex.core.business.services.reference.language.LanguageService;
import com.coretex.core.business.services.reference.zone.ZoneService;
import com.coretex.core.business.services.user.GroupService;
import com.coretex.core.model.catalog.product.relationship.ProductRelationshipType;
import com.coretex.core.model.content.FileContentType;
import com.coretex.core.model.content.ImageContentFile;
import com.coretex.enums.commerce_core_model.CustomerGenderEnum;
import com.coretex.enums.commerce_core_model.GroupTypeEnum;
import com.coretex.enums.commerce_core_model.OrderStatusEnum;
import com.coretex.enums.commerce_core_model.PaymentTypeEnum;
import com.coretex.items.commerce_core_model.BillingItem;
import com.coretex.items.commerce_core_model.CategoryItem;
import com.coretex.items.core.CountryItem;
import com.coretex.items.commerce_core_model.CurrencyItem;
import com.coretex.items.commerce_core_model.CustomerItem;
import com.coretex.items.commerce_core_model.DeliveryItem;
import com.coretex.items.commerce_core_model.GroupItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.items.commerce_core_model.ManufacturerItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.OrderItem;
import com.coretex.items.commerce_core_model.OrderProductDownloadItem;
import com.coretex.items.commerce_core_model.OrderProductItem;
import com.coretex.items.commerce_core_model.OrderProductPriceItem;
import com.coretex.items.commerce_core_model.OrderStatusHistoryItem;
import com.coretex.items.commerce_core_model.OrderTotalItem;
import com.coretex.items.commerce_core_model.ProductAvailabilityItem;
import com.coretex.items.commerce_core_model.ProductImageItem;
import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.commerce_core_model.ProductPriceItem;
import com.coretex.items.commerce_core_model.ProductRelationshipItem;
import com.coretex.items.commerce_core_model.ProductTypeItem;
import com.coretex.items.commerce_core_model.ZoneItem;
import com.coretex.shop.constants.Constants;
import com.coretex.shop.utils.LocaleUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.coretex.core.business.constants.Constants.DEFAULT_STORE;
import static com.coretex.core.business.constants.Constants.GENERAL_TYPE;

@Component
public class InitStoreData implements InitData {

	private static final Logger LOGGER = LoggerFactory.getLogger(InitStoreData.class);

	@Resource
	protected ProductService productService;

	@Resource
	protected ProductAttributeService productAttributeService;

	@Resource
	protected ProductImageService productImageService;

	@Resource
	protected CategoryService categoryService;

	@Resource
	protected MerchantStoreService merchantService;

	@Resource
	protected ProductTypeService productTypeService;

	@Resource
	protected LanguageService languageService;

	@Resource
	protected CountryService countryService;

	@Resource
	protected ZoneService zoneService;

	@Resource
	protected CustomerService customerService;

	@Resource
	protected ManufacturerService manufacturerService;

	@Resource
	protected CurrencyService currencyService;

	@Resource
	protected OrderService orderService;

	@Resource
	private PasswordEncoder passwordEncoder;

	@Resource
	protected GroupService groupService;

	@Resource
	private ProductRelationshipService productRelationshipService;

	public void initInitialData() throws ServiceException {


		LOGGER.info("Starting the initialization of test data");
		Date date = new Date(System.currentTimeMillis());

		//2 languages by default
		LanguageItem en = languageService.getByCode("ru");
		LanguageItem fr = languageService.getByCode("fr");

		CountryItem canada = countryService.getByCode("UA");
		ZoneItem zone = zoneService.getByCode("Kyiv_R");

		//create a merchant
		MerchantStoreItem store = merchantService.getByCode(DEFAULT_STORE);
		ProductTypeItem generalType = productTypeService.getProductType(GENERAL_TYPE);


		CategoryItem book = new CategoryItem();
		book.setMerchantStore(store);
		book.setCode("computerbooks");
		book.setVisible(true);

		book.setName("Computer Books");
		book.setSeUrl("computer-books");

		book.setName("Livres d'informatique", Locale.FRENCH);

		categoryService.create(book);

		CategoryItem novs = new CategoryItem();
		novs.setMerchantStore(store);
		novs.setCode("novels");
		novs.setVisible(false);

		novs.setName("Novels");
		novs.setSeUrl("novels");

		novs.setName("Romans", Locale.FRENCH);

		categoryService.create(novs);

		CategoryItem tech = new CategoryItem();
		tech.setMerchantStore(store);
		tech.setCode("tech");
		tech.setVisible(true);

		tech.setName("Technology");
		tech.setSeUrl("technology");

		tech.setName("Technologie", Locale.FRENCH);

		tech.setParent(book);

		categoryService.create(tech);
		categoryService.addChild(book, tech);

		CategoryItem web = new CategoryItem();
		web.setMerchantStore(store);
		web.setCode("web");
		web.setVisible(true);

		web.setName("Web");
		web.setSeUrl("the-web");

		web.setName("Web", Locale.FRENCH);

		web.setParent(book);

		categoryService.create(web);
		categoryService.addChild(book, web);


		CategoryItem fiction = new CategoryItem();
		fiction.setMerchantStore(store);
		fiction.setCode("fiction");
		fiction.setVisible(true);

		fiction.setName("Fiction");
		fiction.setSeUrl("fiction");

		fiction.setName("Sc Fiction", Locale.FRENCH);

		fiction.setParent(novs);

		categoryService.create(fiction);
		categoryService.addChild(novs, fiction);


		CategoryItem business = new CategoryItem();
		business.setMerchantStore(store);
		business.setCode("business");
		business.setVisible(true);

		business.setName("Business");
		business.setSeUrl("business");

		business.setName("Affaires", Locale.FRENCH);

		categoryService.create(business);


		CategoryItem cloud = new CategoryItem();
		cloud.setMerchantStore(store);
		cloud.setCode("cloud");
		cloud.setVisible(true);

		cloud.setName("Cloud computing");
		cloud.setSeUrl("cloud-computing");

		cloud.setName("Programmation pour le cloud", Locale.FRENCH);

		cloud.setParent(tech);

		categoryService.create(cloud);
		categoryService.addChild(tech, cloud);

		// Add products
		// ProductTypeItem generalType = productTypeService.

		ManufacturerItem oreilley = new ManufacturerItem();
		oreilley.setMerchantStore(store);
		oreilley.setCode("oreilley");
		oreilley.setName("O\'Reilley");

		manufacturerService.create(oreilley);


		ManufacturerItem sams = new ManufacturerItem();
		sams.setMerchantStore(store);
		sams.setCode("sams");
		sams.setName("Sams");

		manufacturerService.create(sams);

		ManufacturerItem packt = new ManufacturerItem();
		packt.setMerchantStore(store);
		packt.setCode("packt");
		packt.setName("Packt");

		manufacturerService.create(packt);

		ManufacturerItem manning = new ManufacturerItem();
		manning.setMerchantStore(store);
		manning.setCode("manning");
		manning.setName("Manning");

		manufacturerService.create(manning);

		ManufacturerItem novells = new ManufacturerItem();
		novells.setMerchantStore(store);
		novells.setCode("novells");
		novells.setName("Novells publishing");

		manufacturerService.create(novells);


		// PRODUCT 1

		ProductItem product = new ProductItem();
		product.setProductHeight(new BigDecimal(10));
		product.setProductLength(new BigDecimal(3));
		product.setProductWidth(new BigDecimal(6));
		product.setSku("TB12345");
		product.setManufacturer(manning);
		product.setType(generalType);
		product.setMerchantStore(store);
		product.setProductShippable(true);
		product.setAvailable(true);
		product.setProductVirtual(false);

		// Availability
		ProductAvailabilityItem availability = new ProductAvailabilityItem();
		availability.setProductDateAvailable(date);
		availability.setProductQuantity(100);
		availability.setRegion("*");
		availability.setProduct(product);// associate with product


		ProductPriceItem dprice = new ProductPriceItem();
		dprice.setCode("basePrice");
		dprice.setDefaultPrice(true);
		dprice.setProductPriceAmount(new BigDecimal(39.99));
		dprice.setProductAvailability(availability);

		dprice.setName("Base price");

		availability.getPrices().add(dprice);
		product.getAvailabilities().add(availability);

		product.setName("Spring in Action");
		product.setSeUrl("Spring-in-Action");

		product.getCategories().add(tech);
		product.getCategories().add(web);


		productService.create(product);

		try {
			ClassPathResource classPathResource = new ClassPathResource("/demo/spring.png");
			InputStream inStream = classPathResource.getInputStream();
			this.saveFile(inStream, "spring.png", product);
		} catch (Exception e) {
			LOGGER.error("Error while reading demo file spring.png", e);
		}


		// PRODUCT 2

		ProductItem product2 = new ProductItem();
		product2.setProductHeight(new BigDecimal(4));
		product2.setProductLength(new BigDecimal(3));
		product2.setProductWidth(new BigDecimal(1));
		product2.setSku("TB2468");
		product2.setManufacturer(packt);
		product2.setType(generalType);
		product2.setMerchantStore(store);
		product2.setProductShippable(true);
		product2.setAvailable(true);
		product2.setProductVirtual(false);

		product2.setName("Node Web Development");
		product2.setSeUrl("Node-Web-Development");


		product2.getCategories().add(tech);
		product2.getCategories().add(web);

		// Availability
		ProductAvailabilityItem availability2 = new ProductAvailabilityItem();
		availability2.setProductDateAvailable(date);
		availability2.setProductQuantity(100);
		availability2.setRegion("*");
		availability2.setProduct(product2);// associate with product

		ProductPriceItem dprice2 = new ProductPriceItem();
		dprice2.setCode("basePrice2");
		dprice2.setDefaultPrice(true);
		dprice2.setProductPriceAmount(new BigDecimal(29.99));
		dprice2.setProductAvailability(availability2);

		dprice2.setName("Base price");

		availability2.getPrices().add(dprice2);
		product2.getAvailabilities().add(availability2);

		productService.create(product2);

		try {
			ClassPathResource classPathResource = new ClassPathResource("/demo/node.jpg");
			InputStream inStream = classPathResource.getInputStream();
			this.saveFile(inStream, "node.jpg", product2);
		} catch (Exception e) {
			LOGGER.error("Error while reading demo file node.jpg", e);
		}


		// PRODUCT 3

		ProductItem product3 = new ProductItem();
		product3.setProductHeight(new BigDecimal(4));
		product3.setProductLength(new BigDecimal(3));
		product3.setProductWidth(new BigDecimal(1));
		product3.setSku("NB1111");
		product3.setManufacturer(oreilley);
		product3.setType(generalType);
		product3.setMerchantStore(store);
		product3.setProductShippable(true);
		product3.setAvailable(true);
		product3.setProductVirtual(false);

		product3.setName("Programming for PAAS");
		product3.setSeUrl("programming-for-paas");

		product3.getCategories().add(cloud);

		// Availability
		ProductAvailabilityItem availability3 = new ProductAvailabilityItem();
		availability3.setProductDateAvailable(date);
		availability3.setProductQuantity(100);
		availability3.setRegion("*");
		availability3.setProduct(product3);// associate with product

		ProductPriceItem dprice3 = new ProductPriceItem();
		dprice3.setCode("basePrice3");
		dprice3.setDefaultPrice(true);
		dprice3.setProductPriceAmount(new BigDecimal(19.99));
		dprice3.setProductAvailability(availability3);

		dprice3.setName("Base price");

		availability3.getPrices().add(dprice3);
		product3.getAvailabilities().add(availability3);


		productService.create(product3);


		try {
			ClassPathResource classPathResource = new ClassPathResource("/demo/paas.JPG");
			InputStream inStream = classPathResource.getInputStream();
			this.saveFile(inStream, "paas.JPG", product3);
		} catch (Exception e) {
			LOGGER.error("Error while reading demo file paas.jpg", e);
		}

		// PRODUCT 4
		ProductItem product4 = new ProductItem();
		product4.setProductHeight(new BigDecimal(4));
		product4.setProductLength(new BigDecimal(3));
		product4.setProductWidth(new BigDecimal(1));
		product4.setSku("SF333345");
		product4.setManufacturer(sams);
		product4.setType(generalType);
		product4.setMerchantStore(store);
		product4.setProductShippable(true);
		product4.setAvailable(true);
		product4.setProductVirtual(false);

		product4.setName("Android development");
		product4.setSeUrl("android-application-development");

		product4.getCategories().add(tech);

		// Availability
		ProductAvailabilityItem availability4 = new ProductAvailabilityItem();
		availability4.setProductDateAvailable(date);
		availability4.setProductQuantity(100);
		availability4.setRegion("*");
		availability4.setProduct(product4);// associate with product


		ProductPriceItem dprice4 = new ProductPriceItem();
		dprice4.setCode("basePrice4");
		dprice4.setDefaultPrice(true);
		dprice4.setProductPriceAmount(new BigDecimal(18.99));
		dprice4.setProductAvailability(availability4);

		dprice4.setName("Base price");

		availability4.getPrices().add(dprice4);
		product4.getAvailabilities().add(availability4);

		productService.create(product4);


		try {
			ClassPathResource classPathResource = new ClassPathResource("/demo/android.jpg");
			InputStream inStream = classPathResource.getInputStream();
			this.saveFile(inStream, "android.jpg", product4);
		} catch (Exception e) {
			LOGGER.error("Error while reading demo file android.jpg", e);
		}

		// PRODUCT 5
		ProductItem product5 = new ProductItem();
		product5.setProductHeight(new BigDecimal(4));
		product5.setProductLength(new BigDecimal(3));
		product5.setProductWidth(new BigDecimal(1));
		product5.setSku("SF333346");
		product5.setManufacturer(packt);
		product5.setType(generalType);
		product5.setMerchantStore(store);
		product5.setProductShippable(true);
		product5.setAvailable(true);
		product5.setProductVirtual(false);

		product5.setName("Android 3.0 Cookbook");
		product5.setSeUrl("android-3-cookbook");

		product5.getCategories().add(tech);


		// Availability
		ProductAvailabilityItem availability5 = new ProductAvailabilityItem();
		availability5.setProductDateAvailable(date);
		availability5.setProductQuantity(100);
		availability5.setRegion("*");
		availability5.setProduct(product5);// associate with product

		// productAvailabilityService.create(availability5);

		ProductPriceItem dprice5 = new ProductPriceItem();
		dprice5.setCode("basePrice5");
		dprice5.setDefaultPrice(true);
		dprice5.setProductPriceAmount(new BigDecimal(18.99));
		dprice5.setProductAvailability(availability5);

		dprice5.setName("Base price");

		availability5.getPrices().add(dprice5);
		product5.getAvailabilities().add(availability5);

		productService.create(product5);


		try {
			ClassPathResource classPathResource = new ClassPathResource("/demo/android2.jpg");
			InputStream inStream = classPathResource.getInputStream();
			this.saveFile(inStream, "android2.jpg", product5);
		} catch (Exception e) {
			LOGGER.error("Error while reading demo file android2.jpg", e);
		}

		// PRODUCT 6

		ProductItem product6 = new ProductItem();
		product6.setProductHeight(new BigDecimal(4));
		product6.setProductLength(new BigDecimal(3));
		product6.setProductWidth(new BigDecimal(1));
		product6.setSku("LL333444");
		product6.setManufacturer(novells);
		product6.setType(generalType);
		product6.setMerchantStore(store);
		product6.setProductShippable(true);
		product6.setAvailable(true);
		product5.setProductVirtual(false);

		product6.setName("The Big Switch");
		product6.setSeUrl("the-big-switch");

		product6.getCategories().add(business);

		// Availability
		ProductAvailabilityItem availability6 = new ProductAvailabilityItem();
		availability6.setProductDateAvailable(date);
		availability6.setProductQuantity(100);
		availability6.setRegion("*");
		availability6.setProduct(product6);// associate with product

		//productAvailabilityService.create(availability6);

		ProductPriceItem dprice6 = new ProductPriceItem();
		dprice6.setCode("basePrice6");
		dprice6.setDefaultPrice(true);
		dprice6.setProductPriceAmount(new BigDecimal(18.99));
		dprice6.setProductAvailability(availability6);
		dprice6.setName("Base price");

		availability6.getPrices().add(dprice6);
		product6.getAvailabilities().add(availability6);

		productService.create(product6);


		try {

			ClassPathResource classPathResource = new ClassPathResource("/demo/google.jpg");
			InputStream inStream = classPathResource.getInputStream();
			this.saveFile(inStream, "google.jpg", product6);
		} catch (Exception e) {
			LOGGER.error("Error while reading demo file google.jpg", e);
		}

		//featured items

		ProductRelationshipItem relationship = new ProductRelationshipItem();
		relationship.setActive(true);
		relationship.setCode(ProductRelationshipType.FEATURED_ITEM.name());
		relationship.setStore(store);
		relationship.setRelatedProduct(product);

		productRelationshipService.saveOrUpdate(relationship);

		relationship = new ProductRelationshipItem();
		relationship.setActive(true);
		relationship.setCode(ProductRelationshipType.FEATURED_ITEM.name());
		relationship.setStore(store);
		relationship.setRelatedProduct(product6);

		productRelationshipService.saveOrUpdate(relationship);


		relationship = new ProductRelationshipItem();
		relationship.setActive(true);
		relationship.setCode(ProductRelationshipType.FEATURED_ITEM.name());
		relationship.setStore(store);
		relationship.setRelatedProduct(product5);

		productRelationshipService.saveOrUpdate(relationship);


		relationship = new ProductRelationshipItem();
		relationship.setActive(true);
		relationship.setCode(ProductRelationshipType.FEATURED_ITEM.name());
		relationship.setStore(store);
		relationship.setRelatedProduct(product2);

		productRelationshipService.saveOrUpdate(relationship);


		//Create a customer (user name[nick] : shopizer password : password)

		CustomerItem customer = new CustomerItem();
		customer.setMerchantStore(store);
		customer.setEmail("test@shopizer.com");
		customer.setGender(CustomerGenderEnum.MALE);
		customer.setAnonymous(false);
		customer.setCompany("CSTI Consulting");
		customer.setDateOfBirth(new Date());

		customer.setDefaultLanguage(en);
		customer.setLogin("shopizer");

		String password = passwordEncoder.encode("password");
		customer.setPassword(password);

		List<GroupItem> groups = groupService.listGroup(GroupTypeEnum.CUSTOMER);


		for (GroupItem group : groups) {
			if (group.getGroupName().equals(Constants.GROUP_CUSTOMER)) {
				customer.getGroups().add(group);
			}
		}

		DeliveryItem delivery = new DeliveryItem();
		delivery.setAddress("358 Du Languadoc");
		delivery.setCity("Boucherville");
		delivery.setCountry(canada);
//		    delivery.setCountryCode(canada.getIsoCode());
		delivery.setFirstName("Leonardo");
		delivery.setLastName("DiCaprio");
		delivery.setPostalCode("J4B-8J9");
		delivery.setZone(zone);

		BillingItem billing = new BillingItem();
		billing.setAddress("358 Du Languadoc");
		billing.setCity("Boucherville");
		billing.setCompany("CSTI Consulting");
		billing.setCountry(canada);
//		    billing.setCountryCode(canada.getIsoCode());
		billing.setFirstName("Leonardo");
		billing.setLastName("DiCaprio");
		billing.setPostalCode("J4B-8J9");
		billing.setZone(zone);

		customer.setBilling(billing);
		customer.setDelivery(delivery);
		customerService.create(customer);

		CurrencyItem currency = currencyService.getByCode("UAH");

		OrderStatusHistoryItem orderStatusHistory = new OrderStatusHistoryItem();

		//create an order

		OrderItem order = new OrderItem();
		order.setDatePurchased(new Date());
		order.setCurrency(currency);
		order.setLastModified(new Date());
		order.setBilling(billing);


		order.setLocale(LocaleUtils.getLocale(store).toString());

		order.setCurrencyValue(new BigDecimal(0.98));//compared to based currency (not necessary)
		order.setCustomerId(customer.getUuid());
		order.setBilling(billing);
		order.setDelivery(delivery);
		order.setCustomerEmailAddress("leo@shopizer.com");
		order.setDelivery(delivery);
		order.setIpAddress("ipAddress");
		order.setMerchant(store);
		order.setOrderDateFinished(new Date());//committed date

		orderStatusHistory.setComments("We received your order");
		orderStatusHistory.setCustomerNotified(1);
		orderStatusHistory.setStatus(OrderStatusEnum.ORDERED);
		orderStatusHistory.setDateAdded(new Date());
		orderStatusHistory.setOrder(order);
		order.getOrderHistory().add(orderStatusHistory);


		order.setPaymentType(PaymentTypeEnum.PAYPAL);
		order.setPaymentModuleCode("paypal");
		order.setStatus(com.coretex.enums.commerce_core_model.OrderStatusEnum.DELIVERED);
		order.setTotal(new BigDecimal(23.99));


		//OrderProductDownloadItem - Digital download
		OrderProductDownloadItem orderProductDownload = new OrderProductDownloadItem();
		orderProductDownload.setDownloadCount(1);
		orderProductDownload.setMaxdays(31);
		orderProductDownload.setOrderProductFilename("Your digital file name");

		//OrderProductPriceItem
		OrderProductPriceItem oproductprice = new OrderProductPriceItem();
		oproductprice.setDefaultPrice(true);
		oproductprice.setProductPrice(new BigDecimal(19.99));
		oproductprice.setProductPriceCode("baseprice");
		oproductprice.setProductPriceName("Base Price");
		//oproductprice.setProductPriceSpecialAmount(new BigDecimal(13.99) );


		//OrderProductItem
		OrderProductItem oproduct = new OrderProductItem();
		oproduct.getDownloads().add(orderProductDownload);
		oproduct.setOneTimeCharge(new BigDecimal(19.99));
		oproduct.setOrder(order);
		oproduct.setProductName("ProductItem name");
		oproduct.setProductQuantity(1);
		oproduct.setSku("TB12345");
		oproduct.getPrices().add(oproductprice);

		oproductprice.setOrderProduct(oproduct);
		orderProductDownload.setOrderProduct(oproduct);
		order.getOrderProducts().add(oproduct);

		//OrderTotalItem
		OrderTotalItem subtotal = new OrderTotalItem();
		subtotal.setModule("summary");
		subtotal.setSortOrder(0);
		subtotal.setText("Summary");
		subtotal.setTitle("Summary");
		subtotal.setOrderTotalCode("subtotal");
		subtotal.setValue(new BigDecimal(19.99));
		subtotal.setOrder(order);

		order.getOrderTotal().add(subtotal);

		OrderTotalItem tax = new OrderTotalItem();
		tax.setModule("tax");
		tax.setSortOrder(1);
		tax.setText("Tax");
		tax.setTitle("Tax");
		tax.setOrderTotalCode("tax");
		tax.setValue(new BigDecimal(4));
		tax.setOrder(order);

		order.getOrderTotal().add(tax);

		OrderTotalItem total = new OrderTotalItem();
		total.setModule("total");
		total.setSortOrder(2);
		total.setText("Total");
		total.setTitle("Total");
		total.setOrderTotalCode("total");
		total.setValue(new BigDecimal(23.99));
		total.setOrder(order);

		order.getOrderTotal().add(total);

		orderService.create(order);

		LOGGER.info("Ending the initialization of test data");

	}

	private void saveFile(InputStream fis, String name, ProductItem product) throws Exception {

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
		productImage.setDefaultImage(true);

		productImageService.addProductImage(product, productImage, cmsContentImage);
	}


}
