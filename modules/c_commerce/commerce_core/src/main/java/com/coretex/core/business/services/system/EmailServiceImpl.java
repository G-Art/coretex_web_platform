package com.coretex.core.business.services.system;

import javax.annotation.Resource;

import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.MerchantConfigurationItem;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.coretex.core.business.constants.Constants;

import com.coretex.core.business.modules.email.Email;
import com.coretex.core.business.modules.email.EmailConfig;
import com.coretex.core.business.modules.email.HtmlEmailSender;

@Service("emailService")
public class EmailServiceImpl implements EmailService {

	@Resource
	private MerchantConfigurationService merchantConfigurationService;

	@Resource
	private HtmlEmailSender sender;

	@Override
	public void sendHtmlEmail(MerchantStoreItem store, Email email) throws Exception {

		EmailConfig emailConfig = getEmailConfiguration(store);

		sender.setEmailConfig(emailConfig);
		sender.send(email);
	}

	@Override
	public EmailConfig getEmailConfiguration(MerchantStoreItem store)  {

		MerchantConfigurationItem configuration = merchantConfigurationService.getMerchantConfiguration(Constants.EMAIL_CONFIG, store);
		EmailConfig emailConfig = null;
		if (configuration != null) {
			String value = configuration.getValue();

			ObjectMapper mapper = new ObjectMapper();
			try {
				emailConfig = mapper.readValue(value, EmailConfig.class);
			} catch (Exception e) {
				throw new RuntimeException("Cannot parse json string " + value);
			}
		}
		return emailConfig;
	}


	@Override
	public void saveEmailConfiguration(EmailConfig emailConfig, MerchantStoreItem store)  {
		MerchantConfigurationItem configuration = merchantConfigurationService.getMerchantConfiguration(Constants.EMAIL_CONFIG, store);
		if (configuration == null) {
			configuration = new MerchantConfigurationItem();
			configuration.setMerchantStore(store);
			configuration.setKey(Constants.EMAIL_CONFIG);
		}

		String value = emailConfig.toJSONString();
		configuration.setValue(value);
		merchantConfigurationService.saveOrUpdate(configuration);
	}

}
