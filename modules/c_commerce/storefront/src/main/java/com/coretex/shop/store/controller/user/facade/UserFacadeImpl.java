package com.coretex.shop.store.controller.user.facade;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Resource;

import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.GroupItem;
import com.coretex.items.commerce_core_model.PermissionItem;
import com.coretex.items.commerce_core_model.UserItem;
import org.apache.commons.lang3.Validate;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.services.merchant.MerchantStoreService;
import com.coretex.core.business.services.reference.language.LanguageService;
import com.coretex.core.business.services.user.PermissionService;
import com.coretex.core.business.services.user.UserService;
import com.coretex.items.core.LocaleItem;
import com.coretex.shop.constants.Constants;
import com.coretex.shop.model.security.ReadableGroup;
import com.coretex.shop.model.security.ReadablePermission;
import com.coretex.shop.model.user.PersistableUser;
import com.coretex.shop.model.user.ReadableUser;
import com.coretex.shop.populator.user.PersistableUserPopulator;
import com.coretex.shop.populator.user.ReadableUserPopulator;
import com.coretex.shop.store.api.exception.ConversionRuntimeException;
import com.coretex.shop.store.api.exception.ResourceNotFoundException;
import com.coretex.shop.store.api.exception.ServiceRuntimeException;

@Service("userFacade")
public class UserFacadeImpl implements UserFacade {

	@Resource
	private MerchantStoreService merchantStoreService;

	@Resource
	private UserService userService;

	@Resource
	private PermissionService permissionService;

	@Resource
	private LanguageService languageService;

	@Resource
	private PersistableUserPopulator persistableUserPopulator;

	@Override
	public ReadableUser findByUserName(String userName, LocaleItem lang) {
		UserItem user = getByUserName(userName);
		if (user == null) {
			throw new ResourceNotFoundException("UserItem [" + userName + "] not found");
		}
		return convertUserToReadableUser(lang, user);
	}

	private ReadableUser convertUserToReadableUser(LocaleItem lang, UserItem user) {
		ReadableUserPopulator populator = new ReadableUserPopulator();
		try {
			ReadableUser readableUser = new ReadableUser();
			readableUser = populator.populate(user, readableUser, user.getMerchantStore(), lang);


			List<UUID> groupIds = readableUser.getGroups().stream().map(ReadableGroup::getId).collect(Collectors.toList());
			List<ReadablePermission> permissions = findPermissionsByGroups(groupIds);
			readableUser.setPermissions(permissions);

			return readableUser;
		} catch (ConversionException e) {
			throw new ConversionRuntimeException(e);
		}
	}

	private UserItem converPersistabletUserToUser(MerchantStoreItem store, LocaleItem lang,
												  UserItem userModel, PersistableUser user) {
		try {
			return persistableUserPopulator.populate(user, userModel, store, lang);
		} catch (ConversionException e) {
			throw new ConversionRuntimeException(e);
		}
	}

	private UserItem getByUserName(String userName) {
		return userService.getByUserName(userName);
	}

	/*
	 * @Override public ReadableUser findByUserNameWithPermissions(String userName, LocaleItem lang) {
	 * ReadableUser readableUser = findByUserName(userName, lang);
	 *
	 */

	/**
	 * Add permissions on top of the groups
	 *
	 * @param ids
	 *//*
	 * //List<Integer> groupIds = readableUser.getGroups().stream().map(ReadableGroup::getUuid) //
	 * .map(Long::intValue).collect(Collectors.toList());
	 *
	 * //List<ReadablePermission> permissions = findPermissionsByGroups(groupIds);
	 * //readableUser.setPermissions(permissions);
	 *
	 * return readableUser; }
	 */
	@Override
	public List<ReadablePermission> findPermissionsByGroups(List<UUID> ids) {
		return getPermissionsByIds(ids).stream()
				.map(this::convertPermissionToReadablePermission)
				.collect(Collectors.toList());
	}

	private ReadablePermission convertPermissionToReadablePermission(PermissionItem permission) {
		ReadablePermission readablePermission = new ReadablePermission();
		readablePermission.setUuid(permission.getUuid());
		readablePermission.setName(permission.getPermissionName());
		return readablePermission;
	}

	private List<PermissionItem> getPermissionsByIds(List<UUID> ids) {
		try {
			return permissionService.getPermissions(ids);
		} catch (ServiceException e) {
			throw new ServiceRuntimeException(e);
		}
	}

	@Override
	public boolean authorizedStore(String userName, String merchantStoreCode) {

		try {
			ReadableUser readableUser = findByUserName(userName, languageService.defaultLanguage());

			// unless superadmin
			for (ReadableGroup group : readableUser.getGroups()) {
				if (Constants.GROUP_SUPERADMIN.equals(group.getName())) {
					return true;
				}
			}


			boolean authorized = false;
			UserItem user = userService.findByStore(readableUser.getUuid(), merchantStoreCode);
			if (user != null) {
				authorized = true;
			}

			return authorized;
		} catch (Exception e) {
			throw new ServiceRuntimeException(
					"Cannot authorize user " + userName + " for store " + merchantStoreCode, e.getMessage());
		}
	}


	@Override
	public void authorizedGroup(String userName, List<String> groupName) {

		ReadableUser readableUser = findByUserName(userName, languageService.defaultLanguage());

		// unless superadmin
		for (ReadableGroup group : readableUser.getGroups()) {
			if (groupName.contains(group.getName())) {
				return;
			}
		}

		throw new ServiceRuntimeException("UserItem " + userName + " not authorized");

	}

	@Override
	public String authenticatedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			String currentUserName = authentication.getName();
			return currentUserName;
		}
		return null;
	}

	@Override
	public void create(PersistableUser user, MerchantStoreItem store) {
		UserItem userModel = new UserItem();
		userModel = converPersistabletUserToUser(store, languageService.defaultLanguage(), userModel, user);
		if (CollectionUtils.isEmpty(userModel.getGroups())) {
			throw new ServiceRuntimeException(
					"No valid group groups associated with user " + user.getUserName());
		}
		userService.save(userModel);
	}

	@Override
	public void delete(String userName) {
		Validate.notNull(userName, "Username cannot be null");

		UserItem user = userService.getByUserName(userName);
		if (user == null) {
			throw new ServiceRuntimeException("Cannot find user [" + userName + "]");
		}

		//cannot delete superadmin
		if (user.getGroups().contains(Constants.GROUP_SUPERADMIN)) {
			throw new ServiceRuntimeException("Cannot delete superadmin user [" + userName + "]");
		}

		userService.delete(user);

	}

	@Override
	public ReadableUser update(String authenticateUser, String storeCode, PersistableUser user) {
		Validate.notNull(user, "UserItem cannot be null");

		UserItem userModel = userService.getByUserName(user.getUserName());
		if (userModel == null) {
			throw new ServiceRuntimeException("Cannot find user [" + user.getUserName() + "]");
		}
		UserItem auth = userService.getByUserName(authenticateUser);
		if (auth == null) {
			throw new ServiceRuntimeException("Cannot find user [" + authenticateUser + "]");
		}
		boolean isActive = userModel.getActive();
		List<GroupItem> originalGroups = userModel.getGroups();
		GroupItem superadmin = originalGroups.stream()
				.filter(group -> Constants.GROUP_SUPERADMIN.equals(group.getGroupName()))
				.findAny()
				.orElse(null);
		MerchantStoreItem store = merchantStoreService.getByCode(storeCode);
		userModel = converPersistabletUserToUser(store, languageService.defaultLanguage(), userModel, user);

		//if superadmin set original permissions, prevent removing super admin
		if (superadmin != null) {
			userModel.setGroups(originalGroups);
		}

		GroupItem adminGroup = auth.getGroups().stream()
				.filter((group) -> Constants.GROUP_SUPERADMIN.equals(group.getGroupName()) || Constants.GROUP_SUPERADMIN.equals(group.getGroupName()))
				.findAny()
				.orElse(null);

		if (adminGroup == null) {
			userModel.setGroups(originalGroups);
			userModel.setActive(isActive);
		}


		user.setPassword(userModel.getPassword());
		userService.save(userModel);
		return this.convertUserToReadableUser(languageService.defaultLanguage(), userModel);

	}

}
