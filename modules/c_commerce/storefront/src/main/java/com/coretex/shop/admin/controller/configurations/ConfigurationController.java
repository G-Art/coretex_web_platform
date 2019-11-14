package com.coretex.shop.admin.controller.configurations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.coretex.enums.commerce_core_model.MerchantConfigurationTypeEnum;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.MerchantConfigurationItem;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.coretex.core.business.modules.email.EmailConfig;
import com.coretex.core.business.services.system.EmailService;
import com.coretex.core.business.services.system.MerchantConfigurationService;
import com.coretex.shop.admin.controller.ControllerConstants;
import com.coretex.core.data.web.ConfigListWrapper;
import com.coretex.core.data.web.Menu;
import com.coretex.shop.constants.Constants;


@Controller
public class ConfigurationController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationController.class);

	@Resource
	private MerchantConfigurationService merchantConfigurationService;

	@Resource
	private EmailService emailService;

	@Resource
	Environment env;


	@PreAuthorize("hasRole('AUTH')")
	@RequestMapping(value = "/admin/configuration/accounts.html", method = RequestMethod.GET)
	public String displayAccountsConfguration(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		setConfigurationMenu(model, request);
		List<MerchantConfigurationItem> configs = new ArrayList<MerchantConfigurationItem>();
		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);
		MerchantConfigurationItem merchantFBConfiguration = merchantConfigurationService.getMerchantConfiguration(Constants.KEY_FACEBOOK_PAGE_URL, store);
		if (null == merchantFBConfiguration) {
			merchantFBConfiguration = new MerchantConfigurationItem();
			merchantFBConfiguration.setKey(Constants.KEY_FACEBOOK_PAGE_URL);
			merchantFBConfiguration.setMerchantConfigurationType(MerchantConfigurationTypeEnum.SOCIAL);
		}
		configs.add(merchantFBConfiguration);

		MerchantConfigurationItem merchantGoogleAnalyticsConfiguration = merchantConfigurationService.getMerchantConfiguration(Constants.KEY_GOOGLE_ANALYTICS_URL, store);
		if (null == merchantGoogleAnalyticsConfiguration) {
			merchantGoogleAnalyticsConfiguration = new MerchantConfigurationItem();
			merchantGoogleAnalyticsConfiguration.setKey(Constants.KEY_GOOGLE_ANALYTICS_URL);
			merchantGoogleAnalyticsConfiguration.setMerchantConfigurationType(MerchantConfigurationTypeEnum.SOCIAL);
		}
		configs.add(merchantGoogleAnalyticsConfiguration);

		MerchantConfigurationItem merchantInstagramConfiguration = merchantConfigurationService.getMerchantConfiguration(Constants.KEY_INSTAGRAM_URL, store);
		if (null == merchantInstagramConfiguration) {
			merchantInstagramConfiguration = new MerchantConfigurationItem();
			merchantInstagramConfiguration.setKey(Constants.KEY_INSTAGRAM_URL);
			merchantInstagramConfiguration.setMerchantConfigurationType(MerchantConfigurationTypeEnum.SOCIAL);
		}
		configs.add(merchantInstagramConfiguration);

		MerchantConfigurationItem merchantPinterestConfiguration = merchantConfigurationService.getMerchantConfiguration(Constants.KEY_PINTEREST_PAGE_URL, store);
		if (null == merchantPinterestConfiguration) {
			merchantPinterestConfiguration = new MerchantConfigurationItem();
			merchantPinterestConfiguration.setKey(Constants.KEY_PINTEREST_PAGE_URL);
			merchantPinterestConfiguration.setMerchantConfigurationType(MerchantConfigurationTypeEnum.SOCIAL);
		}
		configs.add(merchantPinterestConfiguration);

		/**
		 MerchantConfigurationItem merchantGoogleApiConfiguration = merchantConfigurationService.getMerchantConfiguration(Constants.KEY_GOOGLE_API_KEY,store);
		 if(null == merchantGoogleApiConfiguration)
		 {
		 merchantGoogleApiConfiguration = new MerchantConfigurationItem();
		 merchantGoogleApiConfiguration.setKey(Constants.KEY_GOOGLE_API_KEY);
		 merchantGoogleApiConfiguration.setMerchantConfigurationType(MerchantConfigurationTypeEnum.CONFIG);
		 }
		 configs.add(merchantGoogleApiConfiguration);
		 **/

		MerchantConfigurationItem twitterConfiguration = merchantConfigurationService.getMerchantConfiguration(Constants.KEY_TWITTER_HANDLE, store);
		if (null == twitterConfiguration) {
			twitterConfiguration = new MerchantConfigurationItem();
			twitterConfiguration.setKey(Constants.KEY_TWITTER_HANDLE);
			twitterConfiguration.setMerchantConfigurationType(MerchantConfigurationTypeEnum.SOCIAL);
		}
		configs.add(twitterConfiguration);

		ConfigListWrapper configWrapper = new ConfigListWrapper();
		configWrapper.setMerchantConfigs(configs);
		model.addAttribute("configuration", configWrapper);

		return com.coretex.shop.admin.controller.ControllerConstants.Tiles.Configuration.accounts;
	}

	@PreAuthorize("hasRole('AUTH')")
	@RequestMapping(value = "/admin/configuration/saveConfiguration.html", method = RequestMethod.POST)
	public String saveConfigurations(@ModelAttribute("configuration") ConfigListWrapper configWrapper, BindingResult result, Model model, HttpServletRequest request, Locale locale) throws Exception {
		setConfigurationMenu(model, request);
		List<MerchantConfigurationItem> configs = configWrapper.getMerchantConfigs();
		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);
		for (MerchantConfigurationItem mConfigs : configs) {
			mConfigs.setMerchantStore(store);
			if (!StringUtils.isBlank(mConfigs.getValue())) {
				mConfigs.setMerchantConfigurationType(MerchantConfigurationTypeEnum.SOCIAL);
				merchantConfigurationService.saveOrUpdate(mConfigs);
			} else {//remove if submited blank and exists
				MerchantConfigurationItem config = merchantConfigurationService.getMerchantConfiguration(mConfigs.getKey(), store);
				if (config != null) {
					merchantConfigurationService.delete(config);
				}
			}
		}
		model.addAttribute("success", "success");
		model.addAttribute("configuration", configWrapper);
		return com.coretex.shop.admin.controller.ControllerConstants.Tiles.Configuration.accounts;

	}

	@PreAuthorize("hasRole('AUTH')")
	@RequestMapping(value = "/admin/configuration/email.html", method = RequestMethod.GET)
	public String displayEmailSettings(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		setEmailConfigurationMenu(model, request);
		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);
		EmailConfig emailConfig = emailService.getEmailConfiguration(store);
		if (emailConfig == null) {
			emailConfig = new EmailConfig();
			//TODO: Need to check below properties. When there are no record available in MerchantConfguration table with EMAIL_CONFIG key, 
			// instead of showing blank fields in setup screen, show default configured values from email.properties
			emailConfig.setProtocol(env.getProperty("mailSender.protocol"));
			emailConfig.setHost(env.getProperty("mailSender.host"));
			emailConfig.setPort(env.getProperty("mailSender.port}"));
			emailConfig.setUsername(env.getProperty("mailSender.username"));
			emailConfig.setPassword(env.getProperty("mailSender.password"));
			emailConfig.setSmtpAuth(Boolean.parseBoolean(env.getProperty("mailSender.mail.smtp.auth")));
			emailConfig.setStarttls(Boolean.parseBoolean(env.getProperty("mail.smtp.starttls.enable")));
		}

		model.addAttribute("configuration", emailConfig);
		return ControllerConstants.Tiles.Configuration.email;
	}

	@PreAuthorize("hasRole('AUTH')")
	@RequestMapping(value = "/admin/configuration/saveEmailConfiguration.html", method = RequestMethod.POST)
	public String saveEmailSettings(@ModelAttribute("configuration") EmailConfig config, BindingResult result, Model model, HttpServletRequest request, Locale locale) throws Exception {
		setEmailConfigurationMenu(model, request);
		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);
		EmailConfig emailConfig = emailService.getEmailConfiguration(store);
		if (emailConfig == null) {
			emailConfig = new EmailConfig();
		}

		// populte EmailConfig model from UI values
		emailConfig.setProtocol(config.getProtocol());
		emailConfig.setHost(config.getHost());
		emailConfig.setPort(config.getPort());
		emailConfig.setUsername(config.getUsername());
		emailConfig.setPassword(config.getPassword());
		emailConfig.setSmtpAuth(config.isSmtpAuth());
		emailConfig.setStarttls(config.isStarttls());

		emailService.saveEmailConfiguration(emailConfig, store);

		model.addAttribute("configuration", emailConfig);
		model.addAttribute("success", "success");
		return ControllerConstants.Tiles.Configuration.email;
	}

	private void setConfigurationMenu(Model model, HttpServletRequest request) throws Exception {

		Map<String, String> activeMenus = new HashMap<String, String>();
		activeMenus.put("configuration", "configuration");
		activeMenus.put("accounts-conf", "accounts-conf");

		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) request.getAttribute("MENUMAP");

		Menu currentMenu = menus.get("configuration");
		model.addAttribute("currentMenu", currentMenu);
		model.addAttribute("activeMenus", activeMenus);
	}

	private void setEmailConfigurationMenu(Model model, HttpServletRequest request) throws Exception {

		Map<String, String> activeMenus = new HashMap<String, String>();
		activeMenus.put("configuration", "configuration");
		activeMenus.put("email-conf", "email-conf");

		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) request.getAttribute("MENUMAP");

		Menu currentMenu = menus.get("configuration");
		model.addAttribute("currentMenu", currentMenu);
		model.addAttribute("activeMenus", activeMenus);
	}
}
