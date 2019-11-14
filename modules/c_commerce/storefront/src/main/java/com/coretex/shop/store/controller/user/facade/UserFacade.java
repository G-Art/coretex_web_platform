package com.coretex.shop.store.controller.user.facade;

import java.util.List;
import java.util.UUID;

import com.coretex.core.model.common.Criteria;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.shop.model.security.ReadablePermission;
import com.coretex.shop.model.user.PersistableUser;
import com.coretex.shop.model.user.ReadableUser;
import com.coretex.shop.model.user.ReadableUserList;

/**
 * Access to all methods for managing users
 *
 * @author carlsamson
 */
public interface UserFacade {

	/**
	 * Finds a UserItem by userName
	 *
	 * @return
	 * @throws Exception
	 */
	ReadableUser findByUserName(String userName, LanguageItem lang);

	/**
	 * Creates a UserItem
	 *
	 * @param user
	 * @param store
	 */
	void create(PersistableUser user, MerchantStoreItem store);

	/**
	 * List permissions by group
	 *
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	List<ReadablePermission> findPermissionsByGroups(List<UUID> ids);

	/**
	 * Determines if a user is authorized to perform an action on a specific store
	 *
	 * @param userName
	 * @param merchantStoreCode
	 * @return
	 * @throws Exception
	 */
	boolean authorizedStore(String userName, String merchantStoreCode);

	/**
	 * Determines if a user is in a specific group
	 *
	 * @param userName
	 * @param groupName
	 */
	void authorizedGroup(String userName, List<String> groupNames);

	/**
	 * Retrieve authenticated user
	 *
	 * @return
	 */
	String authenticatedUser();

	/**
	 * Get by criteria
	 *
	 * @param criteria
	 * @return
	 */
	ReadableUserList getByCriteria(LanguageItem language, String draw, Criteria criteria);

	/**
	 * Delete user
	 *
	 * @param userName
	 */
	void delete(String userName);

	/**
	 * Update UserItem
	 *
	 * @param user
	 */
	ReadableUser update(String authenticatedUser, String storeCode, PersistableUser user);


}
