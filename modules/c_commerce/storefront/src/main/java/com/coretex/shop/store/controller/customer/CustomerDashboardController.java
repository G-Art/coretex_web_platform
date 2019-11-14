package com.coretex.shop.store.controller.customer;

import com.coretex.core.business.services.customer.attribute.CustomerOptionSetService;
import com.coretex.items.commerce_core_model.CustomerItem;
import com.coretex.items.commerce_core_model.CustomerAttributeItem;
import com.coretex.items.commerce_core_model.CustomerOptionItem;
import com.coretex.items.commerce_core_model.CustomerOptionSetItem;
import com.coretex.core.model.customer.attribute.CustomerOptionType;
import com.coretex.items.commerce_core_model.CustomerOptionValueItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.shop.admin.model.customer.attribute.CustomerOption;
import com.coretex.shop.admin.model.customer.attribute.CustomerOptionValue;
import com.coretex.shop.constants.Constants;
import com.coretex.shop.populator.customer.ReadableCustomerOptionPopulator;
import com.coretex.shop.store.controller.AbstractController;
import com.coretex.shop.store.controller.ControllerConstants;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Entry point for logged in customers
 *
 * @author Carl Samson
 */
@Controller
@RequestMapping("/shop/customer")
public class CustomerDashboardController extends AbstractController {

	@Resource
	private AuthenticationManager customerAuthenticationManager;

	@Resource
	private CustomerOptionSetService customerOptionSetService;


	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
	@RequestMapping(value = "/dashboard.html", method = RequestMethod.GET)
	public String displayCustomerDashboard(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {


		MerchantStoreItem store = getSessionAttribute(Constants.MERCHANT_STORE, request);
		LanguageItem language = (LanguageItem) request.getAttribute(Constants.LANGUAGE);

		CustomerItem customer = (CustomerItem) request.getAttribute(Constants.CUSTOMER);
		getCustomerOptions(model, customer, store, language);


		model.addAttribute("section", "dashboard");


		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.customer).append(".").append(store.getStoreTemplate());

		return template.toString();

	}


	private void getCustomerOptions(Model model, CustomerItem customer, MerchantStoreItem store, LanguageItem language) throws Exception {

		Map<UUID, CustomerOption> options = new HashMap<>();
		//get options
		List<CustomerOptionSetItem> optionSet = customerOptionSetService.listByStore(store, language);
		if (!CollectionUtils.isEmpty(optionSet)) {


			ReadableCustomerOptionPopulator optionPopulator = new ReadableCustomerOptionPopulator();

			Set<CustomerAttributeItem> customerAttributes = customer.getAttributes();

			for (CustomerOptionSetItem optSet : optionSet) {

				CustomerOptionItem custOption = optSet.getCustomerOption();
				if (!custOption.getActive() || !custOption.getPublicOption()) {
					continue;
				}
				CustomerOption customerOption = options.get(custOption.getUuid());

				optionPopulator.setOptionSet(optSet);


				if (customerOption == null) {
					customerOption = new CustomerOption();
					customerOption.setUuid(custOption.getUuid());
					customerOption.setType(custOption.getCustomerOptionType());
					customerOption.setName(custOption.getName());

				}

				optionPopulator.populate(custOption, customerOption, store, language);
				options.put(customerOption.getUuid(), customerOption);

				if (!CollectionUtils.isEmpty(customerAttributes)) {
					for (CustomerAttributeItem customerAttribute : customerAttributes) {
						if (customerAttribute.getCustomerOption().getUuid().equals(customerOption.getUuid())) {
							CustomerOptionValue selectedValue = new CustomerOptionValue();
							CustomerOptionValueItem attributeValue = customerAttribute.getCustomerOptionValue();
							selectedValue.setUuid(attributeValue.getUuid());
							selectedValue.setName(attributeValue.getName());
							customerOption.setDefaultValue(selectedValue);
							if (customerOption.getType().equalsIgnoreCase(CustomerOptionType.Text.name())) {
								selectedValue.setName(customerAttribute.getTextValue());
							}
						}
					}
				}
			}
		}


		model.addAttribute("options", options.values());


	}


}
