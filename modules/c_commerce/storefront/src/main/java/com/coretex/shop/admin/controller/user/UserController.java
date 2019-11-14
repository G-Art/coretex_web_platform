package com.coretex.shop.admin.controller.user;

import com.coretex.core.business.modules.email.Email;
import com.coretex.core.business.services.merchant.MerchantStoreService;
import com.coretex.core.business.services.reference.language.LanguageService;
import com.coretex.core.business.services.system.EmailService;
import com.coretex.core.business.services.user.GroupService;
import com.coretex.core.business.services.user.UserService;
import com.coretex.core.business.utils.ajax.AjaxResponse;
import com.coretex.core.data.web.Menu;
import com.coretex.enums.commerce_core_model.GroupTypeEnum;
import com.coretex.items.commerce_core_model.GroupItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.UserItem;
import com.coretex.shop.admin.controller.ControllerConstants;
import com.coretex.shop.admin.forms.UserForm;
import com.coretex.shop.admin.mapppers.UserFormMapper;
import com.coretex.shop.admin.mapppers.dto.GroupDtoMapper;
import com.coretex.shop.admin.model.secutity.Password;
import com.coretex.shop.admin.security.SecurityQuestion;
import com.coretex.shop.constants.Constants;
import com.coretex.shop.constants.EmailConstants;
import com.coretex.shop.utils.EmailUtils;
import com.coretex.shop.utils.FilePathUtils;
import com.coretex.shop.utils.LabelUtils;
import com.coretex.shop.utils.LocaleUtils;
import com.coretex.shop.utils.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class UserController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Resource
	private LanguageService languageService;

	@Resource
	private UserService userService;

	@Resource
	private GroupService groupService;

	@Resource
	private EmailService emailService;

	@Resource
	private MerchantStoreService merchantStoreService;

	@Resource
	private LabelUtils messages;

	@Resource
	private FilePathUtils filePathUtils;

	@Resource
	private EmailUtils emailUtils;

	@Resource
	@Named("passwordEncoder")
	private PasswordEncoder passwordEncoder;

	@Resource
	private UserFormMapper userFormMapper;

	@Resource
	private GroupDtoMapper groupDtoMapper;

	private final static String NEW_USER_TMPL = "email_template_new_user.ftl";

	@PreAuthorize("hasRole('STORE_ADMIN')")
	@RequestMapping(value = "/admin/users/list.html", method = RequestMethod.GET)
	public String displayUsers(Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {

		//The users are retrieved from the paging method
		setMenu(model, request);
		return ControllerConstants.Tiles.User.users;
	}

	/**
	 * Displays a list of users that can be managed by admins
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('STORE_ADMIN')")
	@RequestMapping(value = "/admin/users/paging.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> pageUsers(HttpServletRequest request,
									 HttpServletResponse response) {

		AjaxResponse resp = new AjaxResponse();
		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		String sCurrentUser = request.getRemoteUser();


		try {

			UserItem currentUser = userService.getByUserName(sCurrentUser);
			List<UserItem> users = null;
			if (UserUtils.userInGroup(currentUser, Constants.GROUP_SUPERADMIN)) {
				users = userService.listUser();
			} else {
				users = userService.listByStore(store);
			}


			for (UserItem user : users) {

				if (!UserUtils.userInGroup(user, Constants.GROUP_SUPERADMIN)) {

					if (!currentUser.equals(user.getAdminName())) {

						@SuppressWarnings("rawtypes")
						Map entry = new HashMap();
						entry.put("userId", user.getUuid().toString());
						entry.put("name", user.getFirstName() + " " + user.getLastName());
						entry.put("email", user.getEmail());
						entry.put("active", user.getActive());
						resp.addDataEntry(entry);

					}
				}
			}

			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);

		} catch (Exception e) {
			LOGGER.error("Error while paging products", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}

		String returnString = resp.toJSONString();
		return new ResponseEntity<String>(returnString, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('AUTH')")
	@RequestMapping(value = "/admin/users/password.html", method = RequestMethod.GET)
	public String displayChangePassword(Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		setMenu(model, request);
		String userName = request.getRemoteUser();
		UserItem user = userService.getByUserName(userName);

		Password password = new Password();
		password.setUser(user);

		model.addAttribute("password", password);
		model.addAttribute("user", user);
		return ControllerConstants.Tiles.User.password;
	}


	@PreAuthorize("hasRole('AUTH')")
	@RequestMapping(value = "/admin/users/savePassword.html", method = RequestMethod.POST)
	public String changePassword(@ModelAttribute("password") Password password, BindingResult result, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		setMenu(model, request);
		String userName = request.getRemoteUser();
		UserItem dbUser = userService.getByUserName(userName);


		if (!password.getUser().getUuid().equals(dbUser.getUuid())) {
			return "redirect:/admin/users/displayUser.html";
		}

		//validate password not empty
		if (StringUtils.isBlank(password.getPassword())) {
			ObjectError error = new ObjectError("password", new StringBuilder().append(messages.getMessage("label.generic.password", locale)).append(" ").append(messages.getMessage("message.cannot.empty", locale)).toString());
			result.addError(error);
			return ControllerConstants.Tiles.User.password;
		}

		if (!passwordEncoder.matches(password.getPassword(), dbUser.getPassword())) {
			ObjectError error = new ObjectError("password", messages.getMessage("message.password.invalid", locale));
			result.addError(error);
			return ControllerConstants.Tiles.User.password;
		}


		if (StringUtils.isBlank(password.getNewPassword())) {
			ObjectError error = new ObjectError("newPassword", new StringBuilder().append(messages.getMessage("label.generic.newpassword", locale)).append(" ").append(messages.getMessage("message.cannot.empty", locale)).toString());
			result.addError(error);
		}

		if (StringUtils.isBlank(password.getRepeatPassword())) {
			ObjectError error = new ObjectError("newPasswordAgain", new StringBuilder().append(messages.getMessage("label.generic.newpassword.repeat", locale)).append(" ").append(messages.getMessage("message.cannot.empty", locale)).toString());
			result.addError(error);
		}

		if (!password.getRepeatPassword().equals(password.getNewPassword())) {
			ObjectError error = new ObjectError("newPasswordAgain", messages.getMessage("message.password.different", locale));
			result.addError(error);
		}

		if (password.getNewPassword().length() < 6) {
			ObjectError error = new ObjectError("newPassword", messages.getMessage("message.password.length", locale));
			result.addError(error);
		}

		if (result.hasErrors()) {
			return ControllerConstants.Tiles.User.password;
		}


		String pass = passwordEncoder.encode(password.getNewPassword());
		dbUser.setPassword(pass);
		userService.update(dbUser);

		model.addAttribute("success", "success");
		return ControllerConstants.Tiles.User.password;
	}

	@PreAuthorize("hasRole('STORE_ADMIN')")
	@RequestMapping(value = "/admin/users/createUser.html", method = RequestMethod.GET)
	public String displayUserCreate(Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		return displayUser(null, model, request, response, locale);
	}

	@PreAuthorize("hasRole('AUTH')")
	@RequestMapping(value = "/admin/users/displayStoreUser.html", method = RequestMethod.GET)
	public String displayUserEdit(@ModelAttribute("id") String id, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {

		UserItem dbUser = userService.getById(UUID.fromString(id));

		if (dbUser == null) {
			LOGGER.info("UserItem is null for id " + id);
			return "redirect://admin/users/list.html";
		}


		return displayUser(dbUser, model, request, response, locale);

	}

	@PreAuthorize("hasRole('AUTH')")
	@RequestMapping(value = "/admin/users/displayUser.html", method = RequestMethod.GET)
	public String displayUserEdit(Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {


		String userName = request.getRemoteUser();
		UserItem user = userService.getByUserName(userName);
		return displayUser(user, model, request, response, locale);

	}

	private void populateUserObjects(UserItem user, MerchantStoreItem store, Model model, Locale locale) throws Exception {

		//get groups
		List<GroupItem> groups = new ArrayList<>();
		List<GroupItem> userGroups = groupService.listGroup(GroupTypeEnum.ADMIN);
		for (GroupItem group : userGroups) {
			if (!group.getGroupName().equals(Constants.GROUP_SUPERADMIN)) {
				groups.add(group);
			}
		}


		List<MerchantStoreItem> stores;
		//stores.add(store);
		stores = merchantStoreService.list();

		model.addAttribute("stores", stores);
		model.addAttribute("languages", store.getLanguages());
		model.addAttribute("groups", groups.stream()
				.map(groupDtoMapper::fromItem)
				.collect(Collectors.toList()));

	}


	private String displayUser(UserItem user, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {


		//display menu
		setMenu(model, request);

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);


		if (user == null) {
			user = new UserItem();
		} else {
			user.setPassword("TRANSIENT");
		}

		this.populateUserObjects(user, store, model, locale);


		model.addAttribute("user", userFormMapper.fromUserItem(user));


		return ControllerConstants.Tiles.User.profile;
	}

	@PreAuthorize("hasRole('AUTH')")
	@RequestMapping(value = "/admin/users/checkUserCode.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> checkUserCode(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String code = request.getParameter("code");
		String id = request.getParameter("id");

		AjaxResponse resp = new AjaxResponse();


		try {

			if (StringUtils.isBlank(code)) {
				resp.setStatus(AjaxResponse.CODE_ALREADY_EXIST);
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString, HttpStatus.OK);
			}

			UserItem user = userService.getByUserName(code);


			if (!StringUtils.isBlank(id) && user != null) {
				try {
					UUID lid = UUID.fromString(id);

					if (user.getAdminName().equals(code) && user.getUuid().equals(lid)) {
						resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
						String returnString = resp.toJSONString();
						return new ResponseEntity<String>(returnString, HttpStatus.OK);
					}
				} catch (Exception e) {
					resp.setStatus(AjaxResponse.CODE_ALREADY_EXIST);
					String returnString = resp.toJSONString();
					return new ResponseEntity<String>(returnString, HttpStatus.OK);
				}

			}


			if (StringUtils.isBlank(code)) {
				resp.setStatus(AjaxResponse.CODE_ALREADY_EXIST);
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString, HttpStatus.OK);
			}

			if (user != null) {
				resp.setStatus(AjaxResponse.CODE_ALREADY_EXIST);
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString, HttpStatus.OK);
			}


			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);

		} catch (Exception e) {
			LOGGER.error("Error while getting user", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}

		String returnString = resp.toJSONString();
		return new ResponseEntity<String>(returnString, HttpStatus.OK);

	}

	@PreAuthorize("hasRole('AUTH')")
	@RequestMapping(value = "/admin/users/save.html", method = RequestMethod.POST)
	public String saveUser(@Valid @ModelAttribute("user") UserForm userForm, BindingResult result, Model model, HttpServletRequest request, Locale locale) throws Exception {


		setMenu(model, request);

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		UserItem user;

		if(Objects.nonNull(userForm.getUuid())){
			user = userService.getById(userForm.getUuid());
			userFormMapper.updateToUserItem(userForm, user);
		}else{
			user = userFormMapper.toUserItem(userForm);
		}


		this.populateUserObjects(user, store, model, locale);

		LanguageItem language = user.getLanguage();

		LanguageItem l = languageService.getById(language.getUuid());

		user.setLanguage(l);

		Locale userLocale = LocaleUtils.getLocale(l);

		UserItem dbUser = null;

		//edit mode, need to get original user important information
		if (user.getUuid() != null) {
			dbUser = userService.getByUserName(user.getAdminName());
			if (dbUser == null) {
				return "redirect://admin/users/displayUser.html";
			}
		}

		List<GroupItem> submitedGroups = user.getGroups();
		Set<UUID> ids = new HashSet<>();
		for (GroupItem group : submitedGroups) {
			ids.add(group.getUuid());
		}


		GroupItem superAdmin = null;

		if (user.getUuid() != null) {
			if (!user.getUuid().equals(dbUser.getUuid())) {
				return "redirect://admin/users/displayUser.html";
			}

			List<GroupItem> groups = dbUser.getGroups();
			//boolean removeSuperAdmin = true;
			for (GroupItem group : groups) {
				//can't revoke super admin
				if (group.getGroupName().equals("SUPERADMIN")) {
					superAdmin = group;
				}
			}

		} else {

			if (user.getPassword().length() < 6) {
				ObjectError error = new ObjectError("adminPassword", messages.getMessage("message.password.length", locale));
				result.addError(error);
			}

		}

		if (superAdmin != null) {
			ids.add(superAdmin.getUuid());
		}

		if (result.hasErrors()) {
			return ControllerConstants.Tiles.User.profile;
		}

		String decodedPassword = userForm.getPassword();
		if (StringUtils.isNotBlank(decodedPassword)) {
			String encoded = passwordEncoder.encode(user.getPassword());
			user.setPassword(encoded);
		} else {

			user.setPassword(dbUser.getPassword());
		}


		if (user.getUuid() == null) {

			//save or update user
			userService.saveOrUpdate(user);

			try {

				//creation of a user, send an email
				String userName = user.getFirstName();
				if (StringUtils.isBlank(userName)) {
					userName = user.getAdminName();
				}
				String[] userNameArg = {userName};


				Map<String, String> templateTokens = emailUtils.createEmailObjectsMap(request.getContextPath(), store, messages, userLocale);
				templateTokens.put(EmailConstants.EMAIL_NEW_USER_TEXT, messages.getMessage("email.greeting", userNameArg, userLocale));
				templateTokens.put(EmailConstants.EMAIL_USER_FIRSTNAME, user.getFirstName());
				templateTokens.put(EmailConstants.EMAIL_USER_LASTNAME, user.getLastName());
				templateTokens.put(EmailConstants.EMAIL_ADMIN_USERNAME_LABEL, messages.getMessage("label.generic.username", userLocale));
				templateTokens.put(EmailConstants.EMAIL_ADMIN_NAME, user.getAdminName());
				templateTokens.put(EmailConstants.EMAIL_TEXT_NEW_USER_CREATED, messages.getMessage("email.newuser.text", userLocale));
				templateTokens.put(EmailConstants.EMAIL_ADMIN_PASSWORD_LABEL, messages.getMessage("label.generic.password", userLocale));
				templateTokens.put(EmailConstants.EMAIL_ADMIN_PASSWORD, decodedPassword);
				templateTokens.put(EmailConstants.EMAIL_ADMIN_URL_LABEL, messages.getMessage("label.adminurl", userLocale));
				templateTokens.put(EmailConstants.EMAIL_ADMIN_URL, filePathUtils.buildAdminUri(store, request));


				Email email = new Email();
				email.setFrom(store.getStoreName());
				email.setFromEmail(store.getStoreEmailAddress());
				email.setSubject(messages.getMessage("email.newuser.title", userLocale));
				email.setTo(user.getEmail());
				email.setTemplateName(NEW_USER_TMPL);
				email.setTemplateTokens(templateTokens);


				emailService.sendHtmlEmail(store, email);

			} catch (Exception e) {
				LOGGER.error("Cannot send email to user", e);
			}

		} else {
			//save or update user
			userService.saveOrUpdate(user);
		}

		model.addAttribute("success", "success");
		return ControllerConstants.Tiles.User.profile;
	}

	@PreAuthorize("hasRole('AUTH')")
	@RequestMapping(value = "/admin/users/remove.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> removeUser(HttpServletRequest request, Locale locale) throws Exception {

		//do not remove super admin

		String sUserId = request.getParameter("userId");

		AjaxResponse resp = new AjaxResponse();


		String userName = request.getRemoteUser();
		UserItem remoteUser = userService.getByUserName(userName);


		try {

			UUID userId = UUID.fromString(sUserId);
			UserItem user = userService.getById(userId);

			/**
			 * In order to remove a UserItem the logged in ser must be STORE_ADMIN
			 * or SUPER_USER
			 */


			if (user == null) {
				resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString, HttpStatus.OK);
			}

			if (!request.isUserInRole(Constants.GROUP_ADMIN)) {
				resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString, HttpStatus.OK);
			}


			//check if the user removed has group ADMIN
			boolean isAdmin = false;
			if (UserUtils.userInGroup(remoteUser, Constants.GROUP_ADMIN) || UserUtils.userInGroup(remoteUser, Constants.GROUP_SUPERADMIN)) {
				isAdmin = true;
			}


			if (!isAdmin) {
				resp.setStatusMessage(messages.getMessage("message.security.caanotremovesuperadmin", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString, HttpStatus.OK);
			}

			userService.delete(user);

			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);


		} catch (Exception e) {
			LOGGER.error("Error while deleting user", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}

		String returnString = resp.toJSONString();
		return new ResponseEntity<String>(returnString, HttpStatus.OK);

	}


	private void setMenu(Model model, HttpServletRequest request) throws Exception {

		//display menu
		Map<String, String> activeMenus = new HashMap<String, String>();
		activeMenus.put("profile", "profile");
		activeMenus.put("user", "create-user");

		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) request.getAttribute("MENUMAP");

		Menu currentMenu = menus.get("profile");
		model.addAttribute("currentMenu", currentMenu);
		model.addAttribute("activeMenus", activeMenus);
		//

	}


}
