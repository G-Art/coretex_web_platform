
package com.coretex.shop.store.controller.customer.facade;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.business.modules.email.Email;
import com.coretex.core.business.services.reference.country.CountryService;
import com.coretex.core.business.services.reference.language.LanguageService;
import com.coretex.core.business.services.reference.zone.ZoneService;
import com.coretex.core.business.services.shoppingcart.ShoppingCartService;
import com.coretex.core.business.services.system.EmailService;
import com.coretex.core.business.services.user.GroupService;
import com.coretex.core.business.utils.CoreConfiguration;
import com.coretex.enums.commerce_core_model.GroupTypeEnum;
import com.coretex.items.commerce_core_model.GroupItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.PermissionItem;
import com.coretex.items.commerce_core_model.ShoppingCartItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.cx_core.CustomerItem;
import com.coretex.shop.admin.model.userpassword.UserReset;
import com.coretex.shop.constants.Constants;
import com.coretex.shop.constants.EmailConstants;
import com.coretex.shop.model.customer.CustomerEntity;
import com.coretex.shop.model.customer.PersistableCustomer;
import com.coretex.shop.model.customer.ReadableCustomer;
import com.coretex.shop.model.customer.UserAlreadyExistException;
import com.coretex.shop.model.customer.address.Address;
import com.coretex.shop.populator.customer.CustomerPopulator;
import com.coretex.shop.populator.customer.ReadableCustomerPopulator;
import com.coretex.shop.store.api.exception.ConversionRuntimeException;
import com.coretex.shop.store.api.exception.ServiceRuntimeException;
import com.coretex.shop.utils.EmailTemplatesUtils;
import com.coretex.shop.utils.EmailUtils;
import com.coretex.shop.utils.ImageFilePath;
import com.coretex.shop.utils.LabelUtils;
import com.coretex.shop.utils.LocaleUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;


/**
 * CustomerItem Facade work as an abstraction layer between Controller and Service layer. It work as an
 * entry point to service layer.
 *
 * @author Umesh Awasthi
 * @version 2.2.1
 *
 */

@Service("customerFacade")
public class CustomerFacadeImpl implements CustomerFacade {

	private static final Logger LOG = LoggerFactory.getLogger(CustomerFacadeImpl.class);
	private final static int USERNAME_LENGTH = 6;

	private final static String RESET_PASSWORD_TPL = "email_template_password_reset_customer.ftl";

	public final static String ROLE_PREFIX = "ROLE_";// Spring Security 4


	@Resource
	private ShoppingCartService shoppingCartService;

	@Resource
	private LanguageService languageService;

	@Resource
	private LabelUtils messages;


	@Resource
	private CountryService countryService;

	@Resource
	private GroupService groupService;


	@Resource
	private ZoneService zoneService;

	@Resource
	private PasswordEncoder passwordEncoder;

	@Resource
	private EmailService emailService;

	@Resource
	private EmailTemplatesUtils emailTemplatesUtils;

	@Resource
	private AuthenticationManager customerAuthenticationManager;


	@Resource
	private CoreConfiguration coreConfiguration;


	@Resource
	private EmailUtils emailUtils;

	@Resource
	@Qualifier("img")
	private ImageFilePath imageUtils;

	/**
	 * Method used to fetch customer based on the username and storecode. CustomerItem username is unique
	 * to each store.
	 *
	 * @param userName
	 * @param store
	 * @throws ConversionException
	 */
	@Override
	public CustomerEntity getCustomerDataByUserName(final String userName, final MerchantStoreItem store,
													final LocaleItem language) throws Exception {
		LOG.info("Fetching customer with userName" + userName);


		return null;

	}


	/*
	 * (non-Javadoc)
	 *
	 * @see com.coretex.web.shop.controller.customer.facade#mergeCart(final CustomerItem
	 * customerModel, final String sessionShoppingCartId ,final MerchantStoreItem store,final LocaleItem
	 * language)
	 */
	@Override
	public ShoppingCartItem mergeCart(final CustomerItem customerModel, final String sessionShoppingCartId,
									  final MerchantStoreItem store, final LocaleItem language) throws Exception {

		LOG.debug("Starting merge cart process");
		if (customerModel != null) {
			ShoppingCartItem customerCart = shoppingCartService.getByCustomer(customerModel);
			if (StringUtils.isNotBlank(sessionShoppingCartId)) {
				ShoppingCartItem sessionShoppingCart =
						shoppingCartService.getByCode(sessionShoppingCartId, store);
				if (sessionShoppingCart != null) {
					if (customerCart == null) {
						if (sessionShoppingCart.getCustomerId() == null) {// saved shopping cart does not belong
							// to a customer
							LOG.debug("Not able to find any shoppingCart with current customer");
							// give it to the customer
							sessionShoppingCart.setCustomerId(customerModel.getUuid());
							shoppingCartService.saveOrUpdate(sessionShoppingCart);
							customerCart = shoppingCartService.getById(sessionShoppingCart.getUuid(), store);
							return customerCart;
							// return populateShoppingCartData(customerCart,store,language);
						} else {
							return null;
						}
					} else {
						if (sessionShoppingCart.getCustomerId() == null) {// saved shopping cart does not belong
							// to a customer
							// assign it to logged in user
							LOG.debug("CustomerItem shopping cart as well session cart is available, merging carts");
							customerCart =
									shoppingCartService.mergeShoppingCarts(customerCart, sessionShoppingCart, store);
							customerCart = shoppingCartService.getById(customerCart.getUuid(), store);
							return customerCart;
							// return populateShoppingCartData(customerCart,store,language);
						} else {
							if (sessionShoppingCart.getCustomerId().equals(customerModel.getUuid())) {
								if (!customerCart.getShoppingCartCode()
										.equals(sessionShoppingCart.getShoppingCartCode())) {
									// merge carts
									LOG.info("CustomerItem shopping cart as well session cart is available");
									customerCart = shoppingCartService.mergeShoppingCarts(customerCart,
											sessionShoppingCart, store);
									customerCart = shoppingCartService.getById(customerCart.getUuid(), store);
									return customerCart;
									// return populateShoppingCartData(customerCart,store,language);
								} else {
									return customerCart;
									// return populateShoppingCartData(sessionShoppingCart,store,language);
								}
							} else {
								// the saved cart belongs to another user
								return null;
							}
						}


					}
				}
			} else {
				// return populateShoppingCartData(customerCart,store,language);
				return customerCart;

			}
		}
		LOG.info(
				"Seems some issue with system, unable to find any customer after successful authentication");
		return null;

	}


	@Override
	public CustomerItem getCustomerByUserName(String userName, MerchantStoreItem store) throws Exception {
		return null;
	}

	@Override
	public ReadableCustomer getByUserName(String userName, MerchantStoreItem merchantStore, LocaleItem language) {
		Validate.notNull(userName, "Username cannot be null");
		Validate.notNull(merchantStore, "MerchantStoreItem cannot be null");

		return null;
	}



	/**
	 * <p>
	 * Method to check if given user exists for given username under given store. System treat
	 * username as unique for a given store, customer is not allowed to use same username twice for a
	 * given store, however it can be used for different stores.
	 * </p>
	 *
	 * @param userName CustomerItem slected userName
	 * @param store store for which customer want to register
	 * @return boolean flag indicating if user exists for given store or not
	 * @throws Exception
	 *
	 */
	@Override
	public boolean checkIfUserExists(final String userName, final MerchantStoreItem store)
			throws Exception {
		if (StringUtils.isNotBlank(userName) && store != null) {
			LOG.info("No customer found with userName {} for store {} ", userName, store.getStoreName());
			return false;

		}
		LOG.info("Either userName is empty or we have not found any value for store");
		return false;
	}


	@Override
	public PersistableCustomer registerCustomer(final PersistableCustomer customer,
												final MerchantStoreItem merchantStore, LocaleItem language) throws Exception {
		LOG.info("Starting customer registration process..");

		if (this.userExist(customer.getUserName())) {
			throw new UserAlreadyExistException("UserItem already exist");
		}

		CustomerItem customerModel = getCustomerModel(customer, merchantStore, language);
		if (customerModel == null) {
			LOG.equals("Unable to create customer in system");
			// throw new CustomerRegistrationException( "Unable to register customer" );
			throw new Exception("Unable to register customer");
		}

		LOG.info("About to persist customer to database.");

		LOG.info("Returning customer data to controller..");
		// return customerEntityPoulator(customerModel,merchantStore);
		customer.setUuid(customerModel.getUuid());
		return customer;
	}

	@Override
	public CustomerItem getCustomerModel(final PersistableCustomer customer,
										 final MerchantStoreItem merchantStore, LocaleItem language) throws Exception {

		LOG.info("Starting to populate customer model from customer data");
		CustomerItem customerModel = null;
		CustomerPopulator populator = new CustomerPopulator();
		populator.setCountryService(countryService);
		populator.setLanguageService(languageService);
		populator.setLanguageService(languageService);
		populator.setZoneService(zoneService);
		populator.setGroupService(groupService);


		customerModel = populator.populate(customer, merchantStore, language);
		customerModel.setAnonymous(false);
		// we are creating or resetting a customer
		if (StringUtils.isBlank(customerModel.getPassword())
				&& !StringUtils.isBlank(customer.getClearPassword())) {
			customerModel.setPassword(customer.getClearPassword());
		}
		// set groups
		if (!StringUtils.isBlank(customerModel.getPassword())
				&& !StringUtils.isBlank(customerModel.getEmail())) {
			customerModel.setPassword(passwordEncoder.encode(customer.getClearPassword()));
			setCustomerModelDefaultProperties(customerModel, merchantStore);
		}


		return customerModel;

	}


	@Override
	public void setCustomerModelDefaultProperties(CustomerItem customer, MerchantStoreItem store)
			throws Exception {
		Validate.notNull(customer, "CustomerItem object cannot be null");
		if (customer.getUuid() == null) {
			if (StringUtils.isBlank(customer.getFirstName())) {
				String userName = UserReset.generateRandomString(USERNAME_LENGTH);
				customer.setFirstName(userName);
			}
			if (StringUtils.isBlank(customer.getPassword())) {
				String password = UserReset.generateRandomString();
				String encodedPassword = passwordEncoder.encode(password);
				customer.setPassword(encodedPassword);
			}
		}

		if (CollectionUtils.isEmpty(customer.getGroups())) {
			List<GroupItem> groups = getListOfGroups(GroupTypeEnum.CUSTOMER);
			for (GroupItem group : groups) {
				if (group.getGroupName().equals(Constants.GROUP_CUSTOMER)) {
					customer.getGroups().add(group);
				}
			}

		}

	}


	public void authenticate(CustomerItem customer, String userName, String password) throws Exception {

		Validate.notNull(customer, "CustomerItem cannot be null");

		Collection<GrantedAuthority> authorities = new ArrayList<>();
		GrantedAuthority role =
				new SimpleGrantedAuthority(ROLE_PREFIX + Constants.PERMISSION_CUSTOMER_AUTHENTICATED);// required
		// to
		// login
		authorities.add(role);
		List<GroupItem> groups = customer.getGroups();
		if (groups != null) {
			for (GroupItem group : groups) {
				for (PermissionItem permission : group.getPermissions()) {
					GrantedAuthority auth = new SimpleGrantedAuthority(permission.getPermissionName());
					authorities.add(auth);
				}

			}
		}

		Authentication authenticationToken =
				new UsernamePasswordAuthenticationToken(userName, password, authorities);

		Authentication authentication = customerAuthenticationManager.authenticate(authenticationToken);

		SecurityContextHolder.getContext().setAuthentication(authentication);

	}


	@Override
	public Address getAddress(UUID userId, final MerchantStoreItem merchantStore,
							  boolean isBillingAddress) throws Exception {
		return null;

	}


	@Override
	public void updateAddress(UUID userId, MerchantStoreItem merchantStore, Address address,
							  final LocaleItem language) throws Exception {

	}

	@Override
	public ReadableCustomer getCustomerById(final UUID id, final MerchantStoreItem merchantStore,
											final LocaleItem language) {

		return null;
	}


	@Override
	public PersistableCustomer create(PersistableCustomer customer, MerchantStoreItem store) {

		//TODO should be reviewed
		if (userExist(customer.getUserName())) {
			throw new ServiceRuntimeException("UserItem already exist");
		}

		CustomerItem customerToPopulate = convertPersistableCustomerToCustomer(customer, store);
		saveCustomer(customerToPopulate);
		customer.setUuid(customerToPopulate.getUuid());

		notifyNewCustomer(customer, store, customerToPopulate.getDefaultLanguage());

		/**
		 * For security reasons set empty passwords
		 */
		customer.setEncodedPassword(null);
		customer.setClearPassword(null);

		return customer;
	}

	private void saveCustomer(CustomerItem customerToPopulate) {
	}

	private boolean userExist(String userName) {
		return true;
	}

	private List<GroupItem> getListOfGroups(GroupTypeEnum groupType) {
			return groupService.listGroup(groupType);
	}

	private CustomerItem convertPersistableCustomerToCustomer(PersistableCustomer customer, MerchantStoreItem store) {

		CustomerItem cust = new CustomerItem();

		CustomerPopulator populator = new CustomerPopulator();
		populator.setCountryService(countryService);
		populator.setLanguageService(languageService);
		populator.setLanguageService(languageService);
		populator.setZoneService(zoneService);
		populator.setGroupService(groupService);
		try {
			populator.populate(customer, cust, store, store.getDefaultLanguage());
		} catch (ConversionException e) {
			throw new ConversionRuntimeException(e);
		}


		List<GroupItem> groups = getListOfGroups(GroupTypeEnum.CUSTOMER);
		cust.setGroups(groups);

		String password = customer.getClearPassword();
		if (StringUtils.isBlank(password)) {
			password = UserReset.generateRandomString();
			customer.setClearPassword(password);
		}


		String encodedPassword = passwordEncoder.encode(password);
		if (!StringUtils.isBlank(customer.getEncodedPassword())) {
			encodedPassword = customer.getEncodedPassword();
			// customer.setClearPassword("");
		}

		cust.setPassword(encodedPassword);

		return cust;

	}

	private void notifyNewCustomer(PersistableCustomer customer, MerchantStoreItem store, LocaleItem lang) {
		Locale customerLocale = LocaleUtils.getLocale(lang);
		String shopSchema = coreConfiguration.getProperty("SHOP_SCHEME");
		emailTemplatesUtils.sendRegistrationEmail(customer, store, customerLocale, shopSchema);
	}


	@Override
	public PersistableCustomer update(PersistableCustomer customer, MerchantStoreItem store) {

		if (customer.getUuid() == null) {
			throw new ServiceRuntimeException("Can't update a customer with null id");
		}

		CustomerPopulator populator = new CustomerPopulator();
		populator.setCountryService(countryService);
		populator.setLanguageService(languageService);
		populator.setZoneService(zoneService);
		populator.setGroupService(groupService);


		String password = customer.getClearPassword();
		if (StringUtils.isBlank(password)) {
			password = UserReset.generateRandomString();
			customer.setClearPassword(password);
		}


		String encodedPassword = passwordEncoder.encode(password);
		if (!StringUtils.isBlank(customer.getEncodedPassword())) {
			encodedPassword = customer.getEncodedPassword();
			customer.setClearPassword("");
		}

		customer.setEncodedPassword(encodedPassword);

		return customer;
	}


	@Override
	public void resetPassword(CustomerItem customer, MerchantStoreItem store, LocaleItem language)
			throws Exception {


		String password = UserReset.generateRandomString();
		String encodedPassword = passwordEncoder.encode(password);

		customer.setPassword(encodedPassword);

		Locale locale = languageService.toLocale(language, store);

		// send email

		try {

			// creation of a user, send an email
			String[] storeEmail = {store.getStoreEmailAddress()};


			Map<String, String> templateTokens =
					emailUtils.createEmailObjectsMap(imageUtils.getContextPath(), store, messages, locale);
			templateTokens.put(EmailConstants.LABEL_HI, messages.getMessage("label.generic.hi", locale));
			templateTokens.put(EmailConstants.EMAIL_CUSTOMER_FIRSTNAME,
					customer.getBilling().getFirstName());
			templateTokens.put(EmailConstants.EMAIL_CUSTOMER_LASTNAME,
					customer.getBilling().getLastName());
			templateTokens.put(EmailConstants.EMAIL_RESET_PASSWORD_TXT,
					messages.getMessage("email.customer.resetpassword.text", locale));
			templateTokens.put(EmailConstants.EMAIL_CONTACT_OWNER,
					messages.getMessage("email.contactowner", storeEmail, locale));
			templateTokens.put(EmailConstants.EMAIL_PASSWORD_LABEL,
					messages.getMessage("label.generic.password", locale));
			templateTokens.put(EmailConstants.EMAIL_CUSTOMER_PASSWORD, password);


			Email email = new Email();
			email.setFrom(store.getStoreName());
			email.setFromEmail(store.getStoreEmailAddress());
			email.setSubject(messages.getMessage("label.generic.changepassword", locale));
			email.setTo(customer.getEmail());
			email.setTemplateName(RESET_PASSWORD_TPL);
			email.setTemplateTokens(templateTokens);


			emailService.sendHtmlEmail(store, email);

		} catch (Exception e) {
			LOG.error("Cannot send email to customer", e);
		}


	}


	@Override
	public void delete(CustomerItem entity) {

	}

	private ReadableCustomer convertCustomerToReadableCustomer(CustomerItem customer, MerchantStoreItem merchantStore, LocaleItem language) {
		ReadableCustomerPopulator populator = new ReadableCustomerPopulator();
		try {
			return populator.populate(customer, new ReadableCustomer(), merchantStore, language);
		} catch (ConversionException e) {
			throw new ConversionRuntimeException(e);
		}
	}

}
