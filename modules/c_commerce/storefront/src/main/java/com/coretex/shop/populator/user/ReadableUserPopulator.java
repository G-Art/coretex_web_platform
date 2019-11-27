package com.coretex.shop.populator.user;

import com.coretex.items.core.LocaleItem;
import com.coretex.items.commerce_core_model.GroupItem;
import com.coretex.items.commerce_core_model.UserItem;
import org.apache.commons.lang3.Validate;
import com.coretex.core.business.constants.Constants;
import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.shop.model.security.ReadableGroup;
import com.coretex.shop.model.user.ReadableUser;
import com.coretex.shop.utils.DateUtil;

/**
 * Converts user model to readable user
 *
 * @author carlsamson
 */
public class ReadableUserPopulator extends AbstractDataPopulator<UserItem, ReadableUser> {

	@Override
	public ReadableUser populate(UserItem source, ReadableUser target, MerchantStoreItem store,
								 LocaleItem language) throws ConversionException {
		Validate.notNull(source, "UserItem cannot be null");

		if (target == null) {
			target = new ReadableUser();
		}

		target.setFirstName(source.getFirstName());
		target.setLastName(source.getLastName());
		target.setEmailAddress(source.getEmail());
		target.setUserName(source.getAdminName());
		target.setActive(source.getActive());

		if (source.getLastAccess() != null) {
			target.setLastAccess(DateUtil.formatLongDate(source.getLastAccess()));
		}

		// set default language
		target.setDefaultLanguage(Constants.DEFAULT_LANGUAGE);

		if (source.getLanguage() != null)
			target.setDefaultLanguage(source.getLanguage().getIso());
		target.setMerchant(store.getCode());
		target.setUuid(source.getUuid());


		for (GroupItem group : source.getGroups()) {

			ReadableGroup g = new ReadableGroup();
			g.setName(group.getGroupName());
			g.setId(group.getUuid());
			target.getGroups().add(g);
		}

		/**
		 * dates DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm a z");
		 * myObjectMapper.setDateFormat(df);
		 */


		return target;
	}

	@Override
	protected ReadableUser createTarget() {
		// TODO Auto-generated method stub
		return null;
	}

}
