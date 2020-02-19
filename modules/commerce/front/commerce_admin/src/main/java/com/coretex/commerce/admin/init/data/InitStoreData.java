package com.coretex.commerce.admin.init.data;


import com.coretex.commerce.admin.Constants;
import com.coretex.commerce.core.services.CategoryService;
import com.coretex.commerce.core.services.CountryService;
import com.coretex.commerce.core.services.CurrencyService;
import com.coretex.commerce.core.services.CustomerService;
import com.coretex.commerce.core.services.GroupService;
import com.coretex.commerce.core.services.LocaleService;
import com.coretex.commerce.core.services.ManufacturerService;
import com.coretex.commerce.core.services.OrderService;
import com.coretex.commerce.core.services.ProductImageService;
import com.coretex.commerce.core.services.ProductService;
import com.coretex.commerce.core.services.StoreService;
import com.coretex.commerce.core.services.ZoneService;
import com.coretex.enums.cx_core.GroupTypeEnum;
import com.coretex.enums.cx_core.OrderStatusEnum;
import com.coretex.items.core.CountryItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.cx_core.CategoryItem;
import com.coretex.items.cx_core.CurrencyItem;
import com.coretex.items.cx_core.CustomerItem;
import com.coretex.items.cx_core.GroupItem;
import com.coretex.items.cx_core.ManufacturerItem;
import com.coretex.items.cx_core.OrderEntryItem;
import com.coretex.items.cx_core.OrderItem;
import com.coretex.items.cx_core.ProductItem;
import com.coretex.items.cx_core.StoreItem;
import com.coretex.items.cx_core.ZoneItem;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.coretex.commerce.core.constants.Constants.DEFAULT_STORE;

@Component
public class InitStoreData implements InitData {

	private static final Logger LOGGER = LoggerFactory.getLogger(InitStoreData.class);

	@Resource
	private InitProductUtil initProductUtil;

	@Resource
	private ProductService productService;

	@Resource
	private ProductImageService productImageService;

	@Resource
	private CategoryService categoryService;

	@Resource
	private LocaleService localeService;

	@Resource
	private CountryService countryService;

	@Resource
	private ZoneService zoneService;

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

	public void initInitialData() {


		LOGGER.info("Starting the initialization of test data");
		Date date = new Date(System.currentTimeMillis());

		//2 languages by default
		LocaleItem en = localeService.getByIso("ru");

		CountryItem canada = countryService.getByCode("UA");
		ZoneItem zone = zoneService.getByCode("Kyiv_R");

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

		CategoryItem business = new CategoryItem();
		business.setStore(s);
		business.setCode("business");
		business.setVisible(true);

		business.setName("Business");

		business.setName("Affaires", Locale.FRENCH);

		categoryService.create(business);
		allCategories.add(business);


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



		//Create a customer (user name[nick] : shopizer password : password)

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

		int r  = RandomUtils.nextInt(0, count);
		ProductItem randomProduct = null;
		for (int i = 0; i < count; i++) {
			// Gen test product
			var product = initProductUtil.createBaseProduct(i, store);

			product.setManufacturer(manufacturerItemList.get(RandomUtils.nextInt(0, manufacturerItemList.size())));

			product.getCategories().add(allCategories.get(RandomUtils.nextInt(0, allCategories.size())));

			productService.create(product);

			if(r == i){
				randomProduct = product;
			}
		}

		return randomProduct;
	}

}
