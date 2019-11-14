package com.coretex.core.business.modules.email;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("htmlEmailSender")
public class EmailComponent implements HtmlEmailSender {

	@Value("${config.emailSender}")
	private String emailSender;

	@Resource
	private EmailModule defaultEmailSender;

	@Resource
	private EmailModule sesEmailSender;

	@Override
	public void send(Email email) throws Exception {
		switch (emailSender) {
			case "default":
				defaultEmailSender.send(email);
				break;
			case "ses":
				sesEmailSender.send(email);
				break;
			default:
				throw new Exception("No email implementation for " + emailSender);
		}

	}

	@Override
	public void setEmailConfig(EmailConfig emailConfig) {
		switch (emailSender) {
			case "default":
				defaultEmailSender.setEmailConfig(emailConfig);
				break;
			case "ses":
				sesEmailSender.setEmailConfig(emailConfig);
				break;
			default:

		}

	}


}
