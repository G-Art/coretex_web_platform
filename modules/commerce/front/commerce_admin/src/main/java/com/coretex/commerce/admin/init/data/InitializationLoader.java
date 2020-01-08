package com.coretex.commerce.admin.init.data;

import com.coretex.commerce.admin.init.permission.Permissions;
import com.coretex.commerce.admin.init.permission.ShopPermission;
import com.coretex.commerce.core.services.StoreService;
import com.coretex.core.activeorm.services.ItemService;
import com.coretex.core.business.constants.Constants;
import com.coretex.core.business.constants.SystemConstants;
import com.coretex.core.business.services.merchant.MerchantStoreService;
import com.coretex.core.business.services.reference.init.InitializationDatabase;
import com.coretex.core.business.services.system.MerchantConfigurationService;
import com.coretex.core.business.services.system.SystemConfigurationService;
import com.coretex.core.business.services.user.GroupService;
import com.coretex.core.business.services.user.PermissionService;
import com.coretex.core.business.utils.CoreConfiguration;
import com.coretex.core.model.system.MerchantConfig;
import com.coretex.enums.commerce_core_model.GroupTypeEnum;
import com.coretex.items.commerce_core_model.GroupItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.PermissionItem;
import com.coretex.items.commerce_core_model.SystemConfigurationItem;
import com.coretex.items.cx_core.StoreItem;
import com.coretex.items.cx_core.UserItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Component
public class InitializationLoader {

	private static final Logger LOGGER = LoggerFactory.getLogger(InitializationLoader.class);

	private static final String DEFAULT_INITIAL_PASSWORD = "nimda";
	private static final String DEFAULT_ADMIN_INITIAL_PASSWORD = "goodmood";
	public final static String POPULATE_TEST_DATA = "POPULATE_TEST_DATA";
	public final static String TEST_DATA_LOADED = "TEST_DATA_LOADED";
	public final static String RECAPTCHA_URL = "shopizer.recapatcha_url";
	public final static String RECAPTCHA_PRIVATE_KEY = "shopizer.recapatcha_private_key";
	public final static String RECAPTCHA_PUBLIC_KEY = "shopizer.recapatcha_public_key";
	public final static String SHOP_SCHEME = "SHOP_SCHEME";
	public final static int MAX_DOWNLOAD_DAYS = 30;

	@Resource
	private MerchantConfigurationService merchantConfigurationService;


	@Resource
	private InitializationDatabase initializationDatabase;

	@Resource
	private ItemService itemService;

	@Resource
	private InitData initData;

	@Resource
	private SystemConfigurationService systemConfigurationService;

	@Resource
	@Qualifier("passwordEncoder")
	private PasswordEncoder passwordEncoder;

	@Resource
	protected PermissionService permissionService;

	@Resource
	protected GroupService groupService;

	@Resource
	private CoreConfiguration configuration;

	@Resource
	protected MerchantStoreService merchantService;

	@Resource
	private StoreService storeService;

	@Resource
	private ObjectMapper jacksonObjectMapper;

	@Resource
	private ResourceLoader resourceLoader;

//	@Resource
//	private List<DataLoader> dataLoaders;

	@PostConstruct
	public void init() {

		try {

			if (initializationDatabase.isEmpty()) {
				//InputStream in =
				//        this.getClass().getClassLoader().getResourceAsStream("/permission/permission.json");


				org.springframework.core.io.Resource permissionXML = resourceLoader.getResource("classpath:/permission/permission.json");

				InputStream xmlSource = permissionXML.getInputStream();

				//File permissionXML=resourceLoader.getResource("classpath:/permission/permission.json").getFile();
				//StreamSource xmlSource = new StreamSource(permissionXML);

				Permissions permissions = jacksonObjectMapper.readValue(xmlSource, Permissions.class);

				//All default data to be created

				LOGGER.info(String.format("%s : CoreTex database is empty, populate it....", "goodmood-shop"));

				initializationDatabase.populate("goodmood-shop");

				MerchantStoreItem store = merchantService.getByCode(Constants.DEFAULT_STORE);

				//security groups and permissions

				Map<String, GroupItem> groupMap = new HashMap<String, GroupItem>();
				if (CollectionUtils.isNotEmpty(permissions.getShopPermission())) {

					for (ShopPermission shopPermission : permissions.getShopPermission()) {

						PermissionItem permission = new PermissionItem();
						permission.setPermissionName(shopPermission.getType());

						for (String groupName : shopPermission.getShopGroup().getName()) {
							if (groupMap.get(groupName) == null) {
								GroupItem group = new GroupItem();
								group.setGroupName(groupName);
								group.setGroupType(GroupTypeEnum.ADMIN);
								groupService.create(group);
								groupMap.put(groupName, group);

								List<GroupItem> groups = permission.getGroups();
								if(Objects.isNull(groups)){
									groups = Lists.newArrayList();
								}
								groups.add(group);
								permission.setGroups(groups);

							} else {

								List<GroupItem> groups = permission.getGroups();
								if(Objects.isNull(groups)){
									groups = Lists.newArrayList();
								}
								groups.add(groupMap.get(groupName));
								permission.setGroups(groups);
							}
							permissionService.create(permission);
						}


					}
				}

				createDefaultAdmin();



				MerchantConfig config = new MerchantConfig();
				config.setAllowPurchaseItems(true);
				config.setDisplayAddToCartOnFeaturedItems(true);

				merchantConfigurationService.saveMerchantConfig(config, store);

				loadData();

//				dataLoaders.forEach(DataLoader::load);
			}

		} catch (Exception e) {
			LOGGER.error("Error in the init method", e);
		}


	}


	public void createDefaultAdmin() {

		//TODO create all groups and permissions

		MerchantStoreItem store = merchantService.getByCode(com.coretex.core.business.constants.Constants.DEFAULT_STORE);

		var s = storeService.getByCode(com.coretex.core.business.constants.Constants.DEFAULT_STORE);

		String password = passwordEncoder.encode(DEFAULT_INITIAL_PASSWORD);

		List<GroupItem> groups = groupService.listGroup(GroupTypeEnum.ADMIN);

		UserItem user = new UserItem();
		user.setLogin("admin");
		user.setPassword(password);
		user.setEmail("admin@coretex.com");
		user.setFirstName("Artem");
		user.setLastName("Herasymenko");
		user.setActive(true);

		for (GroupItem group : groups) {
			if (group.getGroupName().equals(com.coretex.commerce.admin.Constants.GROUP_SUPERADMIN)
					|| group.getGroupName().equals(com.coretex.commerce.admin.Constants.GROUP_ADMIN)) {
				user.getGroups().add(group);
			}
		}

		user.setStore(s);
		itemService.save(user);

		creteAdmins(s);

	}

	private void creteAdmins(StoreItem store) {
		org.springframework.core.io.Resource permissionXML = resourceLoader.getResource("classpath:/permission/admins.json");

		try {
			InputStream xmlSource = permissionXML.getInputStream();
			List<GroupItem> groups = groupService.listGroup(GroupTypeEnum.ADMIN);
			Map admins = jacksonObjectMapper.readValue(xmlSource, Map.class);
			List<Map> employee = (List<Map>) admins.get("employee");
			String password = passwordEncoder.encode(DEFAULT_ADMIN_INITIAL_PASSWORD);
			employee.forEach(map -> {
				UserItem user = new UserItem();
				user.setLogin((String) map.get("name"));
				user.setPassword(password);
				user.setEmail((String) map.get("email"));
				user.setFirstName((String) map.get("firstName"));
				user.setActive((Boolean) map.get("active"));
				List<String> groupList = (List<String>) map.get("groups");
				for (GroupItem group : groups) {
					if (groupList.contains(group.getGroupName())) {
						user.getGroups().add(group);
					}
				}
				user.setStore(store);
				itemService.save(user);
			});

		} catch (IOException e) {
			LOGGER.error("admins.json read exception", e);
		}

	}

	private void loadData()  {

		String loadTestData = configuration.getProperty(POPULATE_TEST_DATA);
		boolean loadData = !StringUtils.isBlank(loadTestData) && loadTestData.equals(SystemConstants.CONFIG_VALUE_TRUE);


		if (loadData) {

			SystemConfigurationItem configuration = systemConfigurationService.getByKey(TEST_DATA_LOADED);

			if (configuration != null) {
				if (configuration.getKey().equals(TEST_DATA_LOADED)) {
					if (configuration.getValue().equals(SystemConstants.CONFIG_VALUE_TRUE)) {
						return;
					}
				}
			}

			initData.initInitialData();

			configuration = new SystemConfigurationItem();
			configuration.setKey(TEST_DATA_LOADED);
			configuration.setValue(SystemConstants.CONFIG_VALUE_TRUE);
			systemConfigurationService.create(configuration);


		}
	}


}
