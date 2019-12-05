package com.coretex.core.business.services.system;



import com.coretex.core.business.modules.email.Email;
import com.coretex.core.business.modules.email.EmailConfig;
import com.coretex.items.commerce_core_model.MerchantStoreItem;


public interface EmailService {

	void sendHtmlEmail(MerchantStoreItem store, Email email) throws Exception;

	EmailConfig getEmailConfiguration(MerchantStoreItem store) ;

	void saveEmailConfiguration(EmailConfig emailConfig, MerchantStoreItem store) ;

}
