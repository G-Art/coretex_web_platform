package com.coretex.shop.populator.user;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;

import com.coretex.items.core.LocaleItem;
import com.coretex.items.commerce_core_model.GroupItem;
import com.coretex.items.commerce_core_model.UserItem;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.coretex.core.business.exception.ConversionException;

import com.coretex.core.business.services.reference.language.LanguageService;
import com.coretex.core.business.services.user.GroupService;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.shop.model.security.PersistableGroup;
import com.coretex.shop.model.user.PersistableUser;


@Component
public class PersistableUserPopulator extends AbstractDataPopulator<PersistableUser, UserItem> {

	@Resource
	private LanguageService languageService;

	@Resource
	private GroupService groupService;

	@Resource
	@Qualifier("passwordEncoder")
	private PasswordEncoder passwordEncoder;

	@Override
	public UserItem populate(PersistableUser source, UserItem target, MerchantStoreItem store, LocaleItem language)
			throws ConversionException {
		Validate.notNull(source, "PersistableUser cannot be null");
		Validate.notNull(store, "MerchantStoreItem cannot be null");

		if (target == null) {
			target = new UserItem();
		}

		target.setFirstName(source.getFirstName());
		target.setLastName(source.getLastName());
		target.setEmail(source.getEmailAddress());
		target.setAdminName(source.getUserName());
		if (!StringUtils.isBlank(source.getPassword())) {
			target.setPassword(passwordEncoder.encode(source.getPassword()));
		}
		target.setActive(source.isActive());

		LocaleItem lang = null;
		try {
			lang = languageService.getByCode(source.getDefaultLanguage());
		} catch (Exception e) {
			throw new ConversionException("Cannot get language [" + source.getDefaultLanguage() + "]", e);
		}

		// set default language
		target.setLanguage(lang);

		target.setMerchantStore(store);

		List<GroupItem> userGroups = new ArrayList<GroupItem>();
		for (PersistableGroup group : source.getGroups()) {

			GroupItem g = null;
			g = groupService.findByName(group.getName());
			if (g == null) {
				throw new ConversionException("Cannot find group [" + group.getName() + "]");
			}
			userGroups.add(g);
		}
		target.setGroups(userGroups);


		return target;
	}

	@Override
	protected UserItem createTarget() {
		// TODO Auto-generated method stub
		return null;
	}

}
