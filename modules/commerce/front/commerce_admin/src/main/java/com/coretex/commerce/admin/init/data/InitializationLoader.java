package com.coretex.commerce.admin.init.data;

import com.coretex.commerce.admin.init.permission.Permissions;
import com.coretex.commerce.admin.init.permission.ShopPermission;
import com.coretex.commerce.core.constants.Constants;
import com.coretex.commerce.core.dao.ProductDao;
import com.coretex.commerce.core.services.GroupService;
import com.coretex.commerce.core.services.PermissionService;
import com.coretex.commerce.core.services.StoreService;
import com.coretex.core.activeorm.services.ItemService;
import com.coretex.enums.cx_core.GroupTypeEnum;
import com.coretex.items.cx_core.GroupItem;
import com.coretex.items.cx_core.PermissionItem;
import com.coretex.items.cx_core.StoreItem;
import com.coretex.items.cx_core.UserItem;
import com.coretex.items.cx_core.VariantProductItem;
import com.coretex.searchengine.solr.client.SolrClientService;
import com.coretex.searchengine.solr.client.TypeSolrDocumentBuildersRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.solr.common.SolrInputDocument;
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

	@Resource
	private InitializationDatabase initializationDatabase;

	@Resource
	private SolrClientService solrClientService;

	@Resource
	private ItemService itemService;

	@Resource
	private InitData initData;

	@Resource
	@Qualifier("passwordEncoder")
	private PasswordEncoder passwordEncoder;

	@Resource
	protected PermissionService permissionService;

	@Resource
	protected GroupService groupService;

	@Resource
	private StoreService storeService;

	@Resource
	private ObjectMapper jacksonObjectMapper;

	@Resource
	private ResourceLoader resourceLoader;

	@Resource
	private ProductDao productDao;

	@Resource
	private TypeSolrDocumentBuildersRegistry typeSolrDocumentBuildersRegistry;


	@PostConstruct
	public void init() {

		try {

			if (initializationDatabase.isEmpty()) {
				org.springframework.core.io.Resource permissionXML = resourceLoader.getResource("classpath:/permission/permission.json");

				InputStream xmlSource = permissionXML.getInputStream();

				Permissions permissions = jacksonObjectMapper.readValue(xmlSource, Permissions.class);

				LOGGER.info(String.format("%s : CoreTex database is empty, populate it....", "goodmood-shop"));

				initializationDatabase.populate("goodmood-shop");

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
								if (Objects.isNull(groups)) {
									groups = Lists.newArrayList();
								}
								groups.add(group);
								permission.setGroups(groups);

							} else {

								List<GroupItem> groups = permission.getGroups();
								if (Objects.isNull(groups)) {
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

				loadData();

			}
			solrClientService.execute(client -> {
				try {
					var solrInputDocumentStream = productDao.findReactive()
							.flatMap(productItem -> productItem.getVariants().stream())
							.flatMap(variantProductItem -> variantProductItem.getVariants().stream())
							.filter(Objects::nonNull)
							.filter(variantProductItem -> CollectionUtils.isEmpty(variantProductItem.getVariants()))
							.map(this::buildSolrInput);

					solrClientService.index(solrInputDocumentStream);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});

		} catch (Exception e) {
			LOGGER.error("Error in the init method", e);
		}

	}

	private SolrInputDocument buildSolrInput(VariantProductItem productItem) {

		LOGGER.info(String.format("Indexing product [code::%s]", productItem.getCode()));
		return typeSolrDocumentBuildersRegistry.findBuilderForClass(VariantProductItem.class).build(productItem);
	}


	public void createDefaultAdmin() {

		var s = storeService.getByCode(Constants.DEFAULT_STORE);

		String password = passwordEncoder.encode(DEFAULT_INITIAL_PASSWORD);

		List<GroupItem> groups = groupService.listGroup(GroupTypeEnum.ADMIN);

		UserItem user = new UserItem();
		user.setLogin("admin");
		user.setPassword(password);
		user.setEmail("admin@coretex.com");
		user.setFirstName("Admin");
		user.setLastName("Admin");
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

	private void loadData() {
		initData.initInitialData();
	}


}
