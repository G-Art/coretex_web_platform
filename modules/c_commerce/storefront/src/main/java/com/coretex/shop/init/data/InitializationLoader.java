package com.coretex.shop.init.data;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import com.coretex.core.business.constants.Constants;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.GroupItem;
import com.coretex.items.commerce_core_model.PermissionItem;
import com.coretex.shop.init.loader.DataLoader;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.coretex.core.business.constants.SystemConstants;

import com.coretex.core.business.services.merchant.MerchantStoreService;
import com.coretex.core.business.services.reference.init.InitializationDatabase;
import com.coretex.core.business.services.system.MerchantConfigurationService;
import com.coretex.core.business.services.system.SystemConfigurationService;
import com.coretex.core.business.services.user.GroupService;
import com.coretex.core.business.services.user.PermissionService;
import com.coretex.core.business.utils.CoreConfiguration;
import com.coretex.core.model.system.MerchantConfig;
import com.coretex.items.commerce_core_model.SystemConfigurationItem;
import com.coretex.enums.commerce_core_model.GroupTypeEnum;
import com.coretex.shop.admin.model.permission.Permissions;
import com.coretex.shop.admin.model.permission.ShopPermission;
import com.coretex.shop.admin.security.WebUserServices;
import com.coretex.shop.constants.ApplicationConstants;


@Component
public class InitializationLoader {

	private static final Logger LOGGER = LoggerFactory.getLogger(InitializationLoader.class);


	@Resource
	private MerchantConfigurationService merchantConfigurationService;


	@Resource
	private InitializationDatabase initializationDatabase;

	@Resource
	private InitData initData;

	@Resource
	private SystemConfigurationService systemConfigurationService;

	@Resource
	private WebUserServices userDetailsService;

	@Resource
	protected PermissionService permissionService;

	@Resource
	protected GroupService groupService;

	@Resource
	private CoreConfiguration configuration;

	@Resource
	protected MerchantStoreService merchantService;

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

				userDetailsService.createDefaultAdmin();



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

	private void loadData()  {

		String loadTestData = configuration.getProperty(ApplicationConstants.POPULATE_TEST_DATA);
		boolean loadData = !StringUtils.isBlank(loadTestData) && loadTestData.equals(SystemConstants.CONFIG_VALUE_TRUE);


		if (loadData) {

			SystemConfigurationItem configuration = systemConfigurationService.getByKey(ApplicationConstants.TEST_DATA_LOADED);

			if (configuration != null) {
				if (configuration.getKey().equals(ApplicationConstants.TEST_DATA_LOADED)) {
					if (configuration.getValue().equals(SystemConstants.CONFIG_VALUE_TRUE)) {
						return;
					}
				}
			}

			initData.initInitialData();

			configuration = new SystemConfigurationItem();
			configuration.setKey(ApplicationConstants.TEST_DATA_LOADED);
			configuration.setValue(SystemConstants.CONFIG_VALUE_TRUE);
			systemConfigurationService.create(configuration);


		}
	}


}
