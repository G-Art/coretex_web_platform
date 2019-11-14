package com.coretex.shop.store.controller.store;

import com.coretex.core.business.services.content.ContentService;
import com.coretex.core.business.utils.CoreConfiguration;
import com.coretex.core.business.utils.ajax.AjaxResponse;
import com.coretex.items.commerce_core_model.ContentItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.shop.constants.ApplicationConstants;
import com.coretex.shop.constants.Constants;
import com.coretex.shop.model.shop.ContactForm;
import com.coretex.shop.model.shop.PageInformation;
import com.coretex.shop.store.controller.AbstractController;
import com.coretex.shop.store.controller.ControllerConstants;
import com.coretex.shop.utils.CaptchaRequestUtils;
import com.coretex.shop.utils.EmailTemplatesUtils;
import com.coretex.shop.utils.LabelUtils;
import com.coretex.shop.utils.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@Controller
public class ContactController extends AbstractController {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ContactController.class);

	@Resource
	private ContentService contentService;

	@Resource
	private CoreConfiguration coreConfiguration;

	@Resource
	private LabelUtils messages;

	@Resource
	private EmailTemplatesUtils emailTemplatesUtils;

	@Resource
	private CaptchaRequestUtils captchaRequestUtils;

	private final static String CONTACT_LINK = "CONTACT";


	@RequestMapping("/shop/store/contactus.html")
	public String displayContact(Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.MERCHANT_STORE);

		request.setAttribute(Constants.LINK_CODE, CONTACT_LINK);

		LanguageItem language = (LanguageItem) request.getAttribute("LANGUAGE");

		ContactForm contact = new ContactForm();
		model.addAttribute("contact", contact);

		model.addAttribute("recapatcha_public_key", coreConfiguration.getProperty(ApplicationConstants.RECAPTCHA_PUBLIC_KEY));

		ContentItem content = contentService.getByCode(Constants.CONTENT_CONTACT_US, store, language);

		if (content != null) {

			//meta information
			PageInformation pageInformation = new PageInformation();
			pageInformation.setPageDescription(content.getMetatagDescription());
			pageInformation.setPageKeywords(content.getMetatagKeywords());
			pageInformation.setPageTitle(content.getTitle());
			pageInformation.setPageUrl(content.getName());

			request.setAttribute(Constants.REQUEST_PAGE_INFORMATION, pageInformation);

			model.addAttribute("content", content);

		}


		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Content.contactus).append(".").append(store.getStoreTemplate());
		return template.toString();


	}


	@RequestMapping(value = {"/shop/store/{storeCode}/contact"}, method = RequestMethod.POST)
	public @ResponseBody
	String sendEmail(@ModelAttribute(value = "contact") ContactForm contact, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {

		AjaxResponse ajaxResponse = new AjaxResponse();

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.MERCHANT_STORE);

		try {

			//if ( StringUtils.isBlank( contact.getCaptchaResponseField() )) {
			//	FieldError error = new FieldError("captchaResponseField","captchaResponseField",messages.getMessage("NotEmpty.contact.captchaResponseField", locale));
			//	bindingResult.addError(error);
			//    ajaxResponse.setErrorString(bindingResult.getAllErrors().get(0).getDefaultMessage());
			//    ajaxResponse.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			//    return ajaxResponse.toJSONString();
			//}

			//ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
			//reCaptcha.setPublicKey( coreConfiguration.getProperty( ApplicationConstants.RECAPTCHA_PUBLIC_KEY));
			//reCaptcha.setPrivateKey( coreConfiguration.getProperty( ApplicationConstants.RECAPTCHA_PRIVATE_KEY ) );
	        
/*	        if ( StringUtils.isNotBlank( contact.getCaptchaChallengeField() )
	                && StringUtils.isNotBlank( contact.getCaptchaResponseField() ) )
	            {
	                ReCaptchaResponse reCaptchaResponse =
	                    reCaptcha.checkAnswer( request.getRemoteAddr(), contact.getCaptchaChallengeField(),
	                                           contact.getCaptchaResponseField() );
	                if ( !reCaptchaResponse.isValid() )
	                {
	                    LOGGER.debug( "Captcha response does not matched" );
	        			FieldError error = new FieldError("captchaChallengeField","captchaChallengeField",messages.getMessage("validaion.recaptcha.not.matched", locale));
	        			bindingResult.addError(error);
	                }

	        }*/

			if (!StringUtils.isBlank(request.getParameter("g-recaptcha-response"))) {
				boolean validateCaptcha = captchaRequestUtils.checkCaptcha(request.getParameter("g-recaptcha-response"));

				if (!validateCaptcha) {
					LOGGER.debug("Captcha response does not matched");
					FieldError error = new FieldError("captchaChallengeField", "captchaChallengeField", messages.getMessage("validaion.recaptcha.not.matched", locale));
					bindingResult.addError(error);
				}
			}


			if (bindingResult.hasErrors()) {
				LOGGER.debug("found {} validation error while validating in customer registration ",
						bindingResult.getErrorCount());
				ajaxResponse.setErrorString(bindingResult.getAllErrors().get(0).getDefaultMessage());
				ajaxResponse.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				return ajaxResponse.toJSONString();

			}

			emailTemplatesUtils.sendContactEmail(contact, store, LocaleUtils.getLocale(store.getDefaultLanguage()), request.getContextPath());

			ajaxResponse.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
		} catch (Exception e) {
			LOGGER.error("An error occured while trying to send an email", e);
			ajaxResponse.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}

		return ajaxResponse.toJSONString();


	}


}
